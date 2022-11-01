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

-- 테이블 생성 --
create table tblMessage(
	no int primary key auto-increament,
	fid varchar(20) not null, -- From id --
	tid varchar(20) not null, -- To id --
	msg varchar(50) not null,
	mdate DATETIME DEFAULT NOW()
);