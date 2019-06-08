package br.org.rpf.cagef.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.rpf.cagef.entity.MinistryOrPosition;

@Repository
public interface MinistryOrPositionRepository extends JpaRepository<MinistryOrPosition, Long> {
	
}
