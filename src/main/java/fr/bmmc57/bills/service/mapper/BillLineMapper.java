package fr.bmmc57.bills.service.mapper;

import fr.bmmc57.bills.domain.*;
import fr.bmmc57.bills.service.dto.BillLineDTO;

import org.mapstruct.*;

/**
 * Mapper for the entity {@link BillLine} and its DTO {@link BillLineDTO}.
 */
@Mapper(componentModel = "spring", uses = {ParticipationMapper.class, BillMapper.class})
public interface BillLineMapper extends EntityMapper<BillLineDTO, BillLine> {

    @Mapping(source = "participation.id", target = "participationId")
    @Mapping(source = "bill.id", target = "billId")
    BillLineDTO toDto(BillLine billLine);

    @Mapping(source = "participationId", target = "participation")
    @Mapping(source = "billId", target = "bill")
    BillLine toEntity(BillLineDTO billLineDTO);

    default BillLine fromId(Long id) {
        if (id == null) {
            return null;
        }
        BillLine billLine = new BillLine();
        billLine.setId(id);
        return billLine;
    }
}
