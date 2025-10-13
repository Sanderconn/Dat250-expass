package dat250.pollApp.domain;

import java.time.Instant;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "votes")
public class Vote {
    
    private Instant publishedAt;

    @ManyToOne(optional = true)
    @JoinColumn(name = "voter_id")
    private User voter;

    @ManyToOne(optional = false)
    @JoinColumn(name = "option_id")
    private VoteOption votesOn;
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; 

    public Vote(){}

    public Instant getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getVoter() {
        return voter;
    }
    public void setVoter(User voter) {
        this.voter = voter;
    }
    public VoteOption getVotesOn() {
        return votesOn;
    }
    public void setVotesOn(VoteOption votesOn) {
        this.votesOn = votesOn;
    }
}
