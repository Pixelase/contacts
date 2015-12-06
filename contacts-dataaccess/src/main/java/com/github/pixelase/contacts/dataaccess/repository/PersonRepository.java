package com.github.pixelase.contacts.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.pixelase.contacts.dataaccess.model.Person;

public interface PersonRepository extends JpaRepository<Person, Integer> {

	@Query(value = "SELECT * FROM person WHERE first_name ~* :firstName AND last_name ~* :lastName", nativeQuery = true)
	List<Person> findAll(@Param("firstName") String firstName, @Param("lastName") String lastName);

	Person findByFirstNameAndLastName(String firstName, String lastName);
}
