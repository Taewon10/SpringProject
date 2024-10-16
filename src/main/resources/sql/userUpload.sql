#Oracle
create table USERIMAGE{
SEQ number primary key,
imageName varchar2(50),
imageContent varchar2(4000),
imageFileName varchar2(100) not null,
imageOriginalFileName varchar2(100) not null);

create sequence SEQ_USERIMAGE nocycle nocache;

#MySQL
CREATE TABLE userUpload (
    seq INT(10) PRIMARY KEY AUTO_INCREMENT,
    imageName VARCHAR(50),
    imageContent VARCHAR(4000),
    imageFileName VARCHAR(100) NOT NULL,
    imageOriginalFileName VARCHAR(100) NOT NULL
);