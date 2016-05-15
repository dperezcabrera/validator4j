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

import java.util.List;
import com.github.dperezcabrera.validator4j.core.Selector;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 */
public class ProviderChain<T> implements Provider<T> {

    private Provider<T> source;
    private List<Function<T>> functions;

    public ProviderChain(Provider<T> source, List<Function<T>> functions) {
        this.source = source;
        this.functions = functions;
    }

    @Override
    public T data(Selector selector) {
        T item = source.data(selector);
        for (Function<T> function : functions) {
            item = function.apply(item, selector);
        }
        return item;
    }
}
