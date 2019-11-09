package fr.bmmc57.bills.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;

import java.io.Serializable;

/**
 * A BillLine.
 */
@Entity
@Table(name = "bill_line")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class BillLine implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amount")
    private Double amount;

    @OneToOne
    @JoinColumn(unique = true)
    private Participation participation;

    @ManyToOne
    @JsonIgnoreProperties("billLines")
    private Bill bill;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public BillLine amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Participation getParticipation() {
        return participation;
    }

    public BillLine participation(Participation participation) {
        this.participation = participation;
        return this;
    }

    public void setParticipation(Participation participation) {
        this.participation = participation;
    }

    public Bill getBill() {
        return bill;
    }

    public BillLine bill(Bill bill) {
        this.bill = bill;
        return this;
    }

    public void setBill(Bill bill) {
        this.bill = bill;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof BillLine)) {
            return false;
        }
        return id != null && id.equals(((BillLine) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "BillLine{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            "}";
    }
}
