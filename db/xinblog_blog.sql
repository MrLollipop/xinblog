/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022/2/27 15:28:47                           */
/*==============================================================*/


drop table if exists t_blog;

drop table if exists t_blog_reply;

-- xinblog_blog.t_blog definition

CREATE TABLE `t_blog` (
  `id` bigint(20) NOT NULL,
  `title` varchar(30) DEFAULT NULL,
  `sub_title` varchar(100) DEFAULT NULL,
  `cover` varchar(500) DEFAULT NULL COMMENT '博客封面地址',
  `status` tinyint(4) DEFAULT NULL COMMENT '0删除，1正常，2草稿',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `like_num` int(11) DEFAULT '0',
  `forward_num` int(11) DEFAULT '0',
  `collect_num` int(11) DEFAULT '0',
  `top` tinyint(1) DEFAULT '0',
  `view_num` int(11) DEFAULT '0',
  `markdown_addr` varchar(500) DEFAULT NULL COMMENT 'markdown地址',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_blog_UN` (`id`,`status`),
  KEY `t_blog_status_IDX` (`status`) USING BTREE,
  KEY `t_blog_status_IDX2` (`status`,`top`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客表';

/*==============================================================*/
/* Table: t_blog_reply                                          */
/*==============================================================*/
-- xinblog_blog.t_blog_reply definition

CREATE TABLE `t_blog_reply` (
  `id` bigint(20) NOT NULL,
  `blog_id` bigint(20) DEFAULT NULL,
  `to_reply_id` bigint(20) DEFAULT NULL,
  `replyer_id` bigint(20) DEFAULT NULL,
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `agree_num` int(11) DEFAULT '0',
  `status` tinyint(4) DEFAULT '1' COMMENT '0删除，1正常',
  `is_top` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='博客回复表';


