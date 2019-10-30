package de.flubba.rally.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class TagAssignment {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    @Column(length = 40, unique = true)
    private String tagId;

    @NotNull
    @Column(unique = true)
    private Long runnerId;

}
