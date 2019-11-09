package fr.bmmc57.bills.domain;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

import javax.persistence.*;
import javax.validation.constraints.*;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

import fr.bmmc57.bills.domain.enumeration.BillStatus;

/**
 * A Bill.
 */
@Entity
@Table(name = "bill")
@Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Column(name = "creation_date", nullable = false)
    private LocalDate creationDate;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private BillStatus status;

    @Column(name = "amount")
    private Double amount;

    @ManyToOne
    @JsonIgnoreProperties("bills")
    private Player player;

    @OneToMany(mappedBy = "bill")
    @Cache(usage = CacheConcurrencyStrategy.NONSTRICT_READ_WRITE)
    private Set<BillLine> lines = new HashSet<>();

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public Bill creationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
        return this;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }

    public BillStatus getStatus() {
        return status;
    }

    public Bill status(BillStatus status) {
        this.status = status;
        return this;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public Bill amount(Double amount) {
        this.amount = amount;
        return this;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Player getPlayer() {
        return player;
    }

    public Bill player(Player player) {
        this.player = player;
        return this;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Set<BillLine> getLines() {
        return lines;
    }

    public Bill lines(Set<BillLine> billLines) {
        this.lines = billLines;
        return this;
    }

    public Bill addLines(BillLine billLine) {
        this.lines.add(billLine);
        billLine.setBill(this);
        return this;
    }

    public Bill removeLines(BillLine billLine) {
        this.lines.remove(billLine);
        billLine.setBill(null);
        return this;
    }

    public void setLines(Set<BillLine> billLines) {
        this.lines = billLines;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Bill)) {
            return false;
        }
        return id != null && id.equals(((Bill) o).id);
    }

    @Override
    public int hashCode() {
        return 31;
    }

    @Override
    public String toString() {
        return "Bill{" +
            "id=" + getId() +
            ", creationDate='" + getCreationDate() + "'" +
            ", status='" + getStatus() + "'" +
            ", amount=" + getAmount() +
            "}";
    }
}
