package com.demo.cutiapp.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.cutiapp.dto.EmployeeDTO;
import com.demo.cutiapp.model.Employee;
import com.demo.cutiapp.util.DbSpecification;

@Service
public interface EmployeeService extends BaseService<Employee, Long> {

	public Page<Employee> findAllByCreatedDateOrModifiedDate(String filter, Date startDate, Date endDate, Pageable pageable);

	public Page<Employee> findAll(DbSpecification<Employee> spec, Pageable pageable);

	public Employee findByNIP(String nip);

	public Employee save(EmployeeDTO request);

	void update(Long id, EmployeeDTO entity);

}

