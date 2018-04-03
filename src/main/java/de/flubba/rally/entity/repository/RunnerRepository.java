package de.flubba.rally.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.flubba.rally.entity.Runner;

public interface RunnerRepository extends JpaRepository<Runner, Long> {
    Runner findOneById(Long runnerId);

    List<Runner> findByNameIgnoreCaseContaining(String name);
}
