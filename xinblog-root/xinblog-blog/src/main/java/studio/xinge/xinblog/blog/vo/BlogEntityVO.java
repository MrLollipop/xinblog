package studio.xinge.xinblog.blog.vo;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import lombok.Data;
import org.hibernate.validator.constraints.URL;
import studio.xinge.xinblog.common.valid.groups.Add;
import studio.xinge.xinblog.common.valid.groups.Update;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

/**
 * @author 欣哥工作室
 * @email haoxin_2014@163.com
 * @date 2021-07-19 14:35:51
 */
@Data
public class BlogEntityVO implements Serializable {

    private static final long serialVersionUID = -3157387596065398746L;
    /**
     * Long类型导致前端js精度丢失，Json序列化为字符串输出
     */
    @JsonSerialize(using = ToStringSerializer.class)
    @NotNull(message = "需指定ID", groups = {Update.class})
    @Null(message = "不能指定ID", groups = Add.class)
    private Long id;

    /**
     *
     */
    @NotBlank(message = "博客标题不可为空", groups = Add.class)
    private String title;

    /**
     *
     */
    private Integer status;
    /**
     *
     */
    private Date createTime;
    /**
     *
     */
    private Date updateTime;

    private Integer likeNum;
    private Integer forwardNum;
    private Integer collectNum;
    private Boolean top;
    private Integer viewNum;
    private String subTitle;

    /**
     * 博客封面地址
     */
    @URL(groups = {Add.class, Update.class})
    private String cover;

    /**
     * @Author xinge
     * @Description markdownAddr在oss保存地址
     * @Date 2022/7/1
     */
    private String markdownAddr;

//    标签key数组 用于管理台接收
    private int[] tags;
//    标签VO ist 页面展示用
    private List<TagVO> tagVOList;

}
