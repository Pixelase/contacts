package com.github.pixelase.contacts.dataaccess.model;
// Generated 06.12.2015 22:21:09 by Hibernate Tools 4.3.1.Final

import java.util.HashSet;
import java.util.Set;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.springframework.data.domain.Persistable;

import com.github.pixelase.contacts.dataaccess.model.Person;
import com.github.pixelase.contacts.dataaccess.model.Phone;

/**
 * Person generated by hbm2java
 */
@Entity
@Table(name = "person")
public class Person implements Persistable<Integer> {

	private static final long serialVersionUID = 1L;
	private Integer id;
	private String firstName;
	private String lastName;
	private Set<Phone> phones = new HashSet<Phone>(0);

	public Person() {
		super();
	}

	public Person(Integer id) {
		super();
		this.id = id;
	}

	public Person(String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
	}

	public Person(Integer id, String firstName, String lastName) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
	}

	@Id
	@GeneratedValue
	@Column(name = "id", unique = true, nullable = false)
	public Integer getId() {
		return this.id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	@Column(name = "first_name")
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	@Column(name = "last_name")
	public String getLastName() {
		return this.lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	@OneToMany(fetch = FetchType.LAZY, mappedBy = "person")
	public Set<Phone> getPhones() {
		return this.phones;
	}

	public void setPhones(Set<Phone> phones) {
		this.phones = phones;
	}
	
	@Override
	@Transient
	public boolean isNew() {
		return id == null;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Person))
			return false;
		Person other = (Person) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + ", firstName=" + firstName + ", lastName=" + lastName + ", phones=" + phones + "]";
	}

	public String toSimpleFormat() {
		return String.format("%s %s", firstName, lastName);
	}
	
	public String toDetailedFormat() {
		StringBuilder result = new StringBuilder();
		result.append(String.format("%s %s\n\nPhone numbers: \n", firstName, lastName));
		
		for (Phone phone : phones) {
			result.append(String.format("* %s\n", phone.getNumber()));
		}

		return result.toString();
	}

}
