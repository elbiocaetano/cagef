package br.org.rpf.cagef.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.rpf.cagef.entity.Cidade;

@Repository
public interface CidadeRepository extends JpaRepository<Cidade, Long> {
	
}
