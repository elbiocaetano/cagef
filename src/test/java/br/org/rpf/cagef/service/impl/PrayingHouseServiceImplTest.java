package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseDTO;
import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseInnerDTO;
import br.org.rpf.cagef.entity.PrayingHouse;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.PrayingHouseRepository;
import br.org.rpf.cagef.service.PrayingHouseService;
import br.org.rpf.cagef.service.UserService;

@RunWith(SpringRunner.class)
public class PrayingHouseServiceImplTest {

	@TestConfiguration
	static class PrayingHouseServiceImplTestConfiguration {
		@Bean(name = "prayinghouseService")
		public PrayingHouseService prayinghouseService() {
			return new PrayingHouseServiceImpl();
		}
	}

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Autowired
	@InjectMocks
	private PrayingHouseServiceImpl prayinghouseService;

	@MockBean
	private UserService userService;

	@MockBean
	private PrayingHouseRepository prayinghouseRepository;

	@MockBean
	private CityRepository cityRepository;

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
	}

	@SuppressWarnings("unchecked")
	@Test
	public void findAllErrorTest() {
		Mockito.when(prayinghouseRepository.findAll(any(Example.class), any(Pageable.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.prayinghouseService.findAll(null, null, null, null, 0, 24, "id", "ASC");
	}

	@SuppressWarnings("unchecked")
	@Test
	public void findAllAdminSuccessTest() {
		Mockito.when(prayinghouseRepository.findAll(any(Example.class), any(Pageable.class)))
				.thenReturn(new PageImpl<PrayingHouse>(getPrayingHousesList()));
		Page<PrayingHouse> prayingHouses = this.prayinghouseService.findAll(null, null, null, null, 0, 24, "id", "ASC");
		List<PrayingHouse> prayingHousesList = prayingHouses.getContent();
		assertEquals(4, prayingHousesList.size());
		assertEquals("abc1234", prayingHousesList.get(0).getReportCode());
		assertEquals("Teste 1", prayingHousesList.get(0).getDistrict());
		assertEquals(1l, prayingHousesList.get(0).getCity().getId().longValue());
		assertEquals("Teste", prayingHousesList.get(0).getCity().getName());
		assertEquals("SP", prayingHousesList.get(0).getCity().getState());
		assertTrue(prayingHousesList.get(0).getCity().getRegional());

		assertEquals("abc1235", prayingHousesList.get(1).getReportCode());
		assertEquals("Teste 2", prayingHousesList.get(1).getDistrict());
		assertEquals(1l, prayingHousesList.get(1).getCity().getId().longValue());
		assertEquals("Teste", prayingHousesList.get(1).getCity().getName());
		assertEquals("SP", prayingHousesList.get(1).getCity().getState());
		assertTrue(prayingHousesList.get(1).getCity().getRegional());

		assertEquals("abc1236", prayingHousesList.get(2).getReportCode());
		assertEquals("Teste 3", prayingHousesList.get(2).getDistrict());
		assertEquals(1l, prayingHousesList.get(2).getCity().getId().longValue());
		assertEquals("Teste", prayingHousesList.get(2).getCity().getName());
		assertEquals("SP", prayingHousesList.get(2).getCity().getState());
		assertTrue(prayingHousesList.get(2).getCity().getRegional());

		assertEquals("abc1237", prayingHousesList.get(3).getReportCode());
		assertEquals("Teste 4", prayingHousesList.get(3).getDistrict());
		assertEquals(1l, prayingHousesList.get(3).getCity().getId().longValue());
		assertEquals("Teste", prayingHousesList.get(3).getCity().getName());
		assertEquals("SP", prayingHousesList.get(3).getCity().getState());
		assertTrue(prayingHousesList.get(3).getCity().getRegional());

	}

	@SuppressWarnings("unchecked")
	@Test
	public void findAllNotAdminSuccessTest() {
		Mockito.when(userService.isAdmin()).thenReturn(false);
		Mockito.when(prayinghouseRepository.findAll(any(Example.class), any(Pageable.class)))
				.thenReturn(new PageImpl<PrayingHouse>(getPrayingHousesList()));
		Page<PrayingHouse> prayingHouses = this.prayinghouseService.findAll(null, null, null, null, 0, 24, "id", "ASC");
		List<PrayingHouse> prayingHousesList = prayingHouses.getContent();
		assertEquals(4, prayingHousesList.size());
		assertEquals("abc1234", prayingHousesList.get(0).getReportCode());
		assertEquals("Teste 1", prayingHousesList.get(0).getDistrict());
		assertEquals(1l, prayingHousesList.get(0).getCity().getId().longValue());
		assertEquals("Teste", prayingHousesList.get(0).getCity().getName());
		assertEquals("SP", prayingHousesList.get(0).getCity().getState());
		assertTrue(prayingHousesList.get(0).getCity().getRegional());

		assertEquals("abc1235", prayingHousesList.get(1).getReportCode());
		assertEquals("Teste 2", prayingHousesList.get(1).getDistrict());
		assertEquals(1l, prayingHousesList.get(1).getCity().getId().longValue());
		assertEquals("Teste", prayingHousesList.get(1).getCity().getName());
		assertEquals("SP", prayingHousesList.get(1).getCity().getState());
		assertTrue(prayingHousesList.get(1).getCity().getRegional());

		assertEquals("abc1236", prayingHousesList.get(2).getReportCode());
		assertEquals("Teste 3", prayingHousesList.get(2).getDistrict());
		assertEquals(1l, prayingHousesList.get(2).getCity().getId().longValue());
		assertEquals("Teste", prayingHousesList.get(2).getCity().getName());
		assertEquals("SP", prayingHousesList.get(2).getCity().getState());
		assertTrue(prayingHousesList.get(2).getCity().getRegional());

		assertEquals("abc1237", prayingHousesList.get(3).getReportCode());
		assertEquals("Teste 4", prayingHousesList.get(3).getDistrict());
		assertEquals(1l, prayingHousesList.get(3).getCity().getId().longValue());
		assertEquals("Teste", prayingHousesList.get(3).getCity().getName());
		assertEquals("SP", prayingHousesList.get(3).getCity().getState());
		assertTrue(prayingHousesList.get(3).getCity().getRegional());
	}

	@Test(expected = ObjectNotFoundException.class)
	public void findByIdNotFoundTest() {
		Mockito.when(prayinghouseRepository.findById("abc123")).thenReturn(Optional.ofNullable(null));

		this.prayinghouseService.byId("abc123");
	}

	@Test()
	@WithMockUser()
	public void findByIdAdminSuccessTest() {
		Mockito.when(cityRepository.findById(1l)).thenReturn(Optional.of(CityServiceImplTest.generateCity()));
		Mockito.when(prayinghouseRepository.findById("abc123")).thenReturn(Optional.of(generatePrayingHouse()));

		PrayingHouse prayinghouse = this.prayinghouseService.byId("abc123");
		assertEquals("abc123", prayinghouse.getReportCode());
		assertEquals("Teste", prayinghouse.getDistrict());
		assertEquals(1l, prayinghouse.getCity().getId().longValue());
		assertEquals("Teste", prayinghouse.getCity().getName());
		assertEquals("SP", prayinghouse.getCity().getState());
		assertTrue(prayinghouse.getCity().getRegional());
	}

	@Test()
	public void findByIdNotAdminSuccessTest() {
		Mockito.when(userService.isAdmin()).thenReturn(false);
		Mockito.when(cityRepository.findById(1l)).thenReturn(Optional.of(CityServiceImplTest.generateCity()));
		Mockito.when(prayinghouseRepository.findById("abc123")).thenReturn(Optional.of(generatePrayingHouse()));

		PrayingHouse prayinghouse = this.prayinghouseService.byId("abc123");
		assertEquals("abc123", prayinghouse.getReportCode());
		assertEquals("Teste", prayinghouse.getDistrict());
		assertEquals(1l, prayinghouse.getCity().getId().longValue());
		assertEquals("Teste", prayinghouse.getCity().getName());
		assertEquals("SP", prayinghouse.getCity().getState());
		assertTrue(prayinghouse.getCity().getRegional());
	}

	@Test(expected = Exception.class)
	public void saveErrorTest() {
		Mockito.when(prayinghouseRepository.save(any())).thenThrow(new Exception("Error"));

		this.prayinghouseService.save(generatePrayingHouseDTO());
	}

	@Test()
	public void saveAdminSuccessTest() {
		Mockito.when(prayinghouseRepository.save(any())).thenReturn(generatePrayingHouse());

		PrayingHouse prayinghouse = this.prayinghouseService.save(generatePrayingHouseDTO());
		assertEquals("abc123", prayinghouse.getReportCode());
		assertEquals("Teste", prayinghouse.getDistrict());
		assertEquals(1l, prayinghouse.getCity().getId().longValue());
		assertEquals("Teste", prayinghouse.getCity().getName());
		assertEquals("SP", prayinghouse.getCity().getState());
		assertTrue(prayinghouse.getCity().getRegional());
	}

	@Test()
	public void saveNotAdminSuccessTest() {
		Mockito.when(userService.isAdmin()).thenReturn(false);
		Mockito.when(prayinghouseRepository.save(any())).thenReturn(generatePrayingHouse());

		PrayingHouse prayinghouse = this.prayinghouseService.save(generatePrayingHouseDTO());
		assertEquals("abc123", prayinghouse.getReportCode());
		assertEquals("Teste", prayinghouse.getDistrict());
		assertEquals(1l, prayinghouse.getCity().getId().longValue());
		assertEquals("Teste", prayinghouse.getCity().getName());
		assertEquals("SP", prayinghouse.getCity().getState());
		assertTrue(prayinghouse.getCity().getRegional());
	}

	@Test(expected = ObjectNotFoundException.class)
	public void saveAdminCityNotFoundTest() {
		Mockito.when(userService.isAdmin()).thenReturn(false);
		Mockito.when(cityRepository.findById(any())).thenReturn(Optional.ofNullable(null));
		Mockito.when(prayinghouseRepository.save(any())).thenReturn(generatePrayingHouse());

		PrayingHouse prayinghouse = this.prayinghouseService.save(generatePrayingHouseDTO());
		assertEquals("abc123", prayinghouse.getReportCode());
		assertEquals("Teste", prayinghouse.getDistrict());
		assertEquals(1l, prayinghouse.getCity().getId().longValue());
		assertEquals("Teste", prayinghouse.getCity().getName());
		assertEquals("SP", prayinghouse.getCity().getState());
		assertTrue(prayinghouse.getCity().getRegional());
	}

	@Test(expected = ObjectNotFoundException.class)
	public void saveNotAdminCityNotFoundTest() {
		Mockito.when(cityRepository.findById(any())).thenReturn(Optional.ofNullable(null));
		Mockito.when(prayinghouseRepository.save(any())).thenReturn(generatePrayingHouse());

		PrayingHouse prayinghouse = this.prayinghouseService.save(generatePrayingHouseDTO());
		assertEquals("abc123", prayinghouse.getReportCode());
		assertEquals("Teste", prayinghouse.getDistrict());
		assertEquals(1l, prayinghouse.getCity().getId().longValue());
		assertEquals("Teste", prayinghouse.getCity().getName());
		assertEquals("SP", prayinghouse.getCity().getState());
		assertTrue(prayinghouse.getCity().getRegional());
	}

	@Test(expected = Exception.class)
	public void updateErrorTest() {
		Mockito.when(prayinghouseRepository.save(any())).thenThrow(new Exception("Error"));

		this.prayinghouseService.save(generatePrayingHouseDTO());
	}

	@Test()
	public void updateAdminSuccessTest() {
		Mockito.when(prayinghouseRepository.save(any())).thenReturn(generatePrayingHouse());

		PrayingHouse prayinghouse = this.prayinghouseService.update("abc123", generatePrayingHouseDTO());
		assertEquals("abc123", prayinghouse.getReportCode());
		assertEquals("Teste", prayinghouse.getDistrict());
		assertEquals(1l, prayinghouse.getCity().getId().longValue());
		assertEquals("Teste", prayinghouse.getCity().getName());
		assertEquals("SP", prayinghouse.getCity().getState());
		assertTrue(prayinghouse.getCity().getRegional());
	}

	@Test()
	public void updateNotAdminSuccessTest() {
		Mockito.when(prayinghouseRepository.save(any())).thenReturn(generatePrayingHouse());
		Mockito.when(userService.isAdmin()).thenReturn(false);

		PrayingHouse prayinghouse = this.prayinghouseService.update("abc123", generatePrayingHouseDTO());
		assertEquals("abc123", prayinghouse.getReportCode());
		assertEquals("Teste", prayinghouse.getDistrict());
		assertEquals(1l, prayinghouse.getCity().getId().longValue());
		assertEquals("Teste", prayinghouse.getCity().getName());
		assertEquals("SP", prayinghouse.getCity().getState());
		assertTrue(prayinghouse.getCity().getRegional());
	}

	@Test(expected = Exception.class)
	public void removeErrorTest() {
		Mockito.doThrow(new Exception("Error")).when(prayinghouseRepository).deleteById("abc123");

		this.prayinghouseService.save(generatePrayingHouseDTO());
	}

	@Test()
	public void removeSuccessTest() {
		Mockito.doNothing().when(prayinghouseRepository).deleteById("abc123");

		this.prayinghouseService.remove("abc123");
	}

	private List<PrayingHouse> getPrayingHousesList() {
		List<PrayingHouse> list = new ArrayList<>();

		list.add(new PrayingHouse("abc1234", "Teste 1", CityServiceImplTest.generateCity()));
		list.add(new PrayingHouse("abc1235", "Teste 2", CityServiceImplTest.generateCity()));
		list.add(new PrayingHouse("abc1236", "Teste 3", CityServiceImplTest.generateCity()));
		list.add(new PrayingHouse("abc1237", "Teste 4", CityServiceImplTest.generateCity()));

		return list;
	}

	public static PrayingHouse generatePrayingHouse() {
		return new PrayingHouse("abc123", "Teste", CityServiceImplTest.generateCity());
	}

	public static PrayingHouseDTO generatePrayingHouseDTO() {
		return new PrayingHouseDTO("abc123", "Teste", CityServiceImplTest.generateCityInnerDTO());
	}

	public static PrayingHouseInnerDTO generatePrayingHouseInnerDTO() {
		return new PrayingHouseInnerDTO("abc123");
	}
}
