package de.flubba.rally.entity.repository;

import de.flubba.rally.entity.Runner;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RunnerRepository extends JpaRepository<Runner, Long> {
    Runner findOneById(Long runnerId);

    Long countByName(String name);

    List<Runner> findByNameIgnoreCaseContaining(String name);
}
