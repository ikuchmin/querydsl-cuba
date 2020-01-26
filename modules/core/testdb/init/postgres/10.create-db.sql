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
)
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


