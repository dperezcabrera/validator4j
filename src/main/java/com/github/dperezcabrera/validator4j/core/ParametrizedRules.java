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

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 * @param <T>
 */
public class ParametrizedRules<T> {

    public enum NullConstraint {

        NULLABLE,
        NOT_NULL,
        NULL
    }

    private String name;
    private NullConstraint nullConstraint;
    private List<Rule<T>> rules = null;
    private Class<T> type;
    
    public ParametrizedRules(String name, Class<T> type) {
        this.name = name;
        this.type = type;
    }

    public void addRule(Rule<T> rule) {
        if (rules == null) {
            rules = new ArrayList<>();
        }
        rules.add(rule);
    }

    public void setNullConstraint(NullConstraint nullConstraint) {
        this.nullConstraint = nullConstraint;
    }

    public NullConstraint getNullConstraint() {
        return nullConstraint;
    }

    public String getName() {
        return name;
    }

    public boolean isActive() {
        return nullConstraint != NullConstraint.NULLABLE || rules != null;
    }

    public void validate(ErrorManager errorManager, Selector selector) {
        T value = selector.select(name, type);
        if (value != null) {
            if (nullConstraint == NullConstraint.NULL) {
                errorManager.addErrorMessage(name, "Object named '"+ name+"' must be 'null' but It was not.");
            } else if (rules != null) {
                rules.stream().forEach(rule -> rule.validate(value, name, selector, errorManager));
            }
        } else if (nullConstraint == NullConstraint.NOT_NULL) {
            errorManager.addErrorMessage(name,"Object named '"+ name+"' was 'null' and It must be not 'null'.");
        }
    }
}
