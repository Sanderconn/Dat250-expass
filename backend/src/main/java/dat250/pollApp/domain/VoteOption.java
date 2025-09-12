package dat250.pollApp.domain;

public class VoteOption {
    
    private String caption;
    private int presentationOrder;
    private long id;
    private long pollId;
    
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
    public long getPollId() {
        return pollId;
    }
    public void setPollId(long pollId) {
        this.pollId = pollId;
    }

}
