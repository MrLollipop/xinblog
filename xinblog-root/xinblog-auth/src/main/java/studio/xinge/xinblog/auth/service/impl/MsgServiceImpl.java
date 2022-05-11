package studio.xinge.xinblog.auth.service.impl;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.RabbitHandler;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;
import studio.xinge.xinblog.auth.service.MsgService;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/3/5
 * @Description
 */
@Service
@RabbitListener(queues = {"xinge.queue"})
@Slf4j
public class MsgServiceImpl implements MsgService {


/*    @RabbitHandler
    public void receiveMsg(Date content) {
        log.info("接收消息\t{}", new SimpleDateFormat("HH:mm:ss").format(content));
    }*/

    @RabbitHandler
    public void receiveMsgManual(Date content, Message message, Channel channel) throws IOException {
//      channel内按顺序自增
        long deliveryTag = message.getMessageProperties().getDeliveryTag();
//        手动ack
        if (false) {
            log.info("接收消息\t{}", new SimpleDateFormat("HH:mm:ss").format(content));
//            回复已收到，消息手动移除队列
            channel.basicAck(deliveryTag, false);
        } else {

//            channel.basicNack(deliveryTag, false, true);
//            拒收
            channel.basicReject(deliveryTag, true);
            log.info("===消息拒收");
        }

    }
}
