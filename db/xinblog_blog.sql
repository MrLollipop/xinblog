/*==============================================================*/
/* DBMS name:      MySQL 5.0                                    */
/* Created on:     2022/2/27 15:28:47                           */
/*==============================================================*/


drop table if exists t_blog;

drop table if exists t_blog_reply;

drop table if exists t_tag;

-- xinblog_blog.t_blog definition

CREATE TABLE `t_blog` (
  `id` bigint(20) NOT NULL,
  `title` varchar(30) DEFAULT NULL,
  `sub_title` varchar(100) DEFAULT NULL,
  `cover` varchar(500) DEFAULT NULL COMMENT '���ͷ����ַ',
  `status` tinyint(4) DEFAULT NULL COMMENT '0ɾ����1������2�ݸ�',
  `create_time` datetime DEFAULT NULL,
  `update_time` datetime DEFAULT NULL,
  `like_num` int(11) DEFAULT '0',
  `forward_num` int(11) DEFAULT '0',
  `collect_num` int(11) DEFAULT '0',
  `top` tinyint(1) DEFAULT '0',
  `view_num` int(11) DEFAULT '0',
  `markdown_addr` varchar(500) DEFAULT NULL COMMENT 'markdown��ַ',
  `tags` varchar(100) DEFAULT NULL COMMENT '��ǩ����',
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_blog_UN` (`id`,`status`),
  KEY `t_blog_status_IDX` (`status`) USING BTREE,
  KEY `t_blog_status_IDX2` (`status`,`top`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='���ͱ�';


-- xinblog_blog.t_blog_reply definition

CREATE TABLE `t_blog_reply` (
  `id` bigint(20) NOT NULL,
  `blog_id` bigint(20) DEFAULT NULL COMMENT '�ظ�����id',
  `reply_id` bigint(20) DEFAULT NULL COMMENT 'һ���ظ�id',
  `replyer_id` bigint(20) DEFAULT NULL COMMENT '�ظ���',
  `replyer_nick_name` varchar(100) DEFAULT NULL COMMENT '�ظ����ǳ�',
  `replyer_mail` varchar(100) DEFAULT NULL COMMENT '�ظ�������',
  `content` varchar(1000) DEFAULT NULL,
  `create_time` datetime DEFAULT NULL,
  `agree_num` int(11) DEFAULT '0',
  `status` tinyint(4) DEFAULT '1' COMMENT '0ɾ����1����',
  `top` tinyint(1) DEFAULT '0',
  PRIMARY KEY (`id`),
  KEY `t_blog_reply_blog_id_IDX` (`blog_id`,`status`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='���ͻظ���';


-- xinblog_blog.t_tag definition

CREATE TABLE `t_tag` (
  `id` bigint(20) NOT NULL AUTO_INCREMENT,
  `label` varchar(100) NOT NULL,
  `disabled` tinyint(1) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `t_tag_UN` (`label`)
) ENGINE=InnoDB  DEFAULT CHARSET=utf8mb4;