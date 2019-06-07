package br.org.rpf.cagef.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.rpf.cagef.entity.City;

@Repository
public interface CityRepository extends JpaRepository<City, Long> {
	
}
