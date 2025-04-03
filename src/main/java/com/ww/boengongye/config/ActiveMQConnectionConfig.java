package com.ww.boengongye.config;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.activemq.ActiveMQPrefetchPolicy;
import org.apache.activemq.jms.pool.PooledConnectionFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.core.JmsTemplate;

import javax.jms.ConnectionFactory;

//@Configuration
public class ActiveMQConnectionConfig {

//    @Bean
//    public JmsTemplate jmsTemplate(ConnectionFactory connectionFactory){
//        ActiveMQConnectionFactory connetionFactory = null;
//        if(connectionFactory instanceof  ActiveMQConnectionFactory){
//            connetionFactory = (ActiveMQConnectionFactory) connectionFactory;
//        }else if(connectionFactory instanceof PooledConnectionFactory){
//            connetionFactory = (ActiveMQConnectionFactory) ((PooledConnectionFactory) connectionFactory).getConnectionFactory();
//        }
//
//        ActiveMQPrefetchPolicy activeMQPrefetchPolicy = new ActiveMQPrefetchPolicy();
//        activeMQPrefetchPolicy.setQueuePrefetch(1);
//        connetionFactory.setPrefetchPolicy(activeMQPrefetchPolicy);
//        JmsTemplate jmsTemplate = new JmsTemplate();
//        jmsTemplate.setConnectionFactory(connectionFactory);
//        // jmsTemplate.setSessionTransacted(true);
//        return jmsTemplate;
//    }



}
