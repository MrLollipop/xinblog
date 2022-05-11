package studio.xinge.xinblog.auth.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import studio.xinge.xinblog.common.utils.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/3/4
 * @Description
 */

@RestController
@Slf4j
@RequestMapping("rabbit")
public class MsgController {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostMapping("sendMsg")
    public R sendMsg() throws ParseException {

        for (int i = 0; i < 5; i++) {
            rabbitTemplate.convertAndSend("xinge.auth", "xinge.auth", new Date());
        }
        return R.ok("消息已发送");
    }
}
