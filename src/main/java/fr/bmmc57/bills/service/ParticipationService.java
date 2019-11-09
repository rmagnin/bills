package fr.bmmc57.bills.service;

import fr.bmmc57.bills.service.dto.ParticipationDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.bmmc57.bills.domain.Participation}.
 */
public interface ParticipationService {

    /**
     * Save a participation.
     *
     * @param participationDTO the entity to save.
     * @return the persisted entity.
     */
    ParticipationDTO save(ParticipationDTO participationDTO);

    /**
     * Get all the participations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ParticipationDTO> findAll(Pageable pageable);


    /**
     * Get the "id" participation.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ParticipationDTO> findOne(Long id);

    /**
     * Delete the "id" participation.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
