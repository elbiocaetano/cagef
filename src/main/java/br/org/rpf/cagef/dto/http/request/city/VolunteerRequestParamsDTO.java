package br.org.rpf.cagef.dto.http.request.city;

import br.org.rpf.cagef.dto.http.request.RequestParamsDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class VolunteerRequestParamsDTO extends RequestParamsDTO {
	private String name;
	private Long[] cityIds;
	private String cityName;
	private String ministryOrPositionDescription;
	private Long[] ministryOrPositionIds;
	private String[] prayingHouseReportCodes;
	private String prayingHouseDistrict;
}
