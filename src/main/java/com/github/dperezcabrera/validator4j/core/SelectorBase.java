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
package com.github.dperezcabrera.validator4j.core;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class SelectorBase implements Selector {

    private static final String GET_PREFIX = "get";
    private Map<String, Integer> indexes;
    private Object[] objects;

    public SelectorBase(Map<String, Integer> indexes, Object[] objects) {
        this.indexes = indexes;
        this.objects = objects;
    }

    @Override
    public <T> T select(String name, Class<T> type) {
        String[] attributePath = name.split("\\.");
        Integer index = indexes.get(attributePath[0]);
        if (index == null) {
            // TODO: add message
            throw new ConfigurationValidatorException("");
        } else if (index < 0 || index >= objects.length) {
            // TODO: add message
            throw new ConfigurationValidatorException("");
        } else {
            return (T) get(attributePath, index, objects[index]);
        }
    }

    private Object get(String[] keys, int index, Object o) {
        try {
            Object temp = o;
            int i = index + 1;
            while (temp != null && i < keys.length) {
                String methodName = getMethodName(keys[i++]);
                Method method = temp.getClass().getMethod(methodName);
                temp = method.invoke(temp);
            }
            return temp;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            // TODO: add message
            throw new ConfigurationValidatorException("", ex);
        }
    }

    private String getMethodName(String attributeName) {
        if (attributeName != null && attributeName.length() > 0) {
            StringBuilder sb = new StringBuilder(GET_PREFIX.length() + attributeName.length());
            sb.append(GET_PREFIX);
            sb.append(Character.toUpperCase(attributeName.charAt(0)));
            sb.append(attributeName.substring(1));
            return sb.toString();
        } else {
            return null;
        }
    }
}
