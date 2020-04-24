package com.demo.cutiapp.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import com.demo.cutiapp.dto.EmployeeDTO;

import lombok.*;
 
@Entity
@Table(name = "employee")
@AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class Employee extends BaseModel {

	private String nip;
	private String name;
	private String address;
	private Integer jatahCuti;
	private Integer sisaCuti;
	
	 public void setForUpdate(EmployeeDTO empDTO) {
	    	this.name = empDTO.getName();
	    	this.nip = empDTO.getNip();
	    	this.address = empDTO.getAddress();
	    	this.jatahCuti = empDTO.getJatahCuti();
	    	this.sisaCuti = empDTO.getSisaCuti();
	 }
	
}
