package studio.xinge.xinblog.blog.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.validation.annotation.Validated;
import studio.xinge.xinblog.common.valid.groups.Add;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Objects;

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
public class TBlogReplyVO implements Serializable {

    private static final long serialVersionUID = -8712043225938398880L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;

    /**
     * 回复博客id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "博客ID不可为空", groups = Add.class)
    private Long blogId;

    /**
     * 一级回复id
     */
    @JsonSerialize(using = ToStringSerializer.class)
    private Long replyId;

    /**
     * 回复者
     */
    private Long replyerId;

    /**
     * 回复者昵称
     */
    @NotBlank(message = "昵称不可为空", groups = Add.class)
    private String replyerNickName;

    /**
     * 回复者邮箱
     */
    @Email(message = "请按邮箱格式输入", groups = Add.class)
    @NotBlank(message = "邮箱不可为空", groups = Add.class)
    private String replyerMail;

    @NotBlank(message = "回复内容不可为空", groups = Add.class)
    private String content;

    private LocalDateTime createTime;

    private Integer agreeNum;

    public TBlogReplyVO() {
    }

    public TBlogReplyVO(Long id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        TBlogReplyVO that = (TBlogReplyVO) o;
        return id.equals(that.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
