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

import org.springframework.aop.config.AopConfigUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.beans.factory.support.BeanDefinitionRegistry;
import org.springframework.beans.factory.support.BeanDefinitionRegistryPostProcessor;
import org.springframework.beans.factory.support.RootBeanDefinition;

/**
 *
 * @author David Pérez Cabrera <dperezcabrera@gmail.com>
 */
public class Validator4jRegistryPostProcessor implements BeanDefinitionRegistryPostProcessor {

    private static final String VALIDATOR_FOR_JAVA_ADVISOR = Validator4jAdvisor.class.getName();

    private static boolean initialized = false;

    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        // Do nothing.
    }

    @Override
    public void postProcessBeanDefinitionRegistry(BeanDefinitionRegistry registry) throws BeansException {
        if (!init()) {
            AopConfigUtils.registerAutoProxyCreatorIfNecessary(registry);
            RootBeanDefinition beanDefinition = new RootBeanDefinition(Validator4jAdvisor.class);
            beanDefinition.setRole(BeanDefinition.ROLE_INFRASTRUCTURE);
            registry.registerBeanDefinition(VALIDATOR_FOR_JAVA_ADVISOR, beanDefinition);
        }
    }

    private static synchronized boolean init() {
        boolean result = initialized;
        initialized = true;
        return result;
    }
}
