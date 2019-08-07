package br.org.rpf.cagef.dto.city;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

public class CityInnerDTO implements Serializable{

	private static final long serialVersionUID = 1078912772390906332L;

	@NotNull
	@Min(1)
	private Long id;
	
	public CityInnerDTO() {
		super();
	}

	public CityInnerDTO(@NotNull @Min(1) Long id) {
		this();
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

}
