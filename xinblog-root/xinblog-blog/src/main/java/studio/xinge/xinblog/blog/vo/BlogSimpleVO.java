package studio.xinge.xinblog.blog.vo;

import lombok.Data;
import studio.xinge.xinblog.common.valid.groups.Add;

import javax.validation.constraints.NotBlank;
import java.io.Serializable;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/22
 * @Description
 */
@Data
public class BlogSimpleVO implements Serializable {
    private static final long serialVersionUID = -7840142468067717252L;

    private Long id;

    private String title;

    public BlogSimpleVO() {
    }

    public BlogSimpleVO(BlogEntityVO vo) {
        this.id = vo.getId();
        this.title = vo.getTitle();
    }
}
