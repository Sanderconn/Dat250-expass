package dat250.pollApp.controllers;

import java.util.Collection;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import dat250.pollApp.PollManager;
import dat250.pollApp.domain.Poll;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/polls")
public class PollController {

    private final PollManager pm;

    public PollController(PollManager pm) {
        this.pm = pm;
    }

    // Create poll
    @PostMapping
    public ResponseEntity<Poll> create(@RequestBody Poll poll) {
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
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        pm.removePoll(id); 
        return ResponseEntity.noContent().build(); 
}
}