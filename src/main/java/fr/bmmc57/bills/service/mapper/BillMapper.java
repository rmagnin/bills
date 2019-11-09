package fr.bmmc57.bills.service.mapper;

import fr.bmmc57.bills.domain.*;
import fr.bmmc57.bills.service.dto.BillDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link Bill} and its DTO {@link BillDTO}.
 */
@Mapper(componentModel = "spring", uses = {PlayerMapper.class})
public interface BillMapper extends EntityMapper<BillDTO, Bill> {

    @Mapping(source = "player.id", target = "playerId")
    BillDTO toDto(Bill bill);

    @Mapping(source = "playerId", target = "player")
    Bill toEntity(BillDTO billDTO);

    default Bill fromId(Long id) {
        if (id == null) {
            return null;
        }
        Bill bill = new Bill();
        bill.setId(id);
        return bill;
    }
}
