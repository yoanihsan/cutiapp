package com.demo.cutiapp.repository;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.cutiapp.model.Employee;

@Repository
public interface EmployeeRepository extends BaseRepository<Employee, Long>, JpaRepository<Employee,Long>, JpaSpecificationExecutor<Employee>{

	@Query("select e from #{#entityName} e where DATE(e.createdDate) BETWEEN :start and :end and e.deleted=false")
	public Page<Employee> findAllByCreatedDateBetween(@Param("start") Date start, @Param("end") Date end, Pageable pageable);
	
	@Query("select e from #{#entityName} e where DATE(e.modifiedDate) BETWEEN :start and :end and e.deleted=false")
	public Page<Employee> findAllByModifiedDateBetween(@Param("start") Date start, @Param("end") Date end, Pageable pageable);

//	@Query("select e from #{#entityName} e where e.deleted=false and e.nip=?1")
    public Employee findByNip(String nip);
	
}
