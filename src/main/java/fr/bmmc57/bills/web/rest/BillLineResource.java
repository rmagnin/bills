package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.service.BillLineService;
import fr.bmmc57.bills.web.rest.errors.BadRequestAlertException;
import fr.bmmc57.bills.service.dto.BillLineDTO;

import io.github.jhipster.web.util.HeaderUtil;
import io.github.jhipster.web.util.PaginationUtil;
import io.github.jhipster.web.util.ResponseUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.bmmc57.bills.domain.BillLine}.
 */
@RestController
@RequestMapping("/api")
public class BillLineResource {

    private final Logger log = LoggerFactory.getLogger(BillLineResource.class);

    private static final String ENTITY_NAME = "billLine";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final BillLineService billLineService;

    public BillLineResource(BillLineService billLineService) {
        this.billLineService = billLineService;
    }

    /**
     * {@code POST  /bill-lines} : Create a new billLine.
     *
     * @param billLineDTO the billLineDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new billLineDTO, or with status {@code 400 (Bad Request)} if the billLine has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/bill-lines")
    public ResponseEntity<BillLineDTO> createBillLine(@RequestBody BillLineDTO billLineDTO) throws URISyntaxException {
        log.debug("REST request to save BillLine : {}", billLineDTO);
        if (billLineDTO.getId() != null) {
            throw new BadRequestAlertException("A new billLine cannot already have an ID", ENTITY_NAME, "idexists");
        }
        BillLineDTO result = billLineService.save(billLineDTO);
        return ResponseEntity.created(new URI("/api/bill-lines/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /bill-lines} : Updates an existing billLine.
     *
     * @param billLineDTO the billLineDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated billLineDTO,
     * or with status {@code 400 (Bad Request)} if the billLineDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the billLineDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/bill-lines")
    public ResponseEntity<BillLineDTO> updateBillLine(@RequestBody BillLineDTO billLineDTO) throws URISyntaxException {
        log.debug("REST request to update BillLine : {}", billLineDTO);
        if (billLineDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        BillLineDTO result = billLineService.save(billLineDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, billLineDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /bill-lines} : get all the billLines.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of billLines in body.
     */
    @GetMapping("/bill-lines")
    public ResponseEntity<List<BillLineDTO>> getAllBillLines(Pageable pageable) {
        log.debug("REST request to get a page of BillLines");
        Page<BillLineDTO> page = billLineService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /bill-lines/:id} : get the "id" billLine.
     *
     * @param id the id of the billLineDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the billLineDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/bill-lines/{id}")
    public ResponseEntity<BillLineDTO> getBillLine(@PathVariable Long id) {
        log.debug("REST request to get BillLine : {}", id);
        Optional<BillLineDTO> billLineDTO = billLineService.findOne(id);
        return ResponseUtil.wrapOrNotFound(billLineDTO);
    }

    /**
     * {@code DELETE  /bill-lines/:id} : delete the "id" billLine.
     *
     * @param id the id of the billLineDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/bill-lines/{id}")
    public ResponseEntity<Void> deleteBillLine(@PathVariable Long id) {
        log.debug("REST request to delete BillLine : {}", id);
        billLineService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
