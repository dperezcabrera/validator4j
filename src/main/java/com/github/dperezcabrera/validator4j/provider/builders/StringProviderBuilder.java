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
import com.github.dperezcabrera.validator4j.core.ValidatorRuleException;
import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.ProviderBase;
import com.github.dperezcabrera.validator4j.provider.ProviderFromSelector;
import com.github.dperezcabrera.validator4j.provider.builders.CalendarProviderBuilder.CalendarProviderBuilderBase;
import com.github.dperezcabrera.validator4j.provider.builders.CalendarProviderBuilder.CalendarProviderBuilderFactory;
import com.github.dperezcabrera.validator4j.provider.builders.IntegerProviderBuilder.IntegerProviderBuilderFactory;
import com.github.dperezcabrera.validator4j.provider.builders.LongProviderBuilder.LongProviderBuilderFactory;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <F>
 */
public class StringProviderBuilder<F extends StringProviderBuilder<F>> extends ObjectProviderBuilder<String, F> {

    protected StringProviderBuilder(Provider<?> provider) {
        super(provider);
    }

    protected StringProviderBuilder(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(provider, functions);
    }

    public F subString(int beginIndex) {
        return addFunction(t -> t.substring(beginIndex));
    }

    public F subString(ProviderBuilder<Integer, ?> providerBuilder) {
        return addFunction((t, s) -> t.substring(providerBuilder.data(s)));
    }

    public F subString(int beginIndex, int endIndex) {
        return addFunction(t -> t.substring(beginIndex, endIndex));
    }

    public F subString(int beginIndex, ProviderBuilder<Integer, ?> endIndexProviderBuilder) {
        return addFunction((t, s) -> t.substring(beginIndex, endIndexProviderBuilder.data(s)));
    }

    public F subString(ProviderBuilder<Integer, ?> beginIndexProviderBuilder, int endIndex) {
        return addFunction((t, s) -> t.substring(beginIndexProviderBuilder.data(s), endIndex));
    }

    public F subString(ProviderBuilder<Integer, ?> beginIndexProviderBuilder, ProviderBuilder<Integer, ?> endIndexProviderBuilder) {
        return addFunction((t, s) -> t.substring(beginIndexProviderBuilder.data(s), endIndexProviderBuilder.data(s)));
    }

    public F concat(String parameter) {
        return addFunction(t -> t + parameter);
    }

    public F concat(ProviderBuilder<String, ?> providerBuilder) {
        return addFunction((t, s) -> t + providerBuilder.data(s));
    }

    public F concatChar(Character parameter) {
        return addFunction(t -> t + parameter);
    }

    public F concatChar(ProviderBuilder<Character, ?> providerBuilder) {
        return addFunction((t, s) -> t + providerBuilder.data(s));
    }

    public IntegerProviderBuilder toInteger() {
        return addFunction(Integer::valueOf, new IntegerProviderBuilderFactory());
    }

    public LongProviderBuilder toLong() {
        return addFunction(Long::valueOf, new LongProviderBuilderFactory());
    }

    public DoubleProviderBuilder toDouble() {
        return addFunction(Double::valueOf, new DoubleProviderBuilder.DoubleProviderBuilderFactory());
    }

    public FloatProviderBuilder toFloat() {
        return addFunction(Float::valueOf, new FloatProviderBuilder.FloatProviderBuilderFactory());
    }

    public CalendarProviderBuilderBase toCalendar(String format) {
        return addFunction(t -> toCalendar(t, format), new CalendarProviderBuilderFactory());
    }

    public CalendarProviderBuilderBase toCalendar(ProviderBuilder<String, ?> providerBuilder) {
        return addFunction2((t, s) -> toCalendar(t, providerBuilder.data(s)), new CalendarProviderBuilderFactory());
    }

    public IntegerProviderBuilder length() {
        return addFunction(String::length, new IntegerProviderBuilderFactory());
    }

    private static Calendar toCalendar(String value, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        Calendar result = Calendar.getInstance();
        try {
            result.setTime(sdf.parse(value));
        } catch (ParseException pe) {
            throw new ValidatorRuleException("toCalendar format error with format '" + format + "'", pe);
        }
        return result;
    }

    public static final class StringProviderBuilderBase extends StringProviderBuilder<StringProviderBuilderBase> {

        private StringProviderBuilderBase(Provider<?> provider) {
            super(provider);
        }

        private StringProviderBuilderBase(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
            super(provider, functions);
        }
    }

    public static StringProviderBuilderBase string(String value) {
        return new StringProviderBuilderBase(new ProviderBase<>(value));
    }

    public static StringProviderBuilderBase stringFrom(String selectorName) {
        return new StringProviderBuilderBase(new ProviderFromSelector<>(selectorName, String.class));
    }

    public static class StringProviderBuilderFactory implements ProviderBuilderFactory<String, StringProviderBuilderBase> {

        @Override
        public ProviderBuilder<String, StringProviderBuilderBase> build(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
            return new StringProviderBuilderBase(provider, functions);
        }
    }
}
