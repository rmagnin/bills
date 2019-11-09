package fr.bmmc57.bills.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.bmmc57.bills.domain.BillLine} entity.
 */
public class BillLineDTO implements Serializable {

    private Long id;

    private Double amount;


    private Long participationId;

    private Long billId;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Long getParticipationId() {
        return participationId;
    }

    public void setParticipationId(Long participationId) {
        this.participationId = participationId;
    }

    public Long getBillId() {
        return billId;
    }

    public void setBillId(Long billId) {
        this.billId = billId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        BillLineDTO billLineDTO = (BillLineDTO) o;
        if (billLineDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), billLineDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "BillLineDTO{" +
            "id=" + getId() +
            ", amount=" + getAmount() +
            ", participation=" + getParticipationId() +
            ", bill=" + getBillId() +
            "}";
    }
}
