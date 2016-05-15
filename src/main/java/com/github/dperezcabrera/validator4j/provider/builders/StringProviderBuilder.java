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
import com.github.dperezcabrera.validator4j.provider.builders.IntegerProviderBuilder.IntegerProviderBuilderBase;
import com.github.dperezcabrera.validator4j.provider.builders.IntegerProviderBuilder.IntegerProviderBuilderFactory;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <B>
 * @param <F>
 */
public class StringProviderBuilder<B, F extends StringProviderBuilder<B, F>> extends ProviderBuilder<B, String, F> {

    protected StringProviderBuilder(Provider<B> provider) {
        super(provider);
    }

    protected StringProviderBuilder(Provider<B> provider, List<BiFunction> functions) {
        super(provider, functions);
    }

    public F subString(int beginIndex) {
        return addFunction(t -> t.substring(beginIndex));
    }
    
    public F subString(ProviderBuilder<?, Integer, ?> providerBuilder) {
        return addFunction((t, s) -> t.substring(providerBuilder.data(s)));
    }
    
    public F subString(int beginIndex, int endIndex) {
        return addFunction(t -> t.substring(beginIndex, endIndex));
    }
    
    public F subString(int beginIndex, ProviderBuilder<?, Integer, ?> endIndexProviderBuilder) {
        return addFunction((t, s) -> t.substring(beginIndex, endIndexProviderBuilder.data(s)));
    }
    
    public F subString(ProviderBuilder<?, Integer, ?> beginIndexProviderBuilder, int endIndex) {
        return addFunction((t, s) -> t.substring(beginIndexProviderBuilder.data(s), endIndex));
    }
    
    public F subString(ProviderBuilder<?, Integer, ?> beginIndexProviderBuilder, ProviderBuilder<?, Integer, ?> endIndexProviderBuilder) {
        return addFunction((t, s) -> t.substring(beginIndexProviderBuilder.data(s), endIndexProviderBuilder.data(s)));
    }
    
    public F concat(String parameter) {
        return addFunction(t -> String.join("", t, parameter));
    }

    public F concat(ProviderBuilder<?, String, ?> providerBuilder) {
        return addFunction((t, s) -> String.join("", t, providerBuilder.data(s)));
    }

    public IntegerProviderBuilderBase<B> toInt() {
        return addFunction(t ->  Integer.valueOf(t), new IntegerProviderBuilderFactory<>());
    }
        
    public static final class StringProviderBuilderBase<U> extends StringProviderBuilder<U, StringProviderBuilderBase<U>> {

        private StringProviderBuilderBase(Provider<U> provider) {
            super(provider);
        }

        private StringProviderBuilderBase(Provider<U> provider, List<BiFunction> functions) {
            super(provider, functions);
        }
    }

    public static StringProviderBuilderBase<String> string(String value) {
        return new StringProviderBuilderBase<>(new ProviderBase<>(value));
    }

    public static StringProviderBuilderBase<String> stringFrom(String selectorName) {
        return new StringProviderBuilderBase<>(new ProviderFromSelector<>(selectorName, String.class));
    }
        
    public static class StringProviderBuilderFactory<U> implements BiProviderBuilderFactory<U, String, StringProviderBuilderBase<U>> {

        @Override
        public ProviderBuilder<U, String, StringProviderBuilderBase<U>> build(Provider<U> provider, List<BiFunction> functions) {
            return new StringProviderBuilderBase<>(provider, functions);
        }
    }
}