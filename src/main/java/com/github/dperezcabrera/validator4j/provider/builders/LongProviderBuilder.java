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
import com.github.dperezcabrera.validator4j.provider.builders.LongProviderBuilder.LongOperator;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class LongProviderBuilder extends NumberIntegerProviderBuilder<Long, LongOperator, LongProviderBuilder> {

    public LongProviderBuilder(LongOperator operator, Provider<Long> provider) {
        super(operator, provider);
    }

    public LongProviderBuilder(LongOperator operator, Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(operator, provider, functions);
    }
    
    public static class LongOperator implements NumberIntegerProviderBuilder.OperatorInteger<Long> {

        public static final LongOperator INSTANCE = new LongOperator();

        protected LongOperator() {
        }

        @Override
        public Long add(Long n0, Long n1) {
            return n0 + n1;
        }

        @Override
        public Long sub(Long n0, Long n1) {
            return n0 - n1;
        }

        @Override
        public Long mult(Long n0, Long n1) {
            return n0 * n1;
        }

        @Override
        public Long div(Long n0, Long n1) {
            return n0 / n1;
        }

        @Override
        public Long remain(Long n0, Long n1) {
            return n0 % n1;
        }
    }

    public static LongProviderBuilder getLong(String selectorName) {
        return new LongProviderBuilder(LongOperator.INSTANCE, new ProviderFromSelector(selectorName, Long.class));
    }

    public static LongProviderBuilder newLong(Long value) {
        return new LongProviderBuilder(LongOperator.INSTANCE, new ProviderBase(value));
    }

    public static class LongProviderBuilderFactory implements ProviderBuilderFactory<Long, LongProviderBuilder> {

        @Override
        public ProviderBuilder<Long, LongProviderBuilder> build(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
            return new LongProviderBuilder(LongOperator.INSTANCE, provider, functions);
        }
    }
}
