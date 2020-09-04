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
import br.org.rpf.cagef.dto.http.request.city.InstrumentRequestParamsDTO;
import br.org.rpf.cagef.dto.instrument.InstrumentDTO;
import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.service.InstrumentService;
import br.org.rpf.cagef.service.impl.InstrumentServiceImplTest;

@RunWith(SpringRunner.class)
public class InstrumentControllerTest {

	private static final String URL = "/instruments";
	private static final String BY_ID = URL.concat("/1");
	private static final String CATEGORIES = URL.concat("/categories");
	private MockMvc mockMvc;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@MockBean
	private InstrumentService instrumentService;
	
	@InjectMocks
	private InstrumentController instrumentController;
	
	@Before
    public void setUp() {
            this.mockMvc = MockMvcBuilders
            .standaloneSetup(instrumentController)
            .setControllerAdvice(new ResourceExceptionHandler())
            .build();
            MockitoAnnotations.initMocks(this);
    }
	
	@Test
    public void findAllBadRequestError() throws Exception {
        when(this.instrumentService.findAll(any(InstrumentRequestParamsDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
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
		when(this.instrumentService.findAll(any(InstrumentRequestParamsDTO.class)))
				.thenReturn(new PageImpl<>(InstrumentServiceImplTest.getInstrumentList()));
		this.mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.content", hasSize(4)))
	    .andExpect(jsonPath("$.content.[0].id", is(1)))
	    .andExpect(jsonPath("$.content.[0].description", is("Saxofone Soprano")))
	    .andExpect(jsonPath("$.content.[0].category.description", is("Madeiras")))
	    .andExpect(jsonPath("$.content.[1].id", is(2)))
	    .andExpect(jsonPath("$.content.[1].description", is("Saxofone Alto")))
	    .andExpect(jsonPath("$.content.[1].category.description", is("Madeiras")))
	    .andExpect(jsonPath("$.content.[2].id", is(3)))
	    .andExpect(jsonPath("$.content.[2].description", is("Violino")))
	    .andExpect(jsonPath("$.content.[2].category.description", is("Cordas")))
	    .andExpect(jsonPath("$.content.[3].id", is(4)))
	    .andExpect(jsonPath("$.content.[3].description", is("Trompete")))
	    .andExpect(jsonPath("$.content.[3].category.description", is("Metais")));
    }
	
	@Test
    public void findAllCategoriesError() throws Exception {
        when(this.instrumentService.findAllCategories(1l, "Teste", 0, 50, "id", "ASC")).thenThrow(new DataIntegrityViolationException("Error"));
        this.mockMvc.perform(get(CATEGORIES.concat("?id=1&description=Teste&offset=0&limit=50&orderBy=id&direction=ASC")).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("timestamp", notNullValue()))
        .andExpect(jsonPath("status", is(400)))
        .andExpect(jsonPath("message", is("Não é possível remover este registro pois está sendo utilizado")))
        .andExpect(jsonPath("error", is("Error")))
        .andExpect(jsonPath("path", is(CATEGORIES)));
    }
	
	@Test
    public void findAllCategoriesSuccessTest() throws Exception {
		when(this.instrumentService.findAllCategories(1l, "Teste", 0, 50, "id", "ASC"))
				.thenReturn(new PageImpl<>(InstrumentServiceImplTest.getInstrumentCategoryList()));
		this.mockMvc.perform(get(CATEGORIES.concat("?id=1&description=Teste&offset=0&limit=50&orderBy=id&direction=ASC")).contentType(MediaType.APPLICATION_JSON))
		    .andExpect(status().isOk())
		    .andExpect(jsonPath("$.content", hasSize(4)))
		    .andExpect(jsonPath("$.content.[0].id", is(1)))
		    .andExpect(jsonPath("$.content.[0].description", is("Madeiras")))
		    .andExpect(jsonPath("$.content.[1].id", is(2)))
		    .andExpect(jsonPath("$.content.[1].description", is("Cordas")))
		    .andExpect(jsonPath("$.content.[2].id", is(3)))
		    .andExpect(jsonPath("$.content.[2].description", is("Metais")))
		    .andExpect(jsonPath("$.content.[3].id", is(4)))
		    .andExpect(jsonPath("$.content.[3].description", is("Outros")));
    }
	
	@Test
    public void findByIdNotFoundError() throws Exception {
            when(this.instrumentService.byId(1l)).thenThrow(new ObjectNotFoundException(1l, Instrument.class.getName()));
            this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("timestamp", notNullValue()))
	            .andExpect(jsonPath("status", is(404)))
	            .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
	            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Instrument#1]")))
	            .andExpect(jsonPath("path", is(BY_ID)));
    }
	
	@Test
    public void findByIdSuccessTest() throws Exception {
		when(this.instrumentService.byId(1l)).thenReturn(InstrumentServiceImplTest.generateInstrument());
        this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("description", is("Saxofone")))
            .andExpect(jsonPath("category.description", is("Madeiras")));
    }
	
	@Test
    public void saveBadRequestError() throws Exception {
        when(this.instrumentService.save(any(InstrumentDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(InstrumentServiceImplTest.generateInstrumentDTO())).contentType(
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
		when(this.instrumentService.save(any(InstrumentDTO.class))).thenReturn(InstrumentServiceImplTest.generateInstrument());
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(InstrumentServiceImplTest.generateInstrumentDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"))
            .andExpect(header().string("location", endsWith(BY_ID)));
    }
	
	@Test
    public void updateNotFoundError() throws Exception {
        when(this.instrumentService.update(eq(1l), any(InstrumentDTO.class))).thenThrow(new ObjectNotFoundException(1l, Instrument.class.getName()));
        this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(InstrumentServiceImplTest.generateInstrumentDTO())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Instrument#1]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void updateSuccessTest() throws Exception {
		when(this.instrumentService.update(eq(1l), any(InstrumentDTO.class))).thenReturn(InstrumentServiceImplTest.generateInstrument());
		this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(InstrumentServiceImplTest.generateInstrumentDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
	
	@Test
    public void removeotFoundError() throws Exception {
		doThrow(new ObjectNotFoundException(1l, Instrument.class.getName())).when(this.instrumentService).remove(1l);
        this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(InstrumentServiceImplTest.generateInstrumentDTO())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Instrument#1]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void removeSuccessTest() throws Exception {
		doNothing().when(this.instrumentService).remove(1l);
		this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(InstrumentServiceImplTest.generateInstrumentDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
