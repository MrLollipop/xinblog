package studio.xinge.xinblog.auth.entity;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import java.io.Serializable;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * <p>
 * 功能表
 * </p>
 *
 * @author xinge
 * @since 2022-05-04
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TFunc implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.ASSIGN_ID)
    private Long id;

    /**
     * 功能名称
     */
    private String name;

    /**
     * 授权(多个用逗号分隔，如：user:list,user:create)
     */
    private String perms;

    /**
     * 类型   0：未定义   1：未定义   2：未定义
     */
    private Integer type;


}
