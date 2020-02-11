package br.org.rpf.cagef.dto.instrument;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class InstrumentDTO {

	@NotNull
	@Length(min = 1)
	private String description;

	@Valid
	@NotNull
	private InstrumentCategoryInnerDTO category;

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public InstrumentCategoryInnerDTO getCategory() {
		return category;
	}

	public void setCategory(InstrumentCategoryInnerDTO category) {
		this.category = category;
	}
}
