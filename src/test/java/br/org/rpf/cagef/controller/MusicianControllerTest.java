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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.org.rpf.cagef.controller.exception.ResourceExceptionHandler;
import br.org.rpf.cagef.dto.http.request.city.MusicianRequestParamsDTO;
import br.org.rpf.cagef.dto.musician.MusicianDTO;
import br.org.rpf.cagef.entity.Musician;
import br.org.rpf.cagef.service.MusicianService;
import br.org.rpf.cagef.service.impl.MusicianServiceImpl;
import br.org.rpf.cagef.service.impl.MusicianServiceImplTest;

@RunWith(SpringRunner.class)
public class MusicianControllerTest {

	private static final String URL = "/musicians";
	private static final String BY_ID = URL.concat("/1");
	private MockMvc mockMvc;

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();
	
	@MockBean
	private MusicianService musicianService;
	
	@InjectMocks
	private MusicianController musicianController;
	
	@TestConfiguration
	static class MusicianControllerTestConfig {
		@Bean
		public MusicianService musicianService() {
			return new MusicianServiceImpl();
		}
	}
	
	@Before
    public void setUp() {
            this.mockMvc = MockMvcBuilders
            .standaloneSetup(musicianController)
            .setControllerAdvice(new ResourceExceptionHandler())
            .build();
            MockitoAnnotations.initMocks(this);
    }
	
