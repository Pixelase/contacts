package com.github.pixelase.contacts.services.impl;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.repository.PersonRepository;
import com.github.pixelase.contacts.services.PersonService;
import com.github.pixelase.contacts.services.common.AbstractGenericService;

@Service
@Transactional
public class PersonServiceImpl extends AbstractGenericService<Person, Integer, PersonRepository>
		implements PersonService {

	private static final Logger LOGGER = LoggerFactory.getLogger(PersonServiceImpl.class);
	
	@Override
	public Person delete(String firstName, String lastName) {
		LOGGER.info("Deleting {} with firstName= \"{}\" and lastName= \"{}\"", simpleTypeName, firstName, lastName);
		return repository.delete(firstName, lastName);
	}

	@Override
	public List<Person> findAllByPartialMatching(String firstName, String lastName) {
		LOGGER.debug("Finding all {} entities by partial matching (firstName= \"{}\" and lastName= \"{}\")",
				simpleTypeName, firstName, lastName);
		List<Person> found = repository
				.findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(firstName, lastName);
		LOGGER.trace("Search results: {}", found);

		return found;
	}

	@Override
	public Person findOne(String firstName, String lastName) {
		LOGGER.debug("Finding {} entity with firstName= \"{}\" and lastName= \"{}\"", simpleTypeName, firstName,
				lastName);
		Person found = repository.findOneByFirstNameAndLastName(firstName, lastName);
		LOGGER.trace("Search results: {}", found);

		return found;
	}



}
