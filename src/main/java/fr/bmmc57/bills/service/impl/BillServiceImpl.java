package fr.bmmc57.bills.service.impl;

import fr.bmmc57.bills.service.BillService;
import fr.bmmc57.bills.domain.Bill;
import fr.bmmc57.bills.repository.BillRepository;
import fr.bmmc57.bills.service.dto.BillDTO;
import fr.bmmc57.bills.service.mapper.BillMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Service Implementation for managing {@link Bill}.
 */
@Service
@Transactional
public class BillServiceImpl implements BillService {

    private final Logger log = LoggerFactory.getLogger(BillServiceImpl.class);

    private final BillRepository billRepository;

    private final BillMapper billMapper;

    public BillServiceImpl(BillRepository billRepository, BillMapper billMapper) {
        this.billRepository = billRepository;
        this.billMapper = billMapper;
    }

    /**
     * Save a bill.
     *
     * @param billDTO the entity to save.
     * @return the persisted entity.
     */
    @Override
    public BillDTO save(BillDTO billDTO) {
        log.debug("Request to save Bill : {}", billDTO);
        Bill bill = billMapper.toEntity(billDTO);
        bill = billRepository.save(bill);
        return billMapper.toDto(bill);
    }

    /**
     * Get all the bills.
     *
     * @param pageable the pagination information.
     * @return the list of entities.
     */
    @Override
    @Transactional(readOnly = true)
    public Page<BillDTO> findAll(Pageable pageable) {
        log.debug("Request to get all Bills");
        return billRepository.findAll(pageable)
            .map(billMapper::toDto);
    }


    /**
     * Get one bill by id.
     *
     * @param id the id of the entity.
     * @return the entity.
     */
    @Override
    @Transactional(readOnly = true)
    public Optional<BillDTO> findOne(Long id) {
        log.debug("Request to get Bill : {}", id);
        return billRepository.findById(id)
            .map(billMapper::toDto);
    }

    /**
     * Delete the bill by id.
     *
     * @param id the id of the entity.
     */
    @Override
    public void delete(Long id) {
        log.debug("Request to delete Bill : {}", id);
        billRepository.deleteById(id);
    }
}
