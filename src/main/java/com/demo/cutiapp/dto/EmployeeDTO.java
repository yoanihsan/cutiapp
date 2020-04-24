package com.demo.cutiapp.dto;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class EmployeeDTO {

	private Long id;
    private String createdDate;
    private String modifiedDate;    
    private boolean deleted;
	private String nip;
	private String name;
	private String address;
}
