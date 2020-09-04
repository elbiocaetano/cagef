package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertEquals;
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

import br.org.rpf.cagef.dto.http.request.city.VolunteerRequestParamsDTO;
import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.Musician;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.entity.enums.MaritalStatusEnum;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.InstrumentRepository;
import br.org.rpf.cagef.repository.MinistryOrPositionRepository;
import br.org.rpf.cagef.repository.MusicianRepository;
import br.org.rpf.cagef.repository.PrayingHouseRepository;
import br.org.rpf.cagef.repository.VolunteerRepository;
import br.org.rpf.cagef.service.UserService;
import br.org.rpf.cagef.util.VolunteerSpecification;

@RunWith(SpringRunner.class)
@SpringBootTest
public class VolunteerServiceImplTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Autowired
	@InjectMocks
	private VolunteerServiceImpl volunteerService;

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
	}

	@Test
	public void findAllErrorTest() {
		when(volunteerRepository.findAll(any(VolunteerSpecification.class), any(Pageable.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.volunteerService.findAll(VolunteerRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
	}

	@Test
	public void findAllSuccessTest() {
		when(volunteerRepository.findAll(any(VolunteerSpecification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<Volunteer>(getVolunteersList()));
		Page<Volunteer> volunteers = this.volunteerService.findAll(VolunteerRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
		List<Volunteer> volunteersList = volunteers.getContent();
		assertEquals(4, volunteersList.size());
		assertEquals(1l, volunteersList.get(0).getId().longValue());
		assertEquals("Teste 1", volunteersList.get(0).getName());
		assertEquals("mail@mail.com", volunteersList.get(0).getEmail());
		assertEquals("Teste", volunteersList.get(0).getAddress());
		assertEquals("0123456789", volunteersList.get(0).getCelNumber());
		assertEquals("14256895789", volunteersList.get(0).getCpf());
		assertEquals("Teste", volunteersList.get(0).getDistrict());
		assertEquals("CASADO", volunteersList.get(0).getMaritalStatus());
		assertEquals("0123654789", volunteersList.get(0).getPhoneNumber());
		assertEquals("SIM", volunteersList.get(0).getPromise());
		assertEquals("12547895X", volunteersList.get(0).getRg());
		assertEquals("12345678", volunteersList.get(0).getZipCode());
		assertEquals(1l, volunteersList.get(0).getCity().getId().longValue());
		assertEquals("Teste", volunteersList.get(0).getCity().getName());
		assertEquals("SP", volunteersList.get(0).getCity().getState());
		assertTrue(volunteersList.get(0).getCity().getRegional());

		assertEquals(1l, volunteersList.get(0).getNaturalness().getId().longValue());
		assertEquals("Teste", volunteersList.get(0).getNaturalness().getName());
		assertEquals("SP", volunteersList.get(0).getNaturalness().getState());
		assertTrue(volunteersList.get(0).getNaturalness().getRegional());

		assertEquals(1, volunteersList.get(0).getMinistryOrPosition().size());
		assertEquals(1l, volunteersList.get(0).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteersList.get(0).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteersList.get(0).getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteersList.get(0).getPrayingHouse().getDistrict());

		assertEquals(2l, volunteersList.get(1).getId().longValue());
		assertEquals( "Teste 2", volunteersList.get(1).getName());
		assertEquals("mail@mail.com", volunteersList.get(1).getEmail());
		assertEquals("Teste", volunteersList.get(1).getAddress());
		assertEquals("0123456789", volunteersList.get(1).getCelNumber());
		assertEquals("14256895789", volunteersList.get(1).getCpf());
		assertEquals("Teste", volunteersList.get(1).getDistrict());
		assertEquals("CASADO", volunteersList.get(1).getMaritalStatus());
		assertEquals("0123654789", volunteersList.get(1).getPhoneNumber());
		assertEquals("SIM", volunteersList.get(1).getPromise());
		assertEquals("12547895X", volunteersList.get(1).getRg());
		assertEquals("12345678", volunteersList.get(1).getZipCode());
		assertEquals(1l, volunteersList.get(1).getCity().getId().longValue());
		assertEquals("Teste", volunteersList.get(1).getCity().getName());
		assertEquals("SP", volunteersList.get(1).getCity().getState());
		assertTrue(volunteersList.get(1).getCity().getRegional());

		assertEquals(1l, volunteersList.get(1).getNaturalness().getId().longValue());
		assertEquals("Teste", volunteersList.get(1).getNaturalness().getName());
		assertEquals("SP", volunteersList.get(1).getNaturalness().getState());
		assertTrue(volunteersList.get(1).getNaturalness().getRegional());

		assertEquals(1, volunteersList.get(1).getMinistryOrPosition().size());
		assertEquals(1l, volunteersList.get(1).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteersList.get(1).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteersList.get(1).getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteersList.get(1).getPrayingHouse().getDistrict());

		assertEquals(3l, volunteersList.get(2).getId().longValue());
		assertEquals("Teste 3", volunteersList.get(2).getName());
		assertEquals("mail@mail.com", volunteersList.get(2).getEmail());
		assertEquals("Teste", volunteersList.get(2).getAddress());
		assertEquals("0123456789", volunteersList.get(2).getCelNumber());
		assertEquals("14256895789", volunteersList.get(2).getCpf());
		assertEquals("Teste", volunteersList.get(2).getDistrict());
		assertEquals("CASADO", volunteersList.get(2).getMaritalStatus());
		assertEquals("0123654789", volunteersList.get(2).getPhoneNumber());
		assertEquals("SIM", volunteersList.get(2).getPromise());
		assertEquals("12547895X", volunteersList.get(2).getRg());
		assertEquals("12345678", volunteersList.get(2).getZipCode());
		assertEquals(1l, volunteersList.get(2).getCity().getId().longValue());
		assertEquals("Teste", volunteersList.get(2).getCity().getName());
		assertEquals("SP", volunteersList.get(2).getCity().getState());
		assertTrue(volunteersList.get(2).getCity().getRegional());

		assertEquals(1l, volunteersList.get(2).getNaturalness().getId().longValue());
		assertEquals("Teste", volunteersList.get(2).getNaturalness().getName());
		assertEquals("SP", volunteersList.get(2).getNaturalness().getState());
		assertTrue(volunteersList.get(2).getNaturalness().getRegional());

		assertEquals(1, volunteersList.get(2).getMinistryOrPosition().size());
		assertEquals(1l, volunteersList.get(2).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteersList.get(2).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteersList.get(2).getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteersList.get(2).getPrayingHouse().getDistrict());

		assertEquals(4l, volunteersList.get(3).getId().longValue());
		assertEquals("Teste 4", volunteersList.get(3).getName());
		assertEquals("mail@mail.com", volunteersList.get(3).getEmail());
		assertEquals("Teste", volunteersList.get(3).getAddress());
		assertEquals("0123456789", volunteersList.get(3).getCelNumber());
		assertEquals("14256895789", volunteersList.get(3).getCpf());
		assertEquals("Teste", volunteersList.get(3).getDistrict());
		assertEquals("CASADO", volunteersList.get(3).getMaritalStatus());
		assertEquals("0123654789", volunteersList.get(3).getPhoneNumber());
		assertEquals("SIM", volunteersList.get(3).getPromise());
		assertEquals("12547895X", volunteersList.get(3).getRg());
		assertEquals("12345678", volunteersList.get(3).getZipCode());
		assertEquals(1l, volunteersList.get(3).getCity().getId().longValue());
		assertEquals("Teste", volunteersList.get(3).getCity().getName());
		assertEquals("SP", volunteersList.get(3).getCity().getState());
		assertTrue(volunteersList.get(3).getCity().getRegional());

		assertEquals(1l, volunteersList.get(3).getNaturalness().getId().longValue());
		assertEquals("Teste", volunteersList.get(3).getNaturalness().getName());
		assertEquals("SP", volunteersList.get(3).getNaturalness().getState());
		assertTrue(volunteersList.get(3).getNaturalness().getRegional());

		assertEquals(1, volunteersList.get(3).getMinistryOrPosition().size());
		assertEquals(1l, volunteersList.get(3).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteersList.get(3).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteersList.get(3).getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteersList.get(3).getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).findAll(any(VolunteerSpecification.class), any(Pageable.class));
	}

	@Test
	public void findAllNotAdminSuccessTest() {
		when(userService.isAdmin()).thenReturn(false);
		when(volunteerRepository.findAll(any(VolunteerSpecification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<Volunteer>(getVolunteersList()));
		Page<Volunteer> volunteers = this.volunteerService.findAll(VolunteerRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
		List<Volunteer> volunteersList = volunteers.getContent();
		assertEquals(4, volunteersList.size());
		assertEquals(1l, volunteersList.get(0).getId().longValue());
		assertEquals("Teste 1", volunteersList.get(0).getName());
		assertEquals("mail@mail.com", volunteersList.get(0).getEmail());
		assertEquals("Teste", volunteersList.get(0).getAddress());
		assertEquals("0123456789", volunteersList.get(0).getCelNumber());
		assertEquals("14256895789", volunteersList.get(0).getCpf());
		assertEquals("Teste", volunteersList.get(0).getDistrict());
		assertEquals("CASADO", volunteersList.get(0).getMaritalStatus());
		assertEquals("0123654789", volunteersList.get(0).getPhoneNumber());
		assertEquals("SIM", volunteersList.get(0).getPromise());
		assertEquals("12547895X", volunteersList.get(0).getRg());
		assertEquals("12345678", volunteersList.get(0).getZipCode());
		assertEquals(1l, volunteersList.get(0).getCity().getId().longValue());
		assertEquals("Teste", volunteersList.get(0).getCity().getName());
		assertEquals("SP", volunteersList.get(0).getCity().getState());
		assertTrue(volunteersList.get(0).getCity().getRegional());

		assertEquals(1l, volunteersList.get(0).getNaturalness().getId().longValue());
		assertEquals("Teste", volunteersList.get(0).getNaturalness().getName());
		assertEquals("SP", volunteersList.get(0).getNaturalness().getState());
		assertTrue(volunteersList.get(0).getNaturalness().getRegional());

		assertEquals(1, volunteersList.get(0).getMinistryOrPosition().size());
		assertEquals(1l, volunteersList.get(0).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteersList.get(0).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteersList.get(0).getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteersList.get(0).getPrayingHouse().getDistrict());

		assertEquals(2l, volunteersList.get(1).getId().longValue());
		assertEquals("Teste 2", volunteersList.get(1).getName());
		assertEquals("mail@mail.com", volunteersList.get(1).getEmail());
		assertEquals("Teste", volunteersList.get(1).getAddress());
		assertEquals("0123456789", volunteersList.get(1).getCelNumber());
		assertEquals("14256895789", volunteersList.get(1).getCpf());
		assertEquals("Teste", volunteersList.get(1).getDistrict());
		assertEquals("CASADO", volunteersList.get(1).getMaritalStatus());
		assertEquals("0123654789", volunteersList.get(1).getPhoneNumber());
		assertEquals("SIM", volunteersList.get(1).getPromise());
		assertEquals("12547895X", volunteersList.get(1).getRg());
		assertEquals("12345678", volunteersList.get(1).getZipCode());
		assertEquals(1l, volunteersList.get(1).getCity().getId().longValue());
		assertEquals("Teste", volunteersList.get(1).getCity().getName());
		assertEquals("SP", volunteersList.get(1).getCity().getState());
		assertTrue(volunteersList.get(1).getCity().getRegional());

		assertEquals(1l, volunteersList.get(1).getNaturalness().getId().longValue());
		assertEquals("Teste", volunteersList.get(1).getNaturalness().getName());
		assertEquals("SP", volunteersList.get(1).getNaturalness().getState());
		assertTrue(volunteersList.get(1).getNaturalness().getRegional());

		assertEquals(1, volunteersList.get(1).getMinistryOrPosition().size());
		assertEquals(1l, volunteersList.get(1).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteersList.get(1).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteersList.get(1).getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteersList.get(1).getPrayingHouse().getDistrict());

		assertEquals(3l, volunteersList.get(2).getId().longValue());
		assertEquals("Teste 3", volunteersList.get(2).getName());
		assertEquals("mail@mail.com", volunteersList.get(2).getEmail());
		assertEquals("Teste", volunteersList.get(2).getAddress());
		assertEquals("0123456789", volunteersList.get(2).getCelNumber());
		assertEquals("14256895789", volunteersList.get(2).getCpf());
		assertEquals("Teste", volunteersList.get(2).getDistrict());
		assertEquals("CASADO", volunteersList.get(2).getMaritalStatus());
		assertEquals("0123654789", volunteersList.get(2).getPhoneNumber());
		assertEquals("SIM", volunteersList.get(2).getPromise());
		assertEquals("12547895X", volunteersList.get(2).getRg());
		assertEquals("12345678", volunteersList.get(2).getZipCode());
		assertEquals(1l, volunteersList.get(2).getCity().getId().longValue());
		assertEquals("Teste", volunteersList.get(2).getCity().getName());
		assertEquals("SP", volunteersList.get(2).getCity().getState());
		assertTrue(volunteersList.get(2).getCity().getRegional());

		assertEquals(1l, volunteersList.get(2).getNaturalness().getId().longValue());
		assertEquals("Teste", volunteersList.get(2).getNaturalness().getName());
		assertEquals("SP", volunteersList.get(2).getNaturalness().getState());
		assertTrue(volunteersList.get(2).getNaturalness().getRegional());

		assertEquals(1, volunteersList.get(2).getMinistryOrPosition().size());
		assertEquals(1l, volunteersList.get(2).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteersList.get(2).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteersList.get(2).getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteersList.get(2).getPrayingHouse().getDistrict());

		assertEquals(4l, volunteersList.get(3).getId().longValue());
		assertEquals("Teste 4", volunteersList.get(3).getName());
		assertEquals("mail@mail.com", volunteersList.get(3).getEmail());
		assertEquals("Teste", volunteersList.get(3).getAddress());
		assertEquals("0123456789", volunteersList.get(3).getCelNumber());
		assertEquals("14256895789", volunteersList.get(3).getCpf());
		assertEquals("Teste", volunteersList.get(3).getDistrict());
		assertEquals("CASADO", volunteersList.get(3).getMaritalStatus());
		assertEquals("0123654789", volunteersList.get(3).getPhoneNumber());
		assertEquals("SIM", volunteersList.get(3).getPromise());
		assertEquals("12547895X", volunteersList.get(3).getRg());
		assertEquals("12345678", volunteersList.get(3).getZipCode());
		assertEquals(1l, volunteersList.get(3).getCity().getId().longValue());
		assertEquals("Teste", volunteersList.get(3).getCity().getName());
		assertEquals("SP", volunteersList.get(3).getCity().getState());
		assertTrue(volunteersList.get(3).getCity().getRegional());

		assertEquals(1l, volunteersList.get(3).getNaturalness().getId().longValue());
		assertEquals("Teste", volunteersList.get(3).getNaturalness().getName());
		assertEquals("SP", volunteersList.get(3).getNaturalness().getState());
		assertTrue(volunteersList.get(3).getNaturalness().getRegional());

		assertEquals(1, volunteersList.get(3).getMinistryOrPosition().size());
		assertEquals(1l, volunteersList.get(3).getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteersList.get(3).getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteersList.get(3).getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteersList.get(3).getPrayingHouse().getDistrict());
		
		verify(userService).isAdmin();
		verify(volunteerRepository).findAll(any(VolunteerSpecification.class), any(Pageable.class));
	}

	@Test(expected = ObjectNotFoundException.class)
	public void findByIdNotFoundTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.ofNullable(null));

		this.volunteerService.byId(1l);
	}

	@Test
	public void findByIdSuccessTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));

		Volunteer volunteer = this.volunteerService.byId(1l);
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());
		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).findById(1l);
	}

	@Test(expected = Exception.class)
	public void saveErrorTest() {
		when(volunteerRepository.save(any())).thenThrow(new Exception("Error"));

		this.volunteerService.save(generateVolunteerDto());
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void saveCityNotFoundTest() {
		when(cityRepository.findById(1l)).thenReturn(Optional.empty());

		this.volunteerService.save(generateVolunteerDto());
	}
	
	@Test
	public void saveWithNaturalnessNullTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.setNaturalness(null);
		
		when(volunteerRepository.save(any(Volunteer.class))).thenReturn(generateVolunteerNotAdmin());

		Volunteer volunteer = this.volunteerService.save(volunteerDto);
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());
		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).save(any(Volunteer.class));
	}
	
	@Test
	public void saveWithPrayingHouseNullTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.setPrayingHouse(null);
		
		when(volunteerRepository.save(any(Volunteer.class))).thenReturn(generateVolunteerNotAdmin());

		Volunteer volunteer = this.volunteerService.save(volunteerDto);
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());
		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).save(any(Volunteer.class));
	}
	
	@Test
	public void saveWithPrayingHouseReportCodeNullTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.getPrayingHouse().setReportCode(null);
		
		when(volunteerRepository.save(any(Volunteer.class))).thenReturn(generateVolunteerNotAdmin());

		Volunteer volunteer = this.volunteerService.save(volunteerDto);
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());
		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).save(any(Volunteer.class));
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void saveNaturalnessNotFoundTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.getNaturalness().setId(2l);
		when(cityRepository.findById(2l)).thenReturn(Optional.empty());

		this.volunteerService.save(volunteerDto);
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void savePrayingHouseNotFoundTest() {
		when(prayingHouseRepository.findById("abc123")).thenReturn(Optional.empty());

		this.volunteerService.save(generateVolunteerDto());
	}

	@Test
	public void saveSuccessTest() {
		when(volunteerRepository.save(any(Volunteer.class))).thenReturn(generateVolunteerNotAdmin());

		Volunteer volunteer = this.volunteerService.save(generateVolunteerDto());
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());
		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).save(any(Volunteer.class));
	}
	
	@Test
	public void saveVolunteerMusicianSuccessTest() {
		when(volunteerRepository.save(any())).thenReturn(generateVolunteerNotAdmin());
		Mockito.doNothing().when(musicianRepository).createMusician(null, null, 1l);

		Volunteer volunteer = this.volunteerService.save(generateVolunteerMusicianDto());
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());
		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).save(any());
		verify(musicianRepository).createMusician(null, null, 1l);
		
		verify(volunteerRepository).save(any(Volunteer.class));
		verify(musicianRepository).createMusician(null, null, 1l);
	}

	@Test(expected = Exception.class)
	public void updateErrorTest() {
		when(volunteerRepository.save(any())).thenThrow(new Exception("Error"));

		this.volunteerService.save(generateVolunteerDto());
	}

	@Test
	public void updateSuccessTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(volunteerRepository.save(any(Volunteer.class))).thenReturn(generateVolunteerNotAdmin());

		Volunteer volunteer = this.volunteerService.update(1l, generateVolunteerDto());
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());
		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).findById(1l);
		verify(volunteerRepository).save(any(Volunteer.class));
	}
	
	@Test
	public void updateVolunteerWhoIsAMusicianSuccessTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(musicianRepository.findById(1l)).thenReturn(Optional.of(MusicianServiceImplTest.generateMusicianNotAdmin()));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any(Musician.class))).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());

		Volunteer volunteer = this.volunteerService.update(1l, generateVolunteerDto());
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());

		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).findById(1l);
		verify(musicianRepository).findById(1l);
		verify(volunteerRepository, Mockito.times(0)).save(any());
		verify(musicianRepository).save(any(Musician.class));
	}
	
	@Test
	public void updateVolunteerWhoIsAMusicianWithoutNaturalnessSuccessTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.setNaturalness(null);
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(musicianRepository.findById(1l)).thenReturn(Optional.of(MusicianServiceImplTest.generateMusicianNotAdmin()));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any(Musician.class))).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());

		Volunteer volunteer = this.volunteerService.update(1l, volunteerDto);
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());

		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).findById(1l);
		verify(musicianRepository).findById(1l);
		verify(volunteerRepository, Mockito.times(0)).save(any());
		verify(musicianRepository).save(any(Musician.class));
		verify(cityRepository).findById(1l);
	}
	
	@Test
	public void updateVolunteerWhoIsAMusicianWithoutPrayingHouseSuccessTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.setPrayingHouse(null);
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(musicianRepository.findById(1l)).thenReturn(Optional.of(MusicianServiceImplTest.generateMusicianNotAdmin()));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any(Musician.class))).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());

		Volunteer volunteer = this.volunteerService.update(1l, volunteerDto);
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());

		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).findById(1l);
		verify(musicianRepository).findById(1l);
		verify(volunteerRepository, Mockito.times(0)).save(any());
		verify(musicianRepository).save(any(Musician.class));
		verify(prayingHouseRepository, Mockito.times(0)).findById("abc123");
	}
	
	@Test
	public void updateVolunteerWhoIsAMusicianWithoutPrayingHouseReportCodeSuccessTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.getPrayingHouse().setReportCode(null);
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(musicianRepository.findById(1l)).thenReturn(Optional.of(MusicianServiceImplTest.generateMusicianNotAdmin()));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any(Musician.class))).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());

		Volunteer volunteer = this.volunteerService.update(1l, volunteerDto);
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());

		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).findById(1l);
		verify(musicianRepository).findById(1l);
		verify(volunteerRepository, Mockito.times(0)).save(any());
		verify(musicianRepository).save(any(Musician.class));
		verify(prayingHouseRepository, Mockito.times(0)).findById("abc123");
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void updateVolunteerWhoIsAMusicianCityNotFoundTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.getNaturalness().setId(2l);;
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(musicianRepository.findById(1l)).thenReturn(Optional.of(MusicianServiceImplTest.generateMusicianNotAdmin()));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any())).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());
		when(cityRepository.findById(1l)).thenReturn(Optional.empty());

		this.volunteerService.update(1l, volunteerDto);
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void updateVolunteerWhoIsAMusicianNaturalnessNotFoundTest() {
		VolunteerDTO volunteerDto = generateVolunteerDto();
		volunteerDto.getNaturalness().setId(2l);;
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(musicianRepository.findById(1l)).thenReturn(Optional.of(MusicianServiceImplTest.generateMusicianNotAdmin()));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any())).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());
		when(cityRepository.findById(2l)).thenReturn(Optional.empty());

		this.volunteerService.update(1l, volunteerDto);
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void updateVolunteerWhoIsAMusicianPrayingHouseNotFoundTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(musicianRepository.findById(1l)).thenReturn(Optional.of(MusicianServiceImplTest.generateMusicianNotAdmin()));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any())).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());
		when(prayingHouseRepository.findById("abc123")).thenReturn(Optional.empty());

		this.volunteerService.update(1l, generateVolunteerDto());
	}
	
	@Test
	public void updateVolunteerWithAMusicianMinisterySuccessTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(musicianRepository.save(any(Musician.class))).thenReturn(MusicianServiceImplTest.generateMusicianNotAdmin());
		Mockito.doNothing().when(this.musicianRepository).createMusician(null, null, 1l);

		Volunteer volunteer = this.volunteerService.update(1l, generateVolunteerMusicianDto());
		assertEquals(1l, volunteer.getId().longValue());
		assertEquals("Teste", volunteer.getName());
		assertEquals("mail@mail.com", volunteer.getEmail());
		assertEquals("Teste", volunteer.getAddress());
		assertEquals("0123456789", volunteer.getCelNumber());
		assertEquals("14256895789", volunteer.getCpf());
		assertEquals("Teste", volunteer.getDistrict());
		assertEquals("CASADO", volunteer.getMaritalStatus());
		assertEquals("0123654789", volunteer.getPhoneNumber());
		assertEquals("SIM", volunteer.getPromise());
		assertEquals("12547895X", volunteer.getRg());
		assertEquals("12345678", volunteer.getZipCode());
		assertEquals(1l, volunteer.getCity().getId().longValue());
		assertEquals("Teste", volunteer.getCity().getName());
		assertEquals("SP", volunteer.getCity().getState());

		assertTrue(volunteer.getCity().getRegional());

		assertEquals(1l, volunteer.getNaturalness().getId().longValue());
		assertEquals("Teste", volunteer.getNaturalness().getName());
		assertEquals("SP", volunteer.getNaturalness().getState());
		assertTrue(volunteer.getNaturalness().getRegional());

		assertEquals(1, volunteer.getMinistryOrPosition().size());
		assertEquals(1l, volunteer.getMinistryOrPosition().get(0).getId().longValue());
		assertEquals("Teste", volunteer.getMinistryOrPosition().get(0).getDescription());

		assertEquals("abc123", volunteer.getPrayingHouse().getReportCode());
		assertEquals("Teste", volunteer.getPrayingHouse().getDistrict());
		
		verify(volunteerRepository).findById(1l);
		verify(musicianRepository).findById(1l);
		verify(volunteerRepository, Mockito.times(0)).save(any());
		verify(musicianRepository).save(any(Musician.class));
		verify(musicianRepository).createMusician(null, null, 1l);
	}

	@Test(expected = ObjectNotFoundException.class)
	public void updateCityNotFoundTest() {
		when(cityRepository.findById(any())).thenReturn(Optional.ofNullable(null));
		when(passwordEncoder.encode(any())).thenReturn("123");
		when(volunteerRepository.save(any())).thenReturn(generateVolunteerNotAdmin());

		this.volunteerService.update(1l, generateVolunteerDto());
	}

	@Test(expected = Exception.class)
	public void removeErrorTest() {
		Mockito.doThrow(new Exception("Error")).when(volunteerRepository).deleteById(1l);

		this.volunteerService.save(generateVolunteerDto());
	}
	
	@Test(expected = ObjectNotFoundException.class)
	public void removeNotFoundSuccessTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.empty());

		this.volunteerService.remove(1l);
	}

	@Test
	public void removeSuccessTest() {
		when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		Mockito.doNothing().when(volunteerRepository).delete(any(Volunteer.class));

		this.volunteerService.remove(1l);
		
		verify(volunteerRepository).findById(1l);
		verify(volunteerRepository).delete(any(Volunteer.class));
	}

	@SuppressWarnings({ "rawtypes", "unchecked" })
	public static List<Volunteer> getVolunteersList() {
		List<Volunteer> list = new ArrayList<>();

		list.add(new Volunteer(1l, "Teste 1", "Teste", "Teste", CityServiceImplTest.generateCity(), "12345678",
				"0123654789", "0123456789", "mail@mail.com", LocalDate.now(), CityServiceImplTest.generateCity(),
				LocalDate.now(), "14256895789", "12547895X", MaritalStatusEnum.CASADO.toString(), LocalDate.now(), null,
				"SIM", null, null, PrayingHouseServiceImplTest.generatePrayingHouse(),
				new ArrayList(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))));
		list.add(new Volunteer(2l, "Teste 2", "Teste", "Teste", CityServiceImplTest.generateCity(), "12345678",
				"0123654789", "0123456789", "mail@mail.com", LocalDate.now(), CityServiceImplTest.generateCity(),
				LocalDate.now(), "14256895789", "12547895X", MaritalStatusEnum.CASADO.toString(), LocalDate.now(), null,
				"SIM", null, null, PrayingHouseServiceImplTest.generatePrayingHouse(),
				new ArrayList(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))));
		list.add(new Volunteer(3l, "Teste 3", "Teste", "Teste", CityServiceImplTest.generateCity(), "12345678",
				"0123654789", "0123456789", "mail@mail.com", LocalDate.now(), CityServiceImplTest.generateCity(),
				LocalDate.now(), "14256895789", "12547895X", MaritalStatusEnum.CASADO.toString(), LocalDate.now(), null,
				"SIM", null, null, PrayingHouseServiceImplTest.generatePrayingHouse(),
				new ArrayList(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))));
		list.add(new Volunteer(4l, "Teste 4", "Teste", "Teste", CityServiceImplTest.generateCity(), "12345678",
				"0123654789", "0123456789", "mail@mail.com", LocalDate.now(), CityServiceImplTest.generateCity(),
				LocalDate.now(), "14256895789", "12547895X", MaritalStatusEnum.CASADO.toString(), LocalDate.now(), null,
				"SIM", null, null, PrayingHouseServiceImplTest.generatePrayingHouse(),
				new ArrayList(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition()))));

		return list;
	}

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Volunteer generateVolunteerNotAdmin() {
		return new Volunteer(1l, "Teste", "Teste", "Teste", CityServiceImplTest.generateCity(), "12345678",
				"0123654789", "0123456789", "mail@mail.com", LocalDate.now(), CityServiceImplTest.generateCity(),
				LocalDate.now(), "14256895789", "12547895X", MaritalStatusEnum.CASADO.toString(), LocalDate.now(), null,
				"SIM", null, null, PrayingHouseServiceImplTest.generatePrayingHouse(),
				new ArrayList(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPosition())));
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static Volunteer generateVolunteerWithMusicianMinistery() {
		return new Volunteer(1l, "Teste", "Teste", "Teste", CityServiceImplTest.generateCity(), "12345678",
				"0123654789", "0123456789", "mail@mail.com", LocalDate.now(), CityServiceImplTest.generateCity(),
				LocalDate.now(), "14256895789", "12547895X", MaritalStatusEnum.CASADO.toString(), LocalDate.now(), null,
				"SIM", null, null, PrayingHouseServiceImplTest.generatePrayingHouse(),
				new ArrayList(Arrays.asList(MinistryOrPositionServiceImplTest.generateMinistryOrPositionMusician())));
	}

	public static VolunteerDTO generateVolunteerDto() {
		return VolunteerDTO.builder().id(1l).name("Teste").address("Teste").district("Teste")
				.city(CityServiceImplTest.generateCityInnerDTO()).zipCode("12345678").phoneNumber("0123654789")
				.celNumber("0123456789").email("mail@mail.com").dateOfBirth(LocalDate.now())
				.naturalness(CityServiceImplTest.generateCityInnerDTO()).dateOfBaptism(LocalDate.now())
				.cpf("14256895789").rg("12547895X").maritalStatus(MaritalStatusEnum.CASADO.getDescription())
				.ministryApresentationDate(LocalDate.now()).promise("SIM")
				.prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouseInnerDTO())
				.ministryOrPosition(MinistryOrPositionServiceImplTest.generateMinistryOrPositionInnerDTO()).build();
	}
	
	public static VolunteerDTO generateVolunteerDto2() {
		return VolunteerDTO.builder().id(1l).name("Teste").address("Teste").district("Teste")
				.city(CityServiceImplTest.generateCityInnerDTO()).zipCode("12345678").phoneNumber("0123654789")
				.celNumber("0123456789").email("mail@mail.com").naturalness(CityServiceImplTest.generateCityInnerDTO())
				.cpf("95359600080").rg("12547895X").promise("SIM")
				.prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouseInnerDTO())
				.ministryOrPosition(MinistryOrPositionServiceImplTest.generateMinistryOrPositionInnerDTO()).build();
	}
	
	public static VolunteerDTO generateVolunteerMusicianDto() {
		return VolunteerDTO.builder().id(1l).name("Teste").address("Teste").district("Teste")
				.city(CityServiceImplTest.generateCityInnerDTO()).zipCode("12345678").phoneNumber("0123654789")
				.celNumber("0123456789").email("mail@mail.com").dateOfBirth(LocalDate.now())
				.naturalness(CityServiceImplTest.generateCityInnerDTO()).dateOfBaptism(LocalDate.now())
				.cpf("14256895789").rg("12547895X").maritalStatus(MaritalStatusEnum.CASADO.getDescription())
				.ministryApresentationDate(LocalDate.now()).promise("SIM")
				.prayingHouse(PrayingHouseServiceImplTest.generatePrayingHouseInnerDTO())
				.ministryOrPosition(MinistryOrPositionServiceImplTest.generateMinistryOrPositionMusicianInnerDTO()).build();
	}
}
