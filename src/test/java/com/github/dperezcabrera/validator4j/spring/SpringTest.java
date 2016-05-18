/*
 * Copyright (C) 2016 David Pérez Cabrera <dperezcabrera@gmail.com>
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
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
package com.github.dperezcabrera.validator4j.spring;

import com.github.dperezcabrera.validator4j.Validator;
import com.github.dperezcabrera.validator4j.ValidatorRegistry;
import com.github.dperezcabrera.validator4j.ValidatorTest;
import static com.github.dperezcabrera.validator4j.provider.builders.CalendarProviderBuilder.now;
import static com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.stringFrom;
import static com.github.dperezcabrera.validator4j.rules.CalendarRuleBuilder.dateRule;
import static com.github.dperezcabrera.validator4j.rules.ComparableRuleBuilder.cmpRule;
import static com.github.dperezcabrera.validator4j.rules.ParametrizedRuleBuilderBase.objectRule;
import static com.github.dperezcabrera.validator4j.rules.StringRuleBuilder.stringRule;
import com.github.dperezcabrera.validator4j.spring.SpringTest.ConfigFoo;
import static java.util.Calendar.DATE;
import static java.util.Calendar.YEAR;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import static com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.string;
import com.github.dperezcabrera.validator4j.ValidatorTest.User;
import static com.github.dperezcabrera.validator4j.provider.builders.IntegerProviderBuilder.getInteger;
import static com.github.dperezcabrera.validator4j.provider.builders.CalendarProviderBuilder.getCalendar;
import static com.github.dperezcabrera.validator4j.ValidatorBuilder.rules;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = ConfigFoo.class)
public class SpringTest {

    private static final String EMAIL_PATTERN = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

    private static final Validator COUNTRY_VALIDATOR = ValidatorRegistry.register("country_validator",
            cmpRule("c.id").notNull().greatherThan(0L),
            stringRule("c.name").notNull().minLength(3).maxLength(12),
            stringRule("c.language").notNull().minLength(4)
    );

    private static final Validator ADDRESS_VALIDATOR = ValidatorRegistry.register("address_validator", rules(
            cmpRule("address.id").notNull().lessThan(1000L),
            stringRule("address.address0").notNull().maxLength(24),
            stringRule("address.address1").notNull().maxLength(24),
            stringRule("address.city").notNull().minLength(2).maxLength(24),
            stringRule("address.region").notNull().minLength(2).maxLength(24),
            stringRule("address.zipCode").notNull().minLength(4).maxLength(8),
            objectRule("address.country").notNull()
    ).include(COUNTRY_VALIDATOR, "address.country", "c"));

    private static final Validator OTHER_VALIDATOR = ValidatorRegistry.register("other_validator",
            stringRule("namePrefix").notNull().maxLength(24),
            stringRule("nameSufix").notNull().maxLength(getInteger("nameLength")),
            stringRule("nameContains").notNull().minLength(getInteger("nameLength").div(string("5").toLong().toInteger().add(getInteger("nameLength")).remain(now().dayOfMonth()))),
            objectRule("nameLength").notNull()
    );

    private static final Validator USER_VALIDATOR = ValidatorRegistry.register("user_validator", rules(cmpRule("user.id").notNull().greatherThan(0L),
            stringRule("user.name").notNull().notEmpty().startsWith(stringFrom("namePrefix")).endsWith(stringFrom("nameSufix")).contains(stringFrom("nameContains")).length(getInteger("nameLength")),
            stringRule("user.email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
            dateRule("user.birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
            dateRule("user.signUpDate").notNull().before(now().ceil(DATE).add(DATE, 1)),
            dateRule("user.lastAccessDate").notNull().after(getCalendar("user.signUpDate").truncate(DATE).sub(DATE, 1)),
            objectRule("user.address").notNull()
    ).include(ADDRESS_VALIDATOR, "user").include(OTHER_VALIDATOR));

    @Autowired
    Foo target;

    @Test
    public void integrationComplexTestIncludeOk() {
        ValidatorTest.UserRepository userRepository = new ValidatorTest.UserRepository();
        Long userId = 1L;

        User expectedResult = userRepository.findOne(userId);
        int nameLength = expectedResult.getName().length();

        String namePrefix = "J";
        String nameSufix = "n";
        String nameContains = "ohn";

        target.aMethod(userRepository.findOne(userId), namePrefix, nameSufix, nameContains, nameLength);
    }

    @Configuration
    @EnableValidator4j
    public static class ConfigFoo {

        @Bean
        public Foo getTarget() {
            return new Foo();
        }
    }

    public static class Foo {

        @ValidateWith("user_validator")
        public void aMethod(User user, String namePrefix, String nameSufix, String nameContains, int nameLength) {
            System.out.println("invoked!");
        }
    }
}
