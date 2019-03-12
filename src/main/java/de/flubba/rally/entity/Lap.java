package de.flubba.rally.entity;

import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.NotNull;

@Data
@Entity
public class Lap {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull
    private long time;

    @NotNull
    private long duration;

    @ManyToOne
    private Runner runner;

}
