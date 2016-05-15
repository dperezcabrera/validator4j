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

import java.util.Calendar;
import com.github.dperezcabrera.validator4j.builders.CalendarRuleBuilder;
import com.github.dperezcabrera.validator4j.builders.ComparableRuleBuilder;
import com.github.dperezcabrera.validator4j.builders.StringRuleBuilder;
import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.ProviderFromSelector;
import com.github.dperezcabrera.validator4j.provider.calendar.CalendarProviderBuilder;
import com.github.dperezcabrera.validator4j.provider.calendar.NowCalendarProvider;
import com.github.dperezcabrera.validator4j.provider.string.StringProviderBuilder;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public final class BuilderBase {

    private BuilderBase() {
    }

    public static final class ComparableRuleBuilderBase extends ComparableRuleBuilder<Comparable, ComparableRuleBuilderBase> {

        private ComparableRuleBuilderBase(ParameterRules<Comparable> parameterRule) {
            super(parameterRule);
        }
    }

    public static ComparableRuleBuilderBase cmpRule(String name) {
        return new ComparableRuleBuilderBase(new ParameterRules<>(name));
    }

    public static final class CalendarRuleBuilderBase extends CalendarRuleBuilder<Calendar, CalendarRuleBuilderBase> {

        private CalendarRuleBuilderBase(ParameterRules<Calendar> parameterRule) {
            super(parameterRule);
        }
    }

    public static CalendarRuleBuilderBase dateRule(String name) {
        return new CalendarRuleBuilderBase(new ParameterRules<>(name));
    }

    public static final class StringRuleBuilderBase extends StringRuleBuilder<StringRuleBuilderBase> {

        private StringRuleBuilderBase(ParameterRules<String> parameterRule) {
            super(parameterRule);
        }
    }

    public static StringRuleBuilderBase stringRule(String name) {
        return new StringRuleBuilderBase(new ParameterRules<>(name));
    }

    public static final class CalendarProviderBuilderBase extends CalendarProviderBuilder<Calendar, CalendarProviderBuilderBase> {

        private CalendarProviderBuilderBase(Provider<Calendar> provider) {
            super(provider);
        }
    }

    public static CalendarProviderBuilderBase now() {
        return new CalendarProviderBuilderBase(new NowCalendarProvider());
    }

    public static CalendarProviderBuilderBase date(String nameForSelector) {
        return new CalendarProviderBuilderBase(new ProviderFromSelector<>(nameForSelector));
    }
   
    public static final class StringProviderBuilderBase extends StringProviderBuilder<StringProviderBuilderBase> {

        private StringProviderBuilderBase(Provider<String> provider) {
            super(provider);
        }
    }

    public static StringProviderBuilderBase string(String nameForSelector) {
        return new StringProviderBuilderBase(new ProviderFromSelector(nameForSelector));
    }
    
    public static <T, F extends ParameterRuleBuilderBase<T, F>> ParameterRuleBuilderBase<T, F> objectRule(String name) {
        return new ParameterRuleBuilderBase<>(new ParameterRules<T>(name));
    }
}
