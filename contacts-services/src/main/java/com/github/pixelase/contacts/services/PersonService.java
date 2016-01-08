package com.github.pixelase.contacts.services;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.services.common.GenericService;

import java.util.List;

public interface PersonService extends GenericService<Person, Integer> {
    Person delete(String firstName, String lastName);

    List<Person> findAllByPartialMatching(String firstName, String lastName);

    Person findOne(String firstName, String lastName);
}
