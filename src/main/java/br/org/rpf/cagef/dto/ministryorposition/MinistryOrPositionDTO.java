package br.org.rpf.cagef.dto.ministryorposition;

import java.io.Serializable;

public class MinistryOrPositionDTO implements Serializable{

	private static final long serialVersionUID = 8872585354539446287L;

	private Long id;
	
	private String description;

	public MinistryOrPositionDTO() {
		super();
	}
	public MinistryOrPositionDTO(Long id, String description) {
		super();
		this.id = id;
		this.description = description;
	}
	
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
}
