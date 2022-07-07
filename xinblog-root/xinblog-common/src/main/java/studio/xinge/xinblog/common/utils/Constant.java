/**
 * Copyright (c) 2016-2019 人人开源 All rights reserved.
 * <p>
 * https://www.renren.io
 * <p>
 * 版权所有，侵权必究！
 */

package studio.xinge.xinblog.common.utils;

/**
 * 常量
 *
 * @author Mark sunlightcs@gmail.com
 */
public class Constant {
    /**
     * 超级管理员ID
     */
    public static final int SUPER_ADMIN = 1;
    /**
     * 当前页码
     */
    public static final String PAGE = "page";
    /**
     * 每页显示记录数
     */
    public static final String LIMIT = "limit";
    /**
     * 排序字段
     */
    public static final String ORDER_FIELD = "sidx";
    /**
     * 排序方式
     */
    public static final String ORDER = "order";
    /**
     * 升序
     */
    public static final String ASC = "asc";

    /**
     * @Author xinge
     * @Description 删除状态
     * @Date 2021/9/21
     */
    public static final Integer DELETED_STATUS = 0;

    /**
     * 菜单类型
     *
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年11月15日 下午1:24:29
     */
    public enum MenuType {
        /**
         * 目录
         */
        CATALOG(0),
        /**
         * 菜单
         */
        MENU(1),
        /**
         * 按钮
         */
        BUTTON(2);

        private int value;

        MenuType(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 定时任务状态
     *
     * @author chenshun
     * @email sunlightcs@gmail.com
     * @date 2016年12月3日 上午12:07:22
     */
    public enum ScheduleStatus {
        /**
         * 正常
         */
        NORMAL(0),
        /**
         * 暂停
         */
        PAUSE(1);

        private int value;

        ScheduleStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    public enum BlogStatus {
        /**
         * 正常
         */
        NORMAL(1),
        /**
         * 草稿
         */
        DRAFT(2),
        /**
         * 删除
         */
        PAUSE(0);

        private int value;

        BlogStatus(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 云服务商
     */
    public enum CloudService {
        /**
         * 七牛云
         */
        QINIU(1),
        /**
         * 阿里云
         */
        ALIYUN(2),
        /**
         * 腾讯云
         */
        QCLOUD(3);

        private int value;

        CloudService(int value) {
            this.value = value;
        }

        public int getValue() {
            return value;
        }
    }

    /**
     * 鉴权auth服务
     *
     * @Author xinge
     * @Description
     * @Date 2022/5/3
     */
//    redis中用户token的前缀
    public static final String TOKEN_KEY = "user_token_";
    //    用户角色前缀
    public static final String ROlE_PREFIX = "ROLE_";
    //   Blog前缀
    public static final String BLOG_KEY = "blog_key_";
    //  BLog redis 写锁
    public static final String BLOG_LOCK = "blog_lock_";
    //  查询不存在的Blog
    public static final String BLOG_NOT_EXIST = "Blog_Not_Exist";
    //    首页缓存
    public static final String BLOG_INDEX_CACHE = "blog_index_cache_";

}
