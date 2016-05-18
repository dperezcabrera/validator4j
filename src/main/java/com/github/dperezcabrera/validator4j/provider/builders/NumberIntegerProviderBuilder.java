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
import com.github.dperezcabrera.validator4j.provider.builders.NumberProviderBuilder.Operator;
import com.github.dperezcabrera.validator4j.provider.builders.NumberIntegerProviderBuilder.OperatorInteger;
import java.util.List;
import java.util.function.BiFunction;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <N>
 * @param <O>
 * @param <F>
 */
public class NumberIntegerProviderBuilder<N extends Number, O extends OperatorInteger<N>, F extends NumberIntegerProviderBuilder<N, O, F>> extends NumberProviderBuilder<N, O, F> {

    public NumberIntegerProviderBuilder(O operator, Provider<?> provider) {
        super(operator, provider);
    }

    public NumberIntegerProviderBuilder(O operator, Provider<?> provider, List<BiFunction<?, Selector, ?>> functions) {
        super(operator, provider, functions);
    }

    public F remain(N parameter) {
        return addFunction((t, s) -> getOperator().remain(t, parameter));
    }

    public F remain(ProviderBuilder<N, ?> providerBuilder) {
        return addFunction((t, s) -> getOperator().remain(t, providerBuilder.data(s)));
    }

    public interface OperatorInteger<N extends Number> extends Operator<N>{

        N remain(N n0, N n1);
    }
}
