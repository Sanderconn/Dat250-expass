package dat250.pollApp.controllers;

import java.time.Instant;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dat250.pollApp.PollManager;
import dat250.pollApp.domain.Poll;
import dat250.pollApp.domain.Vote;
import dat250.pollApp.domain.VoteOption;

@RestController
@RequestMapping("/api/polls/{pollId}/votes")
public class VoteController {

    private final PollManager pm;
    public VoteController(PollManager pm) { this.pm = pm; }

    static class CreateVoteBody { public Long userId; public Long optionId;}
    static class UpdateVoteBody { public Long newOptionId; }

    @GetMapping
    public ResponseEntity<Collection<Vote>> list(@PathVariable long pollId) {
        Poll poll = pm.getPoll(pollId);
        List<Vote> out = new ArrayList<>();
        for (Vote v : pm.getAllVotes()) {
            for (VoteOption o : poll.getVoteOptions()) {
                if (o.getId() == v.getOptionId()) { out.add(v); break; }
            }
        }
        return ResponseEntity.ok(out);
    }

    @PostMapping
    public ResponseEntity<Vote> create(@PathVariable long pollId, @RequestBody CreateVoteBody body) {
        Vote v = new Vote();
        v.setUserId(body.userId);  
        v.setOptionId(body.optionId);
        v.setPollId(pollId);
        v.setPublishedAt(Instant.now());

        Vote saved = pm.addVote(v);
        return ResponseEntity.ok(saved);
    }

    @PutMapping("/{voteId}")
    public ResponseEntity<Vote> update(@PathVariable long pollId, @PathVariable long voteId, @RequestBody UpdateVoteBody body) {
        Vote vote = pm.getVote(voteId);
        vote.setOptionId(body.newOptionId);
        vote.setPollId(pollId);
        vote.setPublishedAt(Instant.now());
        return ResponseEntity.ok(vote);
    }
}
