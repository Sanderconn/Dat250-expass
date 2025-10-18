package dat250.pollApp.repositories;

import java.util.List;
import org.springframework.data.repository.CrudRepository;
import dat250.pollApp.domain.VoteOption;

public interface VoteOptionRepository extends CrudRepository<VoteOption, Long> {
    List<VoteOption> findByPollId(Long pollId);
}
