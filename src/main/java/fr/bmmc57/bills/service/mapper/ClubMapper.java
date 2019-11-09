package fr.bmmc57.bills.service.mapper;

import fr.bmmc57.bills.domain.*;
import fr.bmmc57.bills.service.dto.ClubDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Club} and its DTO {@link ClubDTO}.
 */
@Mapper(componentModel = "spring", uses = {})
public interface ClubMapper extends EntityMapper<ClubDTO, Club> {



    default Club fromId(Long id) {
        if (id == null) {
            return null;
        }
        Club club = new Club();
        club.setId(id);
        return club;
    }
}
