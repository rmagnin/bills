package fr.bmmc57.bills.service.mapper;

import fr.bmmc57.bills.domain.*;
import fr.bmmc57.bills.service.dto.PlayerDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Player} and its DTO {@link PlayerDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface PlayerMapper extends EntityMapper<PlayerDTO, Player> {


    @Mapping(target = "participations", ignore = true)
    @Mapping(target = "removeParticipations", ignore = true)
    @Mapping(target = "bills", ignore = true)
    @Mapping(target = "removeBills", ignore = true)
    Player toEntity(PlayerDTO playerDTO);

    default Player fromId(Long id) {
        if (id == null) {
            return null;
        }
        Player player = new Player();
        player.setId(id);
        return player;
    }
}
