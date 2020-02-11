package br.org.rpf.cagef.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import br.org.rpf.cagef.entity.InstrumentCategory;

@Repository
public interface InstrumentCategoryRepository extends JpaRepository<InstrumentCategory, Long>, JpaSpecificationExecutor<InstrumentCategory> {

}
