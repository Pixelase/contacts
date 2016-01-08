package com.github.pixelase.contacts.services;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.model.Phone;
import com.github.pixelase.contacts.services.common.AbstractServiceTest;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Transactional
public class PhoneServiceTest extends AbstractServiceTest<Phone, Integer, PhoneService> {

    @Autowired
    private PersonService personService;

    @Before
    public void before() {
        final Person person = personService.save(
                new Person(RandomStringUtils.random(MAX_STRING_LENGTH), RandomStringUtils.random(MAX_STRING_LENGTH)));

        entity.setPerson(person);

        for (Phone phone : entities) {
            phone.setPerson(person);
        }
    }

    @Override
    protected Phone generateEntity() {
        return new Phone(RandomStringUtils.random(MAX_STRING_LENGTH), null);
    }

    @Override
    protected Iterable<? extends Phone> generateEntities(int entitieCount) {
        List<Phone> list = new ArrayList<>();

        for (int i = 0; i < MAX_ENTITIES_COUNT; i++) {
            list.add(generateEntity());
        }

        return list;
    }

    @Override
    protected Integer generateId() {
        return RandomUtils.nextInt(1, MAX_NUMBER);
    }

    @Test
    public void deletePhoneByNumberTest() {
        Phone saved = service.save(entity);
        Phone deleted = service.delete(entity.getNumber());

        Assert.assertEquals(saved, deleted);
    }

    @Test
    public void deleteAllPhonesByPersonTest() {
        List<Phone> employees = new ArrayList<>();

        for (int i = 0; i < RandomUtils.nextInt(1, MAX_ENTITIES_COUNT + 1); i++) {
            employees.add(new Phone(entity.getNumber(), entity.getPerson()));
        }

        List<Phone> savedPhones = service.save(employees);
        List<Phone> deletedPhones = service.deleteAll(entity.getPerson());

        Assert.assertEquals(savedPhones, deletedPhones);
    }

    @Test
    public void findAllPhonesByPartialMatchingTest() {
        List<Phone> employees = new ArrayList<>();

        for (int i = 0; i < RandomUtils.nextInt(1, MAX_ENTITIES_COUNT + 1); i++) {
            employees.add(new Phone(entity.getNumber() + RandomStringUtils.random(5), entity.getPerson()));
        }

        List<Phone> saved = service.save(employees);
        List<Phone> found = service.findAllByPartialMatching(entity.getNumber());

        Assert.assertEquals(saved, found);
    }

    @Test
    public void findOnePhoneByNumberTest() {
        Phone saved = service.save(entity);

        Phone found = service.findOne(saved.getNumber());

        Assert.assertEquals(saved, found);
    }

    @Test
    public void findOnePhoneByPersonTest() {
        Phone saved = service.save(entity);

        Phone found = service.findOne(saved.getPerson());

        Assert.assertEquals(saved, found);
    }
}
