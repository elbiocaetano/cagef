package br.org.rpf.cagef.dto.http.request.city;

import br.org.rpf.cagef.dto.http.request.RequestParamsDTO;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class CityRequestParamsDTO extends RequestParamsDTO {
	private String name;
	private String state;
	private Boolean regional;
}
