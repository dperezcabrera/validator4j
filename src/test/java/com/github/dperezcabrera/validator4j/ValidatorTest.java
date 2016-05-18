/*
 * Copyright (C) 2016 David Pérez Cabrera <dperezcabrera@gmail.com>
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
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.apache.commons.lang3.time.DateUtils;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import static com.github.dperezcabrera.validator4j.provider.builders.CalendarProviderBuilder.*;
import static com.github.dperezcabrera.validator4j.provider.builders.IntegerProviderBuilder.*;
import static com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.*;
import static com.github.dperezcabrera.validator4j.rules.ParametrizedRuleBuilderBase.*;
import static com.github.dperezcabrera.validator4j.rules.StringRuleBuilder.*;
import static com.github.dperezcabrera.validator4j.ValidatorBuilder.*;
import static com.github.dperezcabrera.validator4j.rules.CalendarRuleBuilder.dateRule;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static org.junit.Assert.assertEquals;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class ValidatorTest {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private Validator createUserValidator = rules(stringRule("id").mustBeNull(),
            stringRule("name").notNull().startsWith("na").contains("am").endsWith("me"),
            cmpRule("child").notNull().range(2, 5),
            stringRule("email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
            dateRule("birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
            dateRule("activationDate").notNull(),
            dateRule("deactivatedDate").after(getCalendar("activationDate").add(MONTH, 3))
    ).build();
    
    

    @Rule
    public ExpectedException thrown = ExpectedException.none();

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
        Calendar activationDate = Calendar.getInstance();
        Calendar deactivationDate = Calendar.getInstance();
        birthay.add(YEAR, -18);
        birthay.add(DATE, 1);
        activationDate = DateUtils.ceiling(activationDate, DATE);
        deactivationDate.add(MONTH, 3);
        int child = 6;

        thrown.expect(ValidatorException.class);

        createUserValidator.validate(id, name, child, email, birthay, activationDate, deactivationDate);
    }

    @Test
    public void integrationComplexTestCheckOk() {
        UserRepository userRepository = new UserRepository();
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
        User expectedResult = userRepository.findOne(userId);
        Integer nameLength = expectedResult.getName().length();

        String namePrefix = "J";
        String nameSufix = "n";
        String nameContains = "ohn";

        Selector selector = obtainUserValidator.validate(userRepository.findOne(userId), namePrefix, nameSufix, nameContains, nameLength);

        User result = selector.select("user", User.class);

        assertEquals(expectedResult, result);
    }

    @Test
    public void integrationComplexTestIncludeOk() {
        UserRepository userRepository = new UserRepository();
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

        User expectedResult = userRepository.findOne(userId);
        Integer nameLength = expectedResult.getName().length();

        String namePrefix = "J";
        String nameSufix = "n";
        String nameContains = "ohn";

        Selector selector = obtainUserValidator.validate(userRepository.findOne(userId), namePrefix, nameSufix, nameContains, nameLength);

        User result = selector.select("user", User.class);

        assertEquals(expectedResult, result);
    }

    public static class UserRepository {

        private Calendar birthay = Calendar.getInstance();
        private Calendar signUpDate = Calendar.getInstance();

        public UserRepository() {
            birthay.add(YEAR, -18);
            signUpDate.add(DATE, -10);
        }

        public User findOne(Long userId) {
            User user = new User();
            user.setId(userId);
            user.setName("John");
            user.setEmail("john@example.com");
            user.setBirthay(birthay);
            user.setSignUpDate(signUpDate);
            user.setLastAccessDate((Calendar) signUpDate.clone());
            Address address = new Address();
            user.setAddress(address);
            address.setId(22L);
            address.setAddress0("Street 0");
            address.setAddress1("number 1");
            address.setCity("City");
            address.setRegion("Region");
            address.setZipCode("0000");
            Country country = new Country();
            address.setCountry(country);
            country.setId(12L);
            country.setName("Country name");
            country.setLanguage("Language");
            return user;
        }
    }

    public static abstract class AbtractEntity {

        private Long id;

        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        @Override
        public String toString() {
            return ToStringBuilder.reflectionToString(this);
        }

        @Override
        public int hashCode() {
            return HashCodeBuilder.reflectionHashCode(this);
        }

        @Override
        public boolean equals(Object obj) {
            return EqualsBuilder.reflectionEquals(this, obj);
        }
    }

    public static class User extends AbtractEntity {

        private String name;
        private String email;
        private Calendar birthay;
        private Calendar signUpDate;
        private Calendar lastAccessDate;
        private Address address;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public Calendar getBirthay() {
            return birthay;
        }

        public void setBirthay(Calendar birthay) {
            this.birthay = birthay;
        }

        public Calendar getSignUpDate() {
            return signUpDate;
        }

        public void setSignUpDate(Calendar signUpDate) {
            this.signUpDate = signUpDate;
        }

        public Calendar getLastAccessDate() {
            return lastAccessDate;
        }

        public void setLastAccessDate(Calendar lastAccessDate) {
            this.lastAccessDate = lastAccessDate;
        }

        public Address getAddress() {
            return address;
        }

        public void setAddress(Address address) {
            this.address = address;
        }
    }

    public static class Address extends AbtractEntity {

        private String address0;
        private String address1;
        private String city;
        private String region;
        private String zipCode;
        private Country country;

        public String getAddress0() {
            return address0;
        }

        public void setAddress0(String address0) {
            this.address0 = address0;
        }

        public String getAddress1() {
            return address1;
        }

        public void setAddress1(String address1) {
            this.address1 = address1;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getRegion() {
            return region;
        }

        public void setRegion(String region) {
            this.region = region;
        }

        public String getZipCode() {
            return zipCode;
        }

        public void setZipCode(String zipCode) {
            this.zipCode = zipCode;
        }

        public Country getCountry() {
            return country;
        }

        public void setCountry(Country country) {
            this.country = country;
        }
    }

    public static class Country extends AbtractEntity {

        private String language;
        private String name;

        public String getLanguage() {
            return language;
        }

        public void setLanguage(String language) {
            this.language = language;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
