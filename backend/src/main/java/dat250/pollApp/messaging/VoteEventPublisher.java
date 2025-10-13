package dat250.pollApp.messaging;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Service;

import static dat250.pollApp.config.AmqpConfig.VOTE_RK;

@Service
public class VoteEventPublisher {
    private final RabbitTemplate rabbit;
    private final AmqpTopicsService topics;

    public VoteEventPublisher(RabbitTemplate rabbit, AmqpTopicsService topics){
        this.rabbit = rabbit;
        this.topics = topics;

    }

    public void publish(Long pollId, Long userId, Long optionId){
        VoteEvent e = new VoteEvent();
        e.pollId = pollId;
        e.userId = userId;
        e.optionId = optionId;
        rabbit.convertAndSend(topics.ex(pollId), VOTE_RK, e);
    }

}
