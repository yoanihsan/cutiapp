package com.demo.cutiapp.repository;

import java.util.Date;

import javax.transaction.Transactional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.demo.cutiapp.model.PengajuanCuti;

@Repository
public interface PengajuanCutiRepository extends BaseRepository<PengajuanCuti, Long>, JpaRepository<PengajuanCuti,Long>, JpaSpecificationExecutor<PengajuanCuti>{

	@Transactional
	@Modifying
	@Query("update #{#entityName} e set e.approve = true, e.tglApprove=DATE(NOW()) where e.id = :id")
	public void approveById(@Param("id") Long id);
	
	@Query("select e from #{#entityName} e where DATE(e.createdDate) BETWEEN :start and :end and e.deleted=false")
	public Page<PengajuanCuti> findAllByCreatedDateBetween(@Param("start") Date start, @Param("end") Date end, Pageable pageable);
	
	@Query("select e from #{#entityName} e where DATE(e.modifiedDate) BETWEEN :start and :end and e.deleted=false")
	public Page<PengajuanCuti> findAllByModifiedDateBetween(@Param("start") Date start, @Param("end") Date end, Pageable pageable);


}
