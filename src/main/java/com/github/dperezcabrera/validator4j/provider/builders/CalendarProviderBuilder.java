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
package com.github.dperezcabrera.validator4j.provider.builders;

import com.github.dperezcabrera.validator4j.core.Selector;
import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.ProviderBase;
import com.github.dperezcabrera.validator4j.provider.ProviderFromSelector;
import com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.StringProviderBuilderBase;
import com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.StringProviderBuilderFactory;
import java.util.Calendar;
import java.util.List;
import org.apache.commons.lang3.time.DateUtils;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <F>
 */
public class CalendarProviderBuilder<F extends CalendarProviderBuilder<F>> extends ProviderBuilder<Calendar, F> {

    protected CalendarProviderBuilder(Provider<?> provider) {
        super(provider);
    }

    protected CalendarProviderBuilder(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(provider, functions);
    }

    public F add(int amount, int field) {
        return addFunctionConsumer(t -> t.add(field, amount));
    }

    public F add(ProviderBuilder<Integer, ?> amountProviderBuilder, int field) {
        return addFunctionConsumer((t, s) -> t.add(field, amountProviderBuilder.data(s)));
    }

    public F sub(int amount, int field) {
        return addFunctionConsumer(t -> t.add(field, -amount));
    }

    public F sub(ProviderBuilder<Integer, ?> amountProviderBuilder, int field) {
        return addFunctionConsumer((t, s) -> t.add(field, -amountProviderBuilder.data(s)));
    }

    public F ceil(int field) {
        return addFunction(t -> DateUtils.ceiling(t, field));
    }

    public F truncate(int field) {
        return addFunction(t -> DateUtils.truncate(t, field));
    }

    public F round(int field) {
        return addFunction(t -> DateUtils.round(t, field));
    }

    protected F copy() {
        return addFunction(t -> (Calendar) t.clone());
    }

    public StringProviderBuilderBase string() {
        return addFunction(t -> t.toString(), new StringProviderBuilderFactory());
    }

    public static CalendarProviderBuilderBase now() {
        return new CalendarProviderBuilderBase(new ProviderBase<>(Calendar.getInstance()));
    }

    public static CalendarProviderBuilderBase date(Calendar value) {
        return new CalendarProviderBuilderBase(new ProviderBase<>((Calendar) value.clone()));
    }

    public static CalendarProviderBuilderBase date(String selectorName) {
        return new CalendarProviderBuilderBase(new ProviderFromSelector<>(selectorName, Calendar.class)).copy();
    }

    public static final class CalendarProviderBuilderBase extends CalendarProviderBuilder<CalendarProviderBuilderBase> {

        private CalendarProviderBuilderBase(Provider<?> provider) {
            super(provider);
        }

        private CalendarProviderBuilderBase(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
            super(provider, functions);
        }
    }

    public static class CalendarProviderBuilderFactory implements ProviderBuilderFactory<Calendar, CalendarProviderBuilderBase> {

        @Override
        public ProviderBuilder<Calendar, CalendarProviderBuilderBase> build(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
            return new CalendarProviderBuilderBase(provider, functions);
        }
    }
}
