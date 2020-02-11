package br.org.rpf.cagef.dto.instrument;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class InstrumentCategoryInnerDTO implements Serializable{
	
	private static final long serialVersionUID = -5635268674032590299L;

	public InstrumentCategoryInnerDTO() {
		super();
	}
	
	public InstrumentCategoryInnerDTO(Long id) {
		super();
		this.id = id;
	}

	@NotNull
	@Min(1)
	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
