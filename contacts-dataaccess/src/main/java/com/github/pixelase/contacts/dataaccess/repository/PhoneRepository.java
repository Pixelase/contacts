package com.github.pixelase.contacts.dataaccess.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import com.github.pixelase.contacts.dataaccess.model.Phone;

public interface PhoneRepository extends JpaRepository<Phone, Integer> {
	
	@Query(value = "SELECT * FROM phone WHERE number ~* :number", nativeQuery = true)
	List<Phone> findAll(@Param("number") String number);
	
	Phone findByNumber(String number);
}
