package dat250.pollApp.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.QueueBuilder;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("!test")
public class AmqpConfig {
    public static final String APP_VOTE_QUEUE = "pollapp.votes";
    public static final String VOTE_RK = "vote";

    @Bean Jackson2JsonMessageConverter converter() { return new Jackson2JsonMessageConverter(); }
    @Bean RabbitTemplate rabbitTemplate(ConnectionFactory cf, Jackson2JsonMessageConverter c) {
        RabbitTemplate t = new RabbitTemplate(cf); t.setMessageConverter(c); return t;
    }
    @Bean RabbitAdmin amqpAdmin(ConnectionFactory cf) { return new RabbitAdmin(cf); }
    @Bean Queue votesQueue() { return QueueBuilder.durable(APP_VOTE_QUEUE).build(); }
}
