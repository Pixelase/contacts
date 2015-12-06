package com.github.pixelase.contacts.services;

import java.util.List;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.services.common.GenericService;

public interface PersonService extends GenericService<Person, Integer> {
	List<Person> findAll(String firstName, String lastName);
	
	Person findOne(String firstName, String lastName);
}
