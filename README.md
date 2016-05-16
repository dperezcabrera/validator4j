[![License](http://img.shields.io/:license-gpl3-blue.svg)](http://www.gnu.org/licenses/gpl-3.0.html)
[![Build Status](https://travis-ci.org/dperezcabrera/validator4j.svg?branch=master)](https://travis-ci.org/dperezcabrera/validator4j)
[![Coverage Status](https://coveralls.io/repos/github/dperezcabrera/validator4j/badge.svg?branch=master)](https://coveralls.io/github/dperezcabrera/validator4j?branch=master)
[![Codacy Badge](https://api.codacy.com/project/badge/Grade/4c0b73fa27ed46b1b45449db8dcfea34)](https://www.codacy.com/app/dperezcabrera/validator4j?utm_source=github.com&amp;utm_medium=referral&amp;utm_content=dperezcabrera/validator4j&amp;utm_campaign=Badge_Grade)
[![GitHub issues](https://img.shields.io/github/issues-raw/dperezcabrera/validator4j.svg?maxAge=2592000)](https://github.com/dperezcabrera/validator4j/issues)


# Validator for Java

The Simple Validator for Java is a software library that provides a very simple API with a powerful rule engine.

## Rules definition: Creating a validator
```java
    Validator CREATE_USER_VALIDATOR = rules(
        stringRule("name").notNull().startsWith("Sr.").contains("John"),
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

    Selector selector = CREATE_USER_VALIDATOR.check(name, id, children, get(email), birthay, activationDate, deactivationDate);
    
    String emailResult = selector.select("email", String.class);

    assertEquals(email, emailResult);
```

## Validate attributes
```java
    Long userId = ...;

    Validator obtainUserValidator = rules(
        cmpRule("user.id").notNull().greatherThan(0L),
        stringRule("user.name").notNull().notEmpty(),
        stringRule("user.email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
        dateRule("user.birthay").notNull().before(now().ceil(DATE).sub(18, YEAR)),
        dateRule("user.signUpDate").notNull().before(now().ceil(DATE).add(1, DATE)),
        dateRule("user.lastAccessDate").notNull().after(date("user.signUpDate").truncate(DATE).sub(1, DATE)),
        objectRule("user.address").notNull(),
        cmpRule("user.address.id").notNull(),
        stringRule("user.address.address0").notNull().notEmpty().maxLength(24),
        stringRule("user.address.address1").notNull().notEmpty().maxLength(24),
        stringRule("user.address.city").notNull().minLength(4),
        stringRule("user.address.region").notNull().minLength(4),
        stringRule("user.address.zipCode").notNull().minLength(4).maxLength(8),
        objectRule("user.address.country").notNull(),
        cmpRule("user.address.country.id").notNull().greatherThan(0L),
        stringRule("user.address.country.name").notNull().minLength(3).maxLength(12),
        stringRule("user.address.country.language").notNull().minLength(4),
    );

    User expectedResult = userRepository.findOne(userId);

    Selector selector = obtainUserValidator.check(get(userRepository.findOne(userId)));

    User result = selector.select("user", User.class);

    assertEquals(expectedResult, result);
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
