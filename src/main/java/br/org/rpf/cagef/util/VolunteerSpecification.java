package br.org.rpf.cagef.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.org.rpf.cagef.entity.Volunteer;

public class VolunteerSpecification implements Specification<Volunteer> {
	private static final long serialVersionUID = -6512330083197715870L;

	private Long id;
	private String name;
	private Long[] cityIds;
	private String cityName;
	private String ministryOrPositionDescription;
	private Long[] ministryOrPositionIds;
	private Boolean regional;

	public VolunteerSpecification(Long id, String name, Long[] cityIds, String cityName,
			String ministryOrPositionDescription, Long[] ministryOrPositionIds, Boolean regional) {
		super();
		this.id = id;
		this.name = name;
		this.cityIds = cityIds;
		this.cityName = cityName;
		this.ministryOrPositionDescription = ministryOrPositionDescription;
		this.ministryOrPositionIds = ministryOrPositionIds;
		this.regional = regional;
	}

	@Override
	public Predicate toPredicate(Root<Volunteer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		// where(criteriaBuilder.equal(root.get("id"), this.id));
		
		
		query.distinct(false);
		if (this.id != null) {
			predicates.add(criteriaBuilder.equal(root.get("id"), this.id));
		}
		if (this.name != null) {
			predicates.add(criteriaBuilder.like(root.get("name"), "%" + this.name + "%"));
		}
		if (this.cityIds != null && this.cityIds.length > 0) {
			predicates.add(criteriaBuilder.and(root.join("city", JoinType.INNER).get("id").in((Object[]) this.cityIds)));
		}
		if (this.cityName != null) {
			predicates.add(criteriaBuilder.like(root.join("city", JoinType.INNER).get("name"), "%" + this.cityName + "%"));
		}
		if (this.ministryOrPositionDescription != null) {
			predicates.add(criteriaBuilder.like(root.join("ministryOrPosition", JoinType.INNER).get("description"),
					"%" + this.ministryOrPositionDescription + "%"));
		}
		if (this.ministryOrPositionIds != null && this.ministryOrPositionIds.length > 0) {
			predicates.add(root.join("ministryOrPosition", JoinType.INNER).get("id").in((Object[]) this.ministryOrPositionIds));
		}
		if (this.regional != null) {
			predicates.add(criteriaBuilder.equal(root.join("city", JoinType.INNER).get("regional"), this.regional));
		}
		
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
