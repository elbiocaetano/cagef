package br.org.rpf.cagef.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.org.rpf.cagef.dto.volunteer.ReportVolunteerProjection;
import br.org.rpf.cagef.entity.Volunteer;

@Repository
public interface VolunteerRepository extends JpaRepository<Volunteer, Long>, JpaSpecificationExecutor<Volunteer> {

	// SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE
	// obj.nome LIKE %:nome% AND cat IN :categorias
	@Query(value = "select v.id as id, v.name as name, v.city.name as city, m.description as ministryOrPosition, "
			+ "v.phoneNumber as phoneNumber, v.celNumber as celNumber, v.email as email, p.district as prayingHouse "
			+ "from Volunteer v INNER JOIN v.city c INNER JOIN v.ministryOrPosition m inner JOIN v.prayingHouse p ")
	public List<ReportVolunteerProjection> findToReport();
	
	@Query(value = "select v.id as id, v.name as name, v.city.name as city, m.description as ministryOrPosition, "
			+ "v.phoneNumber as phoneNumber, v.celNumber as celNumber, v.email as email, p.district as prayingHouse "
			+ "from Volunteer v INNER JOIN v.city c INNER JOIN v.ministryOrPosition m inner JOIN v.prayingHouse p "
			+ "where m.id in (:ids)")
	public List<ReportVolunteerProjection> findToReportByMinisteryOrPosition(@Param("ids") List<Long> ids);
	
	@Query(value = "select v.id as id, v.name as name, v.city.name as city, m.description as ministryOrPosition, "
			+ "v.phoneNumber as phoneNumber, v.celNumber as celNumber, v.email as email, p.district as prayingHouse "
			+ "from Volunteer v INNER JOIN v.city c INNER JOIN v.ministryOrPosition m inner JOIN v.prayingHouse p "
			+ "where c.id in (:ids)")
	public List<ReportVolunteerProjection> findToReportByCity(@Param("ids") List<Long> ids);

}
