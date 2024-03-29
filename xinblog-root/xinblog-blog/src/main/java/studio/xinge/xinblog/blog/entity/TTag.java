package studio.xinge.xinblog.blog.entity;

import com.baomidou.mybatisplus.annotation.IdType;
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
 * @since 2022-07-13
 */
@Data
@EqualsAndHashCode(callSuper = false)
public class TTag implements Serializable {

    private static final long serialVersionUID = 4580210103292061859L;

    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    private String label;

    private Boolean disabled;


}
