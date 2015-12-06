package com.github.pixelase.contacts.services.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.github.pixelase.contacts.dataaccess.model.Phone;
import com.github.pixelase.contacts.dataaccess.repository.PhoneRepository;
import com.github.pixelase.contacts.services.PhoneService;
import com.github.pixelase.contacts.services.common.AbstractGenericService;

@Service
@Transactional
public class PhoneServiceImpl extends AbstractGenericService<Phone, Integer, PhoneRepository> implements PhoneService {
	
	public List<Phone> findAll(String number) {
		return repository.findAll(number);
	}

	
}
