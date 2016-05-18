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
import com.github.dperezcabrera.validator4j.provider.builders.FloatProviderBuilder.FloatOperator;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class FloatProviderBuilder extends NumberProviderBuilder<Float, FloatOperator, FloatProviderBuilder> {

    public FloatProviderBuilder(FloatOperator operator, Provider<Float> provider) {
        super(operator, provider);
    }

    public FloatProviderBuilder(FloatOperator operator, Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(operator, provider, functions);
    }

    public static class FloatOperator implements NumberProviderBuilder.Operator<Float> {

        public static final FloatOperator INSTANCE = new FloatOperator();

        protected FloatOperator() {
        }

        @Override
        public Float add(Float n0, Float n1) {
            return n0 + n1;
        }

        @Override
        public Float sub(Float n0, Float n1) {
            return n0 - n1;
        }

        @Override
        public Float mult(Float n0, Float n1) {
            return n0 * n1;
        }

        @Override
        public Float div(Float n0, Float n1) {
            return n0 / n1;
        }
    }

    public static FloatProviderBuilder getFloat(String selectorName) {
        return new FloatProviderBuilder(FloatOperator.INSTANCE, new ProviderFromSelector(selectorName, Float.class));
    }

    public static FloatProviderBuilder newFloat(Float value) {
        return new FloatProviderBuilder(FloatOperator.INSTANCE, new ProviderBase(value));
    }

    public static class FloatProviderBuilderFactory implements ProviderBuilderFactory<Float, FloatProviderBuilder> {

        @Override
        public ProviderBuilder<Float, FloatProviderBuilder> build(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
            return new FloatProviderBuilder(FloatOperator.INSTANCE, provider, functions);
        }
    }
}
