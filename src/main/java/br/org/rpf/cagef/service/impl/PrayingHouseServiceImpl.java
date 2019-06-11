package br.org.rpf.cagef.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.ExampleMatcher.GenericPropertyMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseDTO;
import br.org.rpf.cagef.entity.City;
import br.org.rpf.cagef.entity.PrayingHouse;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.PrayingHouseRepository;
import br.org.rpf.cagef.service.PrayingHouseService;
import br.org.rpf.cagef.service.UserService;

@Service
public class PrayingHouseServiceImpl implements PrayingHouseService {

	@Autowired
	private PrayingHouseRepository prayingHouseRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private UserService userService;

	public Page<PrayingHouse> findAll(String reportCode, Long cityId, String cityName, String district, int offset, int limit, String orderBy,
			String direction) {
		PrayingHouse prayingHouse = new PrayingHouse(reportCode, district, new City(cityId, cityName));
		if(!this.userService.isAdmin()) {
			prayingHouse.setCity(UserService.authenticated().getCity());
		}

		return prayingHouseRepository.findAll(
				Example.of(prayingHouse, getExampleMatcher()),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
	}
	
	@Override
	public PrayingHouse byId(String reportCode) {
		return prayingHouseRepository.findById(reportCode).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(reportCode, PrayingHouse.class.getName()));
	}
	
	@Override
	public PrayingHouse save(PrayingHouseDTO casaDeOracaoDTO) {
		return prayingHouseRepository.save(fromDTO(casaDeOracaoDTO.getReportCode(), casaDeOracaoDTO));
	}
	
	@Override
	public PrayingHouse update(String codRelatorio, PrayingHouseDTO casaDeOracaoDTO) {
		return prayingHouseRepository.save(fromDTO(codRelatorio, casaDeOracaoDTO));
	}
	
	@Override
	public void remove(String codRelatorio) {
		this.prayingHouseRepository.deleteById(codRelatorio);
	}
	
	private PrayingHouse fromDTO(String reportCode, PrayingHouseDTO casaDeOracaoDTO) {
		City city = null;
		if(!this.userService.isAdmin()) {
			city = cityRepository.findById(UserService.authenticated().getCity().getId()).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(casaDeOracaoDTO.getCity().getId(), City.class.getName()));
		} else {
			city = cityRepository.findById(casaDeOracaoDTO.getCity().getId()).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(casaDeOracaoDTO.getCity().getId(), City.class.getName()));
		}
		
		
		return new PrayingHouse(reportCode, casaDeOracaoDTO.getDistrict(), city);
	}

	private ExampleMatcher getExampleMatcher() {
		return ExampleMatcher.matching().withIgnoreCase()
				.withMatcher("reportCode", new GenericPropertyMatcher().startsWith())
				.withMatcher("district", new GenericPropertyMatcher().startsWith())
				.withMatcher("city.name", new GenericPropertyMatcher().startsWith());
	}
}
