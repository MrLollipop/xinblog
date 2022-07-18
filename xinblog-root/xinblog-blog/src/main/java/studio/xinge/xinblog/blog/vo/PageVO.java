package studio.xinge.xinblog.blog.vo;

import lombok.Data;

import java.io.Serializable;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/7/18
 * @Description
 */
@Data
public class PageVO implements Serializable {

    private static final long serialVersionUID = -8876516639091460909L;

    private int from;
    private int pageSize;
}
