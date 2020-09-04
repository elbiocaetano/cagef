package br.org.rpf.cagef.dto.http.request.city;

import br.org.rpf.cagef.dto.http.request.RequestParamsDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class PrayingHouseRequestParamsDTO extends RequestParamsDTO {
	private String reportCode;
	private Long cityId;
	private String cityName;
	private String district;
}
