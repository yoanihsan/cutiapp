package com.demo.cutiapp.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.demo.cutiapp.dto.EmployeeDTO;
import com.demo.cutiapp.model.Employee;
import com.demo.cutiapp.response.BaseResponseMessage;
import com.demo.cutiapp.response.PageableResponse;
import com.demo.cutiapp.response.util.JsonResponseUtil;
import com.demo.cutiapp.service.EmployeeService;
import com.demo.cutiapp.util.DbSpecificationUser;
import com.demo.cutiapp.util.SearchCriteria;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;


@RestController
@RequestMapping("/api/employee")
@Api(value = "EmployeeControllerAPI", produces = MediaType.APPLICATION_JSON_VALUE)
public class EmployeeController {

	@Autowired
	private EmployeeService employeeService;

    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
	SimpleDateFormat format2 = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
	
    @GetMapping("")
    @ApiOperation("Get all the employees")
    @ApiResponses(value = {@ApiResponse(code = 200, message = "Succes", response = EmployeeDTO.class) })
    public ResponseEntity<BaseResponseMessage<List<EmployeeDTO>>> selectAll(@RequestParam String filter, @RequestParam String value, @RequestParam Integer limit, @RequestParam Integer page, @RequestParam String sort_by, @RequestParam String sort_dir) throws ParseException {
    	PageableResponse meta = new PageableResponse();
        Sort sort = Sort.by(sort_by).ascending();
		if (sort_by != ""){
			if (sort_dir.equals("asc")){
				sort = Sort.by(sort_by).ascending();
			}else{
				sort = Sort.by(sort_by).descending();
			}
		}
		
        Page<Employee> empPage = new PageImpl<Employee>(new ArrayList<Employee>());
        if ("createdDate".equals(filter)||"modifiedDate".equals(filter) && !"".equals(value)){
        	String[] arrOfStr = value.split(",", 2);
            Date startDate = format.parse(arrOfStr[0]);
            Date endDate = format.parse(arrOfStr[1]);
            Pageable pageable = PageRequest.of(page, limit, sort);
            empPage = employeeService.findAllByCreatedDateOrModifiedDate(filter, startDate, endDate, pageable);
        }else{
            DbSpecificationUser spec = new DbSpecificationUser(new SearchCriteria(filter, ":", value),null);
            Pageable pageable = PageRequest.of(page, limit, sort);
            empPage = employeeService.findAll(spec, pageable);
        }
		Page<EmployeeDTO> pageDto = empPage.map(new Function<Employee, EmployeeDTO>(){
			@Override
			public EmployeeDTO apply(Employee entity) {
				EmployeeDTO dto = new EmployeeDTO();
				// TODO Auto-generated method stub
				dto.setId(entity.getId());
				dto.setDeleted(entity.isDeleted());
				dto.setCreatedDate(format2.format(entity.getCreatedDate()));
				dto.setModifiedDate(format2.format(entity.getModifiedDate()));
				dto.setAddress(entity.getAddress());
				dto.setName(entity.getName());
				dto.setNip(entity.getNip());
				return dto;
			}
		});
		meta.setPageable(empPage.getPageable());
		meta.setTotalRecord(empPage.getTotalElements());
        return JsonResponseUtil.formatSuccessResponse(pageDto.getContent(),meta);
    }

	
}