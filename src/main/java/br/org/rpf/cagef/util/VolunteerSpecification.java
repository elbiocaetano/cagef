package br.org.rpf.cagef.util;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.data.jpa.domain.Specification;

import br.org.rpf.cagef.dto.http.request.city.VolunteerRequestParamsDTO;
import br.org.rpf.cagef.entity.Volunteer;

public class VolunteerSpecification implements Specification<Volunteer> {
	private static final long serialVersionUID = -6512330083197715870L;

	private Long id;
	private String name;
	private Long[] cityIds;
	private String cityName;
	private String ministryOrPositionDescription;
	private Long[] ministryOrPositionIds;
	private String[] prayingHouseReportCodes;
	private String prayingHouseDistrict;
	private Boolean regional;

	public VolunteerSpecification(VolunteerRequestParamsDTO requestParams) {
		super();
		this.id = requestParams.getId();
		this.name = requestParams.getName();
		this.cityIds = requestParams.getCityIds();
		this.cityName = requestParams.getCityName();
		this.ministryOrPositionDescription = requestParams.getMinistryOrPositionDescription();
		this.ministryOrPositionIds = requestParams.getMinistryOrPositionIds();
		this.prayingHouseReportCodes = requestParams.getPrayingHouseReportCodes();
		this.prayingHouseDistrict = requestParams.getPrayingHouseDistrict();
		this.regional = null;
	}

	@Override
	public Predicate toPredicate(Root<Volunteer> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
		List<Predicate> predicates = new ArrayList<>();

		query.distinct(false);
		if (this.id != null) {
			predicates.add(criteriaBuilder.equal(root.get("id"), this.id));
		}
		if (this.name != null) {
			predicates.add(criteriaBuilder.like(root.get("name"), "%" + this.name + "%"));
		}
		if (this.cityIds != null && this.cityIds.length > 0) {
			predicates.add(criteriaBuilder.and(root.get("city").get("id").in((Object[]) this.cityIds)));
		}
		joinCity(root, criteriaBuilder, predicates);
		joinMinisteryOrPosition(root, criteriaBuilder, predicates);
		if (this.prayingHouseReportCodes != null && this.prayingHouseReportCodes.length > 0) {
			predicates.add(root.get("prayingHouse").get("reportCode").in((Object[]) this.prayingHouseReportCodes));
		}
		if (this.prayingHouseDistrict != null) {
			predicates.add(criteriaBuilder.like(root.join("prayingHouse", JoinType.INNER).get("district"),
					"%" + this.prayingHouseDistrict + "%"));
		}

		return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
	}

	private void joinMinisteryOrPosition(Root<Volunteer> root, CriteriaBuilder criteriaBuilder,
			List<Predicate> predicates) {
		if (this.ministryOrPositionDescription != null
				|| (this.ministryOrPositionIds != null && this.ministryOrPositionIds.length > 0)) {
			Join<Object, Object> ministryorPosition = root.join("ministryOrPosition", JoinType.INNER);
			if (this.ministryOrPositionDescription != null) {
				predicates.add(criteriaBuilder.like(ministryorPosition.get("description"),
						"%" + this.ministryOrPositionDescription + "%"));
			}
			if (this.ministryOrPositionIds != null && this.ministryOrPositionIds.length > 0) {
				predicates.add(ministryorPosition.get("id").in((Object[]) this.ministryOrPositionIds));
			}
		}
	}

	private void joinCity(Root<Volunteer> root, CriteriaBuilder criteriaBuilder, List<Predicate> predicates) {
		if (this.cityName != null || this.regional != null) {
			Join<Object, Object> city = root.join("city", JoinType.INNER);
			if (this.cityName != null) {
				predicates.add(criteriaBuilder.like(city.get("name"), "%" + this.cityName + "%"));
			}
			if (this.regional != null) {
				predicates.add(criteriaBuilder.equal(city.get("regional"), this.regional));
			}
		}
	}

}
