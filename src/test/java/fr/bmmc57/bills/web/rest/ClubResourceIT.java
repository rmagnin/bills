package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.BillsApp;
import fr.bmmc57.bills.domain.Club;
import fr.bmmc57.bills.repository.ClubRepository;
import fr.bmmc57.bills.service.ClubService;
import fr.bmmc57.bills.service.dto.ClubDTO;
import fr.bmmc57.bills.service.mapper.ClubMapper;
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
 * Integration tests for the {@link ClubResource} REST controller.
 */
@SpringBootTest(classes = BillsApp.class)
public class ClubResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_DEFAULT_CHAMPIONSHIP_NAME = "AAAAAAAAAA";
    private static final String UPDATED_DEFAULT_CHAMPIONSHIP_NAME = "BBBBBBBBBB";

    private static final Double DEFAULT_DEFAULT_ONE_EVENT_PRICE = 1D;
    private static final Double UPDATED_DEFAULT_ONE_EVENT_PRICE = 2D;

    private static final Double DEFAULT_DEFAULT_TWO_EVENTS_PRICE = 1D;
    private static final Double UPDATED_DEFAULT_TWO_EVENTS_PRICE = 2D;

    private static final Double DEFAULT_DEFAULT_THREE_EVENTS_PRICE = 1D;
    private static final Double UPDATED_DEFAULT_THREE_EVENTS_PRICE = 2D;

    @Autowired
    private ClubRepository clubRepository;

    @Autowired
    private ClubMapper clubMapper;

    @Autowired
    private ClubService clubService;

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

    private MockMvc restClubMockMvc;

    private Club club;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final ClubResource clubResource = new ClubResource(clubService);
        this.restClubMockMvc = MockMvcBuilders.standaloneSetup(clubResource)
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
    public static Club createEntity(EntityManager em) {
        Club club = new Club()
            .name(DEFAULT_NAME)
            .defaultChampionshipName(DEFAULT_DEFAULT_CHAMPIONSHIP_NAME)
            .defaultOneEventPrice(DEFAULT_DEFAULT_ONE_EVENT_PRICE)
            .defaultTwoEventsPrice(DEFAULT_DEFAULT_TWO_EVENTS_PRICE)
            .defaultThreeEventsPrice(DEFAULT_DEFAULT_THREE_EVENTS_PRICE);
        return club;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Club createUpdatedEntity(EntityManager em) {
        Club club = new Club()
            .name(UPDATED_NAME)
            .defaultChampionshipName(UPDATED_DEFAULT_CHAMPIONSHIP_NAME)
            .defaultOneEventPrice(UPDATED_DEFAULT_ONE_EVENT_PRICE)
            .defaultTwoEventsPrice(UPDATED_DEFAULT_TWO_EVENTS_PRICE)
            .defaultThreeEventsPrice(UPDATED_DEFAULT_THREE_EVENTS_PRICE);
        return club;
    }

    @BeforeEach
    public void initTest() {
        club = createEntity(em);
    }

    @Test
    @Transactional
    public void createClub() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);
        restClubMockMvc.perform(post("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isCreated());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate + 1);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testClub.getDefaultChampionshipName()).isEqualTo(DEFAULT_DEFAULT_CHAMPIONSHIP_NAME);
        assertThat(testClub.getDefaultOneEventPrice()).isEqualTo(DEFAULT_DEFAULT_ONE_EVENT_PRICE);
        assertThat(testClub.getDefaultTwoEventsPrice()).isEqualTo(DEFAULT_DEFAULT_TWO_EVENTS_PRICE);
        assertThat(testClub.getDefaultThreeEventsPrice()).isEqualTo(DEFAULT_DEFAULT_THREE_EVENTS_PRICE);
    }

    @Test
    @Transactional
    public void createClubWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = clubRepository.findAll().size();

        // Create the Club with an existing ID
        club.setId(1L);
        ClubDTO clubDTO = clubMapper.toDto(club);

        // An entity with an existing ID cannot be created, so this API call must fail
        restClubMockMvc.perform(post("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = clubRepository.findAll().size();
        // set the field null
        club.setName(null);

        // Create the Club, which fails.
        ClubDTO clubDTO = clubMapper.toDto(club);

        restClubMockMvc.perform(post("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllClubs() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get all the clubList
        restClubMockMvc.perform(get("/api/clubs?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(club.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].defaultChampionshipName").value(hasItem(DEFAULT_DEFAULT_CHAMPIONSHIP_NAME)))
            .andExpect(jsonPath("$.[*].defaultOneEventPrice").value(hasItem(DEFAULT_DEFAULT_ONE_EVENT_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].defaultTwoEventsPrice").value(hasItem(DEFAULT_DEFAULT_TWO_EVENTS_PRICE.doubleValue())))
            .andExpect(jsonPath("$.[*].defaultThreeEventsPrice").value(hasItem(DEFAULT_DEFAULT_THREE_EVENTS_PRICE.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", club.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(club.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.defaultChampionshipName").value(DEFAULT_DEFAULT_CHAMPIONSHIP_NAME))
            .andExpect(jsonPath("$.defaultOneEventPrice").value(DEFAULT_DEFAULT_ONE_EVENT_PRICE.doubleValue()))
            .andExpect(jsonPath("$.defaultTwoEventsPrice").value(DEFAULT_DEFAULT_TWO_EVENTS_PRICE.doubleValue()))
            .andExpect(jsonPath("$.defaultThreeEventsPrice").value(DEFAULT_DEFAULT_THREE_EVENTS_PRICE.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingClub() throws Exception {
        // Get the club
        restClubMockMvc.perform(get("/api/clubs/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Update the club
        Club updatedClub = clubRepository.findById(club.getId()).get();
        // Disconnect from session so that the updates on updatedClub are not directly saved in db
        em.detach(updatedClub);
        updatedClub
            .name(UPDATED_NAME)
            .defaultChampionshipName(UPDATED_DEFAULT_CHAMPIONSHIP_NAME)
            .defaultOneEventPrice(UPDATED_DEFAULT_ONE_EVENT_PRICE)
            .defaultTwoEventsPrice(UPDATED_DEFAULT_TWO_EVENTS_PRICE)
            .defaultThreeEventsPrice(UPDATED_DEFAULT_THREE_EVENTS_PRICE);
        ClubDTO clubDTO = clubMapper.toDto(updatedClub);

        restClubMockMvc.perform(put("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isOk());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
        Club testClub = clubList.get(clubList.size() - 1);
        assertThat(testClub.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testClub.getDefaultChampionshipName()).isEqualTo(UPDATED_DEFAULT_CHAMPIONSHIP_NAME);
        assertThat(testClub.getDefaultOneEventPrice()).isEqualTo(UPDATED_DEFAULT_ONE_EVENT_PRICE);
        assertThat(testClub.getDefaultTwoEventsPrice()).isEqualTo(UPDATED_DEFAULT_TWO_EVENTS_PRICE);
        assertThat(testClub.getDefaultThreeEventsPrice()).isEqualTo(UPDATED_DEFAULT_THREE_EVENTS_PRICE);
    }

    @Test
    @Transactional
    public void updateNonExistingClub() throws Exception {
        int databaseSizeBeforeUpdate = clubRepository.findAll().size();

        // Create the Club
        ClubDTO clubDTO = clubMapper.toDto(club);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restClubMockMvc.perform(put("/api/clubs")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(clubDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Club in the database
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteClub() throws Exception {
        // Initialize the database
        clubRepository.saveAndFlush(club);

        int databaseSizeBeforeDelete = clubRepository.findAll().size();

        // Delete the club
        restClubMockMvc.perform(delete("/api/clubs/{id}", club.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Club> clubList = clubRepository.findAll();
        assertThat(clubList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Club.class);
        Club club1 = new Club();
        club1.setId(1L);
        Club club2 = new Club();
        club2.setId(club1.getId());
        assertThat(club1).isEqualTo(club2);
        club2.setId(2L);
        assertThat(club1).isNotEqualTo(club2);
        club1.setId(null);
        assertThat(club1).isNotEqualTo(club2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ClubDTO.class);
        ClubDTO clubDTO1 = new ClubDTO();
        clubDTO1.setId(1L);
        ClubDTO clubDTO2 = new ClubDTO();
        assertThat(clubDTO1).isNotEqualTo(clubDTO2);
        clubDTO2.setId(clubDTO1.getId());
        assertThat(clubDTO1).isEqualTo(clubDTO2);
        clubDTO2.setId(2L);
        assertThat(clubDTO1).isNotEqualTo(clubDTO2);
        clubDTO1.setId(null);
        assertThat(clubDTO1).isNotEqualTo(clubDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(clubMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(clubMapper.fromId(null)).isNull();
    }
}
