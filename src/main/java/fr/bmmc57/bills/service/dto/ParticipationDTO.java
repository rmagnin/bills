package fr.bmmc57.bills.service.dto;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link fr.bmmc57.bills.domain.Participation} entity.
 */
public class ParticipationDTO implements Serializable {

    private Long id;

    private Boolean singleEvent;

    private Boolean doubleEvent;

    private Boolean mixedEvent;


    private Long championshipId;

    private String championshipName;

    private Long playerId;

    private String playerLastName;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Boolean isSingleEvent() {
        return singleEvent;
    }

    public void setSingleEvent(Boolean singleEvent) {
        this.singleEvent = singleEvent;
    }

    public Boolean isDoubleEvent() {
        return doubleEvent;
    }

    public void setDoubleEvent(Boolean doubleEvent) {
        this.doubleEvent = doubleEvent;
    }

    public Boolean isMixedEvent() {
        return mixedEvent;
    }

    public void setMixedEvent(Boolean mixedEvent) {
        this.mixedEvent = mixedEvent;
    }

    public Long getChampionshipId() {
        return championshipId;
    }

    public void setChampionshipId(Long championshipId) {
        this.championshipId = championshipId;
    }

    public String getChampionshipName() {
        return championshipName;
    }

    public void setChampionshipName(String championshipName) {
        this.championshipName = championshipName;
    }

    public Long getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Long playerId) {
        this.playerId = playerId;
    }

    public String getPlayerLastName() {
        return playerLastName;
    }

    public void setPlayerLastName(String playerLastName) {
        this.playerLastName = playerLastName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }

        ParticipationDTO participationDTO = (ParticipationDTO) o;
        if (participationDTO.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), participationDTO.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "ParticipationDTO{" +
            "id=" + getId() +
            ", singleEvent='" + isSingleEvent() + "'" +
            ", doubleEvent='" + isDoubleEvent() + "'" +
            ", mixedEvent='" + isMixedEvent() + "'" +
            ", championship=" + getChampionshipId() +
            ", championship='" + getChampionshipName() + "'" +
            ", player=" + getPlayerId() +
            ", player='" + getPlayerLastName() + "'" +
            "}";
    }
}
