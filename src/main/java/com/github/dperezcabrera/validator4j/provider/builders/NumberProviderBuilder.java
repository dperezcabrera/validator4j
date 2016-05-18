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
import com.github.dperezcabrera.validator4j.provider.builders.NumberProviderBuilder.Operator;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <N>
 * @param <O>
 * @param <F>
 */
public class NumberProviderBuilder<N extends Number, O extends Operator<N>, F extends NumberProviderBuilder<N, O, F>> extends ObjectProviderBuilder<N, F> {

    private O operator;

    public NumberProviderBuilder(O operator, Provider<?> provider) {
        super(provider);
        this.operator = operator;
    }

    public NumberProviderBuilder(O operator, Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(provider, functions);
        this.operator = operator;
    }

    protected O getOperator() {
        return operator;
    }
    
    
    public IntegerProviderBuilder toInteger() {
        return addFunction(t ->  t.intValue(), new IntegerProviderBuilder.IntegerProviderBuilderFactory());
    }
    
    public LongProviderBuilder toLong() {
        return addFunction(t ->  t.longValue(), new LongProviderBuilder.LongProviderBuilderFactory());
    }
    
    public DoubleProviderBuilder toDouble() {
        return addFunction(t ->  t.doubleValue(), new DoubleProviderBuilder.DoubleProviderBuilderFactory());
    }
    
    public FloatProviderBuilder toFloat() {
        return addFunction(t ->  t.floatValue(), new FloatProviderBuilder.FloatProviderBuilderFactory());
    }
    
    public F add(N parameter) {
        return addFunction(t -> operator.add(t, parameter));
    }

    public F add(ProviderBuilder<N, ?> providerBuilder) {
        return addFunction((t, s) -> operator.add(t, providerBuilder.data(s)));
    }

    public F sub(N parameter) {
        return addFunction(t -> operator.sub(t, parameter));
    }

    public F sub(ProviderBuilder<N, ?> providerBuilder) {
        return addFunction((t, s) -> operator.sub(t, providerBuilder.data(s)));
    }

    public F mult(N parameter) {
        return addFunction(t -> operator.mult(t, parameter));
    }

    public F mult(ProviderBuilder<N, ?> providerBuilder) {
        return addFunction((t, s) -> operator.mult(t, providerBuilder.data(s)));
    }

    public F div(N parameter) {
        return addFunction(t -> operator.div(t, parameter));
    }

    public F div(ProviderBuilder<N, ?> providerBuilder) {
        return addFunction((t, s) -> operator.div(t, providerBuilder.data(s)));
    }

    public interface Operator<N extends Number> {

        N add(N n0, N n1);

        N sub(N n0, N n1);

        N mult(N n0, N n1);

        N div(N n0, N n1);
    }
}
