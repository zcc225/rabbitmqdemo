package com.daixinlian.rabbitmq.config;

import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.CachingConnectionFactory;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.amqp.support.converter.SimpleMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public final static String QUEUE_NAME = "customer.ack.queue";
    public final static String EXCHANGE_NAME = "customer.ack.exchange";
    public final static String ROUTING_KEY = "customer.ack.key";


    // 创建队列
    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME);
    }

    // 创建一个 topic 类型的交换器
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange(EXCHANGE_NAME);
    }

    // 使用路由键（routingKey）把队列（Queue）绑定到交换器（Exchange）
    @Bean
    public Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with(ROUTING_KEY);
    }

    @Bean
    public ConnectionFactory connectionFactory() {
        CachingConnectionFactory connectionFactory = new CachingConnectionFactory("120.79.180.132", 5672);
        connectionFactory.setUsername("zcc225");
        connectionFactory.setPassword("youzhai123");

        // 设置 生产者 confirms
        connectionFactory.setPublisherConfirms(true);

        // 设置 生产者 Returns
        connectionFactory.setPublisherReturns(true);

        return connectionFactory;
    }

    @Bean
    public SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, ChannelAwareMessageListener listener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // 指定消费者
        container.setMessageListener(listener);
        // 指定监听的队列
        container.setQueueNames(QUEUE_NAME);

        // 设置消费者的 ack 模式为手动确认模式
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);

        container.setPrefetchCount(300);

        return container;
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);

        // 必须设置为 true，不然当 发送到交换器成功，但是没有匹配的队列，不会触发 ReturnCallback 回调
        // 而且 ReturnCallback 比 ConfirmCallback 先回调，意思就是 ReturnCallback 执行完了才会执行 ConfirmCallback
        rabbitTemplate.setMandatory(true);

        // 设置 ConfirmCallback 回调
        rabbitTemplate.setConfirmCallback((correlationData, ack, cause) -> {
//            Console.log("ConfirmCallback , correlationData = {} , ack = {} , cause = {} ", correlationData, ack, cause);
            // 如果发送到交换器都没有成功（比如说删除了交换器），ack 返回值为 false
            // 如果发送到交换器成功，但是没有匹配的队列（比如说取消了绑定），ack 返回值为还是 true （这是一个坑，需要注意）
            if (ack) {
                String messageId = correlationData.getId();
                //TODO
//                RabbitMetaMessage rabbitMetaMessage = (RabbitMetaMessage) redisTemplate.opsForHash().get(RedisConfig.RETRY_KEY, messageId);
//                Console.log("rabbitMetaMessage = {}", rabbitMetaMessage);
//                if (!rabbitMetaMessage.isReturnCallback()) {
//
//                    // 到这一步才能完全保证消息成功发送到了 rabbitmq
//                    // 删除 redis 里面的消息
//                    redisTemplate.opsForHash().delete(RedisConfig.RETRY_KEY, messageId);
//                }
            }

        });

        // 设置 ReturnCallback 回调
        // 如果发送到交换器成功，但是没有匹配的队列，就会触发这个回调
        rabbitTemplate.setReturnCallback((message, replyCode, replyText,
                                          exchange, routingKey) -> {
//            Console.log("ReturnCallback unroutable messages, message = {} , replyCode = {} , replyText = {} , exchange = {} , routingKey = {} ", message, replyCode, replyText, exchange, routingKey);
//TODO
            // 从 redis 取出消息，设置 returnCallback 设置为 true
//            String messageId = message.getMessageProperties().getMessageId();
//            RabbitMetaMessage rabbitMetaMessage = (RabbitMetaMessage) redisTemplate.opsForHash().get(RedisConfig.RETRY_KEY, messageId);
////            rabbitMetaMessage.setReturnCallback(true);
//            redisTemplate.opsForHash().put(RedisConfig.RETRY_KEY, messageId, rabbitMetaMessage);
        });
        return rabbitTemplate;
    }

    @Bean
    public MessageConverter messageConverter() {
        return new SimpleMessageConverter();
    }


}
