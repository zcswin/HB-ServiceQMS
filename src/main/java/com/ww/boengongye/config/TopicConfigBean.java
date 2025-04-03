package com.ww.boengongye.config;

import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.jms.annotation.EnableJms;
import org.springframework.stereotype.Component;

import javax.jms.Topic;

@Component
@EnableJms
public class TopicConfigBean {

    @Value("${spring.activemq.queue-name}")
    private String mytopic;

    @Bean //<bean id="" class="">
    public Topic topic() {
        return new ActiveMQTopic(mytopic);
    }

}
