-- begin QUERYDSL_CUBA_ANIMAL
create table QUERYDSL_CUBA_ANIMAL
(
    ID              uuid,
    VERSION         integer not null,
    CREATE_TS       timestamp,
    CREATED_BY      varchar(50),
    UPDATE_TS       timestamp,
    UPDATED_BY      varchar(50),
    DELETE_TS       timestamp,
    DELETED_BY      varchar(50),
    DTYPE           varchar(100),
    --
    INT_ID          integer,
    ALIVE           boolean,
    BIRTHDATE       timestamp,
    WEIGHT          integer,
    TOES            integer,
    BODY_WEIGHT     double precision,
    DOUBLE_PROPERTY double precision,
    COLOR           varchar(255),
    DATE_FIELD      date,
    NAME            varchar(255),
    --
    -- from querydslcuba_Cat
    BREED           integer,
    EYECOLOR        varchar(255),
    CAT_ID          uuid,
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_ANIMAL
-- begin QUERYDSL_CUBA_KITTENS_SET
create table QUERYDSL_CUBA_KITTENS_SET
(
    ID         uuid,
    VERSION    integer not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    CAT_ID     uuid,
    KITTEN_ID  uuid,
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_KITTENS_SET

-- begin QUERYDSL_CUBA_SHOW
create table QUERYDSL_CUBA_SHOW
(
    ID         uuid,
    VERSION    integer not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    acts_key   varchar,
    INT_ID     integer,
    PARENT_ID  uuid,
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_SHOW
-- begin QUERYDSL_CUBA_EMPLOYEE
create table QUERYDSL_CUBA_EMPLOYEE
(
    ID         uuid,
    VERSION    integer not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    INT_ID     integer,
    FIRST_NAME varchar(255),
    LAST_NAME  varchar(255),
    COMPANY_ID uuid,
    USER_ID    uuid,
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_EMPLOYEE
-- begin QUERYDSL_CUBA_EMPLOYEE_JOBFUNCTIONS
create table QUERYDSL_CUBA_EMPLOYEE_JOBFUNCTIONS
(
    EMPLOYEE_ID uuid,
    JOBFUNCTION varchar
);
-- end QUERYDSL_CUBA_EMPLOYEE_JOBFUNCTIONS
-- begin QUERYDSL_CUBA_DEPARTMENT
create table QUERYDSL_CUBA_DEPARTMENT
(
    ID         uuid,
    VERSION    integer not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    NAME       varchar(255),
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_DEPARTMENT
-- begin QUERYDSL_CUBA_USER
create table QUERYDSL_CUBA_USER
(
    ID         uuid,
    VERSION    integer not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    USER_NAME  varchar(255),
    FIRST_NAME varchar(255),
    LAST_NAME  varchar(255),
    COMPANY_ID uuid,
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_USER
-- begin QUERYDSL_CUBA_COMPANY
create table QUERYDSL_CUBA_COMPANY
(
    ID             uuid,
    VERSION        integer not null,
    CREATE_TS      timestamp,
    CREATED_BY     varchar(50),
    UPDATE_TS      timestamp,
    UPDATED_BY     varchar(50),
    DELETE_TS      timestamp,
    DELETED_BY     varchar(50),
    --
    INT_ID         integer,
    NAME           varchar(255),
    OFFICIAL_NAME  varchar(255),
    RATING_ORDINAL varchar(255),
    RATING_STRING  varchar(255),
    EMPLOYEE_ID    uuid,
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_COMPANY
-- begin QUERYDSL_CUBA_NUMERIC
create table QUERYDSL_CUBA_NUMERIC
(
    ID         uuid,
    VERSION    integer not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    --
    LONG_ID    bigint,
    VALUE      decimal(19, 2),
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_NUMERIC
-- begin QUERYDSL_CUBA_ENTITY1
create table QUERYDSL_CUBA_ENTITY1
(
    ID         uuid,
    VERSION    integer not null,
    CREATE_TS  timestamp,
    CREATED_BY varchar(50),
    UPDATE_TS  timestamp,
    UPDATED_BY varchar(50),
    DELETE_TS  timestamp,
    DELETED_BY varchar(50),
    DTYPE      varchar(100),
    --
    INT_ID     integer,
    PROPERTY   varchar(255),
    --
    -- from querydslcuba_Entity2
    PROPERTY2  varchar(255),
    --
    primary key (ID)
);
-- end QUERYDSL_CUBA_ENTITY1
