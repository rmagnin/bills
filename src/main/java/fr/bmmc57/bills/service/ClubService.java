package fr.bmmc57.bills.service;

import fr.bmmc57.bills.service.dto.ClubDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.bmmc57.bills.domain.Club}.
 */
public interface ClubService {

    /**
     * Save a club.
     *
     * @param clubDTO the entity to save.
     * @return the persisted entity.
     */
    ClubDTO save(ClubDTO clubDTO);

    /**
     * Get all the clubs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ClubDTO> findAll(Pageable pageable);


    /**
     * Get the "id" club.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ClubDTO> findOne(Long id);

    /**
     * Delete the "id" club.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
