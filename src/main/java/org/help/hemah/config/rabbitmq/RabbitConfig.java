package org.help.hemah.config.rabbitmq;

import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    private static String queueName = "hemah-queue";
    private static String topicName = "hemah-topic";


//    @Bean
//    Queue queue() {
//        return new Queue(queueName, false);
//    }
//
//    @Bean
//    TopicExchange exchange() {
//        return new TopicExchange(topicName);
//    }
//
//    @Bean
//    Binding binding(Queue queue, TopicExchange exchange) {
//        return BindingBuilder.bind(queue).to(exchange).with("hemmah.#");
//    }
//
//    @Bean
//    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory, MessageListenerAdapter listenerAdapter) {
//        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
//
//        container.setQueueNames(queueName);
//        container.setConnectionFactory(connectionFactory);
//        container.setMessageListener(listenerAdapter);
//
//        return container;
//    }
//
//    @Bean
//    MessageListenerAdapter listenerAdapter(Receiver receiver) {
//        return new MessageListenerAdapter(receiver, "receiveMessage");
//    }
//
//    @Bean
//    CommandLineRunner runner(RabbitTemplate template) {
//        return args -> {
//            template.convertAndSend("Hi");
//        };
//    }
}
