package fr.bmmc57.bills.service.impl;

import fr.bmmc57.bills.service.ChampionshipService;
import fr.bmmc57.bills.domain.Championship;
import fr.bmmc57.bills.repository.ChampionshipRepository;
import fr.bmmc57.bills.service.dto.ChampionshipDTO;
import fr.bmmc57.bills.service.mapper.ChampionshipMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Championship}.
 */
@Service
@Transactional
public class ChampionshipServiceImpl implements ChampionshipService {

    private final Logger log = LoggerFactory.getLogger(ChampionshipServiceImpl.class);

    private final ChampionshipRepository championshipRepository;

    private final ChampionshipMapper championshipMapper;

    public ChampionshipServiceImpl(ChampionshipRepository championshipRepository, ChampionshipMapper championshipMapper) {
        this.championshipRepository = championshipRepository;
        this.championshipMapper = championshipMapper;
    }

    /**
     * Save a championship.
     *
     * @param championshipDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ChampionshipDTO save(ChampionshipDTO championshipDTO) {
        log.debug("Request to save Championship : {}", championshipDTO);
        Championship championship = championshipMapper.toEntity(championshipDTO);
        championship = championshipRepository.save(championship);
        return championshipMapper.toDto(championship);
    }

    /**
     * Get all the championships.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ChampionshipDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Championships");
        return championshipRepository.findAll(pageable)
            .map(championshipMapper::toDto);
    }


    /**
     * Get one championship by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ChampionshipDTO> findOne(Long id) {
        log.debug("Request to get Championship : {}", id);
        return championshipRepository.findById(id)
            .map(championshipMapper::toDto);
    }

    /**
     * Delete the championship by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Championship : {}", id);
        championshipRepository.deleteById(id);
    }
}
