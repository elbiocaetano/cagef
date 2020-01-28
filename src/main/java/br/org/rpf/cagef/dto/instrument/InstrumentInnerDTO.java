package br.org.rpf.cagef.dto.instrument;

import java.io.Serializable;

public class InstrumentInnerDTO implements Serializable{
	private static final long serialVersionUID = 429050103363953895L;
	
	public InstrumentInnerDTO() {
		super();
	}
	
	public InstrumentInnerDTO(Long id) {
		super();
		this.id = id;
	}

	private Long id;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
}
