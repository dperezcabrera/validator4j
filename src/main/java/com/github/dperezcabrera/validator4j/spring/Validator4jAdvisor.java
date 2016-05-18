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

import java.lang.reflect.Method;
import org.aopalliance.aop.Advice;
import org.springframework.aop.Pointcut;
import org.springframework.aop.support.AbstractPointcutAdvisor;
import org.springframework.aop.support.StaticMethodMatcherPointcut;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class Validator4jAdvisor extends AbstractPointcutAdvisor {

    private static final long serialVersionUID = 1L;
    
    private static final Validator4jMethodValidationInterceptor INTERCEPTOR = new Validator4jMethodValidationInterceptor();

    private static final StaticMethodMatcherPointcut POINTCUT = new StaticMethodMatcherPointcut() {
        @Override
        public boolean matches(Method method, Class<?> targetClass) {
            return method.isAnnotationPresent(ValidateWith.class);
        }
    };

    @Override
    public Pointcut getPointcut() {
        return Validator4jAdvisor.POINTCUT;
    }

    @Override
    public Advice getAdvice() {
        return Validator4jAdvisor.INTERCEPTOR;
    }
}
