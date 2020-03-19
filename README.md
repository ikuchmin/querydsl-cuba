## QueryDSL Cuba AppComponent

### Introduction

There is CUBA.platform app component which adds QueryDsl integration for
your CUBA.platform application.

[QueryDSL](http://www.querydsl.com/) is unified queries for Java

### Suggestions in IDE

If you use QueryDSL support for your CUBA.platform project, Your IDE will
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