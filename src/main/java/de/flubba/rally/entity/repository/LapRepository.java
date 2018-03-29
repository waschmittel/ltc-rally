package de.flubba.rally.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import de.flubba.rally.entity.Lap;
import de.flubba.rally.entity.Runner;

public interface LapRepository extends JpaRepository<Lap, Long> {
    @Query("select l from Lap l where l.time in (select max(laps.time) from Lap laps where laps.runner = ?1)")
    Lap findLastLap(Runner runner);
}
