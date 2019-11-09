package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.service.ChampionshipService;
import fr.bmmc57.bills.web.rest.errors.BadRequestAlertException;
import fr.bmmc57.bills.service.dto.ChampionshipDTO;

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
 * REST controller for managing {@link fr.bmmc57.bills.domain.Championship}.
 */
@RestController
@RequestMapping("/api")
public class ChampionshipResource {

    private final Logger log = LoggerFactory.getLogger(ChampionshipResource.class);

    private static final String ENTITY_NAME = "championship";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ChampionshipService championshipService;

    public ChampionshipResource(ChampionshipService championshipService) {
        this.championshipService = championshipService;
    }

    /**
     * {@code POST  /championships} : Create a new championship.
     *
     * @param championshipDTO the championshipDTO to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new championshipDTO, or with status {@code 400 (Bad Request)} if the championship has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/championships")
    public ResponseEntity<ChampionshipDTO> createChampionship(@Valid @RequestBody ChampionshipDTO championshipDTO) throws URISyntaxException {
        log.debug("REST request to save Championship : {}", championshipDTO);
        if (championshipDTO.getId() != null) {
            throw new BadRequestAlertException("A new championship cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ChampionshipDTO result = championshipService.save(championshipDTO);
        return ResponseEntity.created(new URI("/api/championships/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, false, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /championships} : Updates an existing championship.
     *
     * @param championshipDTO the championshipDTO to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated championshipDTO,
     * or with status {@code 400 (Bad Request)} if the championshipDTO is not valid,
     * or with status {@code 500 (Internal Server Error)} if the championshipDTO couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/championships")
    public ResponseEntity<ChampionshipDTO> updateChampionship(@Valid @RequestBody ChampionshipDTO championshipDTO) throws URISyntaxException {
        log.debug("REST request to update Championship : {}", championshipDTO);
        if (championshipDTO.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        ChampionshipDTO result = championshipService.save(championshipDTO);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, false, ENTITY_NAME, championshipDTO.getId().toString()))
            .body(result);
    }

    /**
     * {@code GET  /championships} : get all the championships.
     *

     * @param pageable the pagination information.

     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of championships in body.
     */
    @GetMapping("/championships")
    public ResponseEntity<List<ChampionshipDTO>> getAllChampionships(Pageable pageable) {
        log.debug("REST request to get a page of Championships");
        Page<ChampionshipDTO> page = championshipService.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /championships/:id} : get the "id" championship.
     *
     * @param id the id of the championshipDTO to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the championshipDTO, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/championships/{id}")
    public ResponseEntity<ChampionshipDTO> getChampionship(@PathVariable Long id) {
        log.debug("REST request to get Championship : {}", id);
        Optional<ChampionshipDTO> championshipDTO = championshipService.findOne(id);
        return ResponseUtil.wrapOrNotFound(championshipDTO);
    }

    /**
     * {@code DELETE  /championships/:id} : delete the "id" championship.
     *
     * @param id the id of the championshipDTO to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/championships/{id}")
    public ResponseEntity<Void> deleteChampionship(@PathVariable Long id) {
        log.debug("REST request to delete Championship : {}", id);
        championshipService.delete(id);
        return ResponseEntity.noContent().headers(HeaderUtil.createEntityDeletionAlert(applicationName, false, ENTITY_NAME, id.toString())).build();
    }
}
