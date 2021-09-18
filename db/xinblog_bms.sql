/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2021/9/18 14:30:34                           */
/*==============================================================*/


drop table if exists bms_blog;

drop table if exists bms_blog_reply;

/*==============================================================*/
/* Table: bms_blog                                              */
/*==============================================================*/
create table bms_blog
(
   id                   bigint not null,
   title                varchar(30),
   content              varchar(1000),
   status               tinyint comment '0ɾ����1������2�ݸ�',
   create_time          datetime,
   update_time          datetime,
   like_num             int default 0,
   forward_num          int default 0,
   collect_num          int default 0,
   is_top               bool default false,
   primary key (id)
);

alter table bms_blog comment '���ͱ�';

/*==============================================================*/
/* Table: bms_blog_reply                                        */
/*==============================================================*/
create table bms_blog_reply
(
   id                   bigint not null,
   blog_id              bigint,
   to_reply_id          bigint,
   replyer_id           bigint,
   content              varchar(1000),
   create_time          datetime,
   agree_num            int default 0,
   status               tinyint default 1 comment '0ɾ����1����',
   is_top               bool default false,
   primary key (id)
);

alter table bms_blog_reply comment '���ͻظ���';

