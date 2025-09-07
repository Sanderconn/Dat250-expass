package dat250.pollApp.controllers;

import java.util.Collection;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dat250.pollApp.PollManager;
import dat250.pollApp.domain.Poll;
import dat250.pollApp.domain.VoteOption;

@RestController
@RequestMapping("/api/polls")
public class PollController {

    private final PollManager pm;

    public PollController(PollManager pm) {
        this.pm = pm;
    }

    // DTO for creating a poll
    static class CreatePollBody {
        public String question;
        public java.time.Instant publishedAt;
        public java.time.Instant validUntil;
        public List<VoteOption> voteOptions;
    }

    // Create poll
    @PostMapping
    public ResponseEntity<Poll> create(@RequestBody CreatePollBody body) {
        Poll poll = new Poll();
        poll.setQuestion(body.question);
        poll.setPublishedAt(body.publishedAt);
        poll.setValidUntil(body.validUntil);
        poll.setVoteOptions(body.voteOptions);

        Poll saved = pm.addPoll(poll);
        return ResponseEntity.ok(saved);
    }

    // List all polls
    @GetMapping
    public Collection<Poll> list() {
        return pm.getAllPolls();
    }

    // Delete poll
    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        pm.removePoll(id);
    }
}