package br.org.rpf.cagef.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
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

	public VolunteerSpecification(Long id, String name, Long[] cityIds, String cityName,
			String ministryOrPositionDescription, Long[] ministryOrPositionIds) {
		super();
		this.id = id;
		this.name = name;
		this.cityIds = cityIds;
		this.cityName = cityName;
		this.ministryOrPositionDescription = ministryOrPositionDescription;
		this.ministryOrPositionIds = ministryOrPositionIds;
	}

	@Override
	public Predicate toPredicate(Root<Volunteer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		if (this.id != null) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("id"), this.id)));
		}
		if (this.name != null) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.get("name"), "%" + this.name + "%")));
		}
		if (this.cityIds != null && this.cityIds.length > 0) {
			predicates.add(criteriaBuilder.and(root.join("city").get("id").in((Object[]) this.cityIds)));
		}
		if (this.cityName != null) {
			predicates.add(criteriaBuilder
					.and(criteriaBuilder.like(root.join("city").get("name"), "%" + this.cityName + "%")));
		}
		if (this.ministryOrPositionDescription != null) {
			predicates.add(criteriaBuilder.and(criteriaBuilder.like(root.join("ministryOrPosition").get("description"),
					"%" + this.ministryOrPositionDescription + "%")));
		}
		if (this.ministryOrPositionIds != null && this.ministryOrPositionIds.length > 0) {
			predicates.add(criteriaBuilder
					.and(root.join("ministryOrPosition").get("id").in((Object[]) this.ministryOrPositionIds)));
		}
		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
