package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.dto.http.request.city.PrayingHouseRequestParamsDTO;
import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseDTO;
import br.org.rpf.cagef.entity.PrayingHouse;

public interface PrayingHouseService {
	public Page<PrayingHouse> findAll(PrayingHouseRequestParamsDTO requestParams);
	
	public PrayingHouse byId(String reportCode);

	public PrayingHouse save(PrayingHouseDTO prayingHouseDTO);
	
	public PrayingHouse update(String reportCode, PrayingHouseDTO prayingHouseDTO);

	public void remove(String reportCode);
}
