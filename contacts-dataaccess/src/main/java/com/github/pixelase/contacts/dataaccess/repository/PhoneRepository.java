package com.github.pixelase.contacts.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {

	@Query(value = "DELETE FROM phone WHERE number = ?1 RETURNING *", nativeQuery = true)
	Phone delete(String number);
	
	List<Phone> deleteAllByPerson(Person person);

	List<Phone> findAllByNumberContainingIgnoreCase(String number);

	Phone findOneByNumber(String number);

	Phone findOneByPerson(Person person);
}
