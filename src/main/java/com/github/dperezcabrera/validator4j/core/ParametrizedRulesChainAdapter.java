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
 */
public class ParametrizedRulesChainAdapter {

    private ParametrizedRulesChainAdapter nextAdapter = null;
    private String prefix;
    private String removeFromName;
    private List<ParametrizedRules> parametrizedRules;

    public ParametrizedRulesChainAdapter(String prefix, String removeFromName, List<ParametrizedRules> parametrizedRules) {
        this.prefix = prefix;
        this.removeFromName = removeFromName;
        this.parametrizedRules = new ArrayList<>(parametrizedRules);
    }

    public ParametrizedRulesChainAdapter(String prefix, String removeFromName, ParametrizedRulesChainAdapter nextAdapter) {
        this(prefix, removeFromName, nextAdapter.parametrizedRules);
        this.nextAdapter = nextAdapter;
    }

    public List<ParametrizedRules> getParametrizedRules() {
        return parametrizedRules;
    }

    static String removePrefix(String name, String prefixToRemove){
        String result = name;
        if (name != null){
            if (name.equals(prefixToRemove)){
                result = null;
            } else if (prefixToRemove != null && name.startsWith(prefixToRemove.concat(Constants.ATTRIBUTE_SEPARATOR))){
                result = name.substring(prefixToRemove.length() + 1);
            }
        }
        return result;
    }
    
    static String convert(String prefix, String value, String removeFromName) {
        String name = removePrefix(value, removeFromName);
        String result = prefix;
        if (name != null) {
            if (prefix != null) {
                result = String.join(Constants.ATTRIBUTE_SEPARATOR, prefix, name);
            } else {
                result = name;
            }
        }
        return result;
    }

    public Selector getSelector(Selector selector) {
        Selector result = new SelectorAdapter(selector);
        if (nextAdapter != null) {
            result = nextAdapter.getSelector(result);
        }
        return result;
    }

    public ErrorManager getErrorManager(ErrorManager errorManager) {
        ErrorManager result = new ErrorManagerAdapter(errorManager);
        if (nextAdapter != null) {
            result = nextAdapter.getErrorManager(result);
        }
        return new ErrorManagerAdapter(result);
    }

    private class SelectorAdapter implements Selector {

        private Selector selector;

        public SelectorAdapter(Selector selector) {
            this.selector = selector;
        }

        @Override
        public <T> T select(String name, Class<T> type) {
            return selector.select(convert(prefix, name, removeFromName), type);
        }
    }

    private class ErrorManagerAdapter implements ErrorManager {

        private ErrorManager errorManager;

        public ErrorManagerAdapter(ErrorManager errorManager) {
            this.errorManager = errorManager;
        }

        @Override
        public void addErrorMessage(String attributeName, String message) {
            errorManager.addErrorMessage(convert(prefix, attributeName, removeFromName), message);
        }

        @Override
        public void check() {
            errorManager.check();
        }
    }
}
