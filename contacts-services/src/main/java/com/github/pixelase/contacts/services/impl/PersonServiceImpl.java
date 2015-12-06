package com.github.pixelase.contacts.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.repository.PersonRepository;
import com.github.pixelase.contacts.services.PersonService;
import com.github.pixelase.contacts.services.common.AbstractGenericService;

@Service
@Transactional
public class PersonServiceImpl extends AbstractGenericService<Person, Integer, PersonRepository> implements PersonService {

	@Override
	public List<Person> findAll(String firstName, String lastName) {
		return repository.findAll(firstName, lastName);
	}
	
}
