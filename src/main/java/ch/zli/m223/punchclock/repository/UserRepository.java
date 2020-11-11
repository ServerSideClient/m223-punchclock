package ch.zli.m223.punchclock.repository;

import ch.zli.m223.punchclock.domain.PunchClockUser;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<PunchClockUser, Long> {
    PunchClockUser findByUsername(String username);
}
