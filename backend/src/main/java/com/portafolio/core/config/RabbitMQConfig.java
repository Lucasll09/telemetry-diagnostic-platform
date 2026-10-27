package com.portafolio.core.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    public static final String TELEMETRY_QUEUE = "telemetry.stream";

    // Crea la cola en el broker si no existe
    @Bean
    public Queue telemetryQueue() {
        return new Queue(TELEMETRY_QUEUE, true); // true = duradera (sobrevive a reinicios)
    }

    // Permite que Spring convierta automáticamente los JSON entrantes a clases de Java
    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}
