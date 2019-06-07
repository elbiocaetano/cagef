package br.org.rpf.cagef.dto.city;

import java.io.Serializable;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;

public class CityDTO implements Serializable{
	
	private static final long serialVersionUID = 5492705028119380094L;

	@Min(1)
	private Long id;
	
	@NotNull
	@Length(min = 3, max = 255)
	private String name;
	
	@NotNull
	@Length(min = 3, max = 255)
	private String state;
	
	@NotNull
	private Boolean regional;
	
	public CityDTO() {
		super();
	}

	public CityDTO(Long id, String name, String state, Boolean regional) {
		super();
		this.id = id;
		this.name = name;
		this.state = state;
		this.regional = regional;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getRegional() {
		return regional;
	}

	public void setRegional(Boolean regional) {
		this.regional = regional;
	}
}
