package com.demo.cutiapp.service.impl;

import java.time.LocalDate;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.demo.cutiapp.dto.EmployeeDTO;
import com.demo.cutiapp.dto.PengajuanCutiDTO;
import com.demo.cutiapp.exception.BadRequestException;
import com.demo.cutiapp.exception.ObjectNotFoundException;
import com.demo.cutiapp.model.Employee;
import com.demo.cutiapp.model.PengajuanCuti;
import com.demo.cutiapp.model.PengajuanCutiDetail;
import com.demo.cutiapp.repository.PengajuanCutiRepository;
import com.demo.cutiapp.service.EmployeeService;
import com.demo.cutiapp.service.PengajuanCutiService;
import com.demo.cutiapp.util.DbSpecification;



@Service
public class PengajuanCutiServiceImpl implements PengajuanCutiService{

	@Autowired
	private EmployeeService empService;
	
	@Autowired
	private PengajuanCutiRepository cutiRepo;
	
	@Override
	public List<PengajuanCuti> findAll() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public PengajuanCuti findById(Long id) {
		// TODO Auto-generated method stub
		PengajuanCuti cuti = cutiRepo.findById(id).orElse(null);
		return cuti;
	}

	@Override
	public void save(PengajuanCuti entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void update(Long id, PengajuanCuti entity) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void softDelete(Long id) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public PengajuanCuti save(PengajuanCutiDTO request) {
		// TODO Auto-generated method stub
		Employee emp = empService.findByNIP(request.getNip());

		List<PengajuanCutiDetail> listCutiDetail = new ArrayList<PengajuanCutiDetail>();
		for(Date d : request.getTglCuti()) {
			if(6 == convertToLocalDateViaInstant(d).getDayOfWeek().getValue() || 7 == convertToLocalDateViaInstant(d).getDayOfWeek().getValue()) {
				throw new BadRequestException("Tanggal "+convertToLocalDateViaInstant(d)+"("+ convertToLocalDateViaInstant(d).getDayOfWeek()+") adalah hari libur!");	
			}
			PengajuanCutiDetail cutiDetail = new PengajuanCutiDetail();
			cutiDetail.setTglCuti(d);
			listCutiDetail.add(cutiDetail);
		}
		
		PengajuanCuti cuti = new PengajuanCuti(listCutiDetail, request.getKeterangan(), listCutiDetail.size(), false, null, new Date(), emp);
		cutiRepo.save(cuti);
		
		return cuti;
	}
	
	public static List<LocalDate> getDatesBetweenUsingJava8(
			LocalDate startDate, LocalDate endDate) { 

		long numOfDaysBetween = ChronoUnit.DAYS.between(startDate, endDate); 
		return IntStream.iterate(0, i -> i + 1)
				.limit(numOfDaysBetween)
				.mapToObj(i -> startDate.plusDays(i))
				.collect(Collectors.toList()); 
	}
	
	public LocalDate convertToLocalDateViaInstant(Date dateToConvert) {
	    return dateToConvert.toInstant()
	      .atZone(ZoneId.systemDefault())
	      .toLocalDate();
	}

	@Override
	public void approve(Long id) {
		// TODO Auto-generated method stub
		PengajuanCuti cuti = cutiRepo.findById(id).orElseThrow(()-> new ObjectNotFoundException(""));

		Employee emp = empService.findById(cuti.getEmployee().getId());
		EmployeeDTO empDTO = new EmployeeDTO();
		empDTO.setName(emp.getName());
		empDTO.setNip(emp.getNip());
		empDTO.setJatahCuti(emp.getJatahCuti());
		empDTO.setSisaCuti(emp.getSisaCuti() - cuti.getPengajuanCutiDetail().size());
		
		empService.update(emp.getId(), empDTO);
		cutiRepo.approveById(id);
	}

	@Override
	public Page<PengajuanCuti> findAllByCreatedDateOrModifiedDate(String filter, Date startDate, Date endDate,
			Pageable pageable) {
		// TODO Auto-generated method stub
		Page<PengajuanCuti> cuti = null;
		if ("createdDate".equals(filter)){
			cuti =  cutiRepo.findAllByCreatedDateBetween(startDate, endDate, pageable);
		}else if ("modifiedDate".equals(filter)){
			cuti = cutiRepo.findAllByModifiedDateBetween(startDate, endDate,pageable);
		}
		return cuti;

	}

	@Override
	public Page<PengajuanCuti> findAll(DbSpecification<PengajuanCuti> spec, Pageable pageable) {
		// TODO Auto-generated method stub
		return cutiRepo.findAll(spec, pageable);
	}

	@Override
	public Integer findPengajuanWaitingApproved(Long empId) {
		// TODO Auto-generated method stub
		return cutiRepo.findPengajuanWaitingApproved(empId);
	}
}
