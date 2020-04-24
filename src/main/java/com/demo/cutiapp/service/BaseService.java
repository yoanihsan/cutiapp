package com.demo.cutiapp.service;

import java.util.List;

import org.springframework.data.repository.NoRepositoryBean;

import com.demo.cutiapp.model.BaseModel;

@NoRepositoryBean
public interface BaseService<B extends BaseModel, ID> {
	
	public List<B> findAll();
	
	public B findById(ID id);
	
	public void save(B entity);
	
	public void update(ID id, B entity);
	
	public void softDelete(ID id);
	
}
