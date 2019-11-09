package fr.bmmc57.bills.service;

import fr.bmmc57.bills.service.dto.PlayerDTO;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

/**
 * Service Interface for managing {@link fr.bmmc57.bills.domain.Player}.
 */
public interface PlayerService {

    /**
     * Save a player.
     *
     * @param playerDTO the entity to save.
     * @return the persisted entity.
     */
    PlayerDTO save(PlayerDTO playerDTO);

    /**
     * Get all the players.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    Page<PlayerDTO> findAll(Pageable pageable);


    /**
     * Get the "id" player.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    Optional<PlayerDTO> findOne(Long id);

    /**
     * Delete the "id" player.
     *
     * @param id the id of the entity.
     */
    void delete(Long id);
}
