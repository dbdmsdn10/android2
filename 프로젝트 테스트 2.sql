use webdb;


create table productinfo(code char(5) primary key not null, pname varchar(50) not null, price int, image varchar(50));

insert into productinfo(code, pname, price, image) values('P0001', '5단서랍장', 5, 'img01.jpg');
insert into productinfo(code, pname, price, image) values('P0002', '싱글 나무침대', 120, 'img02.jpg');
insert into productinfo(code, pname, price, image) values('P0003', '더블 나무침대', 150, 'img03.jpg');
insert into productinfo(code, pname, price, image) values('P0004', '4인용 쇼파', 300, 'img04.jpg')

drop table productinfo

select*from productinfo order by price;


select code from productinfo where pname like '%침대%' order by pname desc;

create table foodinfo(seq int not null auto_increment, name varchar(20) not null,tel varchar(20) not null,address varchar(50) not null,latitude double not null,longitude double not null,primary key(seq));

insert into foodinfo(name, tel, address, latitude, longitude) 
values('쿠바', '032-565-6969', '인천광역시 서구 경서동 950-14', 37.54089, 126.6799233);
insert into foodinfo(name, tel, address, latitude, longitude) 
values('테르미니', '02-972-0036', '서울특별시 노원구 공릉동 27-20', 37.6263383, 127.0919478);
insert into foodinfo(name, tel, address, latitude, longitude) 
values('애플민트', '02-975-3710', '서울특별시 노원구 공릉동 113', 37.6263176, 127.0240964);

select*from foodinfo;

delete from foodinfo where seq=5;










create database test

use test
create table food(_id int(50000) primary key not null, name text not null, kcal text not null, once text );

drop table food;


create table food(_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE, name text not null, kcal double not null, once text ,comercial text,home text,who text not null);

desc food;
insert into food(name,kcal,once) values('1','2','3');

use test

select*from food where name like '%감자%' order by kcal desc;
select*from food where who='dbdmsdn10@naver.com'
select*from food where not who='original'

create database AndroidProject;
use AndroidProject;



select*from userid;
delete from userid where _id='dmsdn@naver.com';


create table userid(_id varchar(80) primary key not null,password text not null);
create table userinfo(_id varchar(80) primary key not null, weight double,height double,age int, gender int, how int);

select*from userinfo;

drop table userid;
drop table userinfo;

update userinfo set weight=50,height=200,age=30,gender=2,how=3 where _id='dbdmsdn10@naver.com';

delete from userinfo where _id='dmsdn@naver.com';



create table eatlist(_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE, eatdate text , who text,foodid text,wheneat text);

select*from eatlist order by eatdate desc;



delete from eatlist where _id=30;



select*from eatlist where eatdate='2020/08/12' and who='dbdmsdn10@naver.com' and wheneat='1';


update eatlist set eatdate=2020/08/10;

select*from userid a inner JOIN eatlist b on a._id=b.who;

select*from eatlist a inner join food b on a.foodid=b._id;

create table mettable (_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,name text not null,met double, who text);

drop table mettable 

select *from mettable


create table exercise(_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE,metid text not null,time long, who text,dodate text);

select*from exercise

drop table exercise

select*from exercise a inner join mettable b on a.metid=b._id;


select*from exercise a inner join mettable b on a.metid=b._id where a.who='dbdmsdn10@naver.com' and a.dodate='2020/08/12'


select distinct dodate,eatdate from  exercise a inner join eatlist b on a.who=b.who where a.who='dbdmsdn10@naver.com'

select distinct dodate from exercise where who='dbdmsdn10@naver.com'

select * from exercise

ALTER TABLE eatlist modify eatdate text;
desc eatlist;

create table eatlist2(_id BIGINT UNSIGNED NOT NULL AUTO_INCREMENT UNIQUE, eatdate date , who text,foodid text,wheneat text);
set sql_safe_updates=0;
UPDATE eatlist SET eatdate = null


use androidproject;
update eatlist set eatdate="2020/08/13" where _id=22;
select*from eatlist;


select distinct eatdate from eatlist where who="dbdmsdn10@naver.com" and not eatdate in (select distinct dodate from exercise order by dodate desc) order by eatdate desc;

(select distinct eatdate from eatlist order by eatdate desc);

(select distinct dodate from exercise order by dodate desc);

(select distinct dodate from exercise where who="dbdmsdn10@naver.com" and dodate in (select distinct eatdate from eatlist order by eatdate desc) order by dodate desc);


set global max_connections=1000;


select*from eatlist a inner join food b on a.foodid=b._id where a.who='dbdmsdn10@naver.com' and a.eatdate='2020/08/13'

show global variables like '%_timeout%'

set global interactive_timeout=180;
set global wait_timeout=180;


show variables like '%_timeout%'

set interactive_timeout=180;
set wait_timeout=180;




