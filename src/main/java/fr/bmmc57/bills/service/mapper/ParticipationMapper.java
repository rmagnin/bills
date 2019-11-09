package fr.bmmc57.bills.service.mapper;

import fr.bmmc57.bills.domain.*;
import fr.bmmc57.bills.service.dto.ParticipationDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Participation} and its DTO {@link ParticipationDTO}.
 */
@Mapper(componentModel = "spring", uses = {ChampionshipMapper.class, PlayerMapper.class})
public interface ParticipationMapper extends EntityMapper<ParticipationDTO, Participation> {

    @Mapping(source = "championship.id", target = "championshipId")
    @Mapping(source = "championship.name", target = "championshipName")
    @Mapping(source = "player.id", target = "playerId")
    @Mapping(source = "player.lastName", target = "playerLastName")
    ParticipationDTO toDto(Participation participation);

    @Mapping(source = "championshipId", target = "championship")
    @Mapping(source = "playerId", target = "player")
    Participation toEntity(ParticipationDTO participationDTO);

    default Participation fromId(Long id) {
        if (id == null) {
            return null;
        }
        Participation participation = new Participation();
        participation.setId(id);
        return participation;
    }
}
