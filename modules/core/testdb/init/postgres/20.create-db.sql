-- begin QUERYDSL_CUBA_ANIMAL
alter table QUERYDSL_CUBA_ANIMAL add constraint FK_QUERYDSL_CUBA_ANIMAL_ON_CAT foreign key (CAT_ID) references QUERYDSL_CUBA_ANIMAL(ID);
create unique index IDX_QUERYDSL_CUBA_ANIMAL_UK_INT_ID on QUERYDSL_CUBA_ANIMAL (INT_ID) where DELETE_TS is null;
create index IDX_QUERYDSL_CUBA_ANIMAL_ON_CAT on QUERYDSL_CUBA_ANIMAL (CAT_ID);
-- end QUERYDSL_CUBA_ANIMAL
-- begin QUERYDSL_CUBA_SHOW
alter table QUERYDSL_CUBA_SHOW add constraint FK_FORMODEL_SHOW_ON_PARENT foreign key (PARENT_ID) references QUERYDSL_CUBA_SHOW(ID);
create unique index IDX_FORMODEL_SHOW_UK_INT_ID on QUERYDSL_CUBA_SHOW (INT_ID) where DELETE_TS is null;
create index IDX_FORMODEL_SHOW_ON_PARENT on QUERYDSL_CUBA_SHOW (PARENT_ID);
-- end QUERYDSL_CUBA_SHOW
-- begin QUERYDSL_CUBA_EMPLOYEE
alter table QUERYDSL_CUBA_EMPLOYEE add constraint FK_QUERYDSL_CUBA_EMPLOYEE_ON_COMPANY foreign key (COMPANY_ID) references QUERYDSL_CUBA_COMPANY(ID);
create index IDX_QUERYDSL_CUBA_EMPLOYEE_ON_COMPANY on QUERYDSL_CUBA_EMPLOYEE (COMPANY_ID);
-- end QUERYDSL_CUBA_EMPLOYEE
-- begin QUERYDSL_CUBA_USER
alter table QUERYDSL_CUBA_USER add constraint FK_QUERYDSL_CUBA_USER_ON_COMPANY foreign key (COMPANY_ID) references QUERYDSL_CUBA_COMPANY(ID);
create index IDX_QUERYDSL_CUBA_USER_ON_COMPANY on QUERYDSL_CUBA_USER (COMPANY_ID);
-- end QUERYDSL_CUBA_USER
-- begin QUERYDSL_CUBA_COMPANY
create unique index IDX_QUERYDSL_CUBA_COMPANY_UK_INT_ID on QUERYDSL_CUBA_COMPANY (INT_ID) where DELETE_TS is null;
-- end QUERYDSL_CUBA_COMPANY