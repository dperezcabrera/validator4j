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
        dateRule("birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
        dateRule("activationDate").notNull(),
        dateRule("deactivationDate").after(date("activated").add(MONTH, 3))
    ).build();
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

## Obtaining a valid bean from Selector
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
        stringRule("user.email").notIn("admin@example.com", "pepe@example.com").matches(EMAIL_PATTERN),
        dateRule("user.birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
        dateRule("user.signUpDate").notNull().before(now().ceil(DATE).add(DATE, 1)),
        dateRule("user.lastAccessDate").notNull().after(date("user.signUpDate").truncate(DATE).sub(DATE, 1)),
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
    ).build();

    User expectedResult = userRepository.findOne(userId);

    Selector selector = obtainUserValidator.check(get(userRepository.findOne(userId)));

    User result = selector.select("user", User.class);

    assertEquals(expectedResult, result);
```

## Including rules from other validators
```java

    Long userId = ...;
    Validator countryValidator = rules(
        cmpRule("c.id").notNull().greatherThan(0L),
        stringRule("c.name").notNull().minLength(3).maxLength(12),
        stringRule("c.language").notNull().minLength(4)
    ).build();

    Validator addressValidator = rules(
        cmpRule("address.id").notNull().lessThan(1000L),
        stringRule("address.address0").notNull().maxLength(24),
        stringRule("address.address1").notNull().maxLength(24),
        stringRule("address.city").notNull().minLength(2).maxLength(24),
        stringRule("address.region").notNull().minLength(2).maxLength(24),
        stringRule("address.zipCode").notNull().minLength(4).maxLength(8),
        objectRule("address.country").notNull())
    .include(countryValidator, "address.country", "c").build();

    Validator otherValidator = rules(
        stringRule("namePrefix").notNull().maxLength(24),
        stringRule("nameSufix").notNull().maxLength(integer("nameLength")),
        stringRule("nameContains").notNull().maxLength(integer("nameLength")),
        objectRule("nameLength").notNull()
    ).build();

    Validator obtainUserValidator = rules(
        cmpRule("user.id").notNull().greatherThan(0L),
        stringRule("user.name").notNull().notEmpty().startsWith(stringFrom("namePrefix")).endsWith(stringFrom("nameSufix")).contains(stringFrom("nameContains")).length(integer("nameLength")),
        stringRule("user.email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
        dateRule("user.birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
        dateRule("user.signUpDate").notNull().before(now().ceil(DATE).add(DATE, 1)),
        dateRule("user.lastAccessDate").notNull().after(date("user.signUpDate").truncate(DATE).sub(DATE, 1)),
        objectRule("user.address").notNull())
    .include(addressValidator, "user")
    .include(otherValidator)
    .build();

    Integer nameLength = 4;

    String namePrefix = "J";
    String nameSufix = "n";
    String nameContains = "ohn";

    obtainUserValidator.check(get(userRepository.findOne(userId)), namePrefix, nameSufix, nameContains, nameLength);

```

## Spring-framework integration

### Spring-framework integration: Enable Validator4j
```java

    @Configuration
    @EnableValidator4j
    public class ConfigFoo {
        
        @Bean
        public Foo getFoo() {
            return new Foo();
        }
    }
```

### Spring-framework integration: Register rules and validators:
```java

    ValidatorRegistry.register("country_validator",
            cmpRule("c.id").notNull().greatherThan(0L),
            stringRule("c.name").notNull().minLength(3).maxLength(12),
            stringRule("c.language").notNull().minLength(4)
    );

    ValidatorRegistry.register("address_validator", rules(
            cmpRule("address.id").notNull().lessThan(1000L),
            stringRule("address.address0").notNull().maxLength(24),
            stringRule("address.address1").notNull().maxLength(24),
            stringRule("address.city").notNull().minLength(2).maxLength(24),
            stringRule("address.region").notNull().minLength(2).maxLength(24),
            stringRule("address.zipCode").notNull().minLength(4).maxLength(8),
            objectRule("address.country").notNull()
    ).include("country_validator", "address.country", "c"));

    ValidatorRegistry.register("other_validator",
            stringRule("namePrefix").notNull().maxLength(24),
            stringRule("nameSufix").notNull().maxLength(getInteger("nameLength")),
            stringRule("nameContains").notNull().minLength(getInteger("nameLength").div(string("5").toLong().toInteger().add(getInteger("nameLength")).remain(now().dayOfMonth()))),
            objectRule("nameLength").notNull()
    );

    ValidatorRegistry.register("user_validator", rules(cmpRule("user.id").notNull().greatherThan(0L),
            stringRule("user.name").notNull().notEmpty().startsWith(stringFrom("namePrefix")).endsWith(stringFrom("nameSufix")).contains(stringFrom("nameContains")).length(getInteger("nameLength")),
            stringRule("user.email").notIn("admin@a.com", "pepe@a.com").matches(EMAIL_PATTERN),
            dateRule("user.birthay").notNull().before(now().ceil(DATE).sub(YEAR, 18)),
            dateRule("user.signUpDate").notNull().before(now().ceil(DATE).add(DATE, 1)),
            dateRule("user.lastAccessDate").notNull().after(getCalendar("user.signUpDate").truncate(DATE).sub(DATE, 1)),
            objectRule("user.address").notNull()
    ).include("address_validator", "user").include("other_validator"));
```

### Spring-framework integration: Configure method validation with annotation
```java

    public class Foo {

        @ValidateWith("user_validator")
        public void aMethod(User user, String namePrefix, String nameSufix, String nameContains, int nameLength) {
            ...
        }
        ...
    }
```

### Spring-framework integration: Use:
```java    
    
    Long userId = ...;

    User expectedResult = userRepository.findOne(userId);
    int nameLength = expectedResult.getName().length();

    String namePrefix = "J";
    String nameSufix = "n";
    String nameContains = "ohn";

    target.aMethod(userRepository.findOne(userId), namePrefix, nameSufix, nameContains, nameLength);
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
