package com.demo.cutiapp.model;

import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.*;
 
@Entity
@Table(name = "employee")
@AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class Employee extends BaseModel {

	private String nip;
	private String name;
	private String address;
	
}
