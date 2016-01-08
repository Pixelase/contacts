package com.github.pixelase.contacts.dataaccess.repository;

import com.github.pixelase.contacts.dataaccess.model.Person;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface PersonRepository extends JpaRepository<Person, Integer> {

    @Query(value = "DELETE FROM person WHERE first_name = ?1 AND last_name = ?2 RETURNING *", nativeQuery = true)
    Person delete(String firstName, String lastName);

    List<Person> findAllByFirstNameContainingIgnoreCaseAndLastNameContainingIgnoreCase(String firstName,
                                                                                       String lastName);

    Person findOneByFirstNameAndLastName(String firstName, String lastName);
}
