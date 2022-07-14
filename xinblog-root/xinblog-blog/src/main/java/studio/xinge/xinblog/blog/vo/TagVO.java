package studio.xinge.xinblog.blog.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.io.Serializable;

/**
 * <p>
 * 
 * </p>
 *
 * @author xinge
 * @since 2022-07-13
 */
@Data
public class TagVO implements Serializable {

    private static final long serialVersionUID = 3024497504561174534L;

    private Long key;

    private String label;

    private Boolean disabled;


}
