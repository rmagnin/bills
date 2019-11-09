package fr.bmmc57.bills.service;

import fr.bmmc57.bills.service.dto.BillLineDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.bmmc57.bills.domain.BillLine}.
 */
public interface BillLineService {

    /**
     * Save a billLine.
     *
     * @param billLineDTO the entity to save.
     * @return the persisted entity.
     */
    BillLineDTO save(BillLineDTO billLineDTO);

    /**
     * Get all the billLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<BillLineDTO> findAll(Pageable pageable);


    /**
     * Get the "id" billLine.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<BillLineDTO> findOne(Long id);

    /**
     * Delete the "id" billLine.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
