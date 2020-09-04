package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.dto.city.CityDTO;
import br.org.rpf.cagef.dto.http.request.city.CityRequestParamsDTO;
import br.org.rpf.cagef.entity.City;

public interface CityService {
	
	public Page<City> findAll(CityRequestParamsDTO requestParams);
	
	public City byId(Long id);

	public City save(CityDTO city);

	public City update(Long id, CityDTO cityDTO);

	public void remove(Long id);

}
