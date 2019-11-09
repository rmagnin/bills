package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.service.ClubService;
import fr.bmmc57.bills.web.rest.errors.BadRequestAlertException;
import fr.bmmc57.bills.service.dto.ClubDTO;

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

import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;

import java.util.List;
import java.util.Optional;

/**
 * REST controller for managing {@link fr.bmmc57.bills.domain.Club}.
 */
@RestController
@RequestMapping("/api")
public class ClubResource {

    private final Logger log = LoggerFactory.getLogger(ClubResource.class);

    private static final String ENTITY_NAME = "club";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ClubService clubService;

    public ClubResource(ClubService clubService) {
        this.clubService = clubService;
    }

    /**
     * {@code POST  /clubs} : Create a new club.
     *
     * @param clubDTO the clubDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new clubDTO, or with status {@code 400 (Bad Request)} if the club has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/clubs")
    public ResponseEntity<ClubDTO> createClub(@Valid @RequestBody ClubDTO clubDTO) throws URISyntaxException {
        log.debug("REST request to save Club : {}", clubDTO);
        if (clubDTO.getId() != null) {
            throw new BadRequestAlertException("A new club cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ClubDTO result = clubService.save(clubDTO);
        return ResponseEntity.created(new URI("/api/clubs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /clubs} : Updates an existing club.
     *
     * @param clubDTO the clubDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated clubDTO,
     * or with status {@code 400 (Bad Request)} if the clubDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the clubDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/clubs")
    public ResponseEntity<ClubDTO> updateClub(@Valid @RequestBody ClubDTO clubDTO) throws URISyntaxException {
        log.debug("REST request to update Club : {}", clubDTO);
        if (clubDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ClubDTO result = clubService.save(clubDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, clubDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /clubs} : get all the clubs.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of clubs in body.
     */
    @GetMapping("/clubs")
    public ResponseEntity<List<ClubDTO>> getAllClubs(Pageable pageable) {
        log.debug("REST request to get a page of Clubs");
        Page<ClubDTO> page = clubService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /clubs/:id} : get the "id" club.
     *
     * @param id the id of the clubDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the clubDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/clubs/{id}")
    public ResponseEntity<ClubDTO> getClub(@PathVariable Long id) {
        log.debug("REST request to get Club : {}", id);
        Optional<ClubDTO> clubDTO = clubService.findOne(id);
        return ResponseUtil.wrapOrNotFound(clubDTO);
    }

    /**
     * {@code DELETE  /clubs/:id} : delete the "id" club.
     *
     * @param id the id of the clubDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/clubs/{id}")
    public ResponseEntity<Void> deleteClub(@PathVariable Long id) {
        log.debug("REST request to delete Club : {}", id);
        clubService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
