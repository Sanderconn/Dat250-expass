package dat250.pollApp.messaging;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Service;

import static dat250.pollApp.config.AmqpConfig.VOTE_RK;

@Service
@Profile("!test")
public class AmqpTopicsService {
    private final AmqpAdmin admin;
    private final Queue voteQueue;

    public AmqpTopicsService(AmqpAdmin admin, Queue voteQueue){
        this.admin = admin;
        this.voteQueue = voteQueue;
    }

    public String ex(Long pollId){
        return "poll." + pollId;
    }

    public void registerPoll(long pollId){
        TopicExchange exchange = new TopicExchange(ex(pollId), false, true);
        admin.declareExchange(exchange);
        Binding b = BindingBuilder.bind(voteQueue).to(exchange).with(VOTE_RK);
        admin.declareBinding(b);
    }
    
}
