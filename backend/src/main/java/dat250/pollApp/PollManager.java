package dat250.pollApp;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

import org.springframework.stereotype.Component;

import dat250.pollApp.domain.Poll;
import dat250.pollApp.domain.User;
import dat250.pollApp.domain.Vote;
import dat250.pollApp.domain.VoteOption;
import dat250.pollApp.messaging.AmqpTopicsService;

@Component
public class PollManager {

    private final AmqpTopicsService topics;
    public PollManager(AmqpTopicsService topics) {this.topics = topics;}

    private final Map<Long, User> users = new HashMap<>();
    private final Map<Long, Poll> polls = new HashMap<>();
    private final Map<Long, Vote> votes = new HashMap<>();

    private final AtomicLong userSeq = new AtomicLong(1);
    private final AtomicLong pollSeq = new AtomicLong(1);
    private final AtomicLong voteSeq = new AtomicLong(1);
    private final AtomicLong voteOptionSeq = new AtomicLong(1);

    
    //users
    public User addUser(User user) {
        user.setId(userSeq.getAndIncrement());
        users.put(user.getId(), user);
        return user;
    }
    
    public User getUser(Long id) {
        return users.get(id);
    }

    public Collection<User> getAllUsers() {
        return users.values();
    }

    public void removeUser(Long id) {
        users.remove(id);
    }

    // polls
    public Poll addPoll(Poll poll) {
        poll.setId(pollSeq.getAndIncrement());
        for (VoteOption opt : poll.getOptions()) {
            opt.setId(voteOptionSeq.getAndIncrement());
            opt.setPoll(poll);
            opt.setVotes(0);
        }
        polls.put(poll.getId(), poll);
        topics.registerPoll(poll.getId());
        return poll;
    }

    public Poll getPoll(Long id) {
        return polls.get(id);
    }

    public Collection<Poll> getAllPolls() {
        return polls.values();
    }

    public void removePoll(Long id) {
        polls.remove(id);
        votes.values().removeIf(v -> v.getVotesOn().getPoll().getId().equals(id));
    }

    public VoteOption getVoteOption(Long optionId) {
        for (Poll p : polls.values()) {
            for (VoteOption o : p.getOptions()) {
                if (o.getId() == optionId) return o;
            }
        }
        return null; 
    }

    // votes
    public Vote addVote(Vote vote) {
        vote.setId(voteSeq.getAndIncrement());
        votes.put(vote.getId(), vote);
        VoteOption option = vote.getVotesOn();
        option.setVotes(option.getVotes() + 1);
        return vote;
    }

    public Vote getVote(Long id) {
        return votes.get(id);
    }

    public Collection<Vote> getAllVotes() {
        return votes.values();
    }

    public void removeVote(Long id) {
        votes.remove(id);
    }
}
