package dat250.pollApp;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.net.URI;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import dat250.pollApp.domain.Poll;
import dat250.pollApp.domain.User;
import dat250.pollApp.domain.Vote;
import dat250.pollApp.domain.VoteOption;

@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
public class pollAppTest {
     @Test
    public void testCreateUserAndList(@Autowired TestRestTemplate rest) {
        // Create user
        ResponseEntity<User> created = rest.postForEntity(
                URI.create("/api/users"),
                Map.of("username","abc","email","abc@example.com"),
                User.class);
        assertEquals(HttpStatus.OK, created.getStatusCode());

        // List users
        ResponseEntity<List<User>> list = rest.exchange(
                URI.create("/api/users"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<User>>() {});
        assertEquals(HttpStatus.OK, list.getStatusCode());
        assertEquals(1, list.getBody().size());
    }

    @Test
    public void testCreateTwoUsersAndList(@Autowired TestRestTemplate rest) {
        rest.postForEntity(URI.create("/api/users"),
                Map.of("username","abc","email","abc@example.com"), User.class);
        rest.postForEntity(URI.create("/api/users"),
                Map.of("username","123","email","123@example.com"), User.class);

        ResponseEntity<List<User>> list = rest.exchange(
                URI.create("/api/users"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<User>>() {});
        assertEquals(HttpStatus.OK, list.getStatusCode());
        assertEquals(2, list.getBody().size());
    }

    @Test
    public void testCreatePollWithOptionsAndList(@Autowired TestRestTemplate rest) {
        // Create poll
        ResponseEntity<Poll> pollRes = rest.postForEntity(
                URI.create("/api/polls"),
                Map.of(
                        "question", "What is your favorite number in the alphabet?",
                        "publishedAt", "2025-09-04T00:00:00Z",
                        "validUntil",  "2025-09-10T00:00:00Z", 
                        "options", List.of(
                                Map.of("caption","Blue","presentationOrder",1),
                                Map.of("caption","Green","presentationOrder",2),
                                Map.of("caption","Red","presentationOrder",3)
                        )
                ),
                Poll.class);
        assertEquals(HttpStatus.OK, pollRes.getStatusCode());
        Poll poll = pollRes.getBody();
        assertEquals(3, poll.getOptions().size());

        // List polls 
        ResponseEntity<List<Poll>> polls = rest.exchange(
                URI.create("/api/polls"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Poll>>() {});
        assertEquals(HttpStatus.OK, polls.getStatusCode());
        assertEquals(1, polls.getBody().size());
    }

    @Test
    public void testVoteCreateChangeAndList(@Autowired TestRestTemplate rest) {
        // Setup user and poll
        Long userId = rest.postForEntity(
                URI.create("/api/users"),
                Map.of("username","bob","email","bob@example.com"),
                User.class
        ).getBody().getId();

        Poll poll = rest.postForEntity(
                URI.create("/api/polls"),
                Map.of(
                        "question", "Best color?",
                        "publishedAt", "2025-09-04T00:00:00Z",
                        "validUntil",  "2025-09-10T00:00:00Z",
                        "options", List.of(
                                Map.of("caption","Blue","presentationOrder",1),
                                Map.of("caption","Green","presentationOrder",2)
                        )
                ),
                Poll.class
        ).getBody();

        Long pollId = poll.getId();
        List<VoteOption> opts = poll.getOptions();
        long optA = opts.get(0).getId();
        long optB = opts.get(1).getId();

        // Create vote
        ResponseEntity<Vote> voteRes = rest.postForEntity(
                URI.create("/api/polls/" + pollId + "/votes"),
                Map.of("userId", userId, "optionId", optA),
                Vote.class
        );
        assertEquals(HttpStatus.OK, voteRes.getStatusCode());
        Vote vote = voteRes.getBody();
        assertEquals(pollId, vote.getVotesOn().getPollId());
        assertEquals(optA, vote.getVotesOn().getId());

        // Change vote 
        HttpHeaders h = new HttpHeaders();
        h.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<Map<String, Long>> changeReq = new HttpEntity<>(Map.of("newOptionId", optB), h);

        ResponseEntity<Vote> changed = rest.exchange(
                URI.create("/api/polls/" + pollId + "/votes/" + vote.getId()),
                HttpMethod.PUT,
                changeReq,
                Vote.class
        );
        assertEquals(HttpStatus.OK, changed.getStatusCode());
        assertEquals(optB, changed.getBody().getVotesOn().getId());

        // List votes for poll 
        ResponseEntity<List<Vote>> votes = rest.exchange(
                URI.create("/api/polls/" + pollId + "/votes"),
                HttpMethod.GET,
                HttpEntity.EMPTY,
                new ParameterizedTypeReference<List<Vote>>() {});
        assertEquals(HttpStatus.OK, votes.getStatusCode());
        assertEquals(1, votes.getBody().size());
        assertEquals(pollId, votes.getBody().get(0).getVotesOn().getPollId());
    }

    @Test
    public void testDeletePollCascadesVotes(@Autowired TestRestTemplate rest) {
        // Setup user poll and vote
        Long userId = rest.postForEntity(
                URI.create("/api/users"),
                Map.of("username","u","email","u@example.com"),
                User.class
        ).getBody().getId();

        Poll poll = rest.postForEntity(
                URI.create("/api/polls"),
                Map.of(
                        "question", "Q?",
                        "publishedAt", "2025-09-04T00:00:00Z",
                        "validUntil",  "2025-09-10T00:00:00Z",
                        "options", List.of(
                                Map.of("caption","A","presentationOrder",1),
                                Map.of("caption","B","presentationOrder",2)
                        )
                ),
                Poll.class
        ).getBody();

        rest.postForEntity(
                URI.create("/api/polls/" + poll.getId() + "/votes"),
                Map.of("userId", userId, "optionId", poll.getOptions().get(0).getId()),
                Vote.class
        );

        // Delete poll
        ResponseEntity<Void> del = rest.exchange(
                URI.create("/api/polls/" + poll.getId()),
                HttpMethod.DELETE,
                HttpEntity.EMPTY,
                Void.class
        );
        assertEquals(HttpStatus.NO_CONTENT, del.getStatusCode());

        // Listing votes for deleted poll 
        try {
            rest.getForEntity(URI.create("/api/polls/" + poll.getId() + "/votes"), String.class);
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, e.getStatusCode());
        }
    }
}
