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
package com.github.dperezcabrera.validator4j.validator;

import com.github.dperezcabrera.validator4j.core.ValidatorException;
import java.util.Calendar;
import org.apache.commons.lang3.time.DateUtils;
import static java.util.Calendar.DATE;
import static java.util.Calendar.MONTH;
import static java.util.Calendar.YEAR;
import static com.github.dperezcabrera.validator4j.validator.Validator.*;
import static com.github.dperezcabrera.validator4j.validator.BuilderBase.*;
import org.junit.Ignore;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class ValidatorTest {

    Validator CREATE_USER_VALIDATOR = rules(
            stringRule("name").notNull().startsWith("na").contains("me"),
            stringRule("id").mustBeNull(),
            cmpRule("child").notNull().range(2, 5),
            stringRule("email@a.com").notIn("admin@a.com", "pepe@a.com").contains("@"),
            dateRule("birthay").notNull().before(now().ceil(DATE).sub(18, YEAR)),
            dateRule("activated").notNull(),
            dateRule("deactivated").after(date("activated").add(3, MONTH))
    );

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Ignore
    @Test
    public void integrationTestCheckOk() {
        String name = "name";
        String id = null;
        String email = "email@a";
        Calendar birthay = Calendar.getInstance();
        Calendar active = Calendar.getInstance();
        Calendar deactive = Calendar.getInstance();
        birthay.add(YEAR, -18);
        active = DateUtils.ceiling(active, DATE);
        deactive.add(DATE, 1);
        deactive.add(MONTH, 3);
        deactive = null;
        int child = 3;

        CREATE_USER_VALIDATOR.check(name, id, child, email, birthay, active, deactive);
    }

    @Test
    public void integrationTestCheckFails() {
        String name = "name";
        String id = "";
        String email = "email@";
        Calendar birthay = Calendar.getInstance();
        Calendar active = Calendar.getInstance();
        Calendar deactive = Calendar.getInstance();
        birthay.add(YEAR, -18);
        birthay.add(DATE, 1);
        active = DateUtils.ceiling(active, DATE);
        deactive.add(MONTH, 3);
        deactive = null;
        int child = 6;

        thrown.expect(ValidatorException.class);

        CREATE_USER_VALIDATOR.check(p(name), id, child, p(email), p(birthay), p(active), p(deactive));
    }
}
