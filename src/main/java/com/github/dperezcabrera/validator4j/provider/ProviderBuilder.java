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
package com.github.dperezcabrera.validator4j.provider;

import com.github.dperezcabrera.validator4j.core.Selector;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <B>
 * @param <T>
 * @param <F>
 */
public class ProviderBuilder<B, T, F extends ProviderBuilder<B, T, F>> {

    private Provider<B> provider;
    private List<BiFunction> functions;

    public ProviderBuilder(Provider<B> provider) {
        this.provider = provider;
        this.functions = new ArrayList<>();
    }

    protected ProviderBuilder(Provider<B> provider, List<BiFunction> functions) {
        this.provider = provider;
        this.functions = functions;
    }

    private F add(BiFunction<T, Selector, T> function) {
        this.functions.add(function);
        return (F) this;
    }

    public F addFunction(BiFunction<T, Selector, T> function) {
        return add(function);
    }

    public F addFunction(Function<T, T> function) {
        return add((t, s) -> function.apply(t));
    }
    
    public F addFunctionConsumer(Consumer<T> consumer) {
        return add((t, s) -> {
            consumer.accept(t);
            return t;
        });
    }

    public F addFunctionConsumer(BiConsumer<T, Selector> consumer) {
        return add((t, s) -> {
            consumer.accept(t, s);
            return t;
        });
    }

    private <K, G extends ProviderBuilder<B, K, G>> G add(BiFunction<T, Selector, K> function, BiProviderBuilderFactory<B, K, G> factory) {
        List<BiFunction> nextFunctions = new ArrayList<>(this.functions);
        nextFunctions.add(function);
        return (G) factory.build(provider, new ArrayList<>(nextFunctions));
    }

    public <K, G extends ProviderBuilder<B, K, G>> G addFunction(BiFunction<T, Selector, K> function, BiProviderBuilderFactory<B, K, G> factory) {
        return add(function, factory);
    }

    public <K, G extends ProviderBuilder<B, K, G>> G addFunction(Function<T, K> function, BiProviderBuilderFactory<B, K, G> factory) {
        return add((t, s) -> function.apply(t), factory);
    }

    public T data(Selector s) {
        Object item = provider.data(s);
        for (BiFunction function : functions) {
            item = function.apply(item, s);
        }
        return (T) item;
    }

    @FunctionalInterface
    public interface BiProviderBuilderFactory<B, T, F extends ProviderBuilder<B, T, F>> {

        ProviderBuilder<B, T, F> build(Provider<B> provider, List<BiFunction> functions);
    }
}
