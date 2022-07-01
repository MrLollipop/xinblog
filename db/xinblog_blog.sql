/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022/2/27 15:28:47                           */
/*==============================================================*/


drop table if exists t_blog;

drop table if exists t_blog_reply;

/*==============================================================*/
/* Table: t_blog                                                */
/*==============================================================*/
-- xinblog_blog.t_blog definition

CREATE TABLE `t_blog` (
  `id` bigint(20) NOT NULL,
  `title` varchar(30) DEFAULT NULL,
  `cover` varchar(500) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `status` tinyint(4) DEFAULT NULL COMMENT '0ɾ����1������2�ݸ�',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `like_num` int(11) DEFAULT '0',
  `forward_num` int(11) DEFAULT '0',
  `collect_num` int(11) DEFAULT '0',
  `top` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='���ͱ�';

alter table t_blog comment '���ͱ�';

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
   status               tinyint default 1 comment '0ɾ����1����',
   is_top               bool default false,
   primary key (id)
);

alter table t_blog_reply comment '���ͻظ���';

