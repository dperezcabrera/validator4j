[![License](http://img.shields.io/:license-gpl3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![Build Status](https://travis-ci.org/dperezcabrera/validator4j.svg?branch=master)](https://travis-ci.org/dperezcabrera/validator4j)
[![Coverage Status](https://coveralls.io/repos/github/dperezcabrera/validator4j/badge.svg?branch=master)](https://coveralls.io/github/dperezcabrera/validator4j?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/5988bcd325c14e1ba8723618c3c98c60)](https://www.codacy.com/app/dperezcabrera/validator4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dperezcabrera/validator4j&amp;utm_campaign=Badge_Grade)
[![GitHub issues](https://img.shields.io/github/issues-raw/dperezcabrera/validator4j.svg?maxAge=2592000)](https://github.com/dperezcabrera/validator4j/issues)


# Validator for Java

The Simple Validator for Java is a software library that provides a very simple API with a powerful rule engine.

## Rules definition: Creating a validator

```java
    Validator CREATE_USER_VALIDATOR = rules(
            stringRule("name").notNull().startsWith("sr.").contains("John"),
            stringRule("id").mustBeNull(),
            cmpRule("children").notNull().range(2, 4),
            stringRule("email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
            dateRule("birthay").notNull().before(now().ceil(DATE).sub(18, YEAR)),
            dateRule("activationDate").notNull(),
            dateRule("deactivationDate").after(date("activated").add(3, MONTH))
    );
```

## Basic validation
```java

    String name = "Sr. John Doe";
    String id = null;
    String email = "john.doe@example.com";
    Calendar birthay = Calendar.getInstance();
    Calendar activationDate = Calendar.getInstance();
    Calendar deactivationDate = Calendar.getInstance();
    birthay.add(YEAR, -18);
    activationDate = DateUtils.ceiling(activationDate, DATE);
    deactivationDate.add(DATE, 1);
    deactivationDate.add(MONTH, 3);
    int children = 3;

    CREATE_USER_VALIDATOR.check(name, id, children, email, birthay, activationDate, deactivationDate);
```

## Obtaining a valid beans from Selector
```java

    Long userId = ...;

    Validator userValidator = rules(
        objectRule("user").notNull(),
        stringRule("user.name").notNull().startsWith("Sr."),
        stringRule("user.activationDate").notNull().before(now().ceil(DATE)),
        stringRule("user.deactivationDate").after(now().trunk(DATE).sub(1, DATE)), // It can be null
        stringRule("user.id").notNull().equalsTo(userId)
    );

    Selector selector = userValidator.check(get(userRepository.findOne(userId)));
        
    User user = selector.select("user", User.class);
```


## Maven install
```shell
mvn clean install
```

## Maven local repository dependency
```xml
<dependency>
    <groupId>com.github.dperezcabrera</groupId>
    <artifactId>validator4j</artifactId>
    <version>0.0.1-SNAPSHOT</version>
</dependency>
```
