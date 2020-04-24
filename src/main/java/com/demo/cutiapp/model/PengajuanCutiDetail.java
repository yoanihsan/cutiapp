package com.demo.cutiapp.model;

import java.util.Date;

import javax.persistence.Entity;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;


@Entity
@Table(name = "pengajuan_cuti_detail")
@AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class PengajuanCutiDetail extends BaseModel{
	
	@Temporal(TemporalType.DATE)
	private Date tglCuti;
	
}
