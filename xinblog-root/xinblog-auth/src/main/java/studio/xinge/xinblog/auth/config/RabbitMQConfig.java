package studio.xinge.xinblog.auth.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.connection.CorrelationData;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.lang.Nullable;

import javax.annotation.PostConstruct;

/**
 * @Author xinge
 * @E-mail haoxin_2014@163.com
 * @Date 2022/3/4
 * @Description
 */
@Configuration
@Slf4j
public class RabbitMQConfig {

    @Autowired
    RabbitTemplate rabbitTemplate;

    @PostConstruct
    public void initRabbitTemplate() {

        /**
         * Confirmation callback.
         * @param correlationData correlation data for the callback.
         * @param ack true for ack, false for nack
         * @param cause An optional cause, for nack, when available, otherwise null.
         */
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
            log.info("消息到达服务器\tData:{}\tack:{}\tcause:{}", correlationData, ack, cause);
        });
        /**
         * Returned message callback.
         * @param message the returned message.
         * @param replyCode the reply code.
         * @param replyText the reply text.
         * @param exchange the exchange.
         * @param routingKey the routing key.
         */
        rabbitTemplate.setReturnCallback((message, replyCode, replyText, exchange, routingKey) -> {
            log.info("消息入队列失败:message\t{}\nreplyCode\t{}\nreplyText\t{}\nexchange\t{}\nroutingKey\t{}\n", message, replyCode, replyText, exchange, routingKey);
        });
    }
}
