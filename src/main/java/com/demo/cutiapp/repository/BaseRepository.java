package com.demo.cutiapp.repository;

import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.NoRepositoryBean;

import com.demo.cutiapp.model.BaseModel;

@NoRepositoryBean
public interface BaseRepository<T extends BaseModel, ID> extends JpaRepository<T, ID> {
	
	@Query("select e from #{#entityName} e where e.deleted=false")
    public List<T> findAll();
	
	@Query("select e from #{#entityName} e where e.deleted=false and e.id=?1")
    public Optional<T> findById(ID id);
	
	@Query("select e from #{#entityName} e where e.deleted=true")
    public List<T> findAllDeleted();
	
	@Transactional
	@Modifying
	@Query("update #{#entityName} e set e.deleted=true where e.id=?1 and e.deleted=false")
    public void softDelete(ID id);
	
}
