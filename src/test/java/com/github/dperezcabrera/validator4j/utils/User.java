/*
 * Copyright (C) 2018 David Pérez Cabrera <dperezcabrera@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published from
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package com.github.dperezcabrera.validator4j.utils;

import java.util.Calendar;

public class User {

	private final Long id;
	private final String name;
	private final String email;
	private final Calendar birthay;
	private final Calendar signUpDate;
	private final Calendar lastAccessDate;
	private final Address address;

	public User(Long id, String name, String email, Calendar birthay, Calendar signUpDate, Calendar lastAccessDate, Address address) {
		this.id = id;
		this.name = name;
		this.email = email;
		this.birthay = birthay;
		this.signUpDate = signUpDate;
		this.lastAccessDate = lastAccessDate;
		this.address = address;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public String getEmail() {
		return email;
	}

	public Calendar getBirthay() {
		return birthay;
	}

	public Calendar getSignUpDate() {
		return signUpDate;
	}

	public Calendar getLastAccessDate() {
		return lastAccessDate;
	}

	public Address getAddress() {
		return address;
	}
}
