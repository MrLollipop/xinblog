/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2021/7/19 15:22:23                           */
/*==============================================================*/


drop table if exists bms_blog;

/*==============================================================*/
/* Table: bms_blog                                              */
/*==============================================================*/
create table bms_blog
(
   id                   bigint not null auto_increment,
   title                varchar(30),
   content              varchar(1000),
   status               tinyint,
   create_time          datetime,
   update_time          datetime,
   primary key (id)
);

alter table bms_blog comment '²©¿Í±í';

