package com.daixinlian.rabbitmq.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * 自定义消息元数据
 */
@NoArgsConstructor
@Data
public class RabbitMetaMessage implements Serializable{

    /**
     * 是否是 returnCallback
     */
    private boolean returnCallback;

    /**
     * 承载原始消息数据数据
     */
    private Object payload;

    public RabbitMetaMessage(Object payload) {
        this.payload = payload;
    }
}
