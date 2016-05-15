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
package com.github.dperezcabrera.validator4j.provider.string;

import com.github.dperezcabrera.validator4j.core.Selector;
import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.ProviderFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class ConcatStringFunction extends ProviderFunction<String, String> {

    public ConcatStringFunction(String other) {
        super(other);
    }

    public ConcatStringFunction(Provider<String> otherProvider) {
        super(otherProvider);
    }

    @Override
    public String apply(String item, String other, Selector selector) {
        String result = null;
        if (item == null) {
            result = other;
        } else if (other != null) {
            result = item.concat(other);
        }
        return result;
    }
}
