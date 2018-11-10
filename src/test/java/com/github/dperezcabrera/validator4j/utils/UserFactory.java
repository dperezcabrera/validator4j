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
import static java.util.Calendar.DATE;
import static java.util.Calendar.YEAR;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class UserFactory {

	private Map<Long, User> usersById = new HashMap<>();
	private Map<Long, Address> addressesById = new HashMap<>();
	private Map<Long, Country> countriesById = new HashMap<>();

	private static Calendar calendar(int field, int amount) {
		Calendar result = Calendar.getInstance();
		result.add(field, amount);
		return result;
	}

	public User build(Long userId) {
		return getOrCreate(userId, usersById, this::createUser);
	}

	public <T> T getOrCreate(Long id, Map<Long, T> repository, Function<Long, T> fnCreate) {
		return repository.computeIfAbsent(id, fnCreate);
	}

	private User createUser(Long userId) {
		Calendar birthay = calendar(YEAR, -18);
		Calendar signUpDate = calendar(DATE, -10);
		return new User(userId,
				"John",
				"john@example.com",
				birthay,
				signUpDate,
				(Calendar) signUpDate.clone(),
				getOrCreate(22L, addressesById, this::createAddress));
	}

	private Address createAddress(Long addressId) {
		return new Address(addressId,
				"Street ",
				"number " + addressId,
				"City",
				"Region",
				"0000",
				getOrCreate(12L, countriesById, this::createCountry));
	}

	private Country createCountry(Long countryId) {
		return new Country(countryId, "Country name", "Language");
	}
}
