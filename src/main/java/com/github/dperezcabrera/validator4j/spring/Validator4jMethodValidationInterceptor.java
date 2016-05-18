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
package com.github.dperezcabrera.validator4j.spring;

import com.github.dperezcabrera.validator4j.Validator;
import com.github.dperezcabrera.validator4j.ValidatorRegistry;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class Validator4jMethodValidationInterceptor implements MethodInterceptor {

    private static final Log logger = LogFactory.getLog(Validator4jMethodValidationInterceptor.class);

    @Override
    public Object invoke(MethodInvocation mi) throws Throwable {
        if (mi.getArguments() != null) {
            ValidateWith validateRule = mi.getMethod().getAnnotation(ValidateWith.class);
            Validator validator = ValidatorRegistry.getValidator(validateRule.value());
            if (validator == null) {
                logger.warn("There is not a Validator in ValidatorRegistry named: '" + validateRule.value() + "'");
            } else {
                validator.validate(mi.getArguments());
            }
        }
        return mi.proceed();
    }
}
