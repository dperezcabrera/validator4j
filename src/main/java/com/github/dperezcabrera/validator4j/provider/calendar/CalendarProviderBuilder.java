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
package com.github.dperezcabrera.validator4j.provider.calendar;

import java.util.Calendar;
import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 * @param <F>
 */
public class CalendarProviderBuilder<T extends Calendar, F extends CalendarProviderBuilder<T, F>> extends ProviderBuilder<T, F> {

    public CalendarProviderBuilder(Provider<T> provider) {
        super(provider);
    }

    public F add(int amount, int field) {
        return addFunction(new CalendarFunction<>(amount, field, true));
    }

    public F add(ProviderBuilder<Integer, ?> amountProviderBuilder, int field) {
        return addFunction(new CalendarFunction<>(amountProviderBuilder.build(), field, true));
    }

    public F sub(int amount, int field) {
        return addFunction(new CalendarFunction<>(amount, field, false));
    }

    public F sub(ProviderBuilder<Integer, ?> amountProviderBuilder, int field) {
        return addFunction(new CalendarFunction<>(amountProviderBuilder.build(), field, false));
    }

    public F ceil(int field) {
        return addFunction(new CeilCalendarFunction<>(field));
    }

    public F truncate(int field) {
        return addFunction(new TruncateCalendarFunction<>(field));
    }

    public F round(int field) {
        return addFunction(new RoundCalendarFunction<>(field));
    }
}
