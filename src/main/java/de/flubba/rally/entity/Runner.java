package de.flubba.rally.entity;

import java.math.BigDecimal;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.SequenceGenerator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Runner {
    public enum Gender {
        male, female, child
    }

    @Id
    @SequenceGenerator(name="RUNNER", allocationSize = 1)
    @GeneratedValue(generator = "RUNNER", strategy = GenerationType.SEQUENCE)
    private Long id;

    @NotNull(message = "{runner.name.required}")
    @Column(length = 100)
    @Size(min = 5, max = 100, message = "{runner.name.length}")
    private String name;

    @NotNull(message = "{runner.room.required}")
    @Column(length = 20)
    @Size(min = 1, max = 20, message = "{runner.room.length}")
    private String     roomNumber;
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
    private Long    bonusLaps;

    public BigDecimal getAverage() {
        return average;
    }

    public BigDecimal getDonations() {
        return donations;
    }

    public Gender getGender() {
        return gender;
    }

    public Long getId() {
        return id;
    }

    public List<Lap> getLaps() {
        return laps;
    }

    public String getName() {
        return name;
    }

    public Integer getNumberOfSponsors() {
        return numberOfSponsors;
    }

    public String getRoomNumber() {
        return roomNumber;
    }

    public List<Sponsor> getSponsors() {
        return sponsors;
    }

    public void setAverage(BigDecimal average) {
        this.average = average;
    }

    public void setDonations(BigDecimal donations) {
        this.donations = donations;
    }

    public void setGender(Gender gender) {
        this.gender = gender;
    }

    public void setLaps(List<Lap> laps) {
        this.laps = laps;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setNumberOfSponsors(Integer numberOfSponsors) {
        this.numberOfSponsors = numberOfSponsors;
    }

    public void setRoomNumber(String roomNumber) {
        this.roomNumber = roomNumber;
    }

    public void setSponsors(List<Sponsor> sponsors) {
        this.sponsors = sponsors;
    }

    public Integer getNumberOfLapsRun() {
        return numberOfLapsRun;
    }

    public void setNumberOfLapsRun(Integer numberOfLapsRun) {
        this.numberOfLapsRun = numberOfLapsRun;
    }

    public Long getBonusLaps() {
        return bonusLaps;
    }

    public void setBonusLaps(Long bonusLaps) {
        this.bonusLaps = bonusLaps;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + (int) (id ^ (id >>> 32));
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        Runner other = (Runner) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
