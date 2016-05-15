/*
 * Copyright (C) 2015 David Pérez Cabrera <dperezcabrera@gmail.com>
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
package com.github.dperezcabrera.validator4j.rules;

import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import java.util.Calendar;
import com.github.dperezcabrera.validator4j.validator.ParameterRuleBuilderBase;
import com.github.dperezcabrera.validator4j.validator.ParameterRules;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <F>
 */
public class CalendarRuleBuilder<F extends CalendarRuleBuilder<F>> extends ParameterRuleBuilderBase<Calendar, F> {

    protected CalendarRuleBuilder(ParameterRules<Calendar> parameterRule) {
        super(parameterRule);
    }

    public F before(Calendar parameter) {
        return addRule(t -> t.compareTo(parameter) < 0);
    }

    public F before(ProviderBuilder<?, Calendar, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.compareTo(parameterProviderBuilder.data(s)) < 0);
    }

    public F after(Calendar parameter) {
        return addRule(t -> t.compareTo(parameter) > 0);
    }

    public F after(ProviderBuilder<?, Calendar, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.compareTo(parameterProviderBuilder.data(s)) > 0);
    }

    public static final class CalendarRuleBuilderBase extends CalendarRuleBuilder<CalendarRuleBuilderBase> {

        private CalendarRuleBuilderBase(ParameterRules<Calendar> parameterRule) {
            super(parameterRule);
        }
    }

    public static CalendarRuleBuilderBase dateRule(String name) {
        return new CalendarRuleBuilderBase(new ParameterRules<>(name, Calendar.class));
    }
}
