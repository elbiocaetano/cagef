package br.org.rpf.cagef.dto.http.request.city;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.SuperBuilder;

@Data
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class MusicianRequestParamsDTO extends VolunteerRequestParamsDTO {
	private Long[] instrumentIds;
	private String instrumentDescription;
}
