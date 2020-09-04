package br.org.rpf.cagef.dto.prayinghouse;

import java.io.Serializable;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import br.org.rpf.cagef.dto.city.CityInnerDTO;
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
public class PrayingHouseDTO implements Serializable {
	private static final long serialVersionUID = 751445228719197189L;

	@NotNull
	@Size(min = 3, max = 45)
	private String reportCode;
	@NotNull
	@Size(min = 3, max = 45)
	private String district;
	@Valid
	@NotNull
	private CityInnerDTO city;
}
