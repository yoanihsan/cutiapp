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
		
		if(request.getNip().isEmpty()) {
			bindingResult.rejectValue("nip", "NIP.NotEmpty", "NIP Tidak Boleh Kosong!");
		}else if(emp == null) {
			bindingResult.rejectValue("nip", "Employee.notfound", "Pegawai tidak ditemukan!");
		}else if(emp.getSisaCuti() < request.getTglCuti().length) {
			bindingResult.rejectValue("nip", "Nip.invalid", "Sisa cuti anda habis!");			
		}else if(request.getTglCuti().length > 0) {
			for(Date d : request.getTglCuti()) {
				if(d.before(new Date())) {
					bindingResult.rejectValue("tglCuti", "tglCuti.invalid", "tanggal cuti tidak boleh sebelum hari ini!");
				}
			}
		}else if(request.getKeterangan().isEmpty()) {
			bindingResult.rejectValue("keterangan", "Keterangan.NotEmpty", "Keterangan Tidak Boleh Kosong!");
		}

		if(bindingResult.hasErrors()) {
			return JsonResponseUtil.formatErrors(bindingResult.getFieldErrors());
		}else{
			Integer waitingApprove = cutiService.findPengajuanWaitingApproved(emp.getId());
			if(waitingApprove>0) {
				bindingResult.rejectValue("nip", "NIP.invalid", "Pengajuan Sebelumnya masih aktif!");
				return JsonResponseUtil.formatErrors(bindingResult.getFieldErrors());				
			}
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
		if (limit == null) limit = 10;
		if (page == null) page = 0;
		String opt = ":";
		if ("".equals(filter)) opt = "*"; 
    	Sort sort = Sort.by(sort_by == "" ? "id" : sort_by).ascending();
		if (sort_by != ""){
			if (sort_dir.equals("asc")){
				sort = Sort.by(sort_by == "" ? "id" : sort_by).ascending();
			}else{
				sort = Sort.by(sort_by == "" ? "id" : sort_by).descending();
			}
		}
		Pageable pageable = PageRequest.of(page, limit, sort);
		Page<PengajuanCuti> cutiPage = new PageImpl<PengajuanCuti>(new ArrayList<PengajuanCuti>());
		if ("createdDate".equals(filter)||"modifiedDate".equals(filter) && !"".equals(value)){
			String[] arrOfStr = value.split(",", 2);
			Date startDate = format.parse(arrOfStr[0]);
			Date endDate = format.parse(arrOfStr[1]);
			cutiPage = cutiService.findAllByCreatedDateOrModifiedDate(filter, startDate, endDate, pageable);
		}else{
			DbSpecification<PengajuanCuti> spec = new DbSpecification<PengajuanCuti>(new SearchCriteria(filter, opt, value),null);
			cutiPage = cutiService.findAll(spec, pageable);
		}

		meta.setPageable(cutiPage.getPageable());
		meta.setTotalRecord(cutiPage.getTotalElements());
		return JsonResponseUtil.formatSuccessResponse(cutiPage.getContent(),meta);
	}

}

