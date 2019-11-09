package fr.bmmc57.bills.service.impl;

import fr.bmmc57.bills.service.ParticipationService;
import fr.bmmc57.bills.domain.Participation;
import fr.bmmc57.bills.repository.ParticipationRepository;
import fr.bmmc57.bills.service.dto.ParticipationDTO;
import fr.bmmc57.bills.service.mapper.ParticipationMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Participation}.
 */
@Service
@Transactional
public class ParticipationServiceImpl implements ParticipationService {

    private final Logger log = LoggerFactory.getLogger(ParticipationServiceImpl.class);

    private final ParticipationRepository participationRepository;

    private final ParticipationMapper participationMapper;

    public ParticipationServiceImpl(ParticipationRepository participationRepository, ParticipationMapper participationMapper) {
        this.participationRepository = participationRepository;
        this.participationMapper = participationMapper;
    }

    /**
     * Save a participation.
     *
     * @param participationDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ParticipationDTO save(ParticipationDTO participationDTO) {
        log.debug("Request to save Participation : {}", participationDTO);
        Participation participation = participationMapper.toEntity(participationDTO);
        participation = participationRepository.save(participation);
        return participationMapper.toDto(participation);
    }

    /**
     * Get all the participations.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ParticipationDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Participations");
        return participationRepository.findAll(pageable)
            .map(participationMapper::toDto);
    }


    /**
     * Get one participation by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ParticipationDTO> findOne(Long id) {
        log.debug("Request to get Participation : {}", id);
        return participationRepository.findById(id)
            .map(participationMapper::toDto);
    }

    /**
     * Delete the participation by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Participation : {}", id);
        participationRepository.deleteById(id);
    }
}
