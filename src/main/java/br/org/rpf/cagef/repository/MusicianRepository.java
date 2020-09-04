package br.org.rpf.cagef.repository;

import java.time.LocalDate;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.entity.Musician;

@Repository
public interface MusicianRepository extends JpaRepository<Musician, Long>, JpaSpecificationExecutor<Musician> {
	
	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query("update Musician m set m.instrument = :instrument, m.oficializationDate = :oficializationDate where m.id = :musicianId")
	public void updateMusician(@Param("instrument") Instrument instrument, @Param("oficializationDate") LocalDate oficializationDate, @Param("musicianId") Long id);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = "insert into musician (oficialization_date, id, instrument_id) values(:oficializationDate, :musicianId, :instrumentId)", nativeQuery = true)
	public void createMusician(@Param("instrumentId") Long instrumentId, @Param("oficializationDate") LocalDate oficializationDate, @Param("musicianId") Long id);

	@Transactional(propagation = Propagation.REQUIRED)
	@Modifying(flushAutomatically = true, clearAutomatically = true)
	@Query(value = "delete from musician where id = :id", nativeQuery = true)
	public void removeRelationship(@Param("id") Long id);
	
}
