package com.github.pixelase.contacts.services.common;

import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Persistable;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.jdbc.SqlScriptsTestExecutionListener;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.support.DependencyInjectionTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextBeforeModesTestExecutionListener;
import org.springframework.test.context.support.DirtiesContextTestExecutionListener;
import org.springframework.test.context.transaction.TransactionalTestExecutionListener;
import org.springframework.transaction.annotation.Transactional;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = "classpath:spring-services-context.xml")
@Transactional
@TestExecutionListeners(listeners = {DirtiesContextBeforeModesTestExecutionListener.class,
        DirtiesContextTestExecutionListener.class, DependencyInjectionTestExecutionListener.class,
        TransactionalTestExecutionListener.class, SqlScriptsTestExecutionListener.class})
public abstract class AbstractServiceTest<T extends Persistable<ID>, ID extends Serializable, SERVICE extends GenericService<T, ID>> {

    protected static final int MAX_ENTITIES_COUNT = 20;
    protected static final int MAX_STRING_LENGTH = 10;
    protected static final int MAX_NUMBER = 1000;

    protected final T entity;
    protected final Iterable<? extends T> entities;
    protected final ID id;
    protected final Sort sort;
    protected final Pageable pageable;
    protected final List<String> columnNames;

    @Autowired
    protected SERVICE service;

    public AbstractServiceTest() {
        this.entity = generateEntity();
        this.entities = generateEntities(MAX_ENTITIES_COUNT);
        this.id = generateId();
        this.sort = generateSort();
        this.pageable = generatePageable();
        this.columnNames = getFieldsNames();
    }

    protected abstract T generateEntity();

    protected abstract Iterable<? extends T> generateEntities(int maxEntitiesCount);

    protected abstract ID generateId();

    protected Sort generateSort() {
        return new Sort(getRandomFieldName());
    }

    protected Pageable generatePageable() {
        return new PageRequest(1, RandomUtils.nextInt(1, MAX_ENTITIES_COUNT));
    }

    protected List<String> getFieldsNames() {
        List<String> result = new ArrayList<>();

        for (Field field : entity.getClass().getDeclaredFields()) {
            if (field.getName().equals("serialVersionUID")) {
                continue;
            }
            result.add(field.getName());
        }
        return result;
    }

    protected String getRandomFieldName() {
        return getFieldsNames().get(RandomUtils.nextInt(0, getFieldsNames().size()));
    }

    @Test
    public void countEntitiesTest() {
        long before = service.count();
        service.save(entity);
        long after = service.count();
        Assert.assertEquals(before + 1, after);
    }

    @Test
    public void deleteEntityByIdTest() {
        ID savedId = service.save(entity).getId();
        service.delete(savedId);
        Assert.assertFalse(service.exists(savedId));
    }

    @Test
    public void deleteSequenceOfEntitiesTest() {
        Iterable<? extends T> saved = service.save(entities);
        List<ID> ids = new ArrayList<>();

        for (T t : saved) {
            ids.add(t.getId());
        }

        service.delete(saved);
        Assert.assertEquals(Collections.EMPTY_LIST, service.findAll(ids));
    }

    @Test
    public void deleteEntityTest() {
        T saved = service.save(entity);
        service.delete(saved);
        Assert.assertFalse(service.exists(saved.getId()));
    }

    @Test
    public void deleteAllEntitiesTest() {
        service.save(entity);
        service.deleteAll();
        Assert.assertTrue(service.count() == 0);
    }

    @Test
    public void deleteAllEntitiesInBatchTest() {
        service.save(entity);
        service.deleteAllInBatch();
        Assert.assertTrue(service.count() == 0);
    }

    @Test
    public void deleteSequenceOfEntitiesInBatchTest() {
        @SuppressWarnings("unchecked")
        Iterable<T> saved = (Iterable<T>) service.save(entities);
        List<ID> ids = new ArrayList<>();

        for (T t : saved) {
            ids.add(t.getId());
        }

        service.deleteInBatch(saved);
        Assert.assertEquals(Collections.EMPTY_LIST, service.findAll(ids));
    }

    @Test
    public void entityExistenceByIdTest() {
        T saved = service.save(entity);
        Assert.assertTrue(service.exists(saved.getId()));
    }

    @Test
    public void findAllEntitiesTest() {
        service.deleteAll();
        Iterable<? extends T> saved = service.save(entities);
        Iterable<T> found = service.findAll();
        Assert.assertEquals(saved, found);
    }

    @Test
    public void findAllEntitiesByIds() {
        Iterable<? extends T> saved = service.save(entities);
        List<ID> ids = new ArrayList<>();

        for (T t : saved) {
            ids.add(t.getId());
        }

        Iterable<T> found = service.findAll(ids);
        Assert.assertEquals(saved, found);
    }

    @Test
    public void findAllEntitiesByPageableTest() {
        Iterable<T> found = service.findAll(sort);
        service.delete(found);
        Assert.assertEquals(Collections.EMPTY_LIST, service.findAll(sort));
    }

    @Test
    public void findAllEntitiesBySortTest() {
        Iterable<T> found = service.findAll(sort);
        service.delete(found);
        Assert.assertEquals(Collections.EMPTY_LIST, service.findAll(sort));
    }

    @Test
    public void findOneEntityByIdTest() {
        T found = service.findOne(id);
        Assert.assertEquals(found == null, !service.exists(id));
    }

    @Test
    public void flushPendingChangesTest() {
        // Just invoke this method
        service.flush();
    }

    @Test
    public void saveSequenceOfEntitiesTest() {
        Iterable<? extends T> saved = service.save(entities);

        List<ID> ids = new ArrayList<>();

        for (T t : saved) {
            ids.add(t.getId());
        }

        Assert.assertEquals(saved, service.findAll(ids));
    }

    @Test
    public void saveOneEntityTest() {
        T saved = service.save(entity);
        Assert.assertEquals(entity, service.findOne(saved.getId()));
    }

    @Test
    public void saveOneEntityAndFlushTest() {
        T saved = service.save(entity);
        Assert.assertEquals(entity, service.findOne(saved.getId()));
    }

}
