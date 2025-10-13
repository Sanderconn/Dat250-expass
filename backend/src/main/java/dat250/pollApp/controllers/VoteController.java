package dat250.pollApp.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;

import dat250.pollApp.PollManager;
import dat250.pollApp.cache.RedisCache;
import dat250.pollApp.domain.Poll;
import dat250.pollApp.domain.User;
import dat250.pollApp.domain.Vote;
import dat250.pollApp.domain.VoteOption;
import dat250.pollApp.messaging.VoteEventPublisher;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/polls/{pollId}/votes")
public class VoteController {

    private final PollManager pm;
    private final RedisCache cache = new RedisCache();
    private final VoteEventPublisher publisher;

    public VoteController(PollManager pm, @Autowired(required = false) VoteEventPublisher publisher) { 
        this.pm = pm; 
        this.publisher = publisher;
    }

    static class CreateVoteBody { public Long userId; public Long optionId;}
    static class UpdateVoteBody { public Long newOptionId; }

    @GetMapping
    public ResponseEntity<Collection<Vote>> list(@PathVariable long pollId) {
        Poll poll = pm.getPoll(pollId);
        List<Vote> out = new ArrayList<>();
        for (Vote v : pm.getAllVotes()) {
            if (v.getVotesOn().getPoll().getId() == poll.getId()) {
                out.add(v);
            }
        }
        return ResponseEntity.ok(out);
    }

    @PostMapping
    public ResponseEntity<Vote> create(@PathVariable long pollId, @RequestBody CreateVoteBody body) {
        User user = (body.userId != null) ? pm.getUser(body.userId) : null;
        VoteOption option = pm.getVoteOption(body.optionId);
        Vote v = new Vote();
        v.setVoter(user);
        v.setVotesOn(option);
        v.setPublishedAt(Instant.now());

        Vote saved = pm.addVote(v);
        cache.increment(pollId, body.optionId, 1);
        if (publisher != null) {
            publisher.publish(pollId, saved.getVotesOn().getId(), (saved.getVoter() != null) ? saved.getVoter().getId() : null);
        }
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{voteId}")
    public ResponseEntity<Vote> update(@PathVariable long pollId, @PathVariable long voteId, @RequestBody UpdateVoteBody body) {
        Vote v = pm.getVote(voteId);
        VoteOption oldOption = v.getVotesOn();
        VoteOption newOption = pm.getVoteOption(body.newOptionId);
        v.setVotesOn(newOption);
        v.setPublishedAt(Instant.now());

        cache.increment(pollId, oldOption.getId(), -1);
        cache.increment(pollId, newOption.getId(), 1);
        return ResponseEntity.ok(v);
    }
}
