/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022/2/27 15:28:47                           */
/*==============================================================*/


drop table if exists t_blog;

drop table if exists t_blog_reply;

/*==============================================================*/
/* Table: t_blog                                                */
/*==============================================================*/
create table t_blog
(
   id                   bigint not null,
   title                varchar(30),
   cover                varchar(500),
   content              varchar(1000),
   status               tinyint comment '0删除，1正常，2草稿',
   create_time          datetime,
   update_time          datetime,
   like_num             int default 0,
   forward_num          int default 0,
   collect_num          int default 0,
   is_top               bool default false,
   primary key (id)
);

alter table t_blog comment '博客表';

/*==============================================================*/
/* Table: t_blog_reply                                          */
/*==============================================================*/
create table t_blog_reply
(
   id                   bigint not null,
   blog_id              bigint,
   to_reply_id          bigint,
   replyer_id           bigint,
   content              varchar(1000),
   create_time          datetime,
   agree_num            int default 0,
   status               tinyint default 1 comment '0删除，1正常',
   is_top               bool default false,
   primary key (id)
);

alter table t_blog_reply comment '博客回复表';

