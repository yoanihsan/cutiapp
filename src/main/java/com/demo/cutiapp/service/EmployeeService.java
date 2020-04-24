package com.demo.cutiapp.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.cutiapp.model.Employee;
import com.demo.cutiapp.util.DbSpecificationUser;

@Service
public interface EmployeeService extends BaseService<Employee, Long> {

	public Page<Employee> findAllByCreatedDateOrModifiedDate(String filter, Date startDate, Date endDate, Pageable pageable);

	public Page<Employee> findAll(DbSpecificationUser spec, Pageable pageable);

	public Employee findByNIP(String nip);

}
