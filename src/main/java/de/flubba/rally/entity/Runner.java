package de.flubba.rally.entity;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.math.BigDecimal;
import java.util.List;

@Data
@Entity
public class Runner {
    public enum Gender {
        male, female, child
    }

    @Id
    @SequenceGenerator(name = "RUNNER_ID", allocationSize = 1)
    @GeneratedValue(generator = "RUNNER_ID", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "{runner.name.required}")
    @Column(length = 100)
    @Size(min = 5, max = 100, message = "{runner.name.length}")
    private String name;

    @NotNull(message = "{runner.room.required}")
    @Column(length = 20)
    @Size(min = 1, max = 20, message = "{runner.room.length}")
    private String roomNumber;
    private BigDecimal average;
    private BigDecimal donations;

    @NotNull(message = "{runner.gender.required}")
    private Gender gender;

    @OneToMany(mappedBy = "runner", orphanRemoval = true)
    private List<Lap> laps;

    @OneToMany(mappedBy = "runner", orphanRemoval = true)
    private List<Sponsor> sponsors;

    private Integer numberOfSponsors;
    private Integer numberOfLapsRun;
    private Long bonusLaps;

}
