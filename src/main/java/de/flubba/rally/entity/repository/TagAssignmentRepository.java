package de.flubba.rally.entity.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import de.flubba.rally.entity.TagAssignment;

public interface TagAssignmentRepository extends JpaRepository<TagAssignment, Long> {
    TagAssignment findOneByTagId(String tagId);

    TagAssignment findOneByRunnerId(Long runnerId);
}
