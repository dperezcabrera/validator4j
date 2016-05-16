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
package com.github.dperezcabrera.validator4j.rules;

import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import com.github.dperezcabrera.validator4j.validator.ParameterRuleBuilderBase;
import com.github.dperezcabrera.validator4j.validator.ParameterRules;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 * @param <F>
 */
public class ComparableRuleBuilder<T extends Comparable, F extends ComparableRuleBuilder<T, F>> extends ParameterRuleBuilderBase<T, F> {

    protected ComparableRuleBuilder(ParameterRules<T> attributeRule) {
        super(attributeRule);
    }

    public F greatherThan(T parameter) {
        return addRule(t -> t.compareTo(parameter) > 0);
    }

    public F greatherThan(ProviderBuilder<T, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.compareTo(parameterProviderBuilder.data(s)) > 0);
    }

    public F lessThan(T parameter) {
        return addRule(t -> t.compareTo(parameter) < 0);
    }

    public F lessThan(ProviderBuilder<T, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.compareTo(parameterProviderBuilder.data(s)) < 0);
    }

    public F greatherEqualsThan(T parameter) {
        return addRule(t -> t.compareTo(parameter) >= 0);
    }

    public F greatherEqualsThan(ProviderBuilder<T, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.compareTo(parameterProviderBuilder.data(s)) >= 0);
    }

    public F lessEqualsThan(T parameter) {
        return addRule(t -> t.compareTo(parameter) <= 0);
    }

    public F lessEqualsThan(ProviderBuilder<T, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.compareTo(parameterProviderBuilder.data(s)) <= 0);
    }

    public F range(T minValue, T maxValue) {
        return addRule(t -> range(t, minValue, maxValue));
    }

    public F range(ProviderBuilder<T, ?> minParameterProviderBuilder, T maxValue) {
        return addRule((t, s) -> range(t, minParameterProviderBuilder.data(s), maxValue));
    }

    public F range(T minValue, ProviderBuilder<T, ?> maxParameterProviderBuilder) {
        return addRule((t, s) -> range(t, minValue, maxParameterProviderBuilder.data(s)));
    }

    public F range(ProviderBuilder<T, ?> minParameterProviderBuilder, ProviderBuilder<T, ?> maxParameterProviderBuilder) {
        return addRule((t, s) -> range(t, minParameterProviderBuilder.data(s), maxParameterProviderBuilder.data(s)));
    }

    private boolean range(T target, T min, T max) {
        return min.compareTo(target) <= 0 && 0 < max.compareTo(target);
    }

    public static final class ComparableRuleBuilderBase extends ComparableRuleBuilder<Comparable, ComparableRuleBuilderBase> {

        private ComparableRuleBuilderBase(ParameterRules<Comparable> parameterRule) {
            super(parameterRule);
        }
    }

    public static ComparableRuleBuilderBase cmpRule(String name) {
        return new ComparableRuleBuilderBase(new ParameterRules<>(name, Comparable.class));
    }
}
