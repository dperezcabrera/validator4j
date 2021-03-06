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

import com.github.dperezcabrera.validator4j.core.ParametrizedRules;
import com.github.dperezcabrera.validator4j.core.Rule;
import com.github.dperezcabrera.validator4j.core.RuleBase;
import com.github.dperezcabrera.validator4j.core.Selector;
import com.github.dperezcabrera.validator4j.core.ParametrizedRules.NullConstraint;
import com.github.dperezcabrera.validator4j.core.ParametrizedRuleBuilder;
import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 * @param <F>
 */
public class ParametrizedRuleBuilderBase<T, F extends ParametrizedRuleBuilderBase<T, F>> implements ParametrizedRuleBuilder<T> {

    private ParametrizedRules<T> parameterRule;

    public ParametrizedRuleBuilderBase(ParametrizedRules<T> parameterRule) {
        this.parameterRule = parameterRule;
    }

    public final F addRule(Rule<T> rule) {
        parameterRule.addRule(rule);
        return (F) this;
    }

    public final F addRule(BiPredicate<T, Selector> predicate) {
        return addRule(new RuleBase<>(predicate));
    }

    public final F addRule(Predicate<T> predicate) {
        return addRule(new RuleBase<>((target, s) -> predicate.test(target)));
    }

    private F setNullConstraint(NullConstraint constraint) {
        parameterRule.setNullConstraint(constraint);
        return (F) this;
    }

    public F notNull() {
        return setNullConstraint(NullConstraint.NOT_NULL);
    }

    public F nullable() {
        return setNullConstraint(NullConstraint.NULLABLE);
    }

    public ParametrizedRuleBuilder<T> mustBeNull() {
        return setNullConstraint(NullConstraint.NULL);
    }

    public F in(T... parameters) {
        return addRule(Arrays.asList(parameters)::contains);
    }

    public F in(Collection<T> parameter) {
        return addRule(parameter::contains);
    }

    public F notIn(T... parameters) {
        return addRule(t -> !Arrays.asList(parameters).contains(t));
    }

    public F notIn(Collection<T> parameter) {
        return addRule(t -> !parameter.contains(t));
    }

    public F equalsTo(T parameter) {
        return addRule(t -> t.equals(parameter));
    }

    public F equalsTo(ProviderBuilder<T, ?> parameterProviderBuilder) {
        return addRule((t, s) -> t.equals(parameterProviderBuilder.data(s)));
    }

    public F notEqualsTo(T parameter) {
        return addRule(t -> !t.equals(parameter));
    }

    public F notEqualsTo(ProviderBuilder<T, ?> parameterProviderBuilder) {
        return addRule((t, s) -> !t.equals(parameterProviderBuilder.data(s)));
    }

    @Override
    public ParametrizedRules<T> build() {
        if (parameterRule.getNullConstraint() == null) {
            nullable();
        }
        return parameterRule;
    }
    
    public static <F extends ParametrizedRuleBuilderBase<Object, F>> ParametrizedRuleBuilderBase<Object, F> objectRule(String name) {
        return new ParametrizedRuleBuilderBase<>(new ParametrizedRules<>(name, Object.class));
    }
}
