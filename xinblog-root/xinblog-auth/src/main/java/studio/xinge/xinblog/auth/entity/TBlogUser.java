package studio.xinge.xinblog.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import java.time.LocalDateTime;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 
 * </p>
 *
 * @author xinge
 * @since 2022-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TBlogUser implements Serializable {

    private static final long serialVersionUID = 1L;

    /**
     * 主键，用户id
     */
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String username;

    private String encryptedPassword;

    private String mobile;

    private LocalDateTime createTime;

    private LocalDateTime updateTime;

    /**
     * 用户状态，0删除，1正常
     */
    private Integer status;


}
