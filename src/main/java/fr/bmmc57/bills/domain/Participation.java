package fr.bmmc57.bills.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A Participation.
 */
@Entity
@Table(name = "participation")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Participation implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "single_event")
    private Boolean singleEvent;

    @Column(name = "double_event")
    private Boolean doubleEvent;

    @Column(name = "mixed_event")
    private Boolean mixedEvent;

    @ManyToOne
    @JsonIgnoreProperties("participations")
    private Championship championship;

    @ManyToOne
    @JsonIgnoreProperties("participations")
    private Player player;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSingleEvent() {
        return singleEvent;
    }

    public Participation singleEvent(Boolean singleEvent) {
        this.singleEvent = singleEvent;
        return this;
    }

    public void setSingleEvent(Boolean singleEvent) {
        this.singleEvent = singleEvent;
    }

    public Boolean isDoubleEvent() {
        return doubleEvent;
    }

    public Participation doubleEvent(Boolean doubleEvent) {
        this.doubleEvent = doubleEvent;
        return this;
    }

    public void setDoubleEvent(Boolean doubleEvent) {
        this.doubleEvent = doubleEvent;
    }

    public Boolean isMixedEvent() {
        return mixedEvent;
    }

    public Participation mixedEvent(Boolean mixedEvent) {
        this.mixedEvent = mixedEvent;
        return this;
    }

    public void setMixedEvent(Boolean mixedEvent) {
        this.mixedEvent = mixedEvent;
    }

    public Championship getChampionship() {
        return championship;
    }

    public Participation championship(Championship championship) {
        this.championship = championship;
        return this;
    }

    public void setChampionship(Championship championship) {
        this.championship = championship;
    }

    public Player getPlayer() {
        return player;
    }

    public Participation player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Participation)) {
            return false;
        }
        return id != null && id.equals(((Participation) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Participation{" +
            "id=" + getId() +
            ", singleEvent='" + isSingleEvent() + "'" +
            ", doubleEvent='" + isDoubleEvent() + "'" +
            ", mixedEvent='" + isMixedEvent() + "'" +
            "}";
    }
}
