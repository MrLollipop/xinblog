package studio.xinge.xinblog.auth;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import studio.xinge.xinblog.auth.entity.TBlogUser;
import studio.xinge.xinblog.auth.service.TBlogUserService;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.LinkedList;
import java.util.concurrent.TimeUnit;

@EnableRabbit
@RabbitListener(queues = {"xinge.queue"})
@SpringBootTest
@Slf4j
class AuthApplicationTests {

    @Autowired
    AmqpAdmin amqpAdmin;

    @Autowired
    RabbitTemplate rabbitTemplate;

    @Autowired
    TBlogUserService blogUserService;

    @Test
    void createExchange() {
        DirectExchange exchange = new DirectExchange("xinge.auth", true, false);
        amqpAdmin.declareExchange(exchange);
    }

    @Test
    void createQueue() {
//        String name, boolean durable, boolean exclusive, boolean autoDelete)
        Queue queue = new Queue("xinge.queue", true);
        amqpAdmin.declareQueue(queue);
    }

    @Test
    void createBinding() {
//        String destination, DestinationType destinationType, String exchange, String routingKey,
        Binding binding = new Binding("xinge.queue", Binding.DestinationType.QUEUE, "xinge.auth", "xinge.auth", null);

        amqpAdmin.declareBinding(binding);
    }

    @Test
    void sengMsgToQueue() throws InterruptedException {
//        String exchange, String routingKey, Object message)
        for (int i = 0; i < 10; i++) {
            rabbitTemplate.convertAndSend("xinge.auth", "xinge.auth", new Date());
            TimeUnit.MILLISECONDS.sleep(500);
        }
    }

    @Test
    void encode() {
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        LinkedList<String> list = new LinkedList<>();
        for (int i = 0; i < 5; i++) {
            String encode = encoder.encode("xinge");
            System.out.println(encode);
            list.add(encode);
        }
    }

    @Test
    void saveUserBatch() {
        LinkedList<TBlogUser> list = new LinkedList<>();
        BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
/*        for (int i = 0; i < 1000; i++) {
            TBlogUser user = new TBlogUser();
            user.setMobile("1560000" + makeNum2String(i));
            user.setUsername("xinge" + makeNum2String(i));
            BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
            user.setEncryptedPassword(encoder.encode(makeNum2String(i)));
            user.setStatus(1);
            LocalDateTime now = LocalDateTime.now();
            user.setCreateTime(now);
            user.setUpdateTime(now);

            list.add(user);
        }*/
        System.out.println("数据构造开始:" + LocalDateTime.now());
        for (int i = 2002; i < 3000; i++) {
            TBlogUser user = new TBlogUser();
            String str = String.valueOf(i);
            user.setMobile("1560000" + str);
            user.setUsername("xinge" + str);
            user.setEncryptedPassword(encoder.encode(str));
            user.setStatus(1);
            LocalDateTime now = LocalDateTime.now();
            user.setCreateTime(now);
            user.setUpdateTime(now);

            list.add(user);
        }
        System.out.println("数据构造完毕:" + LocalDateTime.now());
        blogUserService.saveBatch(list);
        System.out.println("数据插入完毕:" + LocalDateTime.now());

    }

    String makeNum2String(int num) {
        int length = 4 - String.valueOf(num).length();
        String res = "";
        for (int i = 0; i < length; i++) {
            res = res.concat("0");
        }
        return res.concat(String.valueOf(num));
    }

    /*
    监听消息，自动触发
    @Test
    @RabbitHandler
    void receive(Date content) {
        SimpleDateFormat format = new
        ("yyyy/MM/dd HH:mm:ss");
        log.info("测试接收{}",format.format(content));
    }*/

}
