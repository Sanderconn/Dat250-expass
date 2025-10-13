package dat250.pollApp.messaging;

import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Component;
import dat250.pollApp.PollManager;
import dat250.pollApp.domain.Vote;
import dat250.pollApp.domain.VoteOption;

@Component
@Profile("!test")
public class VoteEventListener {
    private final PollManager pm;

    public VoteEventListener(PollManager pm) { this.pm = pm; }

    @RabbitListener(queues = "pollapp.votes")
    public void onVote(VoteEvent e) {
        if (e.optionId == null) return; 
        System.out.println("AMQP vote received: poll=" + e.pollId + " option=" + e.optionId + " user=" + e.userId);

        VoteOption option = pm.getVoteOption(e.optionId);
        Vote v = new Vote();
        v.setVotesOn(option);
        pm.addVote(v);

        new dat250.pollApp.cache.RedisCache().increment(e.pollId, e.optionId, 1);
    }
}
