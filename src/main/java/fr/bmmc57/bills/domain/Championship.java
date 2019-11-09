package fr.bmmc57.bills.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Championship.
 */
@Entity
@Table(name = "championship")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Championship implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "year")
    private Integer year;

    @Column(name = "one_event_price")
    private Double oneEventPrice;

    @Column(name = "two_events_price")
    private Double twoEventsPrice;

    @Column(name = "three_events_price")
    private Double threeEventsPrice;

    @ManyToOne
    @JsonIgnoreProperties("championships")
    private Club club;

    @OneToMany(mappedBy = "championship")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Participation> participations = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public Championship name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getYear() {
        return year;
    }

    public Championship year(Integer year) {
        this.year = year;
        return this;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getOneEventPrice() {
        return oneEventPrice;
    }

    public Championship oneEventPrice(Double oneEventPrice) {
        this.oneEventPrice = oneEventPrice;
        return this;
    }

    public void setOneEventPrice(Double oneEventPrice) {
        this.oneEventPrice = oneEventPrice;
    }

    public Double getTwoEventsPrice() {
        return twoEventsPrice;
    }

    public Championship twoEventsPrice(Double twoEventsPrice) {
        this.twoEventsPrice = twoEventsPrice;
        return this;
    }

    public void setTwoEventsPrice(Double twoEventsPrice) {
        this.twoEventsPrice = twoEventsPrice;
    }

    public Double getThreeEventsPrice() {
        return threeEventsPrice;
    }

    public Championship threeEventsPrice(Double threeEventsPrice) {
        this.threeEventsPrice = threeEventsPrice;
        return this;
    }

    public void setThreeEventsPrice(Double threeEventsPrice) {
        this.threeEventsPrice = threeEventsPrice;
    }

    public Club getClub() {
        return club;
    }

    public Championship club(Club club) {
        this.club = club;
        return this;
    }

    public void setClub(Club club) {
        this.club = club;
    }

    public Set<Participation> getParticipations() {
        return participations;
    }

    public Championship participations(Set<Participation> participations) {
        this.participations = participations;
        return this;
    }

    public Championship addParticipations(Participation participation) {
        this.participations.add(participation);
        participation.setChampionship(this);
        return this;
    }

    public Championship removeParticipations(Participation participation) {
        this.participations.remove(participation);
        participation.setChampionship(null);
        return this;
    }

    public void setParticipations(Set<Participation> participations) {
        this.participations = participations;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Championship)) {
            return false;
        }
        return id != null && id.equals(((Championship) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Championship{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", year=" + getYear() +
            ", oneEventPrice=" + getOneEventPrice() +
            ", twoEventsPrice=" + getTwoEventsPrice() +
            ", threeEventsPrice=" + getThreeEventsPrice() +
            "}";
    }
}
