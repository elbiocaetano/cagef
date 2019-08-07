package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

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
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.entity.enums.MaritalStatusEnum;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.MinistryOrPositionRepository;
import br.org.rpf.cagef.repository.PrayingHouseRepository;
import br.org.rpf.cagef.repository.VolunteerRepository;
import br.org.rpf.cagef.service.UserService;
import br.org.rpf.cagef.service.VolunteerService;
import br.org.rpf.cagef.util.VolunteerSpecification;

@RunWith(SpringRunner.class)
public class VolunteerServiceImplTest {

	@TestConfiguration
	static class VolunteerServiceImplTestConfiguration {
		@Bean(name = "volunteerService")
		public VolunteerService volunteerService() {
			return new VolunteerServiceImpl();
		}
	}

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
	private BCryptPasswordEncoder passwordEncoder;

	@Before
	public void setup() {
		Authentication authentication = Mockito.mock(Authentication.class);
		SecurityContext securityContext = Mockito.mock(SecurityContext.class);
		Mockito.when(securityContext.getAuthentication()).thenReturn(authentication);
		SecurityContextHolder.setContext(securityContext);
		Mockito.when(SecurityContextHolder.getContext().getAuthentication().getPrincipal())
				.thenReturn(UserServiceImplTest.generateUserNotAdmin());

		Mockito.when(userService.isAdmin()).thenReturn(true);
		Mockito.when(cityRepository.findById(any())).thenReturn(Optional.of(CityServiceImplTest.generateCity()));
		Mockito.when(prayingHouseRepository.findById(any()))
				.thenReturn(Optional.of(PrayingHouseServiceImplTest.generatePrayingHouse()));
	}

	@Test
	public void findAllErrorTest() {
		Mockito.when(volunteerRepository.findAll(any(VolunteerSpecification.class), any(Pageable.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.volunteerService.findAll(null, null, null, null, null, null, 0, 24, "id", "ASC");
	}

	@Test
	public void findAllSuccessTest() {
		Mockito.when(volunteerRepository.findAll(any(VolunteerSpecification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<Volunteer>(getVolunteersList()));
		Page<Volunteer> volunteers = this.volunteerService.findAll(null, null, null, null, null, null, 0, 24, "id",
				"ASC");
		List<Volunteer> volunteersList = volunteers.getContent();
		assertEquals(volunteersList.size(), 4);
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

		assertEquals(volunteersList.get(1).getId().longValue(), 2l);
		assertEquals(volunteersList.get(1).getName(), "Teste 2");
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

		assertEquals(volunteersList.get(2).getId().longValue(), 3l);
		assertEquals(volunteersList.get(2).getName(), "Teste 3");
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

		assertEquals(volunteersList.get(3).getId().longValue(), 4l);
		assertEquals(volunteersList.get(3).getName(), "Teste 4");
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
	}

	@Test
	public void findAllNotAdminSuccessTest() {
		Mockito.when(userService.isAdmin()).thenReturn(false);
		Mockito.when(volunteerRepository.findAll(any(VolunteerSpecification.class), any(Pageable.class)))
				.thenReturn(new PageImpl<Volunteer>(getVolunteersList()));
		Page<Volunteer> volunteers = this.volunteerService.findAll(null, null, null, null, null, null, 0, 24, "id",
				"ASC");
		List<Volunteer> volunteersList = volunteers.getContent();
		assertEquals(volunteersList.size(), 4);
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

		assertEquals(volunteersList.get(1).getId().longValue(), 2l);
		assertEquals(volunteersList.get(1).getName(), "Teste 2");
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

		assertEquals(volunteersList.get(2).getId().longValue(), 3l);
		assertEquals(volunteersList.get(2).getName(), "Teste 3");
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

		assertEquals(volunteersList.get(3).getId().longValue(), 4l);
		assertEquals(volunteersList.get(3).getName(), "Teste 4");
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
	}

	@Test(expected = ObjectNotFoundException.class)
	public void findByIdNotFoundTest() {
		Mockito.when(volunteerRepository.findById(1l)).thenReturn(Optional.ofNullable(null));

		this.volunteerService.byId(1l);
	}

	@Test()
	public void findByIdSuccessTest() {
		Mockito.when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));

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
	}

	@Test(expected = Exception.class)
	public void saveErrorTest() {
		Mockito.when(volunteerRepository.save(any())).thenThrow(new Exception("Error"));

		this.volunteerService.save(generateVolunteerDto());
	}

	@Test()
	public void saveSuccessTest() {
		Mockito.when(volunteerRepository.save(any())).thenReturn(generateVolunteerNotAdmin());

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
	}

	@Test(expected = Exception.class)
	public void updateErrorTest() {
		Mockito.when(volunteerRepository.save(any())).thenThrow(new Exception("Error"));

		this.volunteerService.save(generateVolunteerDto());
	}

	@Test()
	public void updateSuccessTest() {
		Mockito.when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		Mockito.when(passwordEncoder.encode(any())).thenReturn("123");
		Mockito.when(volunteerRepository.save(any())).thenReturn(generateVolunteerNotAdmin());

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
	}

	@Test(expected = ObjectNotFoundException.class)
	public void updateCityNotFoundTest() {
		Mockito.when(cityRepository.findById(any())).thenReturn(Optional.ofNullable(null));
		Mockito.when(passwordEncoder.encode(any())).thenReturn("123");
		Mockito.when(volunteerRepository.save(any())).thenReturn(generateVolunteerNotAdmin());

		this.volunteerService.update(1l, generateVolunteerDto());
	}

	@Test(expected = Exception.class)
	public void removeErrorTest() {
		Mockito.doThrow(new Exception("Error")).when(volunteerRepository).deleteById(1l);

		this.volunteerService.save(generateVolunteerDto());
	}

	@Test()
	public void removeSuccessTest() {
		Mockito.when(volunteerRepository.findById(1l)).thenReturn(Optional.of(generateVolunteerNotAdmin()));
		Mockito.doNothing().when(volunteerRepository).delete(any(Volunteer.class));

		this.volunteerService.remove(1l);
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

	public static VolunteerDTO generateVolunteerDto() {
		return new VolunteerDTO(1l, "Teste", "Teste", "Teste", CityServiceImplTest.generateCityInnerDTO(), "12345678",
				"0123654789", "0123456789", "mail@mail.com", LocalDate.now(),
				CityServiceImplTest.generateCityInnerDTO(), LocalDate.now(), "14256895789", "12547895X",
				MaritalStatusEnum.CASADO, LocalDate.now(), null, "SIM", null, null,
				PrayingHouseServiceImplTest.generatePrayingHouseInnerDTO(),
				MinistryOrPositionServiceImplTest.generateMinistryOrPositionInnerDTO());
	}
}
