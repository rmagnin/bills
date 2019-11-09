package fr.bmmc57.bills.service.dto;
import javax.validation.constraints.*;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.bmmc57.bills.domain.Championship} entity.
 */
public class ChampionshipDTO implements Serializable {

    private Long id;

    @NotNull
    private String name;

    private Integer year;

    private Double oneEventPrice;

    private Double twoEventsPrice;

    private Double threeEventsPrice;


    private Long clubId;

    private String clubName;

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

    public Integer getYear() {
        return year;
    }

    public void setYear(Integer year) {
        this.year = year;
    }

    public Double getOneEventPrice() {
        return oneEventPrice;
    }

    public void setOneEventPrice(Double oneEventPrice) {
        this.oneEventPrice = oneEventPrice;
    }

    public Double getTwoEventsPrice() {
        return twoEventsPrice;
    }

    public void setTwoEventsPrice(Double twoEventsPrice) {
        this.twoEventsPrice = twoEventsPrice;
    }

    public Double getThreeEventsPrice() {
        return threeEventsPrice;
    }

    public void setThreeEventsPrice(Double threeEventsPrice) {
        this.threeEventsPrice = threeEventsPrice;
    }

    public Long getClubId() {
        return clubId;
    }

    public void setClubId(Long clubId) {
        this.clubId = clubId;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ChampionshipDTO championshipDTO = (ChampionshipDTO) o;
        if (championshipDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), championshipDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ChampionshipDTO{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", year=" + getYear() +
            ", oneEventPrice=" + getOneEventPrice() +
            ", twoEventsPrice=" + getTwoEventsPrice() +
            ", threeEventsPrice=" + getThreeEventsPrice() +
            ", club=" + getClubId() +
            ", club='" + getClubName() + "'" +
            "}";
    }
}
