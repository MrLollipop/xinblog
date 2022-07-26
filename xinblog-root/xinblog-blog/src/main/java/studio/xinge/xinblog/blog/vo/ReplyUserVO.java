package studio.xinge.xinblog.blog.vo;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;

import java.io.Serializable;

/**
 * 前端接收VO类型
 *
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/26
 * @Description
 */
@Data
public class ReplyUserVO implements Serializable {
    private static final long serialVersionUID = 6571894341169319027L;

    //    用户id
    @JsonSerialize(using = ToStringSerializer.class)
    private Long id;
    //    用户昵称
    private String nickName;
    //    用户头像
    private String avatar;
}
