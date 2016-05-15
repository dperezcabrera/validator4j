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
package com.github.dperezcabrera.validator4j.validator;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import com.github.dperezcabrera.validator4j.core.ErrorManagerBase;
import com.github.dperezcabrera.validator4j.core.ErrorManager;
import com.github.dperezcabrera.validator4j.core.Selector;
import com.github.dperezcabrera.validator4j.core.SelectorBase;
import java.util.function.Supplier;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class Validator {

    private List<ParameterRules> rules;
    private Map<String, Integer> index;

    Validator(List<ParameterRules> rules, Map<String, Integer> index) {
        this.rules = rules;
        this.index = index;
    }

    public static Validator rules(ParameterRuleBuilder... ruleFactories) {
        List<ParameterRules> rules = new ArrayList<>(ruleFactories.length);
        Map<String, Integer> index = new HashMap<>();
        int position = 0;
        for (ParameterRuleBuilder<?> builders : ruleFactories) {
            ParameterRules<?> rule = builders.build();
            String parameter = rule.getName().split("\\.")[0];
            if (!index.containsKey(parameter)) {
                index.put(parameter, position++);
            }
            if (rule.isActive()) {
                rules.add(rule);
            }
        }
        return new Validator(rules, index);
    }

    public static <T> Supplier<T> get(T object) {
        return () -> object;
    }

    public Selector check(Object element, Object... elements) {
        Object[] totalElements = new Object[elements.length + 1];
        totalElements[0] = getObject(element);
        for (int i = 0; i < elements.length; i++) {
            totalElements[i + 1] = getObject(elements[i]);
        }
        ErrorManager errorManager = new ErrorManagerBase();
        Selector selector = new SelectorBase(index, totalElements);
        for (ParameterRules attributeRules : rules) {
            attributeRules.validate(errorManager, selector);
        }
        errorManager.check();
        return selector;
    }

    private Object getObject(Object obj) {
        if (obj != null && obj instanceof Supplier) {
            return ((Supplier) obj).get();
        }
        return obj;
    }
}
