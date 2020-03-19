## QueryDSL Cuba AppComponent

### Introduction

There is CUBA.platform app component which adds QueryDsl integration for
your CUBA.platform application.

[QueryDSL](http://www.querydsl.com/) is unified queries for Java

### Suggestions in IDE

If you use QueryDSL support for your CUBA.platform project, your IDE will
make suggestion during writing queries

![Alt Text](doc/query_dsl_support.gif)

### Compilation

If you use QueryDSL support for jpql queries in your services It
will be guarantee that compiler will check that you queries type-safe.

It also helps you during refactoring

### Example

Demo project: https://github.com/ikuchmin/querydsl-shop


```java
CubaQueryFactory queryFactory = new CubaQueryFactory(txDm, metadata);

QOrder order = new QOrder("o");
QOrderStorageItem orderStorageItem = QOrderStorageItem.orderStorageItem;

return queryFactory.select(order)
        .from(orderStorageItem).join(orderStorageItem.order, order)
        .where(orderStorageItem.storage.id.eq(storageId.getValue()))
        .orderBy(order.updateTs.desc())
        .fetch(view);
```

### Getting started

1. Add this component to your CUBA.platform project from CUBA.platform Marketplace

2. Configure global module in build.gradle for your CUBA.platform project
```groovy
configure(globalModule) {
    dependencies {
    
        // begin: generated cuba studio
        if (!JavaVersion.current().isJava8()) {
            runtime('javax.xml.bind:jaxb-api:2.3.1')
            runtime('org.glassfish.jaxb:jaxb-runtime:2.3.1')
        }
        // end: generated cuba studio

        // begin: add by yourself
        compile group: 'com.querydsl', name: 'querydsl-jpa', version: '4.1.4'

        annotationProcessor group: 'com.querydsl', name: 'querydsl-apt', version: '4.1.4', classifier: 'jpa'
        annotationProcessor configurations.compile
        // end: add by yourself
    }
} 
```

3. Assemble project. This part is needed to generate QueryDSL classes.

4. Enjoy type-safe queries
   
