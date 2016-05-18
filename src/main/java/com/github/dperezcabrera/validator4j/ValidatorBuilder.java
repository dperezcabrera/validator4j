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
import static com.github.dperezcabrera.validator4j.util.Utility.checkNotNull;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public final class ValidatorBuilder {

    private List<ParametrizedRules> rules;
    private List<ParametrizedRulesChainAdapter> rulesAdapter = new ArrayList<>();
    private Map<String, Integer> index;
    private int parameterIndex;

    public ValidatorBuilder(List<ParametrizedRules> rules, Map<String, Integer> index, int parameterIndex) {
        this.rules = rules;
        this.index = index;
        this.parameterIndex = parameterIndex;
    }

    public static ValidatorBuilder rules(ParametrizedRuleBuilder... ruleFactories) {
        checkNotNull(ruleFactories);
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
        return new ValidatorBuilder(rules, index, position);
    }

    public ValidatorBuilder include(Validator v) {
        return doInclude(v, null, null);
    }

    public ValidatorBuilder include(Validator v, String prefix) {
        return doInclude(v, prefix, null);
    }

    public ValidatorBuilder include(String validatorName) {
        return doInclude(ValidatorRegistry.getValidator(validatorName), null, null);
    }

    public ValidatorBuilder include(String validatorName, String prefix) {
        return doInclude(ValidatorRegistry.getValidator(validatorName), prefix, null);
    }

    public ValidatorBuilder include(String validatorName, String prefix, String removeFromPreviousName) {
        return doInclude(ValidatorRegistry.getValidator(validatorName), prefix, removeFromPreviousName);
    }

    public ValidatorBuilder include(Validator validator, String prefix, String removeFromPreviousName) {
        return doInclude(validator, prefix, removeFromPreviousName);
    }

    private ValidatorBuilder doInclude(Validator validator, String prefix, String removeFromPreviousName) {
        checkNotNull(validator);
        if (validator instanceof ValidatorBase) {
            ValidatorBase v = (ValidatorBase) validator;
            if (!v.rules.isEmpty()) {
                rulesAdapter.add(new ParametrizedRulesChainAdapter(prefix, removeFromPreviousName, new ArrayList<>(v.rules)));
            }
            v.rulesAdapter.stream().forEach(adapter -> rulesAdapter.add(new ParametrizedRulesChainAdapter(prefix, removeFromPreviousName, adapter)));
            if (prefix == null) {
                v.index.entrySet().stream().sorted((e0, e1) -> Integer.compare(e0.getValue(), e1.getValue())).filter(e -> !index.containsKey(e.getKey())).forEach(e -> index.put(e.getKey(), parameterIndex++));
            }
        }
        return this;
    }

    protected Validator build() {
        return new ValidatorBase(rules, rulesAdapter, index);
    }

    private static class ValidatorBase implements Validator {

        private List<ParametrizedRules> rules;
        private List<ParametrizedRulesChainAdapter> rulesAdapter;
        private Map<String, Integer> index;

        public ValidatorBase(List<ParametrizedRules> rules, List<ParametrizedRulesChainAdapter> rulesAdapter, Map<String, Integer> index) {
            this.rules = rules;
            this.rulesAdapter = rulesAdapter;
            this.index = index;
        }

        @Override
        public Selector validate(Object... elements) {
            ErrorManager errorManager = new ErrorManagerBase();
            Selector selector = new SelectorBase(index, elements);
            rules.stream().forEach(rule -> rule.validate(errorManager, selector));
            for (ParametrizedRulesChainAdapter adapter : rulesAdapter) {
                Selector adaptedSelector = adapter.getSelector(selector);
                ErrorManager adaptedErrorManager = adapter.getErrorManager(errorManager);
                adapter.getParametrizedRules().stream().forEach(rule -> rule.validate(adaptedErrorManager, adaptedSelector));
            }
            errorManager.check();
            return selector;
        }
    }
}
