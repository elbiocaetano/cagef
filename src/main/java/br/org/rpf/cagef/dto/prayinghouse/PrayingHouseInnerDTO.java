package br.org.rpf.cagef.dto.prayinghouse;

import java.io.Serializable;

import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class PrayingHouseInnerDTO implements Serializable {

	private static final long serialVersionUID = -8238567569655001057L;

	@Size(min=1, max=255)
	private String reportCode;
	
}
