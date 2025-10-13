package dat250.pollApp.messaging;

import java.time.Instant;

public class VoteEvent {
    public Long pollId;
    public Long optionId;
    public Long userId;
    public Instant publishedAt;

    public VoteEvent(){}
}
