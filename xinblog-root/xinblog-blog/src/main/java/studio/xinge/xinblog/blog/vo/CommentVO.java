package studio.xinge.xinblog.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * 前端接收VO类型
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/26
 * @Description
 */
@Data
public class CommentVO implements Serializable {
    private static final long serialVersionUID = -650776566418108333L;

    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //  评论用户
    private ReplyUserVO commentUser;
    //  被评论用户
    private ReplyUserVO targetUser;
    private String content;
    private String createDate;
    //  子评论列表
    private List childrenList;
}