	@Test
    public void findAllBadRequestError() throws Exception {
        when(this.musicianService.findAll(any(MusicianRequestParamsDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
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
		when(this.musicianService.findAll(any(MusicianRequestParamsDTO.class)))
				.thenReturn(new PageImpl<>(MusicianServiceImplTest.getMusiciansList()));
        this.mockMvc.perform(get(URL).contentType(MediaType.APPLICATION_JSON))
	        .andExpect(status().isOk())
	        .andExpect(jsonPath("$.content", hasSize(4)))
	        .andExpect(jsonPath("$.content.[0].id", is(1)))
	        .andExpect(jsonPath("$.content.[0].name", is("Teste 1")))
	        .andExpect(jsonPath("$.content.[0].city.id", is(1)))
	        .andExpect(jsonPath("$.content.[0].city.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[0].city.state", is("SP")))
	        .andExpect(jsonPath("$.content.[0].city.regional", is(true)))
	        .andExpect(jsonPath("$.content.[0].phoneNumber", is("0123654789")))
	        .andExpect(jsonPath("$.content.[0].celNumber", is("0123456789")))
	        .andExpect(jsonPath("$.content.[0].email", is("mail@mail.com")))
	        .andExpect(jsonPath("$.content.[0].dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("$.content.[0].naturalness.id", is(1)))
	        .andExpect(jsonPath("$.content.[0].naturalness.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[0].naturalness.state", is("SP")))
	        .andExpect(jsonPath("$.content.[0].naturalness.regional", is(true)))
	        .andExpect(jsonPath("$.content.[0].prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("$.content.[0].prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("$.content.[0].prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("$.content.[0].prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("$.content.[0].prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("$.content.[0].prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("$.content.[0].ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("$.content.[0].ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("$.content.[0].ministryOrPosition.[0].description", is("Teste")))
            .andExpect(jsonPath("$.content.[0].instrument.id", is(1)))
            .andExpect(jsonPath("$.content.[0].instrument.description", is("Saxofone")))
            .andExpect(jsonPath("$.content.[0].oficializationDate", notNullValue()))
            .andExpect(jsonPath("$.content.[0].rehearsalDate", notNullValue()))
            .andExpect(jsonPath("$.content.[0].rjmExamDate", notNullValue()))
            .andExpect(jsonPath("$.content.[0].oficialCultExamDate", notNullValue()))
            .andExpect(jsonPath("$.content.[0].observation", is("Observation")))
            .andExpect(jsonPath("$.content.[1].id", is(2)))
	        .andExpect(jsonPath("$.content.[1].name", is("Teste 2")))
	        .andExpect(jsonPath("$.content.[1].city.id", is(1)))
	        .andExpect(jsonPath("$.content.[1].city.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[1].city.state", is("SP")))
	        .andExpect(jsonPath("$.content.[1].city.regional", is(true)))
	        .andExpect(jsonPath("$.content.[1].phoneNumber", is("1123654789")))
	        .andExpect(jsonPath("$.content.[1].celNumber", is("1123456789")))
	        .andExpect(jsonPath("$.content.[1].email", is("mail2@mail.com")))
	        .andExpect(jsonPath("$.content.[1].dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("$.content.[1].naturalness.id", is(1)))
	        .andExpect(jsonPath("$.content.[1].naturalness.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[1].naturalness.state", is("SP")))
	        .andExpect(jsonPath("$.content.[1].naturalness.regional", is(true)))
	        .andExpect(jsonPath("$.content.[1].prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("$.content.[1].prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("$.content.[1].prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("$.content.[1].prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("$.content.[1].prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("$.content.[1].prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("$.content.[1].ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("$.content.[1].ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("$.content.[1].ministryOrPosition.[0].description", is("Teste")))
            .andExpect(jsonPath("$.content.[1].instrument.id", is(1)))
            .andExpect(jsonPath("$.content.[1].instrument.description", is("Saxofone")))
            .andExpect(jsonPath("$.content.[1].oficializationDate", notNullValue()))
            .andExpect(jsonPath("$.content.[1].rehearsalDate", notNullValue()))
            .andExpect(jsonPath("$.content.[1].rjmExamDate", notNullValue()))
            .andExpect(jsonPath("$.content.[1].oficialCultExamDate", notNullValue()))
            .andExpect(jsonPath("$.content.[1].observation", is("Observation")))
            .andExpect(jsonPath("$.content.[2].id", is(3)))
	        .andExpect(jsonPath("$.content.[2].name", is("Teste 3")))
	        .andExpect(jsonPath("$.content.[2].city.id", is(1)))
	        .andExpect(jsonPath("$.content.[2].city.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[2].city.state", is("SP")))
	        .andExpect(jsonPath("$.content.[2].city.regional", is(true)))
	        .andExpect(jsonPath("$.content.[2].phoneNumber", is("2123654789")))
	        .andExpect(jsonPath("$.content.[2].celNumber", is("2123456789")))
	        .andExpect(jsonPath("$.content.[2].email", is("mail3@mail.com")))
	        .andExpect(jsonPath("$.content.[2].dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("$.content.[2].naturalness.id", is(1)))
	        .andExpect(jsonPath("$.content.[2].naturalness.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[2].naturalness.state", is("SP")))
	        .andExpect(jsonPath("$.content.[2].naturalness.regional", is(true)))
	        .andExpect(jsonPath("$.content.[2].prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("$.content.[2].prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("$.content.[2].prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("$.content.[2].prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("$.content.[2].prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("$.content.[2].prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("$.content.[2].ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("$.content.[2].ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("$.content.[2].ministryOrPosition.[0].description", is("Teste")))
            .andExpect(jsonPath("$.content.[2].instrument.id", is(1)))
            .andExpect(jsonPath("$.content.[2].instrument.description", is("Saxofone")))
            .andExpect(jsonPath("$.content.[2].oficializationDate", notNullValue()))
            .andExpect(jsonPath("$.content.[2].rehearsalDate", notNullValue()))
            .andExpect(jsonPath("$.content.[2].rjmExamDate", notNullValue()))
            .andExpect(jsonPath("$.content.[2].oficialCultExamDate", notNullValue()))
            .andExpect(jsonPath("$.content.[2].observation", is("Observation")))
            .andExpect(jsonPath("$.content.[3].id", is(4)))
	        .andExpect(jsonPath("$.content.[3].name", is("Teste 4")))
	        .andExpect(jsonPath("$.content.[3].city.id", is(1)))
	        .andExpect(jsonPath("$.content.[3].city.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[3].city.state", is("SP")))
	        .andExpect(jsonPath("$.content.[3].city.regional", is(true)))
	        .andExpect(jsonPath("$.content.[3].phoneNumber", is("3123654789")))
	        .andExpect(jsonPath("$.content.[3].celNumber", is("3123456789")))
	        .andExpect(jsonPath("$.content.[3].email", is("mail4@mail.com")))
	        .andExpect(jsonPath("$.content.[3].dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("$.content.[3].naturalness.id", is(1)))
	        .andExpect(jsonPath("$.content.[3].naturalness.name", is("Teste")))
	        .andExpect(jsonPath("$.content.[3].naturalness.state", is("SP")))
	        .andExpect(jsonPath("$.content.[3].naturalness.regional", is(true)))
	        .andExpect(jsonPath("$.content.[3].prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("$.content.[3].prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("$.content.[3].prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("$.content.[3].prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("$.content.[3].prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("$.content.[3].prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("$.content.[3].ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("$.content.[3].ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("$.content.[3].ministryOrPosition.[0].description", is("Teste")))
            .andExpect(jsonPath("$.content.[3].instrument.id", is(1)))
            .andExpect(jsonPath("$.content.[3].instrument.description", is("Saxofone")))
            .andExpect(jsonPath("$.content.[3].oficializationDate", notNullValue()))
            .andExpect(jsonPath("$.content.[3].rehearsalDate", notNullValue()))
            .andExpect(jsonPath("$.content.[3].rjmExamDate", notNullValue()))
            .andExpect(jsonPath("$.content.[3].oficialCultExamDate", notNullValue()))
            .andExpect(jsonPath("$.content.[3].observation", is("Observation")));
    }
	
	@Test
    public void findByIdNotFoundError() throws Exception {
            when(this.musicianService.byId(1l)).thenThrow(new ObjectNotFoundException(1l, Musician.class.getName()));
            this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
	            .andExpect(status().isNotFound())
	            .andExpect(jsonPath("timestamp", notNullValue()))
	            .andExpect(jsonPath("status", is(404)))
	            .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
	            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Musician#1]")))
	            .andExpect(jsonPath("path", is(BY_ID)));
    }
	
	@Test
    public void findByIdSuccessTest() throws Exception {
		when(this.musicianService.byId(1l)).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());
        this.mockMvc.perform(get(BY_ID).contentType(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk())
            .andExpect(jsonPath("id", is(1)))
	        .andExpect(jsonPath("name", is("Teste 1")))
	        .andExpect(jsonPath("city.id", is(1)))
	        .andExpect(jsonPath("city.name", is("Teste")))
	        .andExpect(jsonPath("city.state", is("SP")))
	        .andExpect(jsonPath("city.regional", is(true)))
	        .andExpect(jsonPath("phoneNumber", is("0123654789")))
	        .andExpect(jsonPath("celNumber", is("0123456789")))
	        .andExpect(jsonPath("email", is("mail@mail.com")))
	        .andExpect(jsonPath("dateOfBirth", notNullValue()))
	        .andExpect(jsonPath("naturalness.id", is(1)))
	        .andExpect(jsonPath("naturalness.name", is("Teste")))
	        .andExpect(jsonPath("naturalness.state", is("SP")))
	        .andExpect(jsonPath("naturalness.regional", is(true)))
	        .andExpect(jsonPath("prayingHouse.reportCode", is("abc123")))
            .andExpect(jsonPath("prayingHouse.district", is("Teste")))
            .andExpect(jsonPath("prayingHouse.city.id", is(1)))
            .andExpect(jsonPath("prayingHouse.city.name", is("Teste")))
            .andExpect(jsonPath("prayingHouse.city.state", is("SP")))
            .andExpect(jsonPath("prayingHouse.city.regional", is(true)))
            .andExpect(jsonPath("ministryOrPosition", hasSize(1)))
	        .andExpect(jsonPath("ministryOrPosition.[0].id", is(1)))
            .andExpect(jsonPath("ministryOrPosition.[0].description", is("Teste")))
            .andExpect(jsonPath("instrument.id", is(1)))
            .andExpect(jsonPath("instrument.description", is("Saxofone")))
            .andExpect(jsonPath("oficializationDate", notNullValue()))
            .andExpect(jsonPath("rehearsalDate", notNullValue()))
            .andExpect(jsonPath("rjmExamDate", notNullValue()))
            .andExpect(jsonPath("oficialCultExamDate", notNullValue()))
            .andExpect(jsonPath("observation", is("Observation")));
    }
	
	@Test
    public void saveBadRequestError() throws Exception {
        when(this.musicianService.save(any(MusicianDTO.class))).thenThrow(new DataIntegrityViolationException("Error"));
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(MusicianServiceImplTest.generateMusicianDto2())).contentType(
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
		when(this.musicianService.save(any(MusicianDTO.class))).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());
		this.mockMvc.perform(post(URL)
				.content(new ObjectMapper().writeValueAsString(MusicianServiceImplTest.generateMusicianDto2())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isCreated())
            .andExpect(header().exists("location"))
            .andExpect(header().string("location", endsWith(BY_ID)));
    }
	
	@Test
    public void updateNotFoundError() throws Exception {
        when(this.musicianService.update(eq(1l), any(MusicianDTO.class))).thenThrow(new ObjectNotFoundException(1l, Musician.class.getName()));
        this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(MusicianServiceImplTest.generateMusicianDto2())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Musician#1]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void updateSuccessTest() throws Exception {
		when(this.musicianService.update(eq(1l), any(MusicianDTO.class))).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());
		this.mockMvc.perform(put(BY_ID)
				.content(new ObjectMapper().writeValueAsString(MusicianServiceImplTest.generateMusicianDto2())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
	
	@Test
    public void removeNotFoundError() throws Exception {
		doThrow(new ObjectNotFoundException(1l, Musician.class.getName())).when(this.musicianService).remove(1l);
        this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(MusicianServiceImplTest.generateMusicianDto2())).contentType(
						MediaType.APPLICATION_JSON))
	        .andExpect(status().isNotFound())
	        .andExpect(jsonPath("timestamp", notNullValue()))
	        .andExpect(jsonPath("status", is(404)))
	        .andExpect(jsonPath("message", is("Registro com id 1 não encontrado(a)")))
            .andExpect(jsonPath("error", is("No row with the given identifier exists: [br.org.rpf.cagef.entity.Musician#1]")))
	        .andExpect(jsonPath("path", is(BY_ID)));

    }
	
	@Test
    public void removeSuccessTest() throws Exception {
		doNothing().when(this.musicianService).remove(1l);
		this.mockMvc.perform(delete(BY_ID)
				.content(new ObjectMapper().writeValueAsString(MusicianServiceImplTest.generateMusicianDto())).contentType(
						MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());
    }
}
