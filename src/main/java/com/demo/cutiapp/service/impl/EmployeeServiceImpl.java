package com.demo.cutiapp.service.impl;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.cutiapp.dto.EmployeeDTO;
import com.demo.cutiapp.exception.ObjectNotFoundException;
import com.demo.cutiapp.model.Employee;
import com.demo.cutiapp.repository.EmployeeRepository;
import com.demo.cutiapp.service.EmployeeService;
import com.demo.cutiapp.util.DbSpecification;

@Service
public class EmployeeServiceImpl implements EmployeeService {

	@Autowired
	private EmployeeRepository empRepo;
	
	@Override
	public List<Employee> findAll() {
		// TODO Auto-generated method stub
		return empRepo.findAll();
	}

	@Override
	public Employee findById(Long id) {
		// TODO Auto-generated method stub
		return empRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Cannot find employee with id : " + id));
	}

	@Override
	public void save(Employee entity) {
		// TODO Auto-generated method stub
		empRepo.save(entity);
	}

	@Override
	public void update(Long id, Employee entity) {
		// TODO Auto-generated method stub
			
	}

	
	@Override
	public void update(Long id, EmployeeDTO dto) {
		// TODO Auto-generated method stub
		Employee entity = empRepo.findById(id).orElseThrow(() -> new ObjectNotFoundException("Cannot find employee with id : " + id));
		entity.setForUpdate(dto);
		empRepo.save(entity);
			
	}

	@Override
	public void softDelete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public Page<Employee> findAllByCreatedDateOrModifiedDate(String filter, Date startDate, Date endDate,
			Pageable pageable) {
		// TODO Auto-generated method stub
		Page<Employee> emp = null;
		if ("createdDate".equals(filter)){
			emp =  empRepo.findAllByCreatedDateBetween(startDate, endDate, pageable);
		}else if ("modifiedDate".equals(filter)){
			emp = empRepo.findAllByModifiedDateBetween(startDate, endDate,pageable);
		}
		return emp;
	}

	@Override
	public Page<Employee> findAll(DbSpecification<Employee> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return empRepo.findAll(spec,pageable);	
	}

	@Override
	public Employee findByNIP(String nip) {
		// TODO Auto-generated method stub
		return empRepo.findByNip(nip);
	}

	@Override
	public Employee save(EmployeeDTO request) {
		// TODO Auto-generated method stub
		Employee emp = new Employee(request.getNip(), request.getName(), request.getAddress(),request.getJatahCuti(), request.getJatahCuti());
		empRepo.save(emp);
		return emp;
	}

}
