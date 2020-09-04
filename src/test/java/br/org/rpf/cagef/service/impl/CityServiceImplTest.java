package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.hibernate.ObjectNotFoundException;
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
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.rpf.cagef.dto.city.CityDTO;
import br.org.rpf.cagef.dto.city.CityInnerDTO;
import br.org.rpf.cagef.dto.http.request.city.CityRequestParamsDTO;
import br.org.rpf.cagef.entity.City;
import br.org.rpf.cagef.repository.CityRepository;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CityServiceImplTest {

	@Rule
	public ExpectedException expectedEx = ExpectedException.none();

	@Autowired
	@InjectMocks
	private CityServiceImpl cityService;

	@MockBean
	private CityRepository cityRepository;

	@SuppressWarnings("unchecked")
	@Test
	public void findAllErrorTest() {
		when(cityRepository.findAll(any(Example.class), any(Pageable.class)))
				.thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));

		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.cityService.findAll(CityRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
	}

	@SuppressWarnings("unchecked")
	@Test
	public void findAllSuccessTest() {
		when(cityRepository.findAll(any(Example.class), any(Pageable.class)))
				.thenReturn(new PageImpl<City>(getCitiesList()));
		Page<City> cities = this.cityService.findAll(CityRequestParamsDTO.builder().offset(0).limit(25).orderBy("id").direction("ASC").build());
		List<City> citiesList = cities.getContent();
		assertEquals(4, citiesList.size());
		assertEquals(1l, citiesList.get(0).getId().longValue());
		assertEquals("Casa Branca", citiesList.get(0).getName());
		assertEquals("SP", citiesList.get(0).getState());
		assertTrue(citiesList.get(0).getRegional());

		assertEquals(2l, citiesList.get(1).getId().longValue());
		assertEquals("Mococa", citiesList.get(1).getName());
		assertEquals("SP", citiesList.get(1).getState());
		assertTrue(citiesList.get(1).getRegional());

		assertEquals(3l, citiesList.get(2).getId().longValue());
		assertEquals("Jaguariuna", citiesList.get(2).getName());
		assertEquals("SP", citiesList.get(2).getState());
		assertFalse(citiesList.get(2).getRegional());

		assertEquals(4l, citiesList.get(3).getId().longValue());
		assertEquals("Poços de Caldas", citiesList.get(3).getName());
		assertEquals("MG", citiesList.get(3).getState());
		assertFalse(citiesList.get(3).getRegional());
		
		verify(cityRepository).findAll(any(Example.class), any(Pageable.class));

	}

	@Test(expected = ObjectNotFoundException.class)
	public void findByIdNotFoundTest() {
		when(cityRepository.findById(1l)).thenReturn(Optional.ofNullable(null));

		this.cityService.byId(1l);		
	}

	@Test
	public void findByIdSuccessTest() {
		when(cityRepository.findById(1l)).thenReturn(Optional.of(generateCity()));

		City city = this.cityService.byId(1l);
		assertEquals(1l, city.getId().longValue());
		assertEquals("Teste", city.getName());
		assertEquals("SP", city.getState());
		assertTrue(city.getRegional());
		
		verify(cityRepository).findById(1l);
	}

	@Test(expected = Exception.class)
	public void saveErrorTest() {
		when(cityRepository.save(any())).thenThrow(new Exception("Error"));

		this.cityService.save(generateCityDTO());
	}

	@Test
	public void saveSuccessTest() {
		when(cityRepository.save(any(City.class))).thenReturn(generateCity());

		City city = this.cityService.save(generateCityDTO());
		assertEquals(1l, city.getId().longValue());
		assertEquals("Teste", city.getName());
		assertEquals("SP", city.getState());
		assertTrue(city.getRegional());
		
		verify(cityRepository).save(any(City.class));
	}

	@Test(expected = Exception.class)
	public void updateErrorTest() {
		when(cityRepository.save(any(City.class))).thenThrow(new Exception("Error"));

		this.cityService.save(generateCityDTO());
		
		verify(cityRepository).save(any(City.class));
	}

	@Test
	public void updateSuccessTest() {
		when(cityRepository.save(any(City.class))).thenReturn(generateCity());

		City city = this.cityService.update(1l, generateCityDTO());
		assertEquals(1l, city.getId().longValue());
		assertEquals("Teste", city.getName());
		assertEquals("SP", city.getState());
		assertTrue(city.getRegional());
		
		verify(cityRepository).save(any(City.class));
	}

	@Test(expected = Exception.class)
	public void removeErrorTest() {
		Mockito.doThrow(new Exception("Error")).when(cityRepository).deleteById(1l);

		this.cityService.save(generateCityDTO());
	}

	@Test
	public void removeSuccessTest() {
		Mockito.doNothing().when(cityRepository).deleteById(1l);

		this.cityService.remove(1l);
		
		verify(cityRepository).deleteById(1l);
	}

	public static List<City> getCitiesList() {
		List<City> list = new ArrayList<>();

		list.add(new City(1l, "Casa Branca", "SP", true));
		list.add(new City(2l, "Mococa", "SP", true));
		list.add(new City(3l, "Jaguariuna", "SP", false));
		list.add(new City(4l, "Poços de Caldas", "MG", false));

		return list;
	}

	public static City generateCity() {
		return new City(1l, "Teste", "SP", true);
	}

	public static CityDTO generateCityDTO() {
		return new CityDTO(1l, "Teste", "SP", true);
	}

	public static CityInnerDTO generateCityInnerDTO() {
		return new CityInnerDTO(1l);
	}

}
