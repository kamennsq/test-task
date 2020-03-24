CREATE SEQUENCE IDSEQUENCE AS INTEGER START WITH 16 INCREMENT BY 1;
CREATE TABLE PATIENT(ID BIGINT NOT NULL PRIMARY KEY,NAME VARCHAR(10) NOT NULL,SURNAME VARCHAR(10) NOT NULL,PATRONYMIC VARCHAR(20) NOT NULL,PHONENUMBER VARCHAR(10) NOT NULL);
CREATE TABLE DOCTOR(ID BIGINT NOT NULL PRIMARY KEY,NAME VARCHAR(10) NOT NULL,SURNAME VARCHAR(10) NOT NULL,PATRONYMIC VARCHAR(20) NOT NULL,SPECIALIZATION VARCHAR(20) NOT NULL);
CREATE TABLE PRESCRIPTION(ID BIGINT NOT NULL PRIMARY KEY,DESCRIPTION VARCHAR(50) NOT NULL,PATIENT BIGINT NOT NULL,DOCTOR BIGINT NOT NULL,PRIORITY VARCHAR(10) NOT NULL,CREATIONDATE DATE NOT NULL,EXPIRATIONPERIOD INTEGER NOT NULL,FOREIGN KEY(DOCTOR) REFERENCES PUBLIC.DOCTOR(ID),FOREIGN KEY(PATIENT) REFERENCES PUBLIC.PATIENT(ID));
insert into PATIENT values (1, 'Петр', 'Петров', 'Петрович', '454545');
insert into PATIENT values (2, 'Андрей', 'Андреев', 'Андреевич', '444444');
insert into PATIENT values (3, 'Сергей', 'Сергеев', 'Сергеевич', '123456');
insert into Doctor values (4, 'Семен', 'Семенов', 'Семенович', 'Хирург');
insert into Doctor values (5, 'Иван', 'Иванов', 'Иванович', 'Терапевт');
insert into Doctor values (6, 'Анна', 'Анина', 'Олеговна', 'ЛОР');
insert into Prescription values (7, 'Болеутоляющее', 1, 4, 'NORMAL', to_date('20.03.20', 'DD.MM.YY'), 12);
insert into Prescription values (8, 'Болеутоляющее', 2, 4, 'CITO', sysdate, 4);
insert into Prescription values (9, 'Болеутоляющее', 3, 4, 'STATIM', sysdate, 1);
insert into Prescription values (10, 'Болеутоляющее', 1, 5, 'NORMAL', to_date('21.03.20', 'DD.MM.YY'), 12);
insert into Prescription values (11, 'Болеутоляющее', 2, 5, 'CITO', sysdate, 3);
insert into Prescription values (12, 'Болеутоляющее', 3, 5, 'STATIM', sysdate, 2);
insert into Prescription values (13, 'Болеутоляющее', 1, 6, 'NORMAL', sysdate, 12);
insert into Prescription values (14, 'Болеутоляющее', 2, 6, 'CITO', sysdate, 3);
insert into Prescription values (15, 'Болеутоляющее', 3, 6, 'STATIM', sysdate, 2)