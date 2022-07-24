package studio.xinge.xinblog.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 博客回复表
 * </p>
 *
 * @author xinge
 * @since 2022-07-23
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TBlogReply implements Serializable {

    private static final long serialVersionUID = -5178040133254692974L;

    @JsonSerialize(using = ToStringSerializer.class)
    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 回复博客id
     */
    private Long blogId;

    /**
     * 一级回复id
     */
    private Long replyId;

    /**
     * 回复者
     */
    private Long replyerId;

    /**
     * 回复者昵称
     */
    private String replyerNickName;

    /**
     * 回复者邮箱
     */
    private String replyerMail;

    private String content;

    private LocalDateTime createTime;

    private Integer agreeNum;

    /**
     * 0删除，1正常
     */
    private Integer status;

    private Boolean top;


}
