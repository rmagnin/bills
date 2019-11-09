package fr.bmmc57.bills.service;

import fr.bmmc57.bills.service.dto.ChampionshipDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.bmmc57.bills.domain.Championship}.
 */
public interface ChampionshipService {

    /**
     * Save a championship.
     *
     * @param championshipDTO the entity to save.
     * @return the persisted entity.
     */
    ChampionshipDTO save(ChampionshipDTO championshipDTO);

    /**
     * Get all the championships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<ChampionshipDTO> findAll(Pageable pageable);


    /**
     * Get the "id" championship.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<ChampionshipDTO> findOne(Long id);

    /**
     * Delete the "id" championship.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
