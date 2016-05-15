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

import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <F>
 */
public class StringProviderBuilder<F extends StringProviderBuilder<F>> extends ProviderBuilder<String, F> {

    public StringProviderBuilder(Provider<String> provider) {
        super(provider);
    }

    public F concat(String s) {
        return addFunction(new ConcatStringFunction(s));
    }

    public F concat(ProviderBuilder<String, ?> builder) {
        return addFunction(new ConcatStringFunction(builder.build()));
    }
}
