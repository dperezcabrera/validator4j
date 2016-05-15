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
package com.github.dperezcabrera.validator4j.provider.calendar;

import java.util.Calendar;
import com.github.dperezcabrera.validator4j.core.Selector;
import com.github.dperezcabrera.validator4j.provider.IngoreNullFunction;
import com.github.dperezcabrera.validator4j.provider.Provider;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 */
public class CalendarFunction<T extends Calendar> extends IngoreNullFunction<T> {

    private Provider<Integer> amountProvider = null;
    private int amount;
    private int field;
    private boolean adds;

    public CalendarFunction(Provider<Integer> amountProvider, int field, boolean adds) {
        this.amountProvider = amountProvider;
        this.field = field;
        this.adds = adds;
    }
    
    public CalendarFunction(int amount, int field, boolean adds) {
        this.amount = amount;
        this.field = field;
        this.adds = adds;
    }

    @Override
    protected T applyNotNull(T item, Selector selector) {
        if (amountProvider != null){
            amount = amountProvider.data(selector);
        }
        if (!adds) {
            amount = -amount;
        }
        T result = (T)item.clone();
        result.add(field, amount);
        return result;
    }
}
