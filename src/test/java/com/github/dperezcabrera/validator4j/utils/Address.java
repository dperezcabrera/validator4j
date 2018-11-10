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

public class Address {

	private final Long id;
	private final String address0;
	private final String address1;
	private final String city;
	private final String region;
	private final String zipCode;
	private final Country country;

	public Address(Long id, String address0, String address1, String city, String region, String zipCode, Country country) {
		this.id = id;
		this.address0 = address0;
		this.address1 = address1;
		this.city = city;
		this.region = region;
		this.zipCode = zipCode;
		this.country = country;
	}

	public Long getId() {
		return id;
	}

	public String getAddress0() {
		return address0;
	}

	public String getAddress1() {
		return address1;
	}

	public String getCity() {
		return city;
	}

	public String getRegion() {
		return region;
	}

	public String getZipCode() {
		return zipCode;
	}

	public Country getCountry() {
		return country;
	}
}
