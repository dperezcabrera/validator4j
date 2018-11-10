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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class ErrorManagerBase implements ErrorManager {

    private Map<String, List<String>> errors;

    private Map<String, List<String>> getErrors() {
        if (errors == null) {
            errors = new HashMap<>();
        }
        return errors;
    }

    @Override
    public void addErrorMessage(String name, String message) {
        getErrors().computeIfAbsent(name, k -> new ArrayList<>()).add(message);
    }

    @Override
    public void check() {
        if (errors != null){
            StringBuilder sb = new StringBuilder();
            sb.append("Errors:\n");
            for (Entry<String, List<String>> e : errors.entrySet()) {
                sb.append("\t- attribute '");
                sb.append(e.getKey());
                sb.append("' (");
                sb.append(e.getValue().size());
                sb.append(")\n:");
                for (String msg: e.getValue()) {
                    sb.append("\t\t* ");
                    sb.append(msg);
                    sb.append("\n");
                }
                sb.append("\n");
            }
            throw new ValidatorException(sb.toString(), errors);
        }
    }
}
