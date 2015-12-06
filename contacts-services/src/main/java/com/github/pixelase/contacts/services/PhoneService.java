package com.github.pixelase.contacts.services;

import java.util.List;

import com.github.pixelase.contacts.dataaccess.model.Phone;
import com.github.pixelase.contacts.services.common.GenericService;

public interface PhoneService extends GenericService<Phone, Integer> {
	List<Phone> findAll(String number);
	
	Phone findOne(String number);
}
