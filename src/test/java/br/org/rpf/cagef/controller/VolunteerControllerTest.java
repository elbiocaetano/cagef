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
import br.org.rpf.cagef.dto.http.request.city.VolunteerRequestParamsDTO;
import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.service.VolunteerService;
import br.org.rpf.cagef.service.impl.VolunteerServiceImplTest;

@RunWith(SpringRunner.class)
public class VolunteerControllerTest {

	private static final String URL = "/volunteers";
	private static final String BY_ID = URL.concat("/1");
	private MockMvc mockMvc;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@MockBean
	private VolunteerService volunteerService;
	
	@InjectMocks
	private VolunteerController volunteerController;
	
	@Before
    public void setUp() {
            this.mockMvc = MockMvcBuilders
            .standaloneSetup(volunteerController)
            .setControllerAdvice(new ResourceExceptionHandler())
            .build();
            MockitoAnnotations.initMocks(this);
    }
	
	@Test
    public void findAllBadRequestError() throws Exception {
        when(this.volunteerService.findAll(any(VolunteerRequestParamsDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
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
		when(this.volunteerService.findAll(any(VolunteerRequestParamsDTO.class)))
				.thenReturn(new PageImpl<>(VolunteerServiceImplTest.getVolunteersList()));
        this.mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.content", hasSize(4)))
	        .andExpect(jsonPath("$.content.[0].id", is(1)))
	        .andExpect(jsonPath("$.content.[0].name", is("Teste 1")))
	        .andExpect(jsonPath("$.content.[0].address", is("Teste")))
	        .andExpect(jsonPath("$.content.[0].district", is("Teste")))
	        .andExpect(jsonPath("$.content.[0].city.id", is(1)))
	        .andExpect(jsonPath("$.content.[0].city.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[0].city.state", is("SP")))
	        .andExpect(jsonPath("$.content.[0].city.regional", is(true)))
	        .andExpect(jsonPath("$.content.[0].zipCode", is("12345678")))
	        .andExpect(jsonPath("$.content.[0].phoneNumber", is("0123654789")))
	        .andExpect(jsonPath("$.content.[0].celNumber", is("0123456789")))
	        .andExpect(jsonPath("$.content.[0].email", is("mail@mail.com")))
	        .andExpect(jsonPath("$.content.[0].dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("$.content.[0].naturalness.id", is(1)))
	        .andExpect(jsonPath("$.content.[0].naturalness.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[0].naturalness.state", is("SP")))
	        .andExpect(jsonPath("$.content.[0].naturalness.regional", is(true)))
	        .andExpect(jsonPath("$.content.[0].dateOfBaptism", notNullValue()))
	        .andExpect(jsonPath("$.content.[0].cpf", is("14256895789")))
	        .andExpect(jsonPath("$.content.[0].rg", is("12547895X")))
	        .andExpect(jsonPath("$.content.[0].maritalStatus", is("CASADO")))
	        .andExpect(jsonPath("$.content.[0].ministryApresentationDate", notNullValue()))
	        .andExpect(jsonPath("$.content.[0].promise", is("SIM")))
	        .andExpect(jsonPath("$.content.[0].prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("$.content.[0].prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("$.content.[0].prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("$.content.[0].prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("$.content.[0].prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("$.content.[0].prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("$.content.[0].ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("$.content.[0].ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("$.content.[0].ministryOrPosition.[0].description", is("Teste")))
            .andExpect(jsonPath("$.content.[1].id", is(2)))
	        .andExpect(jsonPath("$.content.[1].name", is("Teste 2")))
	        .andExpect(jsonPath("$.content.[1].address", is("Teste")))
	        .andExpect(jsonPath("$.content.[1].district", is("Teste")))
	        .andExpect(jsonPath("$.content.[1].city.id", is(1)))
	        .andExpect(jsonPath("$.content.[1].city.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[1].city.state", is("SP")))
	        .andExpect(jsonPath("$.content.[1].city.regional", is(true)))
	        .andExpect(jsonPath("$.content.[1].zipCode", is("12345678")))
	        .andExpect(jsonPath("$.content.[1].phoneNumber", is("0123654789")))
	        .andExpect(jsonPath("$.content.[1].celNumber", is("0123456789")))
	        .andExpect(jsonPath("$.content.[1].email", is("mail@mail.com")))
	        .andExpect(jsonPath("$.content.[1].dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("$.content.[1].naturalness.id", is(1)))
	        .andExpect(jsonPath("$.content.[1].naturalness.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[1].naturalness.state", is("SP")))
	        .andExpect(jsonPath("$.content.[1].naturalness.regional", is(true)))
	        .andExpect(jsonPath("$.content.[1].dateOfBaptism", notNullValue()))
	        .andExpect(jsonPath("$.content.[1].cpf", is("14256895789")))
	        .andExpect(jsonPath("$.content.[1].rg", is("12547895X")))
	        .andExpect(jsonPath("$.content.[1].maritalStatus", is("CASADO")))
	        .andExpect(jsonPath("$.content.[1].ministryApresentationDate", notNullValue()))
	        .andExpect(jsonPath("$.content.[1].promise", is("SIM")))
	        .andExpect(jsonPath("$.content.[1].prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("$.content.[1].prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("$.content.[1].prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("$.content.[1].prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("$.content.[1].prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("$.content.[1].prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("$.content.[1].ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("$.content.[1].ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("$.content.[1].ministryOrPosition.[0].description", is("Teste")))
            .andExpect(jsonPath("$.content.[2].id", is(3)))
	        .andExpect(jsonPath("$.content.[2].name", is("Teste 3")))
	        .andExpect(jsonPath("$.content.[2].address", is("Teste")))
	        .andExpect(jsonPath("$.content.[2].district", is("Teste")))
	        .andExpect(jsonPath("$.content.[2].city.id", is(1)))
	        .andExpect(jsonPath("$.content.[2].city.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[2].city.state", is("SP")))
	        .andExpect(jsonPath("$.content.[2].city.regional", is(true)))
	        .andExpect(jsonPath("$.content.[2].zipCode", is("12345678")))
	        .andExpect(jsonPath("$.content.[2].phoneNumber", is("0123654789")))
	        .andExpect(jsonPath("$.content.[2].celNumber", is("0123456789")))
	        .andExpect(jsonPath("$.content.[2].email", is("mail@mail.com")))
	        .andExpect(jsonPath("$.content.[2].dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("$.content.[2].naturalness.id", is(1)))
	        .andExpect(jsonPath("$.content.[2].naturalness.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[2].naturalness.state", is("SP")))
	        .andExpect(jsonPath("$.content.[2].naturalness.regional", is(true)))
	        .andExpect(jsonPath("$.content.[2].dateOfBaptism", notNullValue()))
	        .andExpect(jsonPath("$.content.[2].cpf", is("14256895789")))
	        .andExpect(jsonPath("$.content.[2].rg", is("12547895X")))
	        .andExpect(jsonPath("$.content.[2].maritalStatus", is("CASADO")))
	        .andExpect(jsonPath("$.content.[2].ministryApresentationDate", notNullValue()))
	        .andExpect(jsonPath("$.content.[2].promise", is("SIM")))
	        .andExpect(jsonPath("$.content.[2].prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("$.content.[2].prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("$.content.[2].prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("$.content.[2].prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("$.content.[2].prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("$.content.[2].prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("$.content.[2].ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("$.content.[2].ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("$.content.[2].ministryOrPosition.[0].description", is("Teste")))
            .andExpect(jsonPath("$.content.[3].id", is(4)))
	        .andExpect(jsonPath("$.content.[3].name", is("Teste 4")))
	        .andExpect(jsonPath("$.content.[3].address", is("Teste")))
	        .andExpect(jsonPath("$.content.[3].district", is("Teste")))
	        .andExpect(jsonPath("$.content.[3].city.id", is(1)))
	        .andExpect(jsonPath("$.content.[3].city.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[3].city.state", is("SP")))
	        .andExpect(jsonPath("$.content.[3].city.regional", is(true)))
	        .andExpect(jsonPath("$.content.[3].zipCode", is("12345678")))
	        .andExpect(jsonPath("$.content.[3].phoneNumber", is("0123654789")))
	        .andExpect(jsonPath("$.content.[3].celNumber", is("0123456789")))
	        .andExpect(jsonPath("$.content.[3].email", is("mail@mail.com")))
	        .andExpect(jsonPath("$.content.[3].dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("$.content.[3].naturalness.id", is(1)))
	        .andExpect(jsonPath("$.content.[3].naturalness.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[3].naturalness.state", is("SP")))
	        .andExpect(jsonPath("$.content.[3].naturalness.regional", is(true)))
	        .andExpect(jsonPath("$.content.[3].dateOfBaptism", notNullValue()))
	        .andExpect(jsonPath("$.content.[3].cpf", is("14256895789")))
	        .andExpect(jsonPath("$.content.[3].rg", is("12547895X")))
	        .andExpect(jsonPath("$.content.[3].maritalStatus", is("CASADO")))
	        .andExpect(jsonPath("$.content.[3].ministryApresentationDate", notNullValue()))
	        .andExpect(jsonPath("$.content.[3].promise", is("SIM")))
	        .andExpect(jsonPath("$.content.[3].prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("$.content.[3].prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("$.content.[3].prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("$.content.[3].prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("$.content.[3].prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("$.content.[3].prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("$.content.[3].ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("$.content.[3].ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("$.content.[3].ministryOrPosition.[0].description", is("Teste")));
    }
	
	@Test
    public void findByIdNotFoundError() throws Exception {
            when(this.volunteerService.byId(1l)).thenThrow(new ObjectNotFoundException(1l, Volunteer.class.getName()));
            this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("timestamp", notNullValue()))
	            .andExpect(jsonPath("status", is(404)))
	            .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
	            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Volunteer#1]")))
	            .andExpect(jsonPath("path", is(BY_ID)));
    }
	
	@Test
    public void findByIdSuccessTest() throws Exception {
		when(this.volunteerService.byId(1l)).thenReturn(VolunteerServiceImplTest.generateVolunteerNotAdmin());
        this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(1)))
	        .andExpect(jsonPath("name", is("Teste")))
	        .andExpect(jsonPath("address", is("Teste")))
	        .andExpect(jsonPath("district", is("Teste")))
	        .andExpect(jsonPath("city.id", is(1)))
	        .andExpect(jsonPath("city.name", is("Teste")))
	        .andExpect(jsonPath("city.state", is("SP")))
	        .andExpect(jsonPath("city.regional", is(true)))
	        .andExpect(jsonPath("zipCode", is("12345678")))
	        .andExpect(jsonPath("phoneNumber", is("0123654789")))
	        .andExpect(jsonPath("celNumber", is("0123456789")))
	        .andExpect(jsonPath("email", is("mail@mail.com")))
	        .andExpect(jsonPath("dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("naturalness.id", is(1)))
	        .andExpect(jsonPath("naturalness.name", is("Teste")))
	        .andExpect(jsonPath("naturalness.state", is("SP")))
	        .andExpect(jsonPath("naturalness.regional", is(true)))
	        .andExpect(jsonPath("dateOfBaptism", notNullValue()))
	        .andExpect(jsonPath("cpf", is("14256895789")))
	        .andExpect(jsonPath("rg", is("12547895X")))
	        .andExpect(jsonPath("maritalStatus", is("CASADO")))
	        .andExpect(jsonPath("ministryApresentationDate", notNullValue()))
	        .andExpect(jsonPath("promise", is("SIM")))
	        .andExpect(jsonPath("prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("ministryOrPosition.[0].description", is("Teste")));
    }
	
	@Test
    public void saveBadRequestError() throws Exception {
        when(this.volunteerService.save(any(VolunteerDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(VolunteerServiceImplTest.generateVolunteerDto2())).contentType(
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
		when(this.volunteerService.save(any(VolunteerDTO.class))).thenReturn(VolunteerServiceImplTest.generateVolunteerNotAdmin());
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(VolunteerServiceImplTest.generateVolunteerDto2())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"))
            .andExpect(header().string("location", endsWith(BY_ID)));
    }
	
	@Test
    public void updateNotFoundError() throws Exception {
        when(this.volunteerService.update(eq(1l), any(VolunteerDTO.class))).thenThrow(new ObjectNotFoundException(1l, Volunteer.class.getName()));
        this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(VolunteerServiceImplTest.generateVolunteerDto2())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Volunteer#1]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void updateSuccessTest() throws Exception {
		when(this.volunteerService.update(eq(1l), any(VolunteerDTO.class))).thenReturn(VolunteerServiceImplTest.generateVolunteerNotAdmin());
		this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(VolunteerServiceImplTest.generateVolunteerDto2())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
	
	@Test
    public void removeNotFoundError() throws Exception {
		doThrow(new ObjectNotFoundException(1l, Volunteer.class.getName())).when(this.volunteerService).remove(1l);
        this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(VolunteerServiceImplTest.generateVolunteerDto2())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Volunteer#1]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void removeSuccessTest() throws Exception {
		doNothing().when(this.volunteerService).remove(1l);
		this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(VolunteerServiceImplTest.generateVolunteerDto2())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
