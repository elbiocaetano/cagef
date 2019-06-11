package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseDTO;
import br.org.rpf.cagef.entity.PrayingHouse;

public interface PrayingHouseService {
	public Page<PrayingHouse> findAll(String reportCode, Long cityId, String cityName, String district, int offset, int limit, String orderBy, String direction);
	
	public PrayingHouse byId(String reportCode);

	public PrayingHouse save(PrayingHouseDTO prayingHouseDTO);
	
	public PrayingHouse update(String reportCode, PrayingHouseDTO prayingHouseDTO);

	public void remove(String reportCode);
}
