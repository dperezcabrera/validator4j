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
package com.github.dperezcabrera.validator4j.provider;

import com.github.dperezcabrera.validator4j.core.Selector;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 * @param <K>
 */
public abstract class ProviderFunction<T, K> implements Function<T> {

    private Provider<K> otherProvider = null;
    private K other;
    private boolean ignoreNull = false;

    public ProviderFunction(K other) {
        this.other = other;
    }

    public ProviderFunction(Provider<K> otherProvider) {
        this.otherProvider = otherProvider;
    }

    public ProviderFunction(K other, boolean ignoreNull) {
        this.other = other;
        this.ignoreNull = ignoreNull;
    }

    public ProviderFunction(Provider<K> otherProvider, boolean ignoreNull) {
        this.otherProvider = otherProvider;
        this.ignoreNull = ignoreNull;
    }

    @Override
    public T apply(T item, Selector selector) {
        T result = null;
        if (ignoreNull && item != null) {
            if (otherProvider != null) {
                other = otherProvider.data(selector);
            }
            result = apply(item, other, selector);
        }
        return result;
    }

    public abstract T apply(T item, K other, Selector selector);
}
