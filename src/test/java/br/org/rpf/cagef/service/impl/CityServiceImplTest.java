package br.org.rpf.cagef.service.impl;

import static org.junit.Assert.assertTrue;
import static org.mockito.ArgumentMatchers.any;

import java.util.ArrayList;
import java.util.List;

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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.context.junit4.SpringRunner;

import br.org.rpf.cagef.entity.City;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.service.CityService;

@RunWith(SpringRunner.class)
public class CityServiceImplTest {

	@TestConfiguration
	static class CityServiceImplTestConfiguration {
		@Bean(name="cityService")
		public CityService cityService() {
			return new CityServiceImpl();
		}
	}
	
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
		Mockito.when(cityRepository.findAll(any(Example.class), any(Pageable.class))).thenThrow(new DataIntegrityViolationException("DataIntegrityViolationException"));
		
		expectedEx.expect(DataIntegrityViolationException.class);
		expectedEx.expectMessage("DataIntegrityViolationException");

		this.cityRepository.findAll(Example.of(new City()), PageRequest.of(0, 100, Direction.fromString("ASC"), "id"));
	}
	
	@SuppressWarnings("unchecked")
	@Test
	public void findAllSuccessTest() {
		Mockito.when(cityRepository.findAll(any(Example.class), any(Pageable.class))).thenReturn(new PageImpl<City>(getCitiesList()));
		Page<City> cities = this.cityRepository.findAll(Example.of(new City()), PageRequest.of(0, 100, Direction.fromString("ASC"), "id"));
		List<City> citiesList = cities.getContent();
		assertTrue(citiesList.size() == 4);
		assertTrue(citiesList.get(0).getId() == 1l);
		assertTrue(citiesList.get(0).getName().equals("Casa Branca"));
		assertTrue(citiesList.get(0).getState().equals("SP"));
		assertTrue(citiesList.get(0).getRegional() == true);
		
		assertTrue(citiesList.get(1).getId() == 2l);
		assertTrue(citiesList.get(1).getName().equals("Mococa"));
		assertTrue(citiesList.get(1).getState().equals("SP"));
		assertTrue(citiesList.get(1).getRegional() == true);
		
		assertTrue(citiesList.get(2).getId() == 3l);
		assertTrue(citiesList.get(2).getName().equals("Jaguariuna"));
		assertTrue(citiesList.get(2).getState().equals("SP"));
		assertTrue(citiesList.get(2).getRegional() == false);
		
		assertTrue(citiesList.get(3).getId() == 4l);
		assertTrue(citiesList.get(3).getName().equals("Poços de Caldas"));
		assertTrue(citiesList.get(3).getState().equals("MG"));
		assertTrue(citiesList.get(3).getRegional() == false);
	}

	private List<City> getCitiesList() {
		List<City> list = new ArrayList<>();

		list.add(new City(1l, "Casa Branca", "SP", true));
		list.add(new City(2l, "Mococa", "SP", true));
		list.add(new City(3l, "Jaguariuna", "SP", false));
		list.add(new City(4l, "Poços de Caldas", "MG", false));

		return list;
	}
}
