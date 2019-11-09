package fr.bmmc57.bills.service.dto;
import java.io.Serializable;
import java.util.Objects;
import fr.bmmc57.bills.domain.enumeration.BillStatus;

/**
 * A DTO for the {@link fr.bmmc57.bills.domain.Bill} entity.
 */
public class BillDTO implements Serializable {

    private Long id;

    private BillStatus status;

    private Double amount;


    private Long playerId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public BillStatus getStatus() {
        return status;
    }

    public void setStatus(BillStatus status) {
        this.status = status;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillDTO billDTO = (BillDTO) o;
        if (billDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillDTO{" +
            "id=" + getId() +
            ", status='" + getStatus() + "'" +
            ", amount=" + getAmount() +
            ", player=" + getPlayerId() +
            "}";
    }
}
