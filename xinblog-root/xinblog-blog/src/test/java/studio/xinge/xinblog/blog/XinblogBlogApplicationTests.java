package studio.xinge.xinblog.blog;

import cn.hutool.core.util.RandomUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;
import studio.xinge.xinblog.common.utils.Constant;
import studio.xinge.xinblog.common.utils.PageUtils;

import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;

@SpringBootTest()
@ActiveProfiles("uat")
class XinblogBlogApplicationTests {

    @Autowired
    BlogService blogService;

    @Test
    void contextLoads() {
    }

    @Test
    void save(){
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle("第n篇博客");
        blogEntity.setContent("内容内容XXXXdsddfasdf");
        blogEntity.setStatus(new int[]{0,1,2}[RandomUtil.randomInt(0,3)]);
        Date date = new Date();
        blogEntity.setCreateTime(date);
        blogEntity.setUpdateTime(date);
        blogEntity.setLikeNum(RandomUtil.randomInt(1,200));
        blogEntity.setForwardNum(RandomUtil.randomInt(1,200));
        blogEntity.setCollectNum(RandomUtil.randomInt(1,200));
        blogEntity.setIsTop(false);

        blogService.save(blogEntity);
    }



    @Test
    void batchInsert(){

        LinkedList<BlogEntity> list = new LinkedList<>();

        for (int i = 0; i < 1000; i++) {
            BlogEntity blogEntity = new BlogEntity();
            blogEntity.setTitle(String.format("第%d篇博客", i));
            blogEntity.setContent(String.format("这里记录%d篇博客", i));
            blogEntity.setStatus(new int[]{0,1,2}[RandomUtil.randomInt(0,3)]);
            Date date = new Date();
            blogEntity.setCreateTime(date);
            blogEntity.setUpdateTime(date);
            blogEntity.setLikeNum(RandomUtil.randomInt(1,200));
            blogEntity.setForwardNum(RandomUtil.randomInt(1,200));
            blogEntity.setCollectNum(RandomUtil.randomInt(1,200));
            blogEntity.setIsTop(false);
            list.add(blogEntity);
        }

        blogService.saveBatch(list, 1000);
    }

    @Test
    void query() {

        HashMap<String, Object> conditions = new HashMap<>();
        conditions.put(Constant.PAGE, "20");
        conditions.put(Constant.LIMIT, "2");

        conditions.put("status", "0");

        PageUtils pageUtils = blogService.queryPage(conditions);
        System.out.println(pageUtils.getList());
    }
}
