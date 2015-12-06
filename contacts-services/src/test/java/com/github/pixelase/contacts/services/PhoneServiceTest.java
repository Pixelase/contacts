package com.github.pixelase.contacts.services;

import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.model.Phone;
import com.github.pixelase.contacts.services.common.AbstractServiceTest;

@Transactional
public class PhoneServiceTest extends AbstractServiceTest<Phone, Integer, PhoneService> {

	@Autowired
	PersonService personService;

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
	public void findAllByNumberTest() {
		List<Phone> phones = new ArrayList<>();
		
		for (int i = 0; i < RandomUtils.nextInt(1, MAX_ENTITIES_COUNT + 1); i++) {
			phones.add(entity);
		}
		
		List<Phone> saved = service.save(phones);		
		List<Phone> found = service.findAll(entity.getNumber());
		
		Assert.assertEquals(saved, found);
	}
}
