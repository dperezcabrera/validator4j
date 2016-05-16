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
package com.github.dperezcabrera.validator4j;

import com.github.dperezcabrera.validator4j.core.Constants;
import com.github.dperezcabrera.validator4j.core.ParametrizedRuleBuilder;
import com.github.dperezcabrera.validator4j.core.ParametrizedRules;
import com.github.dperezcabrera.validator4j.core.ErrorManagerBase;
import com.github.dperezcabrera.validator4j.core.ErrorManager;
import com.github.dperezcabrera.validator4j.core.Selector;
import com.github.dperezcabrera.validator4j.core.ParametrizedRulesChainAdapter;
import com.github.dperezcabrera.validator4j.core.SelectorBase;
import java.util.function.Supplier;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class Validator {

    private List<ParametrizedRules> rules;
    private List<ParametrizedRulesChainAdapter> rulesAdapter = new ArrayList<>();
    private Map<String, Integer> index;
    private int parameterIndex;

    private Validator(List<ParametrizedRules> rules, Map<String, Integer> index, int parameterIndex) {
        this.rules = rules;
        this.index = index;
        this.parameterIndex = parameterIndex;
    }

    public static Validator rules(ParametrizedRuleBuilder... ruleFactories) {
        List<ParametrizedRules> rules = new ArrayList<>(ruleFactories.length);
        Map<String, Integer> index = new HashMap<>();
        int position = 0;
        for (ParametrizedRuleBuilder<?> builders : ruleFactories) {
            ParametrizedRules<?> rule = builders.build();
            String parameter = rule.getName().split(Constants.ATTRIBUTE_SEPARATOR_PATTERN)[0];
            if (!index.containsKey(parameter)) {
                index.put(parameter, position++);
            }
            if (rule.isActive()) {
                rules.add(rule);
            }
        }
        return new Validator(rules, index, position);
    }

    public Validator include(Validator v) {
        return include(v, null, null);
    }

    public Validator include(Validator v, String prefix) {
        return include(v, prefix, null);
    }

    public Validator include(Validator v, String prefix, String removeFromPreviousName) {
        if (!v.rules.isEmpty()){
            rulesAdapter.add(new ParametrizedRulesChainAdapter(prefix, removeFromPreviousName, new ArrayList<>(v.rules)));
        }
        v.rulesAdapter.stream().forEach(adapter -> rulesAdapter.add(new ParametrizedRulesChainAdapter(prefix, removeFromPreviousName, adapter)));
        if (prefix == null){
            v.index.entrySet().stream().sorted((e0, e1) -> Integer.compare(e0.getValue(), e1.getValue())).filter(e -> !index.containsKey(e.getKey())).forEach(e -> index.put(e.getKey(), parameterIndex++));
        }
        return this;
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
        rules.stream().forEach(rule -> rule.validate(errorManager, selector));
        for (ParametrizedRulesChainAdapter adapter : rulesAdapter) {
            Selector adaptedSelector = adapter.getSelector(selector);
            ErrorManager adaptedErrorManager = adapter.getErrorManager(errorManager);
            adapter.getParametrizedRules().stream().forEach(rule -> rule.validate(adaptedErrorManager, adaptedSelector));
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
