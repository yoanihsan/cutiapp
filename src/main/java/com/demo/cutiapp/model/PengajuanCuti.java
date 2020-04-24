package com.demo.cutiapp.model;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "pengajuan_cuti")
@AllArgsConstructor @NoArgsConstructor @ToString @Getter @Setter
public class PengajuanCuti extends BaseModel{
	
	@OneToMany(cascade=CascadeType.ALL)
	private List<PengajuanCutiDetail> pengajuanCutiDetail;

	@Column(name="keterangan")
	private String keterangan;
	
	@Column(name="lama_cuti")
	private Integer lamaCuti;
	
	@Column(name="approve")
	private Boolean approve=Boolean.FALSE;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tgl_approve")
	private Date tglApprove;
	
	@Temporal(TemporalType.DATE)
	@Column(name="tgl_pengajuan")
	private Date tglPengajuan;

	@ManyToOne
	private Employee employee;
}
