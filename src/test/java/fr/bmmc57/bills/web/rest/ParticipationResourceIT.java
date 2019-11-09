package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.BillsApp;
import fr.bmmc57.bills.domain.Participation;
import fr.bmmc57.bills.repository.ParticipationRepository;
import fr.bmmc57.bills.service.ParticipationService;
import fr.bmmc57.bills.service.dto.ParticipationDTO;
import fr.bmmc57.bills.service.mapper.ParticipationMapper;
import fr.bmmc57.bills.web.rest.errors.ExceptionTranslator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
import java.util.List;

import static fr.bmmc57.bills.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for the {@link ParticipationResource} REST controller.
 */
@SpringBootTest(classes = BillsApp.class)
public class ParticipationResourceIT {

    private static final Boolean DEFAULT_SINGLE_EVENT = false;
    private static final Boolean UPDATED_SINGLE_EVENT = true;

    private static final Boolean DEFAULT_DOUBLE_EVENT = false;
    private static final Boolean UPDATED_DOUBLE_EVENT = true;

    private static final Boolean DEFAULT_MIXED_EVENT = false;
    private static final Boolean UPDATED_MIXED_EVENT = true;

    @Autowired
    private ParticipationRepository participationRepository;

    @Autowired
    private ParticipationMapper participationMapper;

    @Autowired
    private ParticipationService participationService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

    private MockMvc restParticipationMockMvc;

