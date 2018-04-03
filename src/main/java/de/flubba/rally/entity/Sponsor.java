package de.flubba.rally.entity;

import java.math.BigDecimal;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.validation.constraints.DecimalMax;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

@Entity
public class Sponsor {
    @Id
    @GeneratedValue
    private Long id;

    @NotNull(message = "{sponsor.field.required}")
    @Column(length = 100)
    @Size(min = 2, max = 100, message = "{sponsor.field.size}")
    private String name;

    @NotNull(message = "{sponsor.field.required}")
    @Column(length = 100)
    @Size(min = 2, max = 100, message = "{sponsor.field.size}")
    private String country;

    @NotNull(message = "{sponsor.field.required}")
    @Column(length = 100)
    @Size(min = 2, max = 100, message = "{sponsor.field.size}")
    private String city;

    @NotNull(message = "{sponsor.field.required}")
    @Column(length = 100)
    @Size(min = 2, max = 100, message = "{sponsor.field.size}")
    private String street;

    @DecimalMin(value = "0", message = "{sponsor.donation.one.range}")
    @DecimalMax(value = "10000", message = "{sponsor.donation.one.range}")
    @Column(precision = 8, scale = 2)
    private BigDecimal oneTimeDonation;

    @DecimalMin(value = "0", message = "{sponsor.donation.lap.range}")
    @DecimalMax(value = "50", message = "{sponsor.donation.lap.range}")
    @Column(precision = 8, scale = 2)
    private BigDecimal perLapDonation;

    private BigDecimal totalDonation;

    @ManyToOne
    private Runner runner;

    public String getCity() {
        return city;
    }

    public String getCountry() {
        return country;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public BigDecimal getOneTimeDonation() {
        return oneTimeDonation;
    }

    public BigDecimal getPerLapDonation() {
        return perLapDonation;
    }

    public Runner getRunner() {
        return runner;
    }

    public String getStreet() {
        return street;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setOneTimeDonation(BigDecimal oneTimeDonation) {
        this.oneTimeDonation = oneTimeDonation;
    }

    public void setPerLapDonation(BigDecimal perLapDonation) {
        this.perLapDonation = perLapDonation;
    }

    public void setRunner(Runner runner) {
        this.runner = runner;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public BigDecimal getTotalDonation() {
        return totalDonation;
    }

    public void setTotalDonation(BigDecimal totalDonation) {
        this.totalDonation = totalDonation;
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
        Sponsor other = (Sponsor) obj;
        if (id != other.id) {
            return false;
        }
        return true;
    }

}
