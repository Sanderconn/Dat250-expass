package dat250.pollApp.repositories;

import org.springframework.data.repository.CrudRepository;
import dat250.pollApp.domain.Poll;

public interface PollRepository extends CrudRepository<Poll, Long> { }
