package br.org.rpf.cagef.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.org.rpf.cagef.entity.Instrument;

public class InstrumentSpecification implements Specification<Instrument> {
	
	private static final long serialVersionUID = -4767267276865789845L;
	
	private Long id;
	private String description;
	private Long[] categoryIds;
	private String categoryName;
	
	public InstrumentSpecification() {
		super();
	}

	public InstrumentSpecification(Long id, String description, Long[] categoryIds, String categoryName) {
		super();
		this.id = id;
		this.description = description;
		this.categoryIds = categoryIds;
		this.categoryName = categoryName;
	}

	@Override
	public Predicate toPredicate(Root<Instrument> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		if (this.id != null) {
			predicates.add(criteriaBuilder.equal(root.get("id"), this.id));
		}
		if (this.description != null) {
			predicates.add(criteriaBuilder.like(root.get("description"), "%" + this.description + "%"));
		}
		if (this.categoryIds != null && this.categoryIds.length > 0) {
			predicates.add(criteriaBuilder.and(root.get("category").get("id").in((Object[]) this.categoryIds)));
		}
		if (this.categoryName != null) {
			predicates.add(criteriaBuilder.like(root.join("category", JoinType.INNER).get("description"), "%" + this.categoryName + "%"));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

}
