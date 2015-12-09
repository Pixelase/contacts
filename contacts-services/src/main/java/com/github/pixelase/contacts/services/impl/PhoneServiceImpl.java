package com.github.pixelase.contacts.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.model.Phone;
import com.github.pixelase.contacts.dataaccess.repository.PhoneRepository;
import com.github.pixelase.contacts.services.PhoneService;
import com.github.pixelase.contacts.services.common.AbstractGenericService;

@Service
@Transactional
public class PhoneServiceImpl extends AbstractGenericService<Phone, Integer, PhoneRepository> implements PhoneService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PhoneServiceImpl.class);

	@Override
	public Phone delete(String number) {
		LOGGER.info("Deleting {} with number= {}", simpleTypeName, number);
		return repository.delete(number);
	}

	@Override
	public List<Phone> deleteAll(Person person) {
		LOGGER.info("Deleting all {} with {}", simpleTypeName, person);
		return repository.deleteAllByPerson(person);
	}

	@Override
	public List<Phone> findAllByPartialMatching(String number) {
		LOGGER.debug("Finding all {} entities by partial matching with number= {}", simpleTypeName, number);
		List<Phone> found = repository.findAllByNumberContainingIgnoreCase(number);
		LOGGER.trace("Search results: {}", found);

		return found;
	}

	@Override
	public Phone findOne(String number) {
		LOGGER.debug("Finding {} entity with number= {}", simpleTypeName, number);
		Phone found = repository.findOneByNumber(number);
		LOGGER.trace("Search results: {}", found);

		return found;
	}

	@Override
	public Phone findOne(Person person) {
		LOGGER.debug("Finding {} entity with {}", simpleTypeName, person);
		Phone found = repository.findOneByPerson(person);
		LOGGER.trace("Search results: {}", found);

		return found;
	}

}
