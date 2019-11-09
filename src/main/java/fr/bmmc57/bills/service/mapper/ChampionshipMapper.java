package fr.bmmc57.bills.service.mapper;

import fr.bmmc57.bills.domain.*;
import fr.bmmc57.bills.service.dto.ChampionshipDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Championship} and its DTO {@link ChampionshipDTO}.
 */
@Mapper(componentModel = "spring", uses = {ClubMapper.class})
public interface ChampionshipMapper extends EntityMapper<ChampionshipDTO, Championship> {

    @Mapping(source = "club.id", target = "clubId")
    @Mapping(source = "club.name", target = "clubName")
    ChampionshipDTO toDto(Championship championship);

    @Mapping(source = "clubId", target = "club")
    @Mapping(target = "participations", ignore = true)
    @Mapping(target = "removeParticipations", ignore = true)
    Championship toEntity(ChampionshipDTO championshipDTO);

    default Championship fromId(Long id) {
        if (id == null) {
            return null;
        }
        Championship championship = new Championship();
        championship.setId(id);
        return championship;
    }
}
