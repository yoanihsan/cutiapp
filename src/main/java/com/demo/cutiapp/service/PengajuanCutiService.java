package com.demo.cutiapp.service;

import java.util.Date;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.demo.cutiapp.dto.PengajuanCutiDTO;
import com.demo.cutiapp.model.PengajuanCuti;
import com.demo.cutiapp.util.DbSpecification;

public interface PengajuanCutiService extends BaseService<PengajuanCuti, Long>{

	public PengajuanCuti save(PengajuanCutiDTO request);

	public void approve(Long id);

	public Page<PengajuanCuti> findAllByCreatedDateOrModifiedDate(String filter, Date startDate, Date endDate, Pageable pageable);

	public Page<PengajuanCuti> findAll(DbSpecification<PengajuanCuti> spec, Pageable pageable);

}
