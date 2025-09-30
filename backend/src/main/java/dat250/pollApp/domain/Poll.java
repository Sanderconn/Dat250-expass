package dat250.pollApp.domain;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OrderBy;
import jakarta.persistence.Table;

@Entity
@Table(name = "polls")
public class Poll {

    private String question;
    private Instant publishedAt;
    private Instant validUntil;

    @ManyToOne(optional = false)
    @JoinColumn(name = "created_by_user")
    private User createdBy;

    @OneToMany(mappedBy = "poll", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderBy("presentationOrder ASC")
    @JsonManagedReference("poll-options")
    private List<VoteOption> voteOptions = new ArrayList<>();

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    public List<VoteOption> getOptions() {
        return voteOptions;
    }
    public void setOptions(List<VoteOption> voteOptions) {
        this.voteOptions = voteOptions;
    }
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }
    public User getCreatedBy(){
        return createdBy;
    }
    public void setCreatedBy(User createdBy){
        this.createdBy = createdBy;
    }
    
         /**
     *
     * Adds a new option to this Poll and returns the respective
     * VoteOption object with the given caption.
     * The value of the presentationOrder field gets determined
     * by the size of the currently existing VoteOptions for this Poll.
     * I.e. the first added VoteOption has presentationOrder=0, the secondly
     * registered VoteOption has presentationOrder=1 ans so on.
     */
    public VoteOption addVoteOption(String caption) {
        VoteOption newOption = new VoteOption();
        newOption.setCaption(caption);
        newOption.setPresentationOrder(voteOptions.size());
        newOption.setPoll(this);
        this.voteOptions.add(newOption);
        return newOption;
    }
}
