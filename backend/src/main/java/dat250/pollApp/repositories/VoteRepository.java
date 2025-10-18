package dat250.pollApp.repositories;

import org.springframework.data.repository.CrudRepository;
import dat250.pollApp.domain.Vote;

public interface VoteRepository extends CrudRepository<Vote, Long> { }
