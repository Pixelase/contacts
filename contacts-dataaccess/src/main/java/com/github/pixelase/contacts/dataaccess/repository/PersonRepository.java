package com.github.pixelase.contacts.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.pixelase.contacts.dataaccess.model.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {

	@Query(value = "DELETE FROM person WHERE first_name = ?1 AND last_name = ?2 RETURNING *", nativeQuery = true)
	Person delete(String firstName, String lastName);

	List<Person> findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName,
			String lastName);

	Person findOneByFirstNameAndLastName(String firstName, String lastName);
}
