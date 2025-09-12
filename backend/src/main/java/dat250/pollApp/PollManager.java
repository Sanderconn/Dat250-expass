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

@Component
public class PollManager {

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
        for (VoteOption opt : poll.getVoteOptions()) {
            opt.setId(voteOptionSeq.getAndIncrement());
            opt.setPollId(poll.getId());
        }
        polls.put(poll.getId(), poll);
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
        votes.values().removeIf(v -> v.getPollId().equals(id));
    }

    // votes
    public Vote addVote(Vote vote) {
        vote.setId(voteSeq.getAndIncrement());
        votes.put(vote.getId(), vote);
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
