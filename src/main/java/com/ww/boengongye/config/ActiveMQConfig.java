package com.ww.boengongye.config;

import org.apache.activemq.ActiveMQConnectionFactory;

import org.apache.activemq.command.ActiveMQQueue;
import org.apache.activemq.command.ActiveMQTopic;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jms.config.DefaultJmsListenerContainerFactory;
import org.springframework.jms.config.JmsListenerContainerFactory;
import org.springframework.jms.config.SimpleJmsListenerContainerFactory;
import org.springframework.jms.core.JmsMessagingTemplate;

import javax.jms.ConnectionFactory;
import javax.jms.Destination;
import javax.jms.Queue;

/**
 * 配置类
 */
@Configuration
public class ActiveMQConfig {



    @Value("${spring.activemq.broker-url}")
    private String brokerUrl;

    @Value("${spring.activemq.user}")
    private String userName;

    @Value("${spring.activemq.password}")
    private String password;

    @Value("${spring.activemq.queue-name}")
    private String queueName;
    @Value("${QmsPutAppearanceData}")
    private String appearanceTopicName;

    @Value("${SCADALockMac}")
    private String scadaLockTopic;

    @Bean(name="topic2")
    public Destination getTopic2(){
        return new ActiveMQTopic(appearanceTopicName);
    }

    @Bean(name="topic3")
    public Destination getTopic(){
        return new ActiveMQTopic(scadaLockTopic);
    }

    @Bean(name = "queue")
    public Queue queue() {
        return new ActiveMQQueue(queueName);
    }

//    @Bean(name = "defaultConnectionFactory")
//    public ConnectionFactory connectionFactory(){
//        return new ActiveMQConnectionFactory(userName, password, brokerUrl);
//    }

    @Bean(name = "defaultConnectionFactory")
    public ActiveMQConnectionFactory connectionFactory(
            @Value("${spring.activemq.broker-url}") String brokerUrl,
            @Value("${spring.activemq.user}") String username,
            @Value("${spring.activemq.password}") String password) {
        return createConnectionFactory(brokerUrl, username, password);
    }

    // 在Queue模式中，对消息的监听需要对containerFactory进行配置
    @Bean("queueListener")
    public JmsListenerContainerFactory<?> queueJmsListenerContainerFactory(ConnectionFactory defaultConnectionFactory){
        SimpleJmsListenerContainerFactory factory = new SimpleJmsListenerContainerFactory();
        factory.setConnectionFactory(defaultConnectionFactory);
        factory.setPubSubDomain(false);
        return factory;
    }

    @Bean
    public JmsListenerContainerFactory<?> jmsListenerContainerFactory(ConnectionFactory defaultConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(defaultConnectionFactory);
        factory.setPubSubDomain(true); // 默认是true，如果要使用topic，需要设置为true
        return factory;
    }

    @Bean(name = "jtConnectionFactory")
    public ActiveMQConnectionFactory secondConnectionFactory(
            @Value("${spring.jtmq.broker-url}") String brokerUrl,
            @Value("${spring.jtmq.user}") String username,
            @Value("${spring.jtmq.password}") String password) {
        return createConnectionFactory(brokerUrl, username, password);
    }



    @Bean(name = "defaultJmsTemplate")
    @Primary
    public JmsMessagingTemplate defaultActivemqTemplate(
            @Qualifier("defaultConnectionFactory") ActiveMQConnectionFactory connectionFactory) {
        JmsMessagingTemplate template = new JmsMessagingTemplate(connectionFactory);
        return template;
    }

    @Bean(name = "jtJmsTemplate")
    public JmsMessagingTemplate sasaActivemqTemplate(
            @Qualifier("jtConnectionFactory") ActiveMQConnectionFactory secondConnectionFactory) {
        JmsMessagingTemplate template = new JmsMessagingTemplate(secondConnectionFactory);
        return template;
    }

    @Bean(name = "jtJmsTemplateTopic")
    public JmsMessagingTemplate jtJmsTemplateTopic(
            @Qualifier("jtConnectionFactory") ActiveMQConnectionFactory secondConnectionFactory) {
        JmsMessagingTemplate template = new JmsMessagingTemplate(secondConnectionFactory);

        return template;
    }

    @Bean(name = "defaultJmsListenerContainerFactory")
    public JmsListenerContainerFactory defaultFactory(
            @Qualifier("defaultConnectionFactory") ActiveMQConnectionFactory connectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(connectionFactory);
        return factory;
    }

    @Bean(name = "jtJmsListenerContainerFactory")
    public JmsListenerContainerFactory jtFactory(
            @Qualifier("jtConnectionFactory") ActiveMQConnectionFactory secondConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(secondConnectionFactory);
        return factory;
    }

    @Bean(name = "jtJmsListenerContainerFactoryTopic")
    public JmsListenerContainerFactory jtFactoryTopic(
            @Qualifier("jtConnectionFactory") ActiveMQConnectionFactory secondConnectionFactory) {
        DefaultJmsListenerContainerFactory factory = new DefaultJmsListenerContainerFactory();
        factory.setConnectionFactory(secondConnectionFactory);
        factory.setPubSubDomain(true); // 默认是true，如果要使用topic，需要设置为true
        return factory;
    }

    public ActiveMQConnectionFactory createConnectionFactory(String brokerUrl, String username, String password) {
        ActiveMQConnectionFactory factory = new ActiveMQConnectionFactory();
        factory.setBrokerURL(brokerUrl);
        factory.setUserName(username);
        factory.setPassword(password);
        return factory;
    }



}
