package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;

import br.org.rpf.cagef.dto.http.request.city.MusicianRequestParamsDTO;
import br.org.rpf.cagef.entity.Musician;

public interface MusicianService extends DefaultVolunteerService {
	public Page<Musician> findAll(MusicianRequestParamsDTO requestParams);
}
