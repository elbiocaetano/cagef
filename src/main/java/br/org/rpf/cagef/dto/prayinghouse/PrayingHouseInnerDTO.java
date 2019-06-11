package br.org.rpf.cagef.dto.prayinghouse;

import java.io.Serializable;

import org.hibernate.validator.constraints.Length;

public class PrayingHouseInnerDTO implements Serializable {

	private static final long serialVersionUID = -8238567569655001057L;

	@Length(min=1, max=255)
	private String reportCode;
	
	public PrayingHouseInnerDTO() {
		super();
	}
	
	public PrayingHouseInnerDTO(String reportCode) {
		super();
		this.reportCode = reportCode;
	}

	public String getReportCode() {
		return reportCode;
	}

	public void setReportCode(String reportCode) {
		this.reportCode = reportCode;
	}

}
