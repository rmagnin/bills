package fr.bmmc57.bills.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.bmmc57.bills.domain.Club} entity.
 */
public class ClubDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private String defaultChampionshipName;

    private Double defaultOneEventPrice;

    private Double defaultTwoEventsPrice;

    private Double defaultThreeEventsPrice;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDefaultChampionshipName() {
        return defaultChampionshipName;
    }

    public void setDefaultChampionshipName(String defaultChampionshipName) {
        this.defaultChampionshipName = defaultChampionshipName;
    }

    public Double getDefaultOneEventPrice() {
        return defaultOneEventPrice;
    }

    public void setDefaultOneEventPrice(Double defaultOneEventPrice) {
        this.defaultOneEventPrice = defaultOneEventPrice;
    }

    public Double getDefaultTwoEventsPrice() {
        return defaultTwoEventsPrice;
    }

    public void setDefaultTwoEventsPrice(Double defaultTwoEventsPrice) {
        this.defaultTwoEventsPrice = defaultTwoEventsPrice;
    }

    public Double getDefaultThreeEventsPrice() {
        return defaultThreeEventsPrice;
    }

    public void setDefaultThreeEventsPrice(Double defaultThreeEventsPrice) {
        this.defaultThreeEventsPrice = defaultThreeEventsPrice;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ClubDTO clubDTO = (ClubDTO) o;
        if (clubDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), clubDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ClubDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", defaultChampionshipName='" + getDefaultChampionshipName() + "'" +
            ", defaultOneEventPrice=" + getDefaultOneEventPrice() +
            ", defaultTwoEventsPrice=" + getDefaultTwoEventsPrice() +
            ", defaultThreeEventsPrice=" + getDefaultThreeEventsPrice() +
            "}";
    }
}
