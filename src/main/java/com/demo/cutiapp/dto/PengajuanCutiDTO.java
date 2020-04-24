package com.demo.cutiapp.dto;

import java.util.Date;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class PengajuanCutiDTO {
	private String nip;
	private Date[] tglCuti; 
	private String keterangan;
	private Integer lamaCuti;
	private Date tglApprove;
	private Date tglPengajuan;

}
