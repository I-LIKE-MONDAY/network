CREATE DATABASE mychat;

USE mychat;

CREATE TABLE tblRegister(
	id VARCHAR(20) PRIMARY KEY,
	pwd VARCHAR(20) NOT NULL,
	NAME VARCHAR(20) NOT NULL,
	regdate DATETIME DEFAULT NOW()
)

INSERT tblregister VALUES ('aaa', '1234', '강도창', NOW());
INSERT tblregister VALUES ('bbb', '1234', '오지혁', NOW());
INSERT tblregister VALUES ('ccc', '1234', '홍길동', NOW());

SELECT * FROM tblregister