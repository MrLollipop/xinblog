package studio.xinge.xinblog.blog;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import studio.xinge.xinblog.blog.entity.BlogEntity;
import studio.xinge.xinblog.blog.service.BlogService;

import java.util.Date;
import java.util.LinkedList;
import java.util.List;

@SpringBootTest
class XinblogBlogApplicationTests {

    @Autowired
    BlogService blogService;

    @Test
    void contextLoads() {
    }

    @Test
    void save(){
        BlogEntity blogEntity = new BlogEntity();
        blogEntity.setTitle("第3篇博客");
        blogEntity.setContent("内容内容XXXXdsddfasdf");
        blogEntity.setStatus(1);
        Date date = new Date();
        blogEntity.setCreateTime(date);
        blogEntity.setUpdateTime(date);

        blogService.save(blogEntity);
    }



    @Test
    void batchInsert(){

        LinkedList<BlogEntity> list = new LinkedList<>();

        for (int i = 4; i < 1000; i++) {
            BlogEntity blogEntity = new BlogEntity();
            blogEntity.setTitle(String.format("第%d篇博客", i));
            blogEntity.setContent(String.format("这里记录%d篇博客", i));
            blogEntity.setStatus(1);
            Date date = new Date();
            blogEntity.setCreateTime(date);
            blogEntity.setUpdateTime(date);
            list.add(blogEntity);
        }

        blogService.saveBatch(list, 1000);
    }
}
