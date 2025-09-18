package dat250.pollApp.domain;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonProperty;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Transient;

@Entity
@Table(name = "vote_options")
public class VoteOption {
    
    private String caption;
    private int presentationOrder;
    private int votes;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "poll_id")
    @JsonBackReference("poll-options")
    private Poll poll;

    @Transient
    private Long pollIdShadow;  // used only for JSON readback

    @JsonProperty("pollId")
    public Long getPollId() {
        return (poll != null) ? poll.getId() : pollIdShadow;
    }

    @JsonProperty("pollId")
    public void setPollId(Long id) {
        this.pollIdShadow = id;
    }

    public VoteOption(){}
    
    public String getCaption() {
        return caption;
    }
    public void setCaption(String caption) {
        this.caption = caption;
    }
    public int getPresentationOrder() {
        return presentationOrder;
    }
    public void setPresentationOrder(int presentationOrder) {
        this.presentationOrder = presentationOrder;
    }
     public long getId() {
        return id;
    }
    public void setId(long id) {
        this.id = id;
    }
    public Poll getPoll() {
        return poll;
    }
    public void setPoll(Poll poll) {
        this.poll = poll;
    }

        public int getVotes() {
        return votes;
    }

    public void setVotes(int votes) {
        this.votes = votes;
    }

}
