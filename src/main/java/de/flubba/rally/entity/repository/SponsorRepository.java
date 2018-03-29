package de.flubba.rally.entity.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import de.flubba.rally.entity.Runner;
import de.flubba.rally.entity.Sponsor;

public interface SponsorRepository extends JpaRepository<Sponsor, Long> {
    long countByRunner(Runner runner);

    List<Sponsor> findAllByRunner(Runner runner);
}
