package dat250.pollApp.controllers;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

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
import dat250.pollApp.cache.RedisCache;
import dat250.pollApp.domain.Poll;
import dat250.pollApp.domain.Vote;

@RestController
@CrossOrigin(origins = "http://localhost:5173")
@RequestMapping("/api/polls")
public class PollController {

    private final PollManager pm;
    private final RedisCache cache = new RedisCache();

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

    @GetMapping("/{id}/results")
    public Map<Long, Integer> results(@PathVariable long id) {
        
        var cached = cache.getCounts(id);
        if(!cached.isEmpty()){
            Map<Long, Integer> out = new HashMap<>();
            for (var e: cached.entrySet()){
                out.put(Long.parseLong(e.getKey()), Integer.parseInt(e.getValue()));
            }
            return out;
        }
        Map<Long, Integer> counts = new HashMap<>();
        for (Vote v : pm.getAllVotes()){
            if (v.getVotesOn().getPollId() == id) {
                counts.merge(v.getVotesOn().getId(), 1, Integer::sum);
            }
        }

    cache.putCounts(id, counts);
    return counts;
    
    }

}