package br.org.rpf.cagef.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.org.rpf.cagef.entity.CasaDeOracao;

@Repository
public interface CasaDeOracaoRepository extends JpaRepository<CasaDeOracao, String> {
	
}
