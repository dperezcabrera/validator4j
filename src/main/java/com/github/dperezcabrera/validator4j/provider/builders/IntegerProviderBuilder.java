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
import com.github.dperezcabrera.validator4j.provider.builders.IntegerProviderBuilder.IntegerOperator;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class IntegerProviderBuilder extends NumberProviderBuilder<Integer, IntegerOperator, IntegerProviderBuilder> {

    public IntegerProviderBuilder(IntegerOperator operator, Provider<?> provider) {
        super(operator, provider);
    }

    public IntegerProviderBuilder(IntegerOperator operator, Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(operator, provider, functions);
    }
    
    public LongProviderBuilder toLong() {
        return addFunction(t ->  t.longValue(), new LongProviderBuilder.LongProviderBuilderFactory());
    }

    public static class IntegerOperator implements NumberProviderBuilder.Operator<Integer> {

        public static final IntegerOperator INSTANCE = new IntegerOperator();

        protected IntegerOperator() {
        }

        @Override
        public Integer add(Integer n0, Integer n1) {
            return n0 + n1;
        }

        @Override
        public Integer sub(Integer n0, Integer n1) {
            return n0 - n1;
        }

        @Override
        public Integer mult(Integer n0, Integer n1) {
            return n0 * n1;
        }

        @Override
        public Integer div(Integer n0, Integer n1) {
            return n0 / n1;
        }

        @Override
        public Integer remain(Integer n0, Integer n1) {
            return n0 % n1;
        }
    }

    public static IntegerProviderBuilder integer(String selectorName) {
        return new IntegerProviderBuilder(IntegerOperator.INSTANCE, new ProviderFromSelector<>(selectorName, Integer.class));
    }

    public static IntegerProviderBuilder integer(Integer value) {
        return new IntegerProviderBuilder(IntegerOperator.INSTANCE, new ProviderBase<>(value));
    }

    public static class IntegerProviderBuilderFactory implements ProviderBuilderFactory<Integer, IntegerProviderBuilder> {

        @Override
        public ProviderBuilder<Integer, IntegerProviderBuilder> build(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
            return new IntegerProviderBuilder(IntegerOperator.INSTANCE, provider, functions);
        }
    }
}
