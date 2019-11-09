package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.BillsApp;
import fr.bmmc57.bills.domain.Championship;
import fr.bmmc57.bills.repository.ChampionshipRepository;
import fr.bmmc57.bills.service.ChampionshipService;
import fr.bmmc57.bills.service.dto.ChampionshipDTO;
import fr.bmmc57.bills.service.mapper.ChampionshipMapper;
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
 * Integration tests for the {@link ChampionshipResource} REST controller.
 */
@SpringBootTest(classes = BillsApp.class)
public class ChampionshipResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final Integer DEFAULT_YEAR = 1;
    private static final Integer UPDATED_YEAR = 2;

    private static final Double DEFAULT_ONE_EVENT_PRICE = 1D;
    private static final Double UPDATED_ONE_EVENT_PRICE = 2D;

    private static final Double DEFAULT_TWO_EVENTS_PRICE = 1D;
    private static final Double UPDATED_TWO_EVENTS_PRICE = 2D;

    private static final Double DEFAULT_THREE_EVENTS_PRICE = 1D;
    private static final Double UPDATED_THREE_EVENTS_PRICE = 2D;

    @Autowired
    private ChampionshipRepository championshipRepository;

    @Autowired
    private ChampionshipMapper championshipMapper;

    @Autowired
    private ChampionshipService championshipService;

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

    private MockMvc restChampionshipMockMvc;

    private Championship championship;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ChampionshipResource championshipResource = new ChampionshipResource(championshipService);
        this.restChampionshipMockMvc = MockMvcBuilders.standaloneSetup(championshipResource)
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
    public static Championship createEntity(EntityManager em) {
        Championship championship = new Championship()
            .name(DEFAULT_NAME)
            .year(DEFAULT_YEAR)
            .oneEventPrice(DEFAULT_ONE_EVENT_PRICE)
            .twoEventsPrice(DEFAULT_TWO_EVENTS_PRICE)
            .threeEventsPrice(DEFAULT_THREE_EVENTS_PRICE);
        return championship;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Championship createUpdatedEntity(EntityManager em) {
        Championship championship = new Championship()
            .name(UPDATED_NAME)
            .year(UPDATED_YEAR)
            .oneEventPrice(UPDATED_ONE_EVENT_PRICE)
            .twoEventsPrice(UPDATED_TWO_EVENTS_PRICE)
            .threeEventsPrice(UPDATED_THREE_EVENTS_PRICE);
        return championship;
    }

    @BeforeEach
    public void initTest() {
        championship = createEntity(em);
    }

    @Test
    @Transactional
    public void createChampionship() throws Exception {
        int databaseSizeBeforeCreate = championshipRepository.findAll().size();

        // Create the Championship
        ChampionshipDTO championshipDTO = championshipMapper.toDto(championship);
        restChampionshipMockMvc.perform(post("/api/championships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(championshipDTO)))
            .andExpect(status().isCreated());

        // Validate the Championship in the database
        List<Championship> championshipList = championshipRepository.findAll();
        assertThat(championshipList).hasSize(databaseSizeBeforeCreate + 1);
        Championship testChampionship = championshipList.get(championshipList.size() - 1);
        assertThat(testChampionship.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testChampionship.getYear()).isEqualTo(DEFAULT_YEAR);
        assertThat(testChampionship.getOneEventPrice()).isEqualTo(DEFAULT_ONE_EVENT_PRICE);
        assertThat(testChampionship.getTwoEventsPrice()).isEqualTo(DEFAULT_TWO_EVENTS_PRICE);
        assertThat(testChampionship.getThreeEventsPrice()).isEqualTo(DEFAULT_THREE_EVENTS_PRICE);
    }

    @Test
    @Transactional
    public void createChampionshipWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = championshipRepository.findAll().size();

        // Create the Championship with an existing ID
        championship.setId(1L);
        ChampionshipDTO championshipDTO = championshipMapper.toDto(championship);

        // An entity with an existing ID cannot be created, so this API call must fail
        restChampionshipMockMvc.perform(post("/api/championships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(championshipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Championship in the database
        List<Championship> championshipList = championshipRepository.findAll();
        assertThat(championshipList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = championshipRepository.findAll().size();
        // set the field null
        championship.setName(null);

        // Create the Championship, which fails.
        ChampionshipDTO championshipDTO = championshipMapper.toDto(championship);

        restChampionshipMockMvc.perform(post("/api/championships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(championshipDTO)))
            .andExpect(status().isBadRequest());

        List<Championship> championshipList = championshipRepository.findAll();
        assertThat(championshipList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllChampionships() throws Exception {
        // Initialize the database
        championshipRepository.saveAndFlush(championship);

        // Get all the championshipList
        restChampionshipMockMvc.perform(get("/api/championships?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(championship.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].year").value(hasItem(DEFAULT_YEAR)))
            .andExpect(jsonPath("$.[*].oneEventPrice").value(hasItem(DEFAULT_ONE_EVENT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].twoEventsPrice").value(hasItem(DEFAULT_TWO_EVENTS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].threeEventsPrice").value(hasItem(DEFAULT_THREE_EVENTS_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getChampionship() throws Exception {
        // Initialize the database
        championshipRepository.saveAndFlush(championship);

        // Get the championship
        restChampionshipMockMvc.perform(get("/api/championships/{id}", championship.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(championship.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.year").value(DEFAULT_YEAR))
            .andExpect(jsonPath("$.oneEventPrice").value(DEFAULT_ONE_EVENT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.twoEventsPrice").value(DEFAULT_TWO_EVENTS_PRICE.doubleValue()))
            .andExpect(jsonPath("$.threeEventsPrice").value(DEFAULT_THREE_EVENTS_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingChampionship() throws Exception {
        // Get the championship
        restChampionshipMockMvc.perform(get("/api/championships/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateChampionship() throws Exception {
        // Initialize the database
        championshipRepository.saveAndFlush(championship);

        int databaseSizeBeforeUpdate = championshipRepository.findAll().size();

        // Update the championship
        Championship updatedChampionship = championshipRepository.findById(championship.getId()).get();
        // Disconnect from session so that the updates on updatedChampionship are not directly saved in db
        em.detach(updatedChampionship);
        updatedChampionship
            .name(UPDATED_NAME)
            .year(UPDATED_YEAR)
            .oneEventPrice(UPDATED_ONE_EVENT_PRICE)
            .twoEventsPrice(UPDATED_TWO_EVENTS_PRICE)
            .threeEventsPrice(UPDATED_THREE_EVENTS_PRICE);
        ChampionshipDTO championshipDTO = championshipMapper.toDto(updatedChampionship);

        restChampionshipMockMvc.perform(put("/api/championships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(championshipDTO)))
            .andExpect(status().isOk());

        // Validate the Championship in the database
        List<Championship> championshipList = championshipRepository.findAll();
        assertThat(championshipList).hasSize(databaseSizeBeforeUpdate);
        Championship testChampionship = championshipList.get(championshipList.size() - 1);
        assertThat(testChampionship.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testChampionship.getYear()).isEqualTo(UPDATED_YEAR);
        assertThat(testChampionship.getOneEventPrice()).isEqualTo(UPDATED_ONE_EVENT_PRICE);
        assertThat(testChampionship.getTwoEventsPrice()).isEqualTo(UPDATED_TWO_EVENTS_PRICE);
        assertThat(testChampionship.getThreeEventsPrice()).isEqualTo(UPDATED_THREE_EVENTS_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingChampionship() throws Exception {
        int databaseSizeBeforeUpdate = championshipRepository.findAll().size();

        // Create the Championship
        ChampionshipDTO championshipDTO = championshipMapper.toDto(championship);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restChampionshipMockMvc.perform(put("/api/championships")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(championshipDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Championship in the database
        List<Championship> championshipList = championshipRepository.findAll();
        assertThat(championshipList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteChampionship() throws Exception {
        // Initialize the database
        championshipRepository.saveAndFlush(championship);

        int databaseSizeBeforeDelete = championshipRepository.findAll().size();

        // Delete the championship
        restChampionshipMockMvc.perform(delete("/api/championships/{id}", championship.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Championship> championshipList = championshipRepository.findAll();
        assertThat(championshipList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Championship.class);
        Championship championship1 = new Championship();
        championship1.setId(1L);
        Championship championship2 = new Championship();
        championship2.setId(championship1.getId());
        assertThat(championship1).isEqualTo(championship2);
        championship2.setId(2L);
        assertThat(championship1).isNotEqualTo(championship2);
        championship1.setId(null);
        assertThat(championship1).isNotEqualTo(championship2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ChampionshipDTO.class);
        ChampionshipDTO championshipDTO1 = new ChampionshipDTO();
        championshipDTO1.setId(1L);
        ChampionshipDTO championshipDTO2 = new ChampionshipDTO();
        assertThat(championshipDTO1).isNotEqualTo(championshipDTO2);
        championshipDTO2.setId(championshipDTO1.getId());
        assertThat(championshipDTO1).isEqualTo(championshipDTO2);
        championshipDTO2.setId(2L);
        assertThat(championshipDTO1).isNotEqualTo(championshipDTO2);
        championshipDTO1.setId(null);
        assertThat(championshipDTO1).isNotEqualTo(championshipDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(championshipMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(championshipMapper.fromId(null)).isNull();
    }
}
