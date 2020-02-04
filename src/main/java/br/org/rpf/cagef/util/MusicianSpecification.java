package br.org.rpf.cagef.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.ObjectUtils;

import br.org.rpf.cagef.entity.Musician;

public class MusicianSpecification implements Specification<Musician> {
	private static final long serialVersionUID = -8286869602726971063L;

	private static final Long[] VALID_IDS = { 28l, 29l, 30l, 32l, 33l, 49l };

	private Long id;
	private String name;
	private Long[] cityIds;
	private String cityName;
	private String ministryOrPositionDescription;
	private Long[] ministryOrPositionIds = VALID_IDS;
	private String instrumentDescription;
	private Long[] instrumentIds;

	public MusicianSpecification(Long id, String name, Long[] cityIds, String cityName,
			String ministryOrPositionDescription, Long[] ministryOrPositionIds, String instrumentDescription,
			Long[] instrumentIds) {
		super();
		this.id = id;
		this.name = name;
		this.cityIds = cityIds;
		this.cityName = cityName;
		this.ministryOrPositionDescription = ministryOrPositionDescription;
		if (!ObjectUtils.isEmpty(ministryOrPositionIds)) {
			Long[] ids = (Long[]) List.of(ministryOrPositionIds).stream().filter(m -> List.of(VALID_IDS).contains(m))
					.distinct().toArray();
			if (!ObjectUtils.isEmpty(ids)) {
				this.ministryOrPositionIds = ids;
			}
		}
		this.instrumentDescription = instrumentDescription;
		this.instrumentIds = instrumentIds;
	}

	@Override
	public Predicate toPredicate(Root<Musician> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();

		query.distinct(true);
		if (this.id != null) {
			predicates.add(criteriaBuilder.equal(root.get("id"), this.id));
		}
		if (this.name != null) {
			predicates.add(criteriaBuilder.like(root.get("name"), "%" + this.name + "%"));
		}
		if (this.cityIds != null && this.cityIds.length > 0) {
			predicates
					.add(criteriaBuilder.and(root.join("city", JoinType.INNER).get("id").in((Object[]) this.cityIds)));
		}
		if (this.cityName != null) {
			predicates.add(
					criteriaBuilder.like(root.join("city", JoinType.INNER).get("name"), "%" + this.cityName + "%"));
		}
		if (this.ministryOrPositionDescription != null) {
			predicates.add(criteriaBuilder.like(root.join("ministryOrPosition", JoinType.INNER).get("description"),
					"%" + this.ministryOrPositionDescription + "%"));
		}
		if (!ObjectUtils.isEmpty(ministryOrPositionIds)) {
			predicates.add(root.join("ministryOrPosition", JoinType.INNER).get("id")
					.in((Object[]) this.ministryOrPositionIds));
		}
		if (this.instrumentDescription != null) {
			predicates.add(criteriaBuilder.like(root.join("instrument", JoinType.INNER).get("description"),
					"%" + this.instrumentDescription + "%"));
		}
		if (!ObjectUtils.isEmpty(instrumentIds)) {
			predicates.add(root.join("instrument", JoinType.INNER).get("id").in((Object[]) this.instrumentIds));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}
}
