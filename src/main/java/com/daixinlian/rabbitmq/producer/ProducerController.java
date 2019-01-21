package com.daixinlian.rabbitmq.producer;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.support.CorrelationData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.daixinlian.rabbitmq.config.RabbitMQConfig;
import com.daixinlian.rabbitmq.entity.RabbitMetaMessage;
import com.daixinlian.rabbitmq.util.DefaultKeyGenerator;
import com.daixinlian.test.SQLTest;

import cn.hutool.core.lang.Console;

@RestController
public class ProducerController {

    @Autowired
    private RabbitTemplate rabbitTemplate;
    @Autowired
    private SQLTest sQLTest;
    
    @Autowired
    private DefaultKeyGenerator keyGenerator;

    @GetMapping("/test")
    @ResponseBody
    public String  test() {
    	sQLTest.test_batch_insert_filemap();
    	return "0000";
    }
    
    
    
    
    @GetMapping("/sendMessage")
    public Object sendMessage(String name) {
        new Thread(() -> {
//            HashOperations hashOperations = redisTemplate.opsForHash();
//            for (int i = 0; i < 1; i++) {
                String id = keyGenerator.generateKey() + "";
                String value = "message " + "test";
                RabbitMetaMessage rabbitMetaMessage = new RabbitMetaMessage(name);

                // 先把消息存储到 redis
//                hashOperations.put(RedisConfig.RETRY_KEY, id, rabbitMetaMessage);

                Console.log("send message = {},param = {}", value,name);

                // 再发送到 rabbitmq
                rabbitTemplate.convertAndSend(RabbitMQConfig.EXCHANGE_NAME, RabbitMQConfig.ROUTING_KEY, name, (message) -> {
                    message.getMessageProperties().setMessageId(id);
                    return message;
                }, new CorrelationData(id));
//            }
        }).start();
        return "ok";
    }

}
