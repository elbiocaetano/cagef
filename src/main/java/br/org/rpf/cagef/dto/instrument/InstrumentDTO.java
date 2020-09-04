package br.org.rpf.cagef.dto.instrument;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@Builder
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class InstrumentDTO {

	@NotNull
	@Length(min = 1)
	private String description;

	@Valid
	@NotNull
	private InstrumentCategoryInnerDTO category;
}
