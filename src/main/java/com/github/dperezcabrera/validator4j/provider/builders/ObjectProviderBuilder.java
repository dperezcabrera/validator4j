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
import com.github.dperezcabrera.validator4j.provider.ProviderBuilder;
import com.github.dperezcabrera.validator4j.provider.Provider;
import com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.StringProviderBuilderBase;
import com.github.dperezcabrera.validator4j.provider.builders.StringProviderBuilder.StringProviderBuilderFactory;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 * @param <F>
 */
public class ObjectProviderBuilder<T, F extends ObjectProviderBuilder<T, F>> extends ProviderBuilder<T, F> {

    public ObjectProviderBuilder(Provider<?> provider) {
        super(provider);
    }

    public ObjectProviderBuilder(Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(provider, functions);
    }

    public StringProviderBuilderBase string() {
        return addFunction(t -> t.toString(), new StringProviderBuilderFactory());
    }
}
