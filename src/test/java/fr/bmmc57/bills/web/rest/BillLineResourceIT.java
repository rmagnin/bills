package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.BillsApp;
import fr.bmmc57.bills.domain.BillLine;
import fr.bmmc57.bills.repository.BillLineRepository;
import fr.bmmc57.bills.service.BillLineService;
import fr.bmmc57.bills.service.dto.BillLineDTO;
import fr.bmmc57.bills.service.mapper.BillLineMapper;
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
 * Integration tests for the {@link BillLineResource} REST controller.
 */
@SpringBootTest(classes = BillsApp.class)
public class BillLineResourceIT {

    private static final String DEFAULT_LABEL = "AAAAAAAAAA";
    private static final String UPDATED_LABEL = "BBBBBBBBBB";

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private BillLineRepository billLineRepository;

    @Autowired
    private BillLineMapper billLineMapper;

    @Autowired
    private BillLineService billLineService;

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

    private MockMvc restBillLineMockMvc;

    private BillLine billLine;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BillLineResource billLineResource = new BillLineResource(billLineService);
        this.restBillLineMockMvc = MockMvcBuilders.standaloneSetup(billLineResource)
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
    public static BillLine createEntity(EntityManager em) {
        BillLine billLine = new BillLine()
            .label(DEFAULT_LABEL)
            .amount(DEFAULT_AMOUNT);
        return billLine;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static BillLine createUpdatedEntity(EntityManager em) {
        BillLine billLine = new BillLine()
            .label(UPDATED_LABEL)
            .amount(UPDATED_AMOUNT);
        return billLine;
    }

    @BeforeEach
    public void initTest() {
        billLine = createEntity(em);
    }

    @Test
    @Transactional
    public void createBillLine() throws Exception {
        int databaseSizeBeforeCreate = billLineRepository.findAll().size();

        // Create the BillLine
        BillLineDTO billLineDTO = billLineMapper.toDto(billLine);
        restBillLineMockMvc.perform(post("/api/bill-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billLineDTO)))
            .andExpect(status().isCreated());

        // Validate the BillLine in the database
        List<BillLine> billLineList = billLineRepository.findAll();
        assertThat(billLineList).hasSize(databaseSizeBeforeCreate + 1);
        BillLine testBillLine = billLineList.get(billLineList.size() - 1);
        assertThat(testBillLine.getLabel()).isEqualTo(DEFAULT_LABEL);
        assertThat(testBillLine.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createBillLineWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = billLineRepository.findAll().size();

        // Create the BillLine with an existing ID
        billLine.setId(1L);
        BillLineDTO billLineDTO = billLineMapper.toDto(billLine);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillLineMockMvc.perform(post("/api/bill-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BillLine in the database
        List<BillLine> billLineList = billLineRepository.findAll();
        assertThat(billLineList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void checkLabelIsRequired() throws Exception {
        int databaseSizeBeforeTest = billLineRepository.findAll().size();
        // set the field null
        billLine.setLabel(null);

        // Create the BillLine, which fails.
        BillLineDTO billLineDTO = billLineMapper.toDto(billLine);

        restBillLineMockMvc.perform(post("/api/bill-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billLineDTO)))
            .andExpect(status().isBadRequest());

        List<BillLine> billLineList = billLineRepository.findAll();
        assertThat(billLineList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    public void getAllBillLines() throws Exception {
        // Initialize the database
        billLineRepository.saveAndFlush(billLine);

        // Get all the billLineList
        restBillLineMockMvc.perform(get("/api/bill-lines?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(billLine.getId().intValue())))
            .andExpect(jsonPath("$.[*].label").value(hasItem(DEFAULT_LABEL)))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getBillLine() throws Exception {
        // Initialize the database
        billLineRepository.saveAndFlush(billLine);

        // Get the billLine
        restBillLineMockMvc.perform(get("/api/bill-lines/{id}", billLine.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(billLine.getId().intValue()))
            .andExpect(jsonPath("$.label").value(DEFAULT_LABEL))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBillLine() throws Exception {
        // Get the billLine
        restBillLineMockMvc.perform(get("/api/bill-lines/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBillLine() throws Exception {
        // Initialize the database
        billLineRepository.saveAndFlush(billLine);

        int databaseSizeBeforeUpdate = billLineRepository.findAll().size();

        // Update the billLine
        BillLine updatedBillLine = billLineRepository.findById(billLine.getId()).get();
        // Disconnect from session so that the updates on updatedBillLine are not directly saved in db
        em.detach(updatedBillLine);
        updatedBillLine
            .label(UPDATED_LABEL)
            .amount(UPDATED_AMOUNT);
        BillLineDTO billLineDTO = billLineMapper.toDto(updatedBillLine);

        restBillLineMockMvc.perform(put("/api/bill-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billLineDTO)))
            .andExpect(status().isOk());

        // Validate the BillLine in the database
        List<BillLine> billLineList = billLineRepository.findAll();
        assertThat(billLineList).hasSize(databaseSizeBeforeUpdate);
        BillLine testBillLine = billLineList.get(billLineList.size() - 1);
        assertThat(testBillLine.getLabel()).isEqualTo(UPDATED_LABEL);
        assertThat(testBillLine.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingBillLine() throws Exception {
        int databaseSizeBeforeUpdate = billLineRepository.findAll().size();

        // Create the BillLine
        BillLineDTO billLineDTO = billLineMapper.toDto(billLine);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillLineMockMvc.perform(put("/api/bill-lines")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billLineDTO)))
            .andExpect(status().isBadRequest());

        // Validate the BillLine in the database
        List<BillLine> billLineList = billLineRepository.findAll();
        assertThat(billLineList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBillLine() throws Exception {
        // Initialize the database
        billLineRepository.saveAndFlush(billLine);

        int databaseSizeBeforeDelete = billLineRepository.findAll().size();

        // Delete the billLine
        restBillLineMockMvc.perform(delete("/api/bill-lines/{id}", billLine.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<BillLine> billLineList = billLineRepository.findAll();
        assertThat(billLineList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillLine.class);
        BillLine billLine1 = new BillLine();
        billLine1.setId(1L);
        BillLine billLine2 = new BillLine();
        billLine2.setId(billLine1.getId());
        assertThat(billLine1).isEqualTo(billLine2);
        billLine2.setId(2L);
        assertThat(billLine1).isNotEqualTo(billLine2);
        billLine1.setId(null);
        assertThat(billLine1).isNotEqualTo(billLine2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillLineDTO.class);
        BillLineDTO billLineDTO1 = new BillLineDTO();
        billLineDTO1.setId(1L);
        BillLineDTO billLineDTO2 = new BillLineDTO();
        assertThat(billLineDTO1).isNotEqualTo(billLineDTO2);
        billLineDTO2.setId(billLineDTO1.getId());
        assertThat(billLineDTO1).isEqualTo(billLineDTO2);
        billLineDTO2.setId(2L);
        assertThat(billLineDTO1).isNotEqualTo(billLineDTO2);
        billLineDTO1.setId(null);
        assertThat(billLineDTO1).isNotEqualTo(billLineDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(billLineMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(billLineMapper.fromId(null)).isNull();
    }
}
