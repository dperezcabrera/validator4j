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
import com.github.dperezcabrera.validator4j.validator.ParameterRules;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <F>
 */
public class StringRuleBuilder<F extends StringRuleBuilder<F>> extends ComparableRuleBuilder<String, F> {

    protected StringRuleBuilder(ParameterRules<String> parameterRule) {
        super(parameterRule);
    }

    public F empty() {
        return addRule(t -> t.isEmpty());
    }

    public F notEmpty() {
        return addRule(t -> !t.isEmpty());
    }

    public F startsWith(String parameter) {
        return addRule(t -> t.startsWith(parameter));
    }

    public F startsWith(ProviderBuilder<String, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.startsWith(parameterProviderBuilder.data(s)));
    }

    public F notStartsWith(String parameter) {
        return addRule(t -> !t.startsWith(parameter));
    }

    public F notStartsWith(ProviderBuilder<String, ?> parameterProviderBuilder) {
        return addRule((t, s) -> !t.startsWith(parameterProviderBuilder.data(s)));
    }

    public F endsWith(String parameter) {
        return addRule(t -> t.endsWith(parameter));
    }

    public F endsWith(final ProviderBuilder<String, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.endsWith(parameterProviderBuilder.data(s)));
    }

    public F notEnds(String parameter) {
        return addRule(t -> !t.endsWith(parameter));
    }

    public F notEnds(ProviderBuilder<String, ?> parameterProviderBuilder) {
        return addRule((t, s) -> !t.endsWith(parameterProviderBuilder.data(s)));
    }

    public F contains(CharSequence parameter) {
        return addRule(t -> t.contains(parameter));
    }

    public F contains(ProviderBuilder<String, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.contains(parameterProviderBuilder.data(s)));
    }

    public F notContains(CharSequence parameter) {
        return addRule(t -> !t.contains(parameter));
    }

    public F notContains(ProviderBuilder<String, ?> parameterProviderBuilder) {
        return addRule((t, s) -> !t.contains(parameterProviderBuilder.data(s)));
    }

    public F matches(String parameter) {
        return addRule(t -> t.matches(parameter));
    }

    public F matches(ProviderBuilder<String, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.matches(parameterProviderBuilder.data(s)));
    }

    public F notMatches(String parameter) {
        return addRule(t -> !t.matches(parameter));
    }

    public F notMatches(ProviderBuilder<String, ?> parameterProviderBuilder) {
        return addRule((t, s) -> !t.matches(parameterProviderBuilder.data(s)));
    }

    public F length(Integer parameter) {
        return addRule(t -> t.length() == parameter);
    }

    public F length(ProviderBuilder<Integer, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.length() == parameterProviderBuilder.data(s));
    }

    public F minLength(Integer parameter) {
        return addRule(t -> t.length() >= parameter);
    }

    public F minLength(ProviderBuilder<Integer, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.length() >= parameterProviderBuilder.data(s));
    }

    public F maxLength(Integer parameter) {
        return addRule(t -> t.length() <= parameter);
    }

    public F maxLength(ProviderBuilder<Integer, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.length() <= parameterProviderBuilder.data(s));
    }

    public static final class StringRuleBuilderBase extends StringRuleBuilder<StringRuleBuilderBase> {

        private StringRuleBuilderBase(ParameterRules<String> parameterRule) {
            super(parameterRule);
        }
    }

    public static StringRuleBuilderBase stringRule(String name) {
        return new StringRuleBuilderBase(new ParameterRules<>(name, String.class));
    }
}
