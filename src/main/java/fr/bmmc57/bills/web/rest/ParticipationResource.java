package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.service.ParticipationService;
import fr.bmmc57.bills.web.rest.errors.BadRequestAlertException;
import fr.bmmc57.bills.service.dto.ParticipationDTO;

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
 * REST controller for managing {@link fr.bmmc57.bills.domain.Participation}.
 */
@RestController
@RequestMapping("/api")
public class ParticipationResource {

    private final Logger log = LoggerFactory.getLogger(ParticipationResource.class);

    private static final String ENTITY_NAME = "participation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ParticipationService participationService;

    public ParticipationResource(ParticipationService participationService) {
        this.participationService = participationService;
    }

    /**
     * {@code POST  /participations} : Create a new participation.
     *
     * @param participationDTO the participationDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new participationDTO, or with status {@code 400 (Bad Request)} if the participation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/participations")
    public ResponseEntity<ParticipationDTO> createParticipation(@RequestBody ParticipationDTO participationDTO) throws URISyntaxException {
        log.debug("REST request to save Participation : {}", participationDTO);
        if (participationDTO.getId() != null) {
            throw new BadRequestAlertException("A new participation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ParticipationDTO result = participationService.save(participationDTO);
        return ResponseEntity.created(new URI("/api/participations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /participations} : Updates an existing participation.
     *
     * @param participationDTO the participationDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated participationDTO,
     * or with status {@code 400 (Bad Request)} if the participationDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the participationDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/participations")
    public ResponseEntity<ParticipationDTO> updateParticipation(@RequestBody ParticipationDTO participationDTO) throws URISyntaxException {
        log.debug("REST request to update Participation : {}", participationDTO);
        if (participationDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ParticipationDTO result = participationService.save(participationDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, participationDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /participations} : get all the participations.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of participations in body.
     */
    @GetMapping("/participations")
    public ResponseEntity<List<ParticipationDTO>> getAllParticipations(Pageable pageable) {
        log.debug("REST request to get a page of Participations");
        Page<ParticipationDTO> page = participationService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /participations/:id} : get the "id" participation.
     *
     * @param id the id of the participationDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the participationDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/participations/{id}")
    public ResponseEntity<ParticipationDTO> getParticipation(@PathVariable Long id) {
        log.debug("REST request to get Participation : {}", id);
        Optional<ParticipationDTO> participationDTO = participationService.findOne(id);
        return ResponseUtil.wrapOrNotFound(participationDTO);
    }

    /**
     * {@code DELETE  /participations/:id} : delete the "id" participation.
     *
     * @param id the id of the participationDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/participations/{id}")
    public ResponseEntity<Void> deleteParticipation(@PathVariable Long id) {
        log.debug("REST request to delete Participation : {}", id);
        participationService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
