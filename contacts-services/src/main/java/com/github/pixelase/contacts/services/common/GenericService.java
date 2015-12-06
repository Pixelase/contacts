package com.github.pixelase.contacts.services.common;

import java.io.Serializable;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;

public interface GenericService<T extends Persistable<ID>, ID extends Serializable> {

	public long count();

	public void delete(ID id);

	public void delete(Iterable<? extends T> entities);

	public void delete(T entity);

	public void deleteAll();

	public void deleteAllInBatch();

	public void deleteInBatch(Iterable<T> entities);

	public boolean exists(ID id);

	public List<T> findAll();

	public Iterable<T> findAll(Iterable<ID> ids);

	public Page<T> findAll(Pageable pageable);

	public List<T> findAll(Sort sort);

	public T findOne(ID id);

	public void flush();

	public <S extends T> List<S> save(Iterable<S> entities);

	public <S extends T> S save(S entity);

	public T saveAndFlush(T entity);
}