    private Participation participation;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ParticipationResource participationResource = new ParticipationResource(participationService);
        this.restParticipationMockMvc = MockMvcBuilders.standaloneSetup(participationResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participation createEntity(EntityManager em) {
        Participation participation = new Participation()
            .singleEvent(DEFAULT_SINGLE_EVENT)
            .doubleEvent(DEFAULT_DOUBLE_EVENT)
            .mixedEvent(DEFAULT_MIXED_EVENT);
        return participation;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Participation createUpdatedEntity(EntityManager em) {
        Participation participation = new Participation()
            .singleEvent(UPDATED_SINGLE_EVENT)
            .doubleEvent(UPDATED_DOUBLE_EVENT)
            .mixedEvent(UPDATED_MIXED_EVENT);
        return participation;
    }

    @BeforeEach
    public void initTest() {
        participation = createEntity(em);
    }

    @Test
    @Transactional
    public void createParticipation() throws Exception {
        int databaseSizeBeforeCreate = participationRepository.findAll().size();

        // Create the Participation
        ParticipationDTO participationDTO = participationMapper.toDto(participation);
        restParticipationMockMvc.perform(post("/api/participations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participationDTO)))
            .andExpect(status().isCreated());

        // Validate the Participation in the database
        List<Participation> participationList = participationRepository.findAll();
        assertThat(participationList).hasSize(databaseSizeBeforeCreate + 1);
        Participation testParticipation = participationList.get(participationList.size() - 1);
        assertThat(testParticipation.isSingleEvent()).isEqualTo(DEFAULT_SINGLE_EVENT);
        assertThat(testParticipation.isDoubleEvent()).isEqualTo(DEFAULT_DOUBLE_EVENT);
        assertThat(testParticipation.isMixedEvent()).isEqualTo(DEFAULT_MIXED_EVENT);
    }

    @Test
    @Transactional
    public void createParticipationWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = participationRepository.findAll().size();

        // Create the Participation with an existing ID
        participation.setId(1L);
        ParticipationDTO participationDTO = participationMapper.toDto(participation);

        // An entity with an existing ID cannot be created, so this API call must fail
        restParticipationMockMvc.perform(post("/api/participations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Participation in the database
        List<Participation> participationList = participationRepository.findAll();
        assertThat(participationList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllParticipations() throws Exception {
        // Initialize the database
        participationRepository.saveAndFlush(participation);

        // Get all the participationList
        restParticipationMockMvc.perform(get("/api/participations?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(participation.getId().intValue())))
            .andExpect(jsonPath("$.[*].singleEvent").value(hasItem(DEFAULT_SINGLE_EVENT.booleanValue())))
            .andExpect(jsonPath("$.[*].doubleEvent").value(hasItem(DEFAULT_DOUBLE_EVENT.booleanValue())))
            .andExpect(jsonPath("$.[*].mixedEvent").value(hasItem(DEFAULT_MIXED_EVENT.booleanValue())));
    }
    
    @Test
    @Transactional
    public void getParticipation() throws Exception {
        // Initialize the database
        participationRepository.saveAndFlush(participation);

        // Get the participation
        restParticipationMockMvc.perform(get("/api/participations/{id}", participation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(participation.getId().intValue()))
            .andExpect(jsonPath("$.singleEvent").value(DEFAULT_SINGLE_EVENT.booleanValue()))
            .andExpect(jsonPath("$.doubleEvent").value(DEFAULT_DOUBLE_EVENT.booleanValue()))
            .andExpect(jsonPath("$.mixedEvent").value(DEFAULT_MIXED_EVENT.booleanValue()));
    }

    @Test
    @Transactional
    public void getNonExistingParticipation() throws Exception {
        // Get the participation
        restParticipationMockMvc.perform(get("/api/participations/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateParticipation() throws Exception {
        // Initialize the database
        participationRepository.saveAndFlush(participation);

        int databaseSizeBeforeUpdate = participationRepository.findAll().size();

        // Update the participation
        Participation updatedParticipation = participationRepository.findById(participation.getId()).get();
        // Disconnect from session so that the updates on updatedParticipation are not directly saved in db
        em.detach(updatedParticipation);
        updatedParticipation
            .singleEvent(UPDATED_SINGLE_EVENT)
            .doubleEvent(UPDATED_DOUBLE_EVENT)
            .mixedEvent(UPDATED_MIXED_EVENT);
        ParticipationDTO participationDTO = participationMapper.toDto(updatedParticipation);

        restParticipationMockMvc.perform(put("/api/participations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participationDTO)))
            .andExpect(status().isOk());

        // Validate the Participation in the database
        List<Participation> participationList = participationRepository.findAll();
        assertThat(participationList).hasSize(databaseSizeBeforeUpdate);
        Participation testParticipation = participationList.get(participationList.size() - 1);
        assertThat(testParticipation.isSingleEvent()).isEqualTo(UPDATED_SINGLE_EVENT);
        assertThat(testParticipation.isDoubleEvent()).isEqualTo(UPDATED_DOUBLE_EVENT);
        assertThat(testParticipation.isMixedEvent()).isEqualTo(UPDATED_MIXED_EVENT);
    }

    @Test
    @Transactional
    public void updateNonExistingParticipation() throws Exception {
        int databaseSizeBeforeUpdate = participationRepository.findAll().size();

        // Create the Participation
        ParticipationDTO participationDTO = participationMapper.toDto(participation);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restParticipationMockMvc.perform(put("/api/participations")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(participationDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Participation in the database
        List<Participation> participationList = participationRepository.findAll();
        assertThat(participationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteParticipation() throws Exception {
        // Initialize the database
        participationRepository.saveAndFlush(participation);

        int databaseSizeBeforeDelete = participationRepository.findAll().size();

        // Delete the participation
        restParticipationMockMvc.perform(delete("/api/participations/{id}", participation.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Participation> participationList = participationRepository.findAll();
        assertThat(participationList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Participation.class);
        Participation participation1 = new Participation();
        participation1.setId(1L);
        Participation participation2 = new Participation();
        participation2.setId(participation1.getId());
        assertThat(participation1).isEqualTo(participation2);
        participation2.setId(2L);
        assertThat(participation1).isNotEqualTo(participation2);
        participation1.setId(null);
        assertThat(participation1).isNotEqualTo(participation2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ParticipationDTO.class);
        ParticipationDTO participationDTO1 = new ParticipationDTO();
        participationDTO1.setId(1L);
        ParticipationDTO participationDTO2 = new ParticipationDTO();
        assertThat(participationDTO1).isNotEqualTo(participationDTO2);
        participationDTO2.setId(participationDTO1.getId());
        assertThat(participationDTO1).isEqualTo(participationDTO2);
        participationDTO2.setId(2L);
        assertThat(participationDTO1).isNotEqualTo(participationDTO2);
        participationDTO1.setId(null);
        assertThat(participationDTO1).isNotEqualTo(participationDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(participationMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(participationMapper.fromId(null)).isNull();
    }
}
