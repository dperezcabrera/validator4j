/*
 * Copyright (C) 2016-2018 David Pérez Cabrera <dperezcabrera@gmail.com>
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
package com.github.dperezcabrera.validator4j;

import com.github.dperezcabrera.validator4j.core.Selector;
import com.github.dperezcabrera.validator4j.core.ValidatorException;
import java.util.Calendar;
import org.apache.commons.lang3.time.DateUtils;
import static com.github.dperezcabrera.validator4j.provider.builders.CalendarProviderBuilder.*;
import static com.github.dperezcabrera.validator4j.provider.builders.IntegerProviderBuilder.*;
import static com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.*;
import static com.github.dperezcabrera.validator4j.rules.ParametrizedRuleBuilderBase.*;
import static com.github.dperezcabrera.validator4j.rules.StringRuleBuilder.*;
import static com.github.dperezcabrera.validator4j.ValidatorBuilder.*;
import static com.github.dperezcabrera.validator4j.rules.CalendarRuleBuilder.dateRule;
import com.github.dperezcabrera.validator4j.utils.User;
import com.github.dperezcabrera.validator4j.utils.UserFactory;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.junit.Assert.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class ValidatorTests {

	private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

	private Validator createUserValidator = rules(stringRule("id").mustBeNull(),
			stringRule("name").notNull().startsWith("na").contains("am").endsWith("me"),
			cmpRule("child").notNull().range(2, 5),
			stringRule("email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
			dateRule("birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
			dateRule("activationDate").notNull(),
			dateRule("deactivatedDate").after(getCalendar("activationDate").add(MONTH, 3))
	).build();

	@Test
	public void integrationBasicTestCheckOk() {
		String id = null;
		String name = "name";
		String email = "email@a.com";
		Calendar birthay = Calendar.getInstance();
		Calendar activationDate = Calendar.getInstance();
		Calendar deactivationDate = Calendar.getInstance();
		birthay.add(YEAR, -18);
		activationDate = DateUtils.ceiling(activationDate, DATE);
		deactivationDate.add(DATE, 1);
		deactivationDate.add(MONTH, 3);
		int child = 3;

		Selector selector = createUserValidator.validate(id, name, child, email, birthay, activationDate, deactivationDate);
		String resultEmail = selector.select("email", String.class);
		Calendar resultBirthay = selector.select("birthay", Calendar.class);

		assertEquals(email, resultEmail);
		assertEquals(birthay, resultBirthay);
	}

	@Test
	public void integrationTestCheckFails() {
		String name = "name";
		String id = "";
		String email = "email@";
		Calendar birthay = Calendar.getInstance();
		Calendar activationDate = DateUtils.ceiling(Calendar.getInstance(), DATE);
		Calendar deactivationDate = Calendar.getInstance();
		birthay.add(YEAR, -18);
		birthay.add(DATE, 1);
		deactivationDate.add(MONTH, 3);
		int child = 6;

		assertThrows(ValidatorException.class, () -> createUserValidator.validate(id, name, child, email, birthay, activationDate, deactivationDate));
	}

	@Test
	public void integrationComplexTestCheckOk() {
		UserFactory userFactory = new UserFactory();
		Long userId = 1L;

		Validator obtainUserValidator = rules(cmpRule("user.id").notNull().greatherThan(0L),
				stringRule("user.name").notNull().notEmpty().startsWith(stringFrom("namePrefix")).endsWith(stringFrom("nameSufix")).contains(stringFrom("nameContains")).length(getInteger("nameLength")),
				stringRule("user.email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
				dateRule("user.birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
				dateRule("user.signUpDate").notNull().before(now().ceil(DATE).add(DATE, 1)),
				dateRule("user.lastAccessDate").notNull().after(getCalendar("user.signUpDate").truncate(DATE).sub(DATE, 1)),
				objectRule("user.address").notNull(),
				cmpRule("user.address.id").notNull().lessThan(1000L),
				stringRule("user.address.address0").notNull().maxLength(24),
				stringRule("user.address.address1").notNull().maxLength(24),
				stringRule("user.address.city").notNull().minLength(2).maxLength(24),
				stringRule("user.address.region").notNull().minLength(2).maxLength(24),
				stringRule("user.address.zipCode").notNull().minLength(4).maxLength(8),
				objectRule("user.address.country").notNull(),
				cmpRule("user.address.country.id").notNull().greatherThan(0L),
				stringRule("user.address.country.name").notNull().minLength(3).maxLength(12),
				stringRule("user.address.country.language").notNull().minLength(4),
				stringRule("namePrefix").notNull().maxLength(24),
				stringRule("nameSufix").notNull().maxLength(getInteger("nameLength")),
				stringRule("nameContains").notNull().maxLength(getInteger("nameLength")),
				objectRule("nameLength").notNull()
		).build();

		User expectedResult = userFactory.build(userId);
		Integer nameLength = expectedResult.getName().length();
		String namePrefix = "J";
		String nameSufix = "n";
		String nameContains = "ohn";

		Selector selector = obtainUserValidator.validate(userFactory.build(userId), namePrefix, nameSufix, nameContains, nameLength);

		User result = selector.select("user", User.class);

		assertEquals(expectedResult, result);
	}

	@Test
	public void integrationComplexTestIncludeOk() {
		UserFactory userFactory = new UserFactory();
		Long userId = 1L;
		
		Validator countryValidator = rules(
				cmpRule("c.id").notNull().greatherThan(0L),
				stringRule("c.name").notNull().minLength(3).maxLength(12),
				stringRule("c.language").notNull().minLength(4)
		).build();

		Validator addressValidator = rules(
				cmpRule("address.id").notNull().lessThan(1000L),
				stringRule("address.address0").notNull().maxLength(24),
				stringRule("address.address1").notNull().maxLength(24),
				stringRule("address.city").notNull().minLength(2).maxLength(24),
				stringRule("address.region").notNull().minLength(2).maxLength(24),
				stringRule("address.zipCode").notNull().minLength(4).maxLength(8),
				objectRule("address.country").notNull()
		).include(countryValidator, "address.country", "c").build();

		Validator otherValidator = rules(stringRule("namePrefix").notNull().maxLength(24),
				stringRule("nameSufix").notNull().maxLength(getInteger("nameLength")),
				stringRule("nameContains").notNull().minLength(getInteger("nameLength").div(string("5").toLong().toInteger().add(getInteger("nameLength")).remain(now().dayOfMonth()))),
				objectRule("nameLength").notNull()
		).build();

		Validator obtainUserValidator = rules(cmpRule("user.id").notNull().greatherThan(0L),
				stringRule("user.name").notNull().notEmpty().startsWith(stringFrom("namePrefix")).endsWith(stringFrom("nameSufix")).contains(stringFrom("nameContains")).length(getInteger("nameLength")),
				stringRule("user.email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
				dateRule("user.birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
				dateRule("user.signUpDate").notNull().before(now().ceil(DATE).add(DATE, 1)),
				dateRule("user.lastAccessDate").notNull().after(getCalendar("user.signUpDate").truncate(DATE).sub(DATE, 1)),
				objectRule("user.address").notNull()
		).include(addressValidator, "user").include(otherValidator).build();

		User expectedResult = userFactory.build(userId);
		Integer nameLength = expectedResult.getName().length();
		String namePrefix = "J";
		String nameSufix = "n";
		String nameContains = "ohn";

		Selector selector = obtainUserValidator.validate(userFactory.build(userId), namePrefix, nameSufix, nameContains, nameLength);

		User result = selector.select("user", User.class);

		assertEquals(expectedResult, result);
	}
}
