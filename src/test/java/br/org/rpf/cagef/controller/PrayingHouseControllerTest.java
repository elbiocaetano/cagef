package br.org.rpf.cagef.controller;

import static org.hamcrest.CoreMatchers.notNullValue;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.rpf.cagef.controller.exception.ResourceExceptionHandler;
import br.org.rpf.cagef.dto.http.request.city.PrayingHouseRequestParamsDTO;
import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseDTO;
import br.org.rpf.cagef.entity.PrayingHouse;
import br.org.rpf.cagef.service.PrayingHouseService;
import br.org.rpf.cagef.service.impl.PrayingHouseServiceImplTest;

@RunWith(SpringRunner.class)
public class PrayingHouseControllerTest {

	private static final String URL = "/prayinghouses";
	private static final String BY_ID = URL.concat("/abc123");
	private MockMvc mockMvc;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@MockBean
	private PrayingHouseService prayingHouseService;
	
	@InjectMocks
	private PrayingHouseController prayingHouseController;
	
	@Before
    public void setUp() {
            this.mockMvc = MockMvcBuilders
            .standaloneSetup(prayingHouseController)
            .setControllerAdvice(new ResourceExceptionHandler())
            .build();
            MockitoAnnotations.initMocks(this);
    }
	
	@Test
    public void findAllBadRequestError() throws Exception {
        when(this.prayingHouseService.findAll(any(PrayingHouseRequestParamsDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
        this.mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("timestamp", notNullValue()))
        .andExpect(jsonPath("status", is(400)))
        .andExpect(jsonPath("message", is("Não é possível remover este registro pois está sendo utilizado")))
        .andExpect(jsonPath("error", is("Error")))
        .andExpect(jsonPath("path", is(URL)));
    }
	
	@Test
    public void findAllSuccessTest() throws Exception {
		when(this.prayingHouseService.findAll(any(PrayingHouseRequestParamsDTO.class)))
				.thenReturn(new PageImpl<>(PrayingHouseServiceImplTest.getPrayingHousesList()));
		this.mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.content", hasSize(4)))
	    .andExpect(jsonPath("$.content.[0].reportCode", is("abc1234")))
	    .andExpect(jsonPath("$.content.[0].district", is("Teste 1")))
	    .andExpect(jsonPath("$.content.[0].city.id", is(1)))
        .andExpect(jsonPath("$.content.[0].city.name", is("Teste")))
        .andExpect(jsonPath("$.content.[0].city.state", is("SP")))
        .andExpect(jsonPath("$.content.[0].city.regional", is(true)))
	    .andExpect(jsonPath("$.content.[1].reportCode", is("abc1235")))
	    .andExpect(jsonPath("$.content.[1].district", is("Teste 2")))
	    .andExpect(jsonPath("$.content.[1].city.id", is(1)))
        .andExpect(jsonPath("$.content.[1].city.name", is("Teste")))
        .andExpect(jsonPath("$.content.[1].city.state", is("SP")))
        .andExpect(jsonPath("$.content.[1].city.regional", is(true)))
	    .andExpect(jsonPath("$.content.[2].reportCode", is("abc1236")))
	    .andExpect(jsonPath("$.content.[2].district", is("Teste 3")))
	    .andExpect(jsonPath("$.content.[2].city.id", is(1)))
        .andExpect(jsonPath("$.content.[2].city.name", is("Teste")))
        .andExpect(jsonPath("$.content.[2].city.state", is("SP")))
        .andExpect(jsonPath("$.content.[2].city.regional", is(true)))
	    .andExpect(jsonPath("$.content.[3].reportCode", is("abc1237")))
	    .andExpect(jsonPath("$.content.[3].district", is("Teste 4")))
	    .andExpect(jsonPath("$.content.[3].city.id", is(1)))
        .andExpect(jsonPath("$.content.[3].city.name", is("Teste")))
        .andExpect(jsonPath("$.content.[3].city.state", is("SP")))
        .andExpect(jsonPath("$.content.[3].city.regional", is(true)));
    }
	
	@Test
    public void findByIdNotFoundError() throws Exception {
        when(this.prayingHouseService.byId("abc123")).thenThrow(new ObjectNotFoundException("abc123", PrayingHouse.class.getName()));
        this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("timestamp", notNullValue()))
            .andExpect(jsonPath("status", is(404)))
            .andExpect(jsonPath("message", is("Registro com id abc123 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.PrayingHouse#abc123]")))
            .andExpect(jsonPath("path", is(BY_ID)));
    }
	
	@Test
    public void findByIdSuccessTest() throws Exception {
		when(this.prayingHouseService.byId("abc123")).thenReturn(PrayingHouseServiceImplTest.generatePrayingHouse());
        this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("reportCode", is("abc123")))
            .andExpect(jsonPath("district", is("Teste")))
            .andExpect(jsonPath("city.id", is(1)))
            .andExpect(jsonPath("city.name", is("Teste")))
            .andExpect(jsonPath("city.state", is("SP")))
            .andExpect(jsonPath("city.regional", is(true)));
    }
	
	@Test
    public void saveBadRequestError() throws Exception {
        when(this.prayingHouseService.save(any(PrayingHouseDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(PrayingHouseServiceImplTest.generatePrayingHouseDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("timestamp", notNullValue()))
            .andExpect(jsonPath("status", is(400)))
            .andExpect(jsonPath("message", is("Não é possível remover este registro pois está sendo utilizado")))
            .andExpect(jsonPath("error", is("Error")))
            .andExpect(jsonPath("path", is(URL)));
    }
	
	@Test
    public void saveSuccessTest() throws Exception {
		when(this.prayingHouseService.save(any(PrayingHouseDTO.class))).thenReturn(PrayingHouseServiceImplTest.generatePrayingHouse());
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(PrayingHouseServiceImplTest.generatePrayingHouseDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"))
            .andExpect(header().string("location", endsWith(BY_ID)));
    }
	
	@Test
    public void updateNotFoundError() throws Exception {
        when(this.prayingHouseService.update(eq("abc123"), any(PrayingHouseDTO.class))).thenThrow(new ObjectNotFoundException("abc123", PrayingHouse.class.getName()));
        this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(PrayingHouseServiceImplTest.generatePrayingHouseDTO())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id abc123 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.PrayingHouse#abc123]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void updateSuccessTest() throws Exception {
		when(this.prayingHouseService.update(eq("abc123"), any(PrayingHouseDTO.class))).thenReturn(PrayingHouseServiceImplTest.generatePrayingHouse());
		this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(PrayingHouseServiceImplTest.generatePrayingHouseDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
	
	@Test
    public void removeotFoundError() throws Exception {
		doThrow(new ObjectNotFoundException("abc123", PrayingHouse.class.getName())).when(this.prayingHouseService).remove("abc123");
        this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(PrayingHouseServiceImplTest.generatePrayingHouseDTO())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id abc123 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.PrayingHouse#abc123]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void removeSuccessTest() throws Exception {
		doNothing().when(this.prayingHouseService).remove("abc123");
		this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(PrayingHouseServiceImplTest.generatePrayingHouseDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
