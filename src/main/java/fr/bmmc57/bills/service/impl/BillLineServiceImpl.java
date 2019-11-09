package fr.bmmc57.bills.service.impl;

import fr.bmmc57.bills.service.BillLineService;
import fr.bmmc57.bills.domain.BillLine;
import fr.bmmc57.bills.repository.BillLineRepository;
import fr.bmmc57.bills.service.dto.BillLineDTO;
import fr.bmmc57.bills.service.mapper.BillLineMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link BillLine}.
 */
@Service
@Transactional
public class BillLineServiceImpl implements BillLineService {

    private final Logger log = LoggerFactory.getLogger(BillLineServiceImpl.class);

    private final BillLineRepository billLineRepository;

    private final BillLineMapper billLineMapper;

    public BillLineServiceImpl(BillLineRepository billLineRepository, BillLineMapper billLineMapper) {
        this.billLineRepository = billLineRepository;
        this.billLineMapper = billLineMapper;
    }

    /**
     * Save a billLine.
     *
     * @param billLineDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BillLineDTO save(BillLineDTO billLineDTO) {
        log.debug("Request to save BillLine : {}", billLineDTO);
        BillLine billLine = billLineMapper.toEntity(billLineDTO);
        billLine = billLineRepository.save(billLine);
        return billLineMapper.toDto(billLine);
    }

    /**
     * Get all the billLines.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BillLineDTO> findAll(Pageable pageable) {
        log.debug("Request to get all BillLines");
        return billLineRepository.findAll(pageable)
            .map(billLineMapper::toDto);
    }


    /**
     * Get one billLine by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BillLineDTO> findOne(Long id) {
        log.debug("Request to get BillLine : {}", id);
        return billLineRepository.findById(id)
            .map(billLineMapper::toDto);
    }

    /**
     * Delete the billLine by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete BillLine : {}", id);
        billLineRepository.deleteById(id);
    }
}
