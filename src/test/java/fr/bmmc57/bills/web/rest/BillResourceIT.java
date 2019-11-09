package fr.bmmc57.bills.web.rest;

import fr.bmmc57.bills.BillsApp;
import fr.bmmc57.bills.domain.Bill;
import fr.bmmc57.bills.repository.BillRepository;
import fr.bmmc57.bills.service.BillService;
import fr.bmmc57.bills.service.dto.BillDTO;
import fr.bmmc57.bills.service.mapper.BillMapper;
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

import fr.bmmc57.bills.domain.enumeration.BillStatus;
/**
 * Integration tests for the {@link BillResource} REST controller.
 */
@SpringBootTest(classes = BillsApp.class)
public class BillResourceIT {

    private static final BillStatus DEFAULT_STATUS = BillStatus.CREATED;
    private static final BillStatus UPDATED_STATUS = BillStatus.PAID;

    private static final Double DEFAULT_AMOUNT = 1D;
    private static final Double UPDATED_AMOUNT = 2D;

    @Autowired
    private BillRepository billRepository;

    @Autowired
    private BillMapper billMapper;

    @Autowired
    private BillService billService;

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

    private MockMvc restBillMockMvc;

    private Bill bill;

    @BeforeEach
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final BillResource billResource = new BillResource(billService);
        this.restBillMockMvc = MockMvcBuilders.standaloneSetup(billResource)
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
    public static Bill createEntity(EntityManager em) {
        Bill bill = new Bill()
            .status(DEFAULT_STATUS)
            .amount(DEFAULT_AMOUNT);
        return bill;
    }
    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Bill createUpdatedEntity(EntityManager em) {
        Bill bill = new Bill()
            .status(UPDATED_STATUS)
            .amount(UPDATED_AMOUNT);
        return bill;
    }

    @BeforeEach
    public void initTest() {
        bill = createEntity(em);
    }

    @Test
    @Transactional
    public void createBill() throws Exception {
        int databaseSizeBeforeCreate = billRepository.findAll().size();

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);
        restBillMockMvc.perform(post("/api/bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isCreated());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeCreate + 1);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testBill.getAmount()).isEqualTo(DEFAULT_AMOUNT);
    }

    @Test
    @Transactional
    public void createBillWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = billRepository.findAll().size();

        // Create the Bill with an existing ID
        bill.setId(1L);
        BillDTO billDTO = billMapper.toDto(bill);

        // An entity with an existing ID cannot be created, so this API call must fail
        restBillMockMvc.perform(post("/api/bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeCreate);
    }


    @Test
    @Transactional
    public void getAllBills() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get all the billList
        restBillMockMvc.perform(get("/api/bills?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(bill.getId().intValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.toString())))
            .andExpect(jsonPath("$.[*].amount").value(hasItem(DEFAULT_AMOUNT.doubleValue())));
    }
    
    @Test
    @Transactional
    public void getBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        // Get the bill
        restBillMockMvc.perform(get("/api/bills/{id}", bill.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(bill.getId().intValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.toString()))
            .andExpect(jsonPath("$.amount").value(DEFAULT_AMOUNT.doubleValue()));
    }

    @Test
    @Transactional
    public void getNonExistingBill() throws Exception {
        // Get the bill
        restBillMockMvc.perform(get("/api/bills/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updateBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Update the bill
        Bill updatedBill = billRepository.findById(bill.getId()).get();
        // Disconnect from session so that the updates on updatedBill are not directly saved in db
        em.detach(updatedBill);
        updatedBill
            .status(UPDATED_STATUS)
            .amount(UPDATED_AMOUNT);
        BillDTO billDTO = billMapper.toDto(updatedBill);

        restBillMockMvc.perform(put("/api/bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isOk());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
        Bill testBill = billList.get(billList.size() - 1);
        assertThat(testBill.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testBill.getAmount()).isEqualTo(UPDATED_AMOUNT);
    }

    @Test
    @Transactional
    public void updateNonExistingBill() throws Exception {
        int databaseSizeBeforeUpdate = billRepository.findAll().size();

        // Create the Bill
        BillDTO billDTO = billMapper.toDto(bill);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restBillMockMvc.perform(put("/api/bills")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(billDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Bill in the database
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deleteBill() throws Exception {
        // Initialize the database
        billRepository.saveAndFlush(bill);

        int databaseSizeBeforeDelete = billRepository.findAll().size();

        // Delete the bill
        restBillMockMvc.perform(delete("/api/bills/{id}", bill.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Bill> billList = billRepository.findAll();
        assertThat(billList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Bill.class);
        Bill bill1 = new Bill();
        bill1.setId(1L);
        Bill bill2 = new Bill();
        bill2.setId(bill1.getId());
        assertThat(bill1).isEqualTo(bill2);
        bill2.setId(2L);
        assertThat(bill1).isNotEqualTo(bill2);
        bill1.setId(null);
        assertThat(bill1).isNotEqualTo(bill2);
    }

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(BillDTO.class);
        BillDTO billDTO1 = new BillDTO();
        billDTO1.setId(1L);
        BillDTO billDTO2 = new BillDTO();
        assertThat(billDTO1).isNotEqualTo(billDTO2);
        billDTO2.setId(billDTO1.getId());
        assertThat(billDTO1).isEqualTo(billDTO2);
        billDTO2.setId(2L);
        assertThat(billDTO1).isNotEqualTo(billDTO2);
        billDTO1.setId(null);
        assertThat(billDTO1).isNotEqualTo(billDTO2);
    }

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(billMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(billMapper.fromId(null)).isNull();
    }
}
