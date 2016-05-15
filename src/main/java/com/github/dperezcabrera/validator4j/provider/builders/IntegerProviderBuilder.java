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

import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.ProviderBase;
import com.github.dperezcabrera.validator4j.provider.ProviderFromSelector;
import com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.StringProviderBuilderBase;
import com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.StringProviderBuilderFactory;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <B>
 * @param <F>
 */
public class IntegerProviderBuilder<B, F extends IntegerProviderBuilder<B, F>> extends ProviderBuilder<B, Integer, F> {

    protected IntegerProviderBuilder(Provider<B> provider) {
        super(provider);
    }

    protected IntegerProviderBuilder(Provider<B> provider, List<BiFunction> functions) {
        super(provider, functions);
    }

    public F add(int parameter) {
        return addFunction(t -> t + parameter);
    }
    
    public F add(ProviderBuilder<?, Integer, ?> providerBuilder) {
        return addFunction((t, s) -> t + providerBuilder.data(s));
    }
    
    public F sub(int parameter) {
        return addFunction(t -> t - parameter);
    }
    
    public F sub(ProviderBuilder<?, Integer, ?> providerBuilder) {
        return addFunction((t, s) -> t - providerBuilder.data(s));
    }
    
    public F multiply(int parameter) {
        return addFunction(t -> t * parameter);
    }
    
    public F multiply(ProviderBuilder<?, Integer, ?> providerBuilder) {
        return addFunction((t, s) -> t * providerBuilder.data(s));
    }
    
    public F div(int parameter) {
        return addFunction(t -> t / parameter);
    }
    
    public F div(ProviderBuilder<?, Integer, ?> providerBuilder) {
        return addFunction((t, s) -> t / providerBuilder.data(s));
    }
    
    public F remain(int parameter) {
        return addFunction(t -> t % parameter);
    }
    
    public F remain(ProviderBuilder<?, Integer, ?> providerBuilder) {
        return addFunction((t, s) -> t % providerBuilder.data(s));
    }
       
    public StringProviderBuilderBase<B> toStr() {
        return addFunction(t ->  String.valueOf(t), new StringProviderBuilderFactory<>());
    }

    public static final class IntegerProviderBuilderBase<U> extends IntegerProviderBuilder<U, IntegerProviderBuilderBase<U>> {

        private IntegerProviderBuilderBase(Provider<U> provider) {
            super(provider);
        }

        private IntegerProviderBuilderBase(Provider<U> provider, List<BiFunction> functions) {
            super(provider, functions);
        }
    }
    
    public static IntegerProviderBuilderBase<Integer> integer(Integer value) {
        return new IntegerProviderBuilderBase<>(new ProviderBase<>(value));
    }
    
    public static IntegerProviderBuilderBase<Integer> integer(String selectorName) {
        return new IntegerProviderBuilderBase<>(new ProviderFromSelector<>(selectorName, Integer.class));
    }

    public static class IntegerProviderBuilderFactory<U> implements BiProviderBuilderFactory<U, Integer, IntegerProviderBuilderBase<U>> {

        @Override
        public ProviderBuilder<U, Integer, IntegerProviderBuilderBase<U>> build(Provider<U> provider, List<BiFunction> functions) {
            return new IntegerProviderBuilderBase<>(provider, functions);
        }
    }
}
