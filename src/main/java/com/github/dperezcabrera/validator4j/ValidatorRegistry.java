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

import com.github.dperezcabrera.validator4j.core.ParametrizedRuleBuilder;
import com.github.dperezcabrera.validator4j.spring.Validator4jMethodValidationInterceptor;
import static com.github.dperezcabrera.validator4j.util.Utility.checkNotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public enum ValidatorRegistry {
    INSTANCE;

    private static final Log logger = LogFactory.getLog(Validator4jMethodValidationInterceptor.class);

    private Map<String, Validator> validators = new HashMap<>();
    private final Lock readLock;
    private final Lock writeLock;

    private ValidatorRegistry() {
        ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();
        readLock = readWriteLock.readLock();
        writeLock = readWriteLock.writeLock();
    }

    public static Validator register(String name, ParametrizedRuleBuilder... rules) {
        checkNotNull(rules);
        return register(name, ValidatorBuilder.rules(rules));
    }

    public static Validator register(String name, ValidatorBuilder builder) {
        checkNotNull(name);
        Validator validator = builder.build();
        Validator lastValidator = null;
        INSTANCE.writeLock.lock();
        try {
            lastValidator = INSTANCE.validators.put(name, validator);
        } finally {
            INSTANCE.writeLock.unlock();
        }
        if (lastValidator != null) {
            logger.warn("There was another Validator named '" + name + "' but It has been replaced.");
        }
        return validator;
    }

    public static Validator getValidator(String name) {
        checkNotNull(name);
        INSTANCE.readLock.lock();
        try {
            return INSTANCE.validators.get(name);
        } finally {
            INSTANCE.readLock.unlock();
        }
    }
}
