package br.org.rpf.cagef.dto.volunteer;

import java.io.Serializable;
import java.time.LocalDate;

public class ReportVolunteerDTO implements Serializable{

	private static final long serialVersionUID = 7990558118429712855L;
	
	private Long id;
	
	private String name;
	
	private String city;
	
	private String state;
	
	private String ministryOrPosition;
	
	private LocalDate dateOfBirth;
	

	public ReportVolunteerDTO() {
		super();
	}
	
	public ReportVolunteerDTO(Long id, String name, String city, String state, String ministryOrPosition,
			LocalDate dateOfBirth) {
		this();
		this.id = id;
		this.name = name;
		this.city = city;
		this.state = state;
		this.ministryOrPosition = ministryOrPosition;
		this.dateOfBirth = dateOfBirth;
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

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getMinistryOrPosition() {
		return ministryOrPosition;
	}

	public void setMinistryOrPosition(String ministryOrPosition) {
		this.ministryOrPosition = ministryOrPosition;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}
	
}
