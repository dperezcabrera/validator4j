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
import java.util.Arrays;
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
        this.objects = Arrays.copyOf(objects, objects.length);
    }

    @Override
    public <T> T select(String name, Class<T> type) {
        String[] attributePath = name.split("\\.");
        Integer index = indexes.get(attributePath[0]);
        if (index == null) {
            throw new ConfigurationValidatorException("Unknown bean identified by '"+name+"'");
        } else if (index < 0 || index >= objects.length) {
            throw new ConfigurationValidatorException("Unknown bean identified by '"+name+"', perhaps wrong order in the call to check beans.");
        } else {
            return (T) get(attributePath, index, objects[index]);
        }
    }

    private Object get(String[] keys, int index, Object o) {
        int i = index + 1;
        try {
            Object temp = o;
            while (temp != null && i < keys.length) {
                String methodName = getMethodName(keys[i++]);
                Method method = temp.getClass().getMethod(methodName);
                temp = method.invoke(temp);
            }
            return temp;
        } catch (NoSuchMethodException | SecurityException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            throw new ConfigurationValidatorException("Error trying to get a property, '"+keys[i]+"' was not found in the object named: '"+keys[i-1]+"' ", ex);
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
