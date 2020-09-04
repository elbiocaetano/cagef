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
import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionDTO;
import br.org.rpf.cagef.entity.MinistryOrPosition;
import br.org.rpf.cagef.service.MinistryOrPositionService;
import br.org.rpf.cagef.service.impl.MinistryOrPositionServiceImplTest;

@RunWith(SpringRunner.class)
public class MinistryOrPositionControllerTest {

	private static final String URL = "/ministeriesOrPositions";
	private static final String BY_ID = URL.concat("/1");
	private MockMvc mockMvc;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@MockBean
	private MinistryOrPositionService ministryOrPositionService;
	
	@InjectMocks
	private MinistryOrPositionController ministryOrPositionController;
	
	@Before
    public void setUp() {
            this.mockMvc = MockMvcBuilders
            .standaloneSetup(ministryOrPositionController)
            .setControllerAdvice(new ResourceExceptionHandler())
            .build();
            MockitoAnnotations.initMocks(this);
    }
	
	@Test
    public void findAllBadRequestError() throws Exception {
        when(this.ministryOrPositionService.findAll(1l, new Long[] {1l}, "Teste", 0, 50, "id", "ASC")).thenThrow(new DataIntegrityViolationException("Error"));
        this.mockMvc.perform(get(URL.concat("?id=1&id.in=1&description=Teste&offset=0&limit=50&orderBy=id&direction=ASC")).contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isBadRequest())
        .andExpect(jsonPath("timestamp", notNullValue()))
        .andExpect(jsonPath("status", is(400)))
        .andExpect(jsonPath("message", is("Não é possível remover este registro pois está sendo utilizado")))
        .andExpect(jsonPath("error", is("Error")))
        .andExpect(jsonPath("path", is(URL)));
    }
	
	@Test
    public void findAllSuccessTest() throws Exception {
		when(this.ministryOrPositionService.findAll(1l, new Long[] {1l}, "Teste", 0, 50, "id", "ASC"))
				.thenReturn(new PageImpl<>(MinistryOrPositionServiceImplTest.getMinisteriesOrPositionsList()));
		this.mockMvc.perform(get(URL.concat("?id=1&id.in=1&description=Teste&offset=0&limit=50&orderBy=id&direction=ASC")).contentType(MediaType.APPLICATION_JSON))
	    .andExpect(status().isOk())
	    .andExpect(jsonPath("$.content", hasSize(4)))
	    .andExpect(jsonPath("$.content.[0].id", is(1)))
	    .andExpect(jsonPath("$.content.[0].description", is("Ancião")))
	    .andExpect(jsonPath("$.content.[1].id", is(2)))
	    .andExpect(jsonPath("$.content.[1].description", is("Cooperador do Ofício Ministerial")))
	    .andExpect(jsonPath("$.content.[2].id", is(3)))
	    .andExpect(jsonPath("$.content.[2].description", is("Diácono")))
	    .andExpect(jsonPath("$.content.[3].id", is(4)))
	    .andExpect(jsonPath("$.content.[3].description", is("Encarregado Regional")));		
    }
	
	@Test
    public void findByIdNotFoundError() throws Exception {
        when(this.ministryOrPositionService.byId(1l)).thenThrow(new ObjectNotFoundException(1l, MinistryOrPosition.class.getName()));
        this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("timestamp", notNullValue()))
            .andExpect(jsonPath("status", is(404)))
            .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.MinistryOrPosition#1]")))
            .andExpect(jsonPath("path", is(BY_ID)));
    }
	
	@Test
    public void findByIdSuccessTest() throws Exception {
		when(this.ministryOrPositionService.byId(1l)).thenReturn(MinistryOrPositionServiceImplTest.generateMinistryOrPosition());
        this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(1)))
            .andExpect(jsonPath("description", is("Teste")));
    }
	
	@Test
    public void saveBadRequestError() throws Exception {
        when(this.ministryOrPositionService.save(any(MinistryOrPositionDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(MinistryOrPositionServiceImplTest.generateMinistryOrPositionDTO())).contentType(
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
		when(this.ministryOrPositionService.save(any(MinistryOrPositionDTO.class))).thenReturn(MinistryOrPositionServiceImplTest.generateMinistryOrPosition());
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(MinistryOrPositionServiceImplTest.generateMinistryOrPositionDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"))
            .andExpect(header().string("location", endsWith(BY_ID)));
    }
	
	@Test
    public void updateNotFoundError() throws Exception {
        when(this.ministryOrPositionService.update(eq(1l), any(MinistryOrPositionDTO.class))).thenThrow(new ObjectNotFoundException(1l, MinistryOrPosition.class.getName()));
        this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(MinistryOrPositionServiceImplTest.generateMinistryOrPositionDTO())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.MinistryOrPosition#1]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void updateSuccessTest() throws Exception {
		when(this.ministryOrPositionService.update(eq(1l), any(MinistryOrPositionDTO.class))).thenReturn(MinistryOrPositionServiceImplTest.generateMinistryOrPosition());
		this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(MinistryOrPositionServiceImplTest.generateMinistryOrPositionDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
	
	@Test
    public void removeotFoundError() throws Exception {
		doThrow(new ObjectNotFoundException(1l, MinistryOrPosition.class.getName())).when(this.ministryOrPositionService).remove(1l);
        this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(MinistryOrPositionServiceImplTest.generateMinistryOrPositionDTO())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.MinistryOrPosition#1]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void removeSuccessTest() throws Exception {
		doNothing().when(this.ministryOrPositionService).remove(1l);
		this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(MinistryOrPositionServiceImplTest.generateMinistryOrPositionDTO())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
