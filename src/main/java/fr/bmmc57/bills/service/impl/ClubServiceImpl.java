package fr.bmmc57.bills.service.impl;

import fr.bmmc57.bills.service.ClubService;
import fr.bmmc57.bills.domain.Club;
import fr.bmmc57.bills.repository.ClubRepository;
import fr.bmmc57.bills.service.dto.ClubDTO;
import fr.bmmc57.bills.service.mapper.ClubMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Club}.
 */
@Service
@Transactional
public class ClubServiceImpl implements ClubService {

    private final Logger log = LoggerFactory.getLogger(ClubServiceImpl.class);

    private final ClubRepository clubRepository;

    private final ClubMapper clubMapper;

    public ClubServiceImpl(ClubRepository clubRepository, ClubMapper clubMapper) {
        this.clubRepository = clubRepository;
        this.clubMapper = clubMapper;
    }

    /**
     * Save a club.
     *
     * @param clubDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public ClubDTO save(ClubDTO clubDTO) {
        log.debug("Request to save Club : {}", clubDTO);
        Club club = clubMapper.toEntity(clubDTO);
        club = clubRepository.save(club);
        return clubMapper.toDto(club);
    }

    /**
     * Get all the clubs.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<ClubDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Clubs");
        return clubRepository.findAll(pageable)
            .map(clubMapper::toDto);
    }


    /**
     * Get one club by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<ClubDTO> findOne(Long id) {
        log.debug("Request to get Club : {}", id);
        return clubRepository.findById(id)
            .map(clubMapper::toDto);
    }

    /**
     * Delete the club by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Club : {}", id);
        clubRepository.deleteById(id);
    }
}
