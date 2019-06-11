package br.org.rpf.cagef.dto.prayinghouse;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import br.org.rpf.cagef.dto.city.CityInnerDTO;

public class PrayingHouseDTO implements Serializable {
	private static final long serialVersionUID = 751445228719197189L;

	@NotNull
	@Length(min = 3, max = 45)
	private String reportCode;
	@NotNull
	@Length(min = 3, max = 45)
	private String district;
	@Valid
	@NotNull
	private CityInnerDTO city;

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public CityInnerDTO getCity() {
		return city;
	}

	public void setCity(CityInnerDTO city) {
		this.city = city;
	}
}
