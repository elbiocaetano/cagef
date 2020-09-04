package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.rpf.cagef.dto.http.request.city.MusicianRequestParamsDTO;
import br.org.rpf.cagef.dto.instrument.InstrumentInnerDTO;
import br.org.rpf.cagef.dto.musician.MusicianDTO;
import br.org.rpf.cagef.entity.Musician;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.InstrumentRepository;
import br.org.rpf.cagef.repository.MinistryOrPositionRepository;
import br.org.rpf.cagef.repository.MusicianRepository;
import br.org.rpf.cagef.repository.PrayingHouseRepository;
import br.org.rpf.cagef.repository.VolunteerRepository;
import br.org.rpf.cagef.service.UserService;
import br.org.rpf.cagef.util.MusicianSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest
public class MusicianServiceImplTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Autowired
	@InjectMocks
	private MusicianServiceImpl musicianService;

	@MockBean
	private UserService userService;

	@MockBean
	private VolunteerRepository volunteerRepository;

	@MockBean
	private CityRepository cityRepository;

	@MockBean
	private MinistryOrPositionRepository ministryOrPositionRepository;

	@MockBean
	private PrayingHouseRepository prayingHouseRepository;
	
	@MockBean
	private MusicianRepository musicianRepository;
	
	@MockBean
	private InstrumentRepository instrumentRepository;

	@MockBean
	private BCryptPasswordEncoder passwordEncoder;

	@Before
	public void setup() {
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(UserServiceImplTest.generateUserNotAdmin());

		when(userService.isAdmin()).thenReturn(true);
		when(cityRepository.findById(any())).thenReturn(Optional.of(CityServiceImplTest.generateCity()));
		when(prayingHouseRepository.findById(any()))
				.thenReturn(Optional.of(PrayingHouseServiceImplTest.generatePrayingHouse()));
		when(instrumentRepository.findById(any())).thenReturn(Optional.of(InstrumentServiceImplTest.generateInstrument()));
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateMusicianNotAdmin()));
	}

	@Test
	public void findAllErrorTest() {
		when(musicianRepository.findAll(any(MusicianSpecification.class), any(Pageable.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.musicianService.findAll(MusicianRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
	}

	@Test
	public void findAllSuccessTest() {
		when(musicianRepository.findAll(any(MusicianSpecification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<Musician>(getMusiciansList()));
		Page<Musician> musicians = this.musicianService.findAll(MusicianRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
		List<Musician> musiciansList = musicians.getContent();
		assertEquals(4, musiciansList.size());
		assertEquals(1l, musiciansList.get(0).getId().longValue());
		assertEquals("Teste 1", musiciansList.get(0).getName());
		assertEquals("mail@mail.com", musiciansList.get(0).getEmail());
		assertEquals(null, musiciansList.get(0).getAddress());
		assertEquals("0123456789", musiciansList.get(0).getCelNumber());
		assertEquals(null, musiciansList.get(0).getCpf());
		assertEquals(null, musiciansList.get(0).getDistrict());
		assertEquals(null, musiciansList.get(0).getMaritalStatus());
		assertEquals("0123654789", musiciansList.get(0).getPhoneNumber());
		assertEquals(null, musiciansList.get(0).getPromise());
		assertEquals(null, musiciansList.get(0).getRg());
		assertEquals(null, musiciansList.get(0).getZipCode());
		assertEquals(1l, musiciansList.get(0).getCity().getId().longValue());
		assertEquals("Teste", musiciansList.get(0).getCity().getName());
		assertEquals("SP", musiciansList.get(0).getCity().getState());
		assertTrue(musiciansList.get(0).getCity().getRegional());

		assertEquals(1l, musiciansList.get(0).getNaturalness().getId().longValue());
		assertEquals("Teste", musiciansList.get(0).getNaturalness().getName());
		assertEquals("SP", musiciansList.get(0).getNaturalness().getState());
		assertTrue(musiciansList.get(0).getNaturalness().getRegional());

		assertEquals(1, musiciansList.get(0).getMinistryOrPosition().size());
		assertEquals(1l, musiciansList.get(0).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musiciansList.get(0).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musiciansList.get(0).getPrayingHouse().getReportCode());
		assertEquals("Teste", musiciansList.get(0).getPrayingHouse().getDistrict());
		
		assertEquals(1l, musiciansList.get(0).getInstrument().getId().longValue());
		assertEquals("Saxofone", musiciansList.get(0).getInstrument().getDescription());
		
		assertNotNull(musiciansList.get(0).getOficializationDate());
		assertNotNull(musiciansList.get(0).getRehearsalDate());
		assertNotNull(musiciansList.get(0).getRjmExamDate());
		assertNotNull(musiciansList.get(0).getOficialCultExamDate());
		
		assertEquals("Observation", musiciansList.get(0).getObservation());
		
		assertEquals(2l, musiciansList.get(1).getId().longValue());
		assertEquals("Teste 2", musiciansList.get(1).getName());
		assertEquals("mail2@mail.com", musiciansList.get(1).getEmail());
		assertEquals(null, musiciansList.get(1).getAddress());
		assertEquals("1123456789", musiciansList.get(1).getCelNumber());
		assertEquals(null, musiciansList.get(1).getCpf());
		assertEquals(null, musiciansList.get(1).getDistrict());
		assertEquals(null, musiciansList.get(1).getMaritalStatus());
		assertEquals("1123654789", musiciansList.get(1).getPhoneNumber());
		assertEquals(null, musiciansList.get(1).getPromise());
		assertEquals(null, musiciansList.get(1).getRg());
		assertEquals(null, musiciansList.get(1).getZipCode());
		assertEquals(1l, musiciansList.get(1).getCity().getId().longValue());
		assertEquals("Teste", musiciansList.get(1).getCity().getName());
		assertEquals("SP", musiciansList.get(1).getCity().getState());
		assertTrue(musiciansList.get(1).getCity().getRegional());

		assertEquals(1l, musiciansList.get(1).getNaturalness().getId().longValue());
		assertEquals("Teste", musiciansList.get(1).getNaturalness().getName());
		assertEquals("SP", musiciansList.get(1).getNaturalness().getState());
		assertTrue(musiciansList.get(1).getNaturalness().getRegional());

		assertEquals(1, musiciansList.get(1).getMinistryOrPosition().size());
		assertEquals(1l, musiciansList.get(1).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musiciansList.get(1).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musiciansList.get(1).getPrayingHouse().getReportCode());
		assertEquals("Teste", musiciansList.get(1).getPrayingHouse().getDistrict());
		
		assertEquals(1l, musiciansList.get(1).getInstrument().getId().longValue());
		assertEquals("Saxofone", musiciansList.get(1).getInstrument().getDescription());
		
		assertNotNull(musiciansList.get(1).getOficializationDate());
		assertNotNull(musiciansList.get(1).getRehearsalDate());
		assertNotNull(musiciansList.get(1).getRjmExamDate());
		assertNotNull(musiciansList.get(1).getOficialCultExamDate());
		
		assertEquals("Observation", musiciansList.get(1).getObservation());
		
		assertEquals(3l, musiciansList.get(2).getId().longValue());
		assertEquals("Teste 3", musiciansList.get(2).getName());
		assertEquals("mail3@mail.com", musiciansList.get(2).getEmail());
		assertEquals(null, musiciansList.get(2).getAddress());
		assertEquals("2123456789", musiciansList.get(2).getCelNumber());
		assertEquals(null, musiciansList.get(2).getCpf());
		assertEquals(null, musiciansList.get(2).getDistrict());
		assertEquals(null, musiciansList.get(2).getMaritalStatus());
		assertEquals("2123654789", musiciansList.get(2).getPhoneNumber());
		assertEquals(null, musiciansList.get(2).getPromise());
		assertEquals(null, musiciansList.get(2).getRg());
		assertEquals(null, musiciansList.get(2).getZipCode());
		assertEquals(1l, musiciansList.get(2).getCity().getId().longValue());
		assertEquals("Teste", musiciansList.get(2).getCity().getName());
		assertEquals("SP", musiciansList.get(2).getCity().getState());
		assertTrue(musiciansList.get(2).getCity().getRegional());

		assertEquals(1l, musiciansList.get(2).getNaturalness().getId().longValue());
		assertEquals("Teste", musiciansList.get(2).getNaturalness().getName());
		assertEquals("SP", musiciansList.get(2).getNaturalness().getState());
		assertTrue(musiciansList.get(2).getNaturalness().getRegional());

		assertEquals(1, musiciansList.get(2).getMinistryOrPosition().size());
		assertEquals(1l, musiciansList.get(2).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musiciansList.get(2).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musiciansList.get(2).getPrayingHouse().getReportCode());
		assertEquals("Teste", musiciansList.get(2).getPrayingHouse().getDistrict());
		
		assertEquals(1l, musiciansList.get(2).getInstrument().getId().longValue());
		assertEquals("Saxofone", musiciansList.get(2).getInstrument().getDescription());
		
		assertNotNull(musiciansList.get(2).getOficializationDate());
		assertNotNull(musiciansList.get(2).getRehearsalDate());
		assertNotNull(musiciansList.get(2).getRjmExamDate());
		assertNotNull(musiciansList.get(2).getOficialCultExamDate());
		
		assertEquals("Observation", musiciansList.get(2).getObservation());
		
		assertEquals(4l, musiciansList.get(3).getId().longValue());
		assertEquals("Teste 4", musiciansList.get(3).getName());
		assertEquals("mail4@mail.com", musiciansList.get(3).getEmail());
		assertEquals(null, musiciansList.get(3).getAddress());
		assertEquals("3123456789", musiciansList.get(3).getCelNumber());
		assertEquals(null, musiciansList.get(3).getCpf());
		assertEquals(null, musiciansList.get(3).getDistrict());
		assertEquals(null, musiciansList.get(3).getMaritalStatus());
		assertEquals("3123654789", musiciansList.get(3).getPhoneNumber());
		assertEquals(null, musiciansList.get(3).getPromise());
		assertEquals(null, musiciansList.get(3).getRg());
		assertEquals(null, musiciansList.get(3).getZipCode());
		assertEquals(1l, musiciansList.get(3).getCity().getId().longValue());
		assertEquals("Teste", musiciansList.get(3).getCity().getName());
		assertEquals("SP", musiciansList.get(3).getCity().getState());
		assertTrue(musiciansList.get(3).getCity().getRegional());

		assertEquals(1l, musiciansList.get(3).getNaturalness().getId().longValue());
		assertEquals("Teste", musiciansList.get(3).getNaturalness().getName());
		assertEquals("SP", musiciansList.get(3).getNaturalness().getState());
		assertTrue(musiciansList.get(3).getNaturalness().getRegional());

		assertEquals(1, musiciansList.get(3).getMinistryOrPosition().size());
		assertEquals(1l, musiciansList.get(3).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musiciansList.get(3).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musiciansList.get(3).getPrayingHouse().getReportCode());
		assertEquals("Teste", musiciansList.get(3).getPrayingHouse().getDistrict());
		
		assertEquals(1l, musiciansList.get(3).getInstrument().getId().longValue());
		assertEquals("Saxofone", musiciansList.get(3).getInstrument().getDescription());
		
		assertNotNull(musiciansList.get(3).getOficializationDate());
		assertNotNull(musiciansList.get(3).getRehearsalDate());
		assertNotNull(musiciansList.get(3).getRjmExamDate());
		assertNotNull(musiciansList.get(3).getOficialCultExamDate());
		
		assertEquals("Observation", musiciansList.get(3).getObservation());
		
		verify(musicianRepository).findAll(any(MusicianSpecification.class), any(Pageable.class));
	}
	
	@Test
	public void findAllNotAdminSuccessTest() {
		when(userService.isAnyAdmin()).thenReturn(true);
		when(musicianRepository.findAll(any(MusicianSpecification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<Musician>(getMusiciansList()));
		Page<Musician> musicians = this.musicianService.findAll(MusicianRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
		List<Musician> musiciansList = musicians.getContent();
		assertEquals(4, musiciansList.size());
		assertEquals(1l, musiciansList.get(0).getId().longValue());
		assertEquals("Teste 1", musiciansList.get(0).getName());
		assertEquals("mail@mail.com", musiciansList.get(0).getEmail());
		assertEquals(null, musiciansList.get(0).getAddress());
		assertEquals("0123456789", musiciansList.get(0).getCelNumber());
		assertEquals(null, musiciansList.get(0).getCpf());
		assertEquals(null, musiciansList.get(0).getDistrict());
		assertEquals(null, musiciansList.get(0).getMaritalStatus());
		assertEquals("0123654789", musiciansList.get(0).getPhoneNumber());
		assertEquals(null, musiciansList.get(0).getPromise());
		assertEquals(null, musiciansList.get(0).getRg());
		assertEquals(null, musiciansList.get(0).getZipCode());
		assertEquals(1l, musiciansList.get(0).getCity().getId().longValue());
		assertEquals("Teste", musiciansList.get(0).getCity().getName());
		assertEquals("SP", musiciansList.get(0).getCity().getState());
		assertTrue(musiciansList.get(0).getCity().getRegional());

		assertEquals(1l, musiciansList.get(0).getNaturalness().getId().longValue());
		assertEquals("Teste", musiciansList.get(0).getNaturalness().getName());
		assertEquals("SP", musiciansList.get(0).getNaturalness().getState());
		assertTrue(musiciansList.get(0).getNaturalness().getRegional());

		assertEquals(1, musiciansList.get(0).getMinistryOrPosition().size());
		assertEquals(1l, musiciansList.get(0).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musiciansList.get(0).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musiciansList.get(0).getPrayingHouse().getReportCode());
		assertEquals("Teste", musiciansList.get(0).getPrayingHouse().getDistrict());
		
		assertEquals(1l, musiciansList.get(0).getInstrument().getId().longValue());
		assertEquals("Saxofone", musiciansList.get(0).getInstrument().getDescription());
		
		assertNotNull(musiciansList.get(0).getOficializationDate());
		assertNotNull(musiciansList.get(0).getRehearsalDate());
		assertNotNull(musiciansList.get(0).getRjmExamDate());
		assertNotNull(musiciansList.get(0).getOficialCultExamDate());
		
		assertEquals("Observation", musiciansList.get(0).getObservation());
		
		assertEquals(2l, musiciansList.get(1).getId().longValue());
		assertEquals("Teste 2", musiciansList.get(1).getName());
		assertEquals("mail2@mail.com", musiciansList.get(1).getEmail());
		assertEquals(null, musiciansList.get(1).getAddress());
		assertEquals("1123456789", musiciansList.get(1).getCelNumber());
		assertEquals(null, musiciansList.get(1).getCpf());
		assertEquals(null, musiciansList.get(1).getDistrict());
		assertEquals(null, musiciansList.get(1).getMaritalStatus());
		assertEquals("1123654789", musiciansList.get(1).getPhoneNumber());
		assertEquals(null, musiciansList.get(1).getPromise());
		assertEquals(null, musiciansList.get(1).getRg());
		assertEquals(null, musiciansList.get(1).getZipCode());
		assertEquals(1l, musiciansList.get(1).getCity().getId().longValue());
		assertEquals("Teste", musiciansList.get(1).getCity().getName());
		assertEquals("SP", musiciansList.get(1).getCity().getState());
		assertTrue(musiciansList.get(1).getCity().getRegional());

		assertEquals(1l, musiciansList.get(1).getNaturalness().getId().longValue());
		assertEquals("Teste", musiciansList.get(1).getNaturalness().getName());
		assertEquals("SP", musiciansList.get(1).getNaturalness().getState());
		assertTrue(musiciansList.get(1).getNaturalness().getRegional());

		assertEquals(1, musiciansList.get(1).getMinistryOrPosition().size());
		assertEquals(1l, musiciansList.get(1).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musiciansList.get(1).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musiciansList.get(1).getPrayingHouse().getReportCode());
		assertEquals("Teste", musiciansList.get(1).getPrayingHouse().getDistrict());
		
		assertEquals(1l, musiciansList.get(1).getInstrument().getId().longValue());
		assertEquals("Saxofone", musiciansList.get(1).getInstrument().getDescription());
		
		assertNotNull(musiciansList.get(1).getOficializationDate());
		assertNotNull(musiciansList.get(1).getRehearsalDate());
		assertNotNull(musiciansList.get(1).getRjmExamDate());
		assertNotNull(musiciansList.get(1).getOficialCultExamDate());
		
		assertEquals("Observation", musiciansList.get(1).getObservation());
		
		assertEquals(3l, musiciansList.get(2).getId().longValue());
		assertEquals("Teste 3", musiciansList.get(2).getName());
		assertEquals("mail3@mail.com", musiciansList.get(2).getEmail());
		assertEquals(null, musiciansList.get(2).getAddress());
		assertEquals("2123456789", musiciansList.get(2).getCelNumber());
		assertEquals(null, musiciansList.get(2).getCpf());
		assertEquals(null, musiciansList.get(2).getDistrict());
		assertEquals(null, musiciansList.get(2).getMaritalStatus());
		assertEquals("2123654789", musiciansList.get(2).getPhoneNumber());
		assertEquals(null, musiciansList.get(2).getPromise());
		assertEquals(null, musiciansList.get(2).getRg());
		assertEquals(null, musiciansList.get(2).getZipCode());
		assertEquals(1l, musiciansList.get(2).getCity().getId().longValue());
		assertEquals("Teste", musiciansList.get(2).getCity().getName());
		assertEquals("SP", musiciansList.get(2).getCity().getState());
		assertTrue(musiciansList.get(2).getCity().getRegional());

		assertEquals(1l, musiciansList.get(2).getNaturalness().getId().longValue());
		assertEquals("Teste", musiciansList.get(2).getNaturalness().getName());
		assertEquals("SP", musiciansList.get(2).getNaturalness().getState());
		assertTrue(musiciansList.get(2).getNaturalness().getRegional());

		assertEquals(1, musiciansList.get(2).getMinistryOrPosition().size());
		assertEquals(1l, musiciansList.get(2).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musiciansList.get(2).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musiciansList.get(2).getPrayingHouse().getReportCode());
		assertEquals("Teste", musiciansList.get(2).getPrayingHouse().getDistrict());
		
		assertEquals(1l, musiciansList.get(2).getInstrument().getId().longValue());
		assertEquals("Saxofone", musiciansList.get(2).getInstrument().getDescription());
		
		assertNotNull(musiciansList.get(2).getOficializationDate());
		assertNotNull(musiciansList.get(2).getRehearsalDate());
		assertNotNull(musiciansList.get(2).getRjmExamDate());
		assertNotNull(musiciansList.get(2).getOficialCultExamDate());
		
		assertEquals("Observation", musiciansList.get(2).getObservation());
		
		assertEquals(4l, musiciansList.get(3).getId().longValue());
		assertEquals("Teste 4", musiciansList.get(3).getName());
		assertEquals("mail4@mail.com", musiciansList.get(3).getEmail());
		assertEquals(null, musiciansList.get(3).getAddress());
		assertEquals("3123456789", musiciansList.get(3).getCelNumber());
		assertEquals(null, musiciansList.get(3).getCpf());
		assertEquals(null, musiciansList.get(3).getDistrict());
		assertEquals(null, musiciansList.get(3).getMaritalStatus());
		assertEquals("3123654789", musiciansList.get(3).getPhoneNumber());
		assertEquals(null, musiciansList.get(3).getPromise());
		assertEquals(null, musiciansList.get(3).getRg());
		assertEquals(null, musiciansList.get(3).getZipCode());
		assertEquals(1l, musiciansList.get(3).getCity().getId().longValue());
		assertEquals("Teste", musiciansList.get(3).getCity().getName());
		assertEquals("SP", musiciansList.get(3).getCity().getState());
		assertTrue(musiciansList.get(3).getCity().getRegional());

		assertEquals(1l, musiciansList.get(3).getNaturalness().getId().longValue());
		assertEquals("Teste", musiciansList.get(3).getNaturalness().getName());
		assertEquals("SP", musiciansList.get(3).getNaturalness().getState());
		assertTrue(musiciansList.get(3).getNaturalness().getRegional());

		assertEquals(1, musiciansList.get(3).getMinistryOrPosition().size());
		assertEquals(1l, musiciansList.get(3).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musiciansList.get(3).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musiciansList.get(3).getPrayingHouse().getReportCode());
		assertEquals("Teste", musiciansList.get(3).getPrayingHouse().getDistrict());
		
		assertEquals(1l, musiciansList.get(3).getInstrument().getId().longValue());
		assertEquals("Saxofone", musiciansList.get(3).getInstrument().getDescription());
		
		assertNotNull(musiciansList.get(3).getOficializationDate());
		assertNotNull(musiciansList.get(3).getRehearsalDate());
		assertNotNull(musiciansList.get(3).getRjmExamDate());
		assertNotNull(musiciansList.get(3).getOficialCultExamDate());
		
		assertEquals("Observation", musiciansList.get(3).getObservation());
		
		verify(musicianRepository).findAll(any(MusicianSpecification.class), any(Pageable.class));
	}

	@Test(expected = ObjectNotFoundException.class)
	public void findByIdNotFoundTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.empty());

		this.musicianService.byId(1l);
	}

	@Test
	public void findByIdSuccessTest() {
		Musician musician = (Musician) this.musicianService.byId(1l);
		assertEquals(1l, musician.getId().longValue());
		assertEquals("Teste 1", musician.getName());
		assertEquals("mail@mail.com", musician.getEmail());
		assertEquals(null, musician.getAddress());
		assertEquals("0123456789", musician.getCelNumber());
		assertEquals(null, musician.getCpf());
		assertEquals(null, musician.getDistrict());
		assertEquals(null, musician.getMaritalStatus());
		assertEquals("0123654789", musician.getPhoneNumber());
		assertEquals(null, musician.getPromise());
		assertEquals(null, musician.getRg());
		assertEquals(null, musician.getZipCode());
		assertEquals(1l, musician.getCity().getId().longValue());
		assertEquals("Teste", musician.getCity().getName());
		assertEquals("SP", musician.getCity().getState());
		assertTrue(musician.getCity().getRegional());

		assertEquals(1l, musician.getNaturalness().getId().longValue());
		assertEquals("Teste", musician.getNaturalness().getName());
		assertEquals("SP", musician.getNaturalness().getState());
		assertTrue(musician.getNaturalness().getRegional());

		assertEquals(1, musician.getMinistryOrPosition().size());
		assertEquals(1l, musician.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musician.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musician.getPrayingHouse().getReportCode());
		assertEquals("Teste", musician.getPrayingHouse().getDistrict());
		
		assertEquals(1l, musician.getInstrument().getId().longValue());
		assertEquals("Saxofone", musician.getInstrument().getDescription());
		
		assertNotNull(musician.getOficializationDate());
		assertNotNull(musician.getRehearsalDate());
		assertNotNull(musician.getRjmExamDate());
		assertNotNull(musician.getOficialCultExamDate());
		
		assertEquals("Observation", musician.getObservation());
		
		verify(volunteerRepository).findById(1l);
	}

	@Test(expected = Exception.class)
	public void saveErrorTest() {
		when(musicianRepository.save(any())).thenThrow(new Exception("Error"));

		this.musicianService.save(generateMusicianDto());
	}

	@Test
	public void saveSuccessTest() {
		when(musicianRepository.save(any(Musician.class))).thenReturn(generateMusicianNotAdmin());

		Musician musician = this.musicianService.save(generateMusicianDto());
		assertEquals(1l, musician.getId().longValue());
		assertEquals(1l, musician.getId().longValue());
		assertEquals("Teste 1", musician.getName());
		assertEquals("mail@mail.com", musician.getEmail());
		assertEquals(null, musician.getAddress());
		assertEquals("0123456789", musician.getCelNumber());
		assertEquals(null, musician.getCpf());
		assertEquals(null, musician.getDistrict());
		assertEquals(null, musician.getMaritalStatus());
		assertEquals("0123654789", musician.getPhoneNumber());
		assertEquals(null, musician.getPromise());
		assertEquals(null, musician.getRg());
		assertEquals(null, musician.getZipCode());
		assertEquals(1l, musician.getCity().getId().longValue());
		assertEquals("Teste", musician.getCity().getName());
		assertEquals("SP", musician.getCity().getState());
		assertTrue(musician.getCity().getRegional());

		assertEquals(1l, musician.getNaturalness().getId().longValue());
		assertEquals("Teste", musician.getNaturalness().getName());
		assertEquals("SP", musician.getNaturalness().getState());
		assertTrue(musician.getNaturalness().getRegional());

		assertEquals(1, musician.getMinistryOrPosition().size());
		assertEquals(1l, musician.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musician.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musician.getPrayingHouse().getReportCode());
		assertEquals("Teste", musician.getPrayingHouse().getDistrict());
		
		assertEquals(1l, musician.getInstrument().getId().longValue());
		assertEquals("Saxofone", musician.getInstrument().getDescription());
		
		assertNotNull(musician.getOficializationDate());
		assertNotNull(musician.getRehearsalDate());
		assertNotNull(musician.getRjmExamDate());
		assertNotNull(musician.getOficialCultExamDate());
		
		assertEquals("Observation", musician.getObservation());
		
		verify(musicianRepository).save(any(Musician.class));
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void saveCityNotFoundTest() {
		when(musicianRepository.save(any())).thenReturn(generateMusicianNotAdmin());
		when(cityRepository.findById(1l)).thenReturn(Optional.empty());

		this.musicianService.save(generateMusicianDto());
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void saveInstrumentNotFoundTest() {
		when(musicianRepository.save(any())).thenReturn(generateMusicianNotAdmin());
		when(instrumentRepository.findById(1l)).thenReturn(Optional.empty());

		this.musicianService.save(generateMusicianDto());
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void savePrayinghouseNotFoundTest() {
		when(musicianRepository.save(any())).thenReturn(generateMusicianNotAdmin());
		when(prayingHouseRepository.findById("abc123")).thenReturn(Optional.empty());

		this.musicianService.save(generateMusicianDto());
	}
	
	@Test
	public void saveWithoutPrayingHouseSuccessTest() {
		MusicianDTO musicianDto = generateMusicianDto();
		musicianDto.setPrayingHouse(null);
		when(musicianRepository.save(any(Musician.class))).thenReturn(generateMusicianNotAdmin());
		
		Musician musician = this.musicianService.save(musicianDto);
		assertEquals(1l, musician.getId().longValue());
		assertEquals(1l, musician.getId().longValue());
		assertEquals("Teste 1", musician.getName());
		assertEquals("mail@mail.com", musician.getEmail());
		assertEquals(null, musician.getAddress());
		assertEquals("0123456789", musician.getCelNumber());
		assertEquals(null, musician.getCpf());
		assertEquals(null, musician.getDistrict());
		assertEquals(null, musician.getMaritalStatus());
		assertEquals("0123654789", musician.getPhoneNumber());
		assertEquals(null, musician.getPromise());
		assertEquals(null, musician.getRg());
		assertEquals(null, musician.getZipCode());
		assertEquals(1l, musician.getCity().getId().longValue());
		assertEquals("Teste", musician.getCity().getName());
		assertEquals("SP", musician.getCity().getState());
		assertTrue(musician.getCity().getRegional());

		assertEquals(1l, musician.getNaturalness().getId().longValue());
		assertEquals("Teste", musician.getNaturalness().getName());
		assertEquals("SP", musician.getNaturalness().getState());
		assertTrue(musician.getNaturalness().getRegional());

		assertEquals(1, musician.getMinistryOrPosition().size());
		assertEquals(1l, musician.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musician.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musician.getPrayingHouse().getReportCode());
		assertEquals("Teste", musician.getPrayingHouse().getDistrict());
		
		assertEquals(1l, musician.getInstrument().getId().longValue());
		assertEquals("Saxofone", musician.getInstrument().getDescription());
		
		assertNotNull(musician.getOficializationDate());
		assertNotNull(musician.getRehearsalDate());
		assertNotNull(musician.getRjmExamDate());
		assertNotNull(musician.getOficialCultExamDate());
		
		assertEquals("Observation", musician.getObservation());
		verify(prayingHouseRepository, Mockito.times(0)).findById("abc123");
		verify(musicianRepository).save(any(Musician.class));
	}
	
	@Test
	public void saveWithoutPrayingHouseIdSuccessTest() {
		MusicianDTO musicianDto = generateMusicianDto();
		musicianDto.getPrayingHouse().setReportCode(null);
		when(musicianRepository.save(any())).thenReturn(generateMusicianNotAdmin());
		
		Musician musician = this.musicianService.save(musicianDto);
		assertEquals(1l, musician.getId().longValue());
		assertEquals(1l, musician.getId().longValue());
		assertEquals("Teste 1", musician.getName());
		assertEquals("mail@mail.com", musician.getEmail());
		assertEquals(null, musician.getAddress());
		assertEquals("0123456789", musician.getCelNumber());
		assertEquals(null, musician.getCpf());
		assertEquals(null, musician.getDistrict());
		assertEquals(null, musician.getMaritalStatus());
		assertEquals("0123654789", musician.getPhoneNumber());
		assertEquals(null, musician.getPromise());
		assertEquals(null, musician.getRg());
		assertEquals(null, musician.getZipCode());
		assertEquals(1l, musician.getCity().getId().longValue());
		assertEquals("Teste", musician.getCity().getName());
		assertEquals("SP", musician.getCity().getState());
		assertTrue(musician.getCity().getRegional());

		assertEquals(1l, musician.getNaturalness().getId().longValue());
		assertEquals("Teste", musician.getNaturalness().getName());
		assertEquals("SP", musician.getNaturalness().getState());
		assertTrue(musician.getNaturalness().getRegional());

		assertEquals(1, musician.getMinistryOrPosition().size());
		assertEquals(1l, musician.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musician.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musician.getPrayingHouse().getReportCode());
		assertEquals("Teste", musician.getPrayingHouse().getDistrict());
		
		assertEquals(1l, musician.getInstrument().getId().longValue());
		assertEquals("Saxofone", musician.getInstrument().getDescription());
		
		assertNotNull(musician.getOficializationDate());
		assertNotNull(musician.getRehearsalDate());
		assertNotNull(musician.getRjmExamDate());
		assertNotNull(musician.getOficialCultExamDate());
		
		assertEquals("Observation", musician.getObservation());
		verify(prayingHouseRepository, Mockito.times(0)).findById("abc123");
		verify(musicianRepository).save(any(Musician.class));
	}
	
	@Test(expected = Exception.class)
	public void updateErrorTest() {
		when(musicianRepository.save(any())).thenThrow(new Exception("Error"));

		this.musicianService.save(generateMusicianDto());
	}

	@Test
	public void updateSuccessTest() {
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any())).thenReturn(generateMusicianNotAdmin());

		Musician musician = (Musician) this.musicianService.update(1l, generateMusicianDto());
		assertEquals(1l, musician.getId().longValue());
		assertEquals("Teste 1", musician.getName());
		assertEquals("mail@mail.com", musician.getEmail());
		assertEquals(null, musician.getAddress());
		assertEquals("0123456789", musician.getCelNumber());
		assertEquals(null, musician.getCpf());
		assertEquals(null, musician.getDistrict());
		assertEquals(null, musician.getMaritalStatus());
		assertEquals("0123654789", musician.getPhoneNumber());
		assertEquals(null, musician.getPromise());
		assertEquals(null, musician.getRg());
		assertEquals(null, musician.getZipCode());
		assertEquals(1l, musician.getCity().getId().longValue());
		assertEquals("Teste", musician.getCity().getName());
		assertEquals("SP", musician.getCity().getState());
		assertTrue(musician.getCity().getRegional());

		assertEquals(1l, musician.getNaturalness().getId().longValue());
		assertEquals("Teste", musician.getNaturalness().getName());
		assertEquals("SP", musician.getNaturalness().getState());
		assertTrue(musician.getNaturalness().getRegional());

		assertEquals(1, musician.getMinistryOrPosition().size());
		assertEquals(1l, musician.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musician.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musician.getPrayingHouse().getReportCode());
		assertEquals("Teste", musician.getPrayingHouse().getDistrict());
		
		assertEquals(1l, musician.getInstrument().getId().longValue());
		assertEquals("Saxofone", musician.getInstrument().getDescription());
		
		assertNotNull(musician.getOficializationDate());
		assertNotNull(musician.getRehearsalDate());
		assertNotNull(musician.getRjmExamDate());
		assertNotNull(musician.getOficialCultExamDate());
		
		assertEquals("Observation", musician.getObservation());
		
		verify(musicianRepository).save(any(Musician.class));
	}
	
	@Test
	public void updateWithMusicianRelationshipSuccessTest() {
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any())).thenReturn(generateMusicianNotAdmin());
		when(musicianRepository.findById(1l)).thenReturn(Optional.of(MusicianServiceImplTest.generateMusicianNotAdmin()));

		Musician musician = (Musician) this.musicianService.update(1l, generateMusicianDto());
		assertEquals(1l, musician.getId().longValue());
		assertEquals("Teste 1", musician.getName());
		assertEquals("mail@mail.com", musician.getEmail());
		assertEquals(null, musician.getAddress());
		assertEquals("0123456789", musician.getCelNumber());
		assertEquals(null, musician.getCpf());
		assertEquals(null, musician.getDistrict());
		assertEquals(null, musician.getMaritalStatus());
		assertEquals("0123654789", musician.getPhoneNumber());
		assertEquals(null, musician.getPromise());
		assertEquals(null, musician.getRg());
		assertEquals(null, musician.getZipCode());
		assertEquals(1l, musician.getCity().getId().longValue());
		assertEquals("Teste", musician.getCity().getName());
		assertEquals("SP", musician.getCity().getState());
		assertTrue(musician.getCity().getRegional());

		assertEquals(1l, musician.getNaturalness().getId().longValue());
		assertEquals("Teste", musician.getNaturalness().getName());
		assertEquals("SP", musician.getNaturalness().getState());
		assertTrue(musician.getNaturalness().getRegional());

		assertEquals(1, musician.getMinistryOrPosition().size());
		assertEquals(1l, musician.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", musician.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", musician.getPrayingHouse().getReportCode());
		assertEquals("Teste", musician.getPrayingHouse().getDistrict());
		
		assertEquals(1l, musician.getInstrument().getId().longValue());
		assertEquals("Saxofone", musician.getInstrument().getDescription());
		
		assertNotNull(musician.getOficializationDate());
		assertNotNull(musician.getRehearsalDate());
		assertNotNull(musician.getRjmExamDate());
		assertNotNull(musician.getOficialCultExamDate());
		
		assertEquals("Observation", musician.getObservation());
		
		verify(musicianRepository).findById(1l);
		verify(volunteerRepository, Mockito.times(0)).save(any());
		verify(musicianRepository).save(any());
	}

	@Test(expected = ObjectNotFoundException.class)
	public void updateCityNotFoundTest() {
		when(cityRepository.findById(any())).thenReturn(Optional.ofNullable(null));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any())).thenReturn(generateMusicianNotAdmin());

		this.musicianService.update(1l, generateMusicianDto());
	}

	@Test(expected = Exception.class)
	public void removeErrorTest() {
		Mockito.doThrow(new Exception("Error")).when(musicianRepository).delete(any());

		this.musicianService.remove(1l);
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void removeNotFoundTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.empty());

		this.musicianService.remove(1l);
	}


	@Test
	public void removeSuccessTest() {
		Mockito.doNothing().when(musicianRepository).delete(any(Musician.class));

		ExpectedException.none();
		
		this.musicianService.remove(1l);
		
		verify(volunteerRepository).findById(1l);
		verify(volunteerRepository).delete(any(Musician.class));
	}

	public static List<Musician> getMusiciansList() {
		List<Musician> list = new ArrayList<>();

		list.add(Musician.builder().id(1l).name("Teste 1").city(CityServiceImplTest.generateCity())
				.naturalness(CityServiceImplTest.generateCity()).phoneNumber("0123654789").celNumber("0123456789")
				.email("mail@mail.com").dateOfBirth(LocalDate.now()).updatedAt(LocalDate.now())
				.createdAt(LocalDate.now()).prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouse())
				.ministryOrPosition(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))
				.instrument(InstrumentServiceImplTest.generateInstrument()).oficializationDate(LocalDate.now()).rehearsalDate(LocalDate.now())
				.rjmExamDate(LocalDate.now()).oficialCultExamDate(LocalDate.now()).observation("Observation").build());
		
		list.add(Musician.builder().id(2l).name("Teste 2").city(CityServiceImplTest.generateCity())
				.naturalness(CityServiceImplTest.generateCity()).phoneNumber("1123654789").celNumber("1123456789")
				.email("mail2@mail.com").dateOfBirth(LocalDate.now()).updatedAt(LocalDate.now())
				.createdAt(LocalDate.now()).prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouse())
				.ministryOrPosition(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))
				.instrument(InstrumentServiceImplTest.generateInstrument()).oficializationDate(LocalDate.now()).rehearsalDate(LocalDate.now())
				.rjmExamDate(LocalDate.now()).oficialCultExamDate(LocalDate.now()).observation("Observation").build());
		
		list.add(Musician.builder().id(3l).name("Teste 3").city(CityServiceImplTest.generateCity())
				.naturalness(CityServiceImplTest.generateCity()).phoneNumber("2123654789").celNumber("2123456789")
				.email("mail3@mail.com").dateOfBirth(LocalDate.now()).updatedAt(LocalDate.now())
				.createdAt(LocalDate.now()).prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouse())
				.ministryOrPosition(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))
				.instrument(InstrumentServiceImplTest.generateInstrument()).oficializationDate(LocalDate.now()).rehearsalDate(LocalDate.now())
				.rjmExamDate(LocalDate.now()).oficialCultExamDate(LocalDate.now()).observation("Observation").build());
		
		list.add(Musician.builder().id(4l).name("Teste 4").city(CityServiceImplTest.generateCity())
				.naturalness(CityServiceImplTest.generateCity()).phoneNumber("3123654789").celNumber("3123456789")
				.email("mail4@mail.com").dateOfBirth(LocalDate.now()).updatedAt(LocalDate.now())
				.createdAt(LocalDate.now()).prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouse())
				.ministryOrPosition(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))
				.instrument(InstrumentServiceImplTest.generateInstrument()).oficializationDate(LocalDate.now()).rehearsalDate(LocalDate.now())
				.rjmExamDate(LocalDate.now()).oficialCultExamDate(LocalDate.now()).observation("Observation").build());

		return list;
	}

	public static Musician generateMusicianNotAdmin() {
		return Musician.builder().id(1l).name("Teste 1").city(CityServiceImplTest.generateCity())
				.naturalness(CityServiceImplTest.generateCity()).phoneNumber("0123654789").celNumber("0123456789")
				.email("mail@mail.com").dateOfBirth(LocalDate.now()).updatedAt(LocalDate.now())
				.createdAt(LocalDate.now()).prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouse())
				.ministryOrPosition(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))
				.instrument(InstrumentServiceImplTest.generateInstrument()).oficializationDate(LocalDate.now()).rehearsalDate(LocalDate.now())
				.rjmExamDate(LocalDate.now()).oficialCultExamDate(LocalDate.now()).observation("Observation").build();
	}

	public static MusicianDTO generateMusicianDto() {
		return MusicianDTO.builder().id(1l).name("Teste").city(CityServiceImplTest.generateCityInnerDTO())
				.phoneNumber("0123654789").celNumber("0123654789").email("mail@mail.com").dateOfBirth(LocalDate.now())
				.prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouseInnerDTO())
				.ministryOrPosition(MinistryOrPositionServiceImplTest.generateMinistryOrPositionMusicianInnerDTO())
				.instrument(generateInstrumentInnerDTO()).oficializationDate(LocalDate.now())
				.rehearsalDate(LocalDate.now()).rjmExamDate(LocalDate.now()).oficialCultExamDate(LocalDate.now())
				.observation("Observation").build();
	}
	
	public static MusicianDTO generateMusicianDto2() {
		return MusicianDTO.builder().id(1l).name("Teste").city(CityServiceImplTest.generateCityInnerDTO())
				.phoneNumber("0123654789").celNumber("0123654789").email("mail@mail.com")
				.prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouseInnerDTO())
				.ministryOrPosition(MinistryOrPositionServiceImplTest.generateMinistryOrPositionMusicianInnerDTO())
				.instrument(generateInstrumentInnerDTO()).observation("Observation").build();
	}

	private static InstrumentInnerDTO generateInstrumentInnerDTO() {
		return new InstrumentInnerDTO(1l);
	}
}
