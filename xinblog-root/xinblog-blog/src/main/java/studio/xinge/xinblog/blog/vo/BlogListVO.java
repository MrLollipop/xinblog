package studio.xinge.xinblog.blog.vo;

import lombok.Data;

import java.io.Serializable;
import java.util.List;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/9
 * @Description
 */
@Data
public class BlogListVO implements Serializable {
    private static final long serialVersionUID = -7490334714363277722L;

    private List list;
    private int from;

    public BlogListVO(List list, int from) {
        this.list = list;
        this.from = from;
    }

    public BlogListVO() {
    }

}
