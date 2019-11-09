package fr.bmmc57.bills.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * A Player.
 */
@Entity
@Table(name = "player")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Player implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotNull
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @OneToMany(mappedBy = "player")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Participation> participations = new HashSet<>();

    @OneToMany(mappedBy = "player")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<Bill> bills = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public Player firstName(String firstName) {
        this.firstName = firstName;
        return this;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Player lastName(String lastName) {
        this.lastName = lastName;
        return this;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Set<Participation> getParticipations() {
        return participations;
    }

    public Player participations(Set<Participation> participations) {
        this.participations = participations;
        return this;
    }

    public Player addParticipations(Participation participation) {
        this.participations.add(participation);
        participation.setPlayer(this);
        return this;
    }

    public Player removeParticipations(Participation participation) {
        this.participations.remove(participation);
        participation.setPlayer(null);
        return this;
    }

    public void setParticipations(Set<Participation> participations) {
        this.participations = participations;
    }

    public Set<Bill> getBills() {
        return bills;
    }

    public Player bills(Set<Bill> bills) {
        this.bills = bills;
        return this;
    }

    public Player addBills(Bill bill) {
        this.bills.add(bill);
        bill.setPlayer(this);
        return this;
    }

    public Player removeBills(Bill bill) {
        this.bills.remove(bill);
        bill.setPlayer(null);
        return this;
    }

    public void setBills(Set<Bill> bills) {
        this.bills = bills;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Player)) {
            return false;
        }
        return id != null && id.equals(((Player) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Player{" +
            "id=" + getId() +
            ", firstName='" + getFirstName() + "'" +
            ", lastName='" + getLastName() + "'" +
            "}";
    }
}
