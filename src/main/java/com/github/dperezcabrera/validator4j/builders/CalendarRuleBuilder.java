/*
 * Copyright (C) 2015 David Pérez Cabrera <dperezcabrera@gmail.com>
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
package com.github.dperezcabrera.validator4j.builders;

import java.util.Calendar;
import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import com.github.dperezcabrera.validator4j.validator.ParameterRuleBuilderBase;
import com.github.dperezcabrera.validator4j.validator.ParameterRules;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 * @param <F>
 */
public class CalendarRuleBuilder<T extends Calendar, F extends CalendarRuleBuilder<T, F>> extends ParameterRuleBuilderBase<T, F> {

    protected CalendarRuleBuilder(ParameterRules<T> parameterRule) {
        super(parameterRule);
    }

    public F before(T parameter) {
        return addRule(t -> t.compareTo(parameter) < 0);
    }
    
    public F before(ProviderBuilder<T,?> parameterProviderBuilder) {
        return addRule((t, s) -> t.compareTo(parameterProviderBuilder.data(s)) < 0);
    }
    
    public F after(T parameter) {
        return addRule(t -> t.compareTo(parameter) > 0);
    }
    
    public F after(ProviderBuilder<T,?> parameterProviderBuilder) {
        return addRule((t, s) -> t.compareTo(parameterProviderBuilder.data(s)) > 0);
    }
}
