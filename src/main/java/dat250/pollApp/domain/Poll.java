package dat250.pollApp.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class Poll {

    private String question;
    private Instant publishedAt;
    private Instant validUntil;
    private List<VoteOption> voteOptions = new ArrayList<>();
    private Long id;

    public Poll(){}

    public String getQuestion() {
        return question;
    }
    public void setQuestion(String question) {
        this.question = question;
    }
    public Instant getPublishedAt() {
        return publishedAt;
    }
    public void setPublishedAt(Instant publishedAt) {
        this.publishedAt = publishedAt;
    }
    public Instant getValidUntil() {
        return validUntil;
    }
    public void setValidUntil(Instant validUntil) {
        this.validUntil = validUntil;
    }
    public List<VoteOption> getVoteOptions() {
        return voteOptions;
    }
    public void setVoteOptions(List<VoteOption> voteOptions) {
        this.voteOptions = voteOptions;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    
}
