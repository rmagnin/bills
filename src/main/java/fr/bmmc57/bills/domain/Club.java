package fr.bmmc57.bills.domain;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;

/**
 * A Club.
 */
@Entity
@Table(name = "club")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Club implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "default_championship_name")
    private String defaultChampionshipName;

    @Column(name = "default_one_event_price")
    private Double defaultOneEventPrice;

    @Column(name = "default_two_events_price")
    private Double defaultTwoEventsPrice;

    @Column(name = "default_three_events_price")
    private Double defaultThreeEventsPrice;

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

    public Club name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultChampionshipName() {
        return defaultChampionshipName;
    }

    public Club defaultChampionshipName(String defaultChampionshipName) {
        this.defaultChampionshipName = defaultChampionshipName;
        return this;
    }

    public void setDefaultChampionshipName(String defaultChampionshipName) {
        this.defaultChampionshipName = defaultChampionshipName;
    }

    public Double getDefaultOneEventPrice() {
        return defaultOneEventPrice;
    }

    public Club defaultOneEventPrice(Double defaultOneEventPrice) {
        this.defaultOneEventPrice = defaultOneEventPrice;
        return this;
    }

    public void setDefaultOneEventPrice(Double defaultOneEventPrice) {
        this.defaultOneEventPrice = defaultOneEventPrice;
    }

    public Double getDefaultTwoEventsPrice() {
        return defaultTwoEventsPrice;
    }

    public Club defaultTwoEventsPrice(Double defaultTwoEventsPrice) {
        this.defaultTwoEventsPrice = defaultTwoEventsPrice;
        return this;
    }

    public void setDefaultTwoEventsPrice(Double defaultTwoEventsPrice) {
        this.defaultTwoEventsPrice = defaultTwoEventsPrice;
    }

    public Double getDefaultThreeEventsPrice() {
        return defaultThreeEventsPrice;
    }

    public Club defaultThreeEventsPrice(Double defaultThreeEventsPrice) {
        this.defaultThreeEventsPrice = defaultThreeEventsPrice;
        return this;
    }

    public void setDefaultThreeEventsPrice(Double defaultThreeEventsPrice) {
        this.defaultThreeEventsPrice = defaultThreeEventsPrice;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Club)) {
            return false;
        }
        return id != null && id.equals(((Club) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Club{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", defaultChampionshipName='" + getDefaultChampionshipName() + "'" +
            ", defaultOneEventPrice=" + getDefaultOneEventPrice() +
            ", defaultTwoEventsPrice=" + getDefaultTwoEventsPrice() +
            ", defaultThreeEventsPrice=" + getDefaultThreeEventsPrice() +
            "}";
    }
}
