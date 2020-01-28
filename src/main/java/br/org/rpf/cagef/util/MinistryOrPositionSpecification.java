package br.org.rpf.cagef.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.org.rpf.cagef.entity.MinistryOrPosition;

public class MinistryOrPositionSpecification implements Specification<MinistryOrPosition> {
	private static final long serialVersionUID = -6512330083197715870L;

	private Long id;
	private String description;
	private Long[] idIn;

	public MinistryOrPositionSpecification(Long id, String name, Long[] idIn) {
		super();
		this.id = id;
		this.description = name;
		this.idIn = idIn;
	}

	@Override
	public Predicate toPredicate(Root<MinistryOrPosition> root, CriteriaQuery<?> query,
			CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();
		query.distinct(true);
		if (this.id != null) {
			predicates.add(criteriaBuilder.equal(root.get("id"), this.id));
		}
		if (this.description != null) {
			predicates.add(criteriaBuilder.like(root.get("description"), "%" + this.description + "%"));
		}
		if (this.idIn != null && this.idIn.length > 0) {
			predicates.add(criteriaBuilder.and(root.get("id").in((Object[]) this.idIn)));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
