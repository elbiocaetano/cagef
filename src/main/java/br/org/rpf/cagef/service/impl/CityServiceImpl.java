package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.city.CityDTO;
import br.org.rpf.cagef.entity.City;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.service.CityService;

@Service
public class CityServiceImpl implements CityService {
	
	@Autowired
	private CityRepository cityRepository;

	@Override
	public Page<City> findAll(Long id, String name, String state, Boolean regional, int offset, int limit, String orderBy,
			String direction) {

		return cityRepository.findAll(
				Example.of(new City(id, name, state, regional), getExampleMatcher()),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}
	
	@Override
	public City byId(Long id) {
		return cityRepository.findById(id).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, City.class.getName()));
	}

	@Override
	public City save(CityDTO city) {
		return this.cityRepository.save(this.fromDTO(null, city));
	}
	
	@Override
	public City update(Long id, CityDTO cityDTO) {
		return this.cityRepository.save(this.fromDTO(id, cityDTO));
	}
	
	@Override
	public void remove(Long id) {
		this.cityRepository.deleteById(id);
	}

	private City fromDTO(Long id, CityDTO cityDTO) {
		return new City(id, cityDTO.getName(),cityDTO.getState(), cityDTO.getRegional());
	}
	
	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase()
				.withMatcher("name", new GenericPropertyMatcher().contains())
				.withMatcher("state", new GenericPropertyMatcher().contains());
		
	}
}
