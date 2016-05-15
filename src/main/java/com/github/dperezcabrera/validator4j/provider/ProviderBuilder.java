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
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 * @param <F>
 */
public class ProviderBuilder<T, F extends ProviderBuilder<T, F>> {

    private Provider<T> provider;
    private List<Function<T>> functions;

    public ProviderBuilder(Provider<T> provider) {
        this.provider = provider;
    }

    public F addFunction(Function<T> function) {
        if (functions == null) {
            functions = new ArrayList<>();
        }
        functions.add(function);
        return (F) this;
    }

    public Provider<T> build() {
        Provider<T> result = provider;
        if (functions != null) {
            result = new ProviderChain<>(provider, functions);
        }
        return result;
    }
    
    public T data(Selector s) {
        Provider<T> result = provider;
        if (functions != null) {
            result = new ProviderChain<>(provider, functions);
        }
        return result.data(s);
    }
}
