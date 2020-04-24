package com.demo.cutiapp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.cutiapp.dto.PengajuanCutiDTO;
import com.demo.cutiapp.model.Employee;
import com.demo.cutiapp.model.PengajuanCuti;
import com.demo.cutiapp.response.BaseResponseMessage;
import com.demo.cutiapp.response.PageableResponse;
import com.demo.cutiapp.response.ResponseCodeEnum;
import com.demo.cutiapp.response.util.JsonResponseUtil;
import com.demo.cutiapp.service.EmployeeService;
import com.demo.cutiapp.service.PengajuanCutiService;
import com.demo.cutiapp.util.DbSpecification;
import com.demo.cutiapp.util.SearchCriteria;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/api/cuti")
@Api(value = "PengajuanControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class PengajuanCutiController {

	@Autowired
	private EmployeeService empService;
	
	@Autowired
	private PengajuanCutiService cutiService;
	
	SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
	@PostMapping("")
    @ApiOperation("Create pengajuan cuti")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Succes", response = PengajuanCuti.class) })
    public ResponseEntity<BaseResponseMessage<PengajuanCuti>> saveUser(@Valid @RequestBody PengajuanCutiDTO request, BindingResult bindingResult){
		PengajuanCuti response = new PengajuanCuti();
		Employee emp = empService.findByNIP(request.getNip());
		if(emp == null) {
			bindingResult.rejectValue("nip", "Employee.notfound", "Pegawai tidak ditemukan!");
		}else if(request.getTglCuti().length > 0) {
			for(Date d : request.getTglCuti()) {
				if(d.before(new Date())) {
					bindingResult.rejectValue("tglCuti", "tglCuti.invalid", "tanggal cuti tidak boleh sebelum hari ini!");
				}
			}
		}
		if(bindingResult.hasErrors()) {
			return JsonResponseUtil.formatErrors(bindingResult.getFieldErrors());
		}else{
			response = cutiService.save(request);
		}
		
    	return JsonResponseUtil.formatSuccessResponse(response);
    }
	
	@PostMapping("/approve/{id}")
    @ApiOperation("Approve pengajuan cuti by id pengajuan")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Succes") })
    public ResponseEntity<BaseResponseMessage<PengajuanCuti>> approveCuti(@PathVariable("id") Long id){
		PengajuanCuti cuti = cutiService.findById(id);
		if(cuti==null) {
			return JsonResponseUtil.formatException(HttpStatus.BAD_REQUEST, ResponseCodeEnum.VALIDATE_ERROR, "Pengajuan cuti Not Found!");
		}else if(cuti.getApprove()) {			
			return JsonResponseUtil.formatException(HttpStatus.BAD_REQUEST, ResponseCodeEnum.VALIDATE_ERROR, "Cuti telah di approved!");
		}
		cutiService.approve(id);

		return JsonResponseUtil.formatSuccessResponse(null);
    }
	
	@GetMapping("")
    @ApiOperation("Get all pengajuan cuti")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Succes", response = PengajuanCuti.class) })
	public ResponseEntity<BaseResponseMessage<List<PengajuanCuti>>> selectAll(@RequestParam String filter, @RequestParam String value, @RequestParam Integer limit, @RequestParam Integer page, @RequestParam String sort_by, @RequestParam String sort_dir) throws ParseException {
		PageableResponse meta = new PageableResponse();
		Sort sort = Sort.by(sort_by).ascending();
		String opt = ":";
		if ("".equals(filter)) opt = "*"; 
		if (sort_by != ""){
			if (sort_dir.equals("asc")){
				sort = Sort.by(sort_by).ascending();
			}else{
				sort = Sort.by(sort_by).descending();
			}
		}

		Page<PengajuanCuti> empPage = new PageImpl<PengajuanCuti>(new ArrayList<PengajuanCuti>());
		if ("createdDate".equals(filter)||"modifiedDate".equals(filter) && !"".equals(value)){
			String[] arrOfStr = value.split(",", 2);
			Date startDate = format.parse(arrOfStr[0]);
			Date endDate = format.parse(arrOfStr[1]);
			Pageable pageable = PageRequest.of(page, limit, sort);
			empPage = cutiService.findAllByCreatedDateOrModifiedDate(filter, startDate, endDate, pageable);
		}else{
			DbSpecification<PengajuanCuti> spec = new DbSpecification<PengajuanCuti>(new SearchCriteria(filter, opt, value),null);
			Pageable pageable = PageRequest.of(page, limit, sort);
			empPage = cutiService.findAll(spec, pageable);
		}

		meta.setPageable(empPage.getPageable());
		meta.setTotalRecord(empPage.getTotalElements());
		return JsonResponseUtil.formatSuccessResponse(empPage.getContent(),meta);
	}

}

