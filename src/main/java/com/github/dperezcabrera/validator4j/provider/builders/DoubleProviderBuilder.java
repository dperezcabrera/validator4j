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
import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.ProviderBase;
import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import com.github.dperezcabrera.validator4j.provider.ProviderFromSelector;
import com.github.dperezcabrera.validator4j.provider.builders.DoubleProviderBuilder.DoubleOperator;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class DoubleProviderBuilder extends NumberProviderBuilder<Double, DoubleOperator, DoubleProviderBuilder> {

    public DoubleProviderBuilder(DoubleOperator operator, Provider<Double> provider) {
        super(operator, provider);
    }

    public DoubleProviderBuilder(DoubleOperator operator, Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(operator, provider, functions);
    }

    public static class DoubleOperator implements NumberProviderBuilder.Operator<Double> {

        public static final DoubleOperator INSTANCE = new DoubleOperator();

        protected DoubleOperator() {
        }

        @Override
        public Double add(Double n0, Double n1) {
            return n0 + n1;
        }

        @Override
        public Double sub(Double n0, Double n1) {
            return n0 - n1;
        }

        @Override
        public Double mult(Double n0, Double n1) {
            return n0 * n1;
        }

        @Override
        public Double div(Double n0, Double n1) {
            return n0 / n1;
        }
    }

    public static DoubleProviderBuilder getDouble(String selectorName) {
        return new DoubleProviderBuilder(DoubleOperator.INSTANCE, new ProviderFromSelector(selectorName, Double.class));
    }

    public static DoubleProviderBuilder newDouble(Double value) {
        return new DoubleProviderBuilder(DoubleOperator.INSTANCE, new ProviderBase(value));
    }

    public static class DoubleProviderBuilderFactory implements ProviderBuilderFactory<Double, DoubleProviderBuilder> {

        @Override
        public ProviderBuilder<Double, DoubleProviderBuilder> build(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
            return new DoubleProviderBuilder(DoubleOperator.INSTANCE, provider, functions);
        }
    }
}
