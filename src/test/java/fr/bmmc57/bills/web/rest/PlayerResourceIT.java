package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.BillsApp;
import fr.bmmc57.bills.domain.Player;
import fr.bmmc57.bills.repository.PlayerRepository;
import fr.bmmc57.bills.service.PlayerService;
import fr.bmmc57.bills.service.dto.PlayerDTO;
import fr.bmmc57.bills.service.mapper.PlayerMapper;
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
 * Integration tests for the {@link PlayerResource} REST controller.
 */
@SpringBootTest(classes = BillsApp.class)
public class PlayerResourceIT {

    private static final String DEFAULT_FIRST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_FIRST_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_LAST_NAME = "AAAAAAAAAA";
    private static final String UPDATED_LAST_NAME = "BBBBBBBBBB";

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerMapper playerMapper;

    @Autowired
    private PlayerService playerService;

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

    private MockMvc restPlayerMockMvc;

    private Player player;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PlayerResource playerResource = new PlayerResource(playerService);
        this.restPlayerMockMvc = MockMvcBuilders.standaloneSetup(playerResource)
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
    public static Player createEntity(EntityManager em) {
        Player player = new Player()
            .firstName(DEFAULT_FIRST_NAME)
            .lastName(DEFAULT_LAST_NAME);
        return player;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Player createUpdatedEntity(EntityManager em) {
        Player player = new Player()
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        return player;
    }

    @BeforeEach
    public void initTest() {
        player = createEntity(em);
    }

    @Test
    @Transactional
    public void createPlayer() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player
        PlayerDTO playerDTO = playerMapper.toDto(player);
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isCreated());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate + 1);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getFirstName()).isEqualTo(DEFAULT_FIRST_NAME);
        assertThat(testPlayer.getLastName()).isEqualTo(DEFAULT_LAST_NAME);
    }

    @Test
    @Transactional
    public void createPlayerWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = playerRepository.findAll().size();

        // Create the Player with an existing ID
        player.setId(1L);
        PlayerDTO playerDTO = playerMapper.toDto(player);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkFirstNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setFirstName(null);

        // Create the Player, which fails.
        PlayerDTO playerDTO = playerMapper.toDto(player);

        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void checkLastNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = playerRepository.findAll().size();
        // set the field null
        player.setLastName(null);

        // Create the Player, which fails.
        PlayerDTO playerDTO = playerMapper.toDto(player);

        restPlayerMockMvc.perform(post("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllPlayers() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get all the playerList
        restPlayerMockMvc.perform(get("/api/players?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(player.getId().intValue())))
            .andExpect(jsonPath("$.[*].firstName").value(hasItem(DEFAULT_FIRST_NAME)))
            .andExpect(jsonPath("$.[*].lastName").value(hasItem(DEFAULT_LAST_NAME)));
    }
    
    @Test
    @Transactional
    public void getPlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", player.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(player.getId().intValue()))
            .andExpect(jsonPath("$.firstName").value(DEFAULT_FIRST_NAME))
            .andExpect(jsonPath("$.lastName").value(DEFAULT_LAST_NAME));
    }

    @Test
    @Transactional
    public void getNonExistingPlayer() throws Exception {
        // Get the player
        restPlayerMockMvc.perform(get("/api/players/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Update the player
        Player updatedPlayer = playerRepository.findById(player.getId()).get();
        // Disconnect from session so that the updates on updatedPlayer are not directly saved in db
        em.detach(updatedPlayer);
        updatedPlayer
            .firstName(UPDATED_FIRST_NAME)
            .lastName(UPDATED_LAST_NAME);
        PlayerDTO playerDTO = playerMapper.toDto(updatedPlayer);

        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isOk());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
        Player testPlayer = playerList.get(playerList.size() - 1);
        assertThat(testPlayer.getFirstName()).isEqualTo(UPDATED_FIRST_NAME);
        assertThat(testPlayer.getLastName()).isEqualTo(UPDATED_LAST_NAME);
    }

    @Test
    @Transactional
    public void updateNonExistingPlayer() throws Exception {
        int databaseSizeBeforeUpdate = playerRepository.findAll().size();

        // Create the Player
        PlayerDTO playerDTO = playerMapper.toDto(player);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPlayerMockMvc.perform(put("/api/players")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(playerDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Player in the database
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePlayer() throws Exception {
        // Initialize the database
        playerRepository.saveAndFlush(player);

        int databaseSizeBeforeDelete = playerRepository.findAll().size();

        // Delete the player
        restPlayerMockMvc.perform(delete("/api/players/{id}", player.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Player> playerList = playerRepository.findAll();
        assertThat(playerList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Player.class);
        Player player1 = new Player();
        player1.setId(1L);
        Player player2 = new Player();
        player2.setId(player1.getId());
        assertThat(player1).isEqualTo(player2);
        player2.setId(2L);
        assertThat(player1).isNotEqualTo(player2);
        player1.setId(null);
        assertThat(player1).isNotEqualTo(player2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PlayerDTO.class);
        PlayerDTO playerDTO1 = new PlayerDTO();
        playerDTO1.setId(1L);
        PlayerDTO playerDTO2 = new PlayerDTO();
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
        playerDTO2.setId(playerDTO1.getId());
        assertThat(playerDTO1).isEqualTo(playerDTO2);
        playerDTO2.setId(2L);
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
        playerDTO1.setId(null);
        assertThat(playerDTO1).isNotEqualTo(playerDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(playerMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(playerMapper.fromId(null)).isNull();
    }
}
