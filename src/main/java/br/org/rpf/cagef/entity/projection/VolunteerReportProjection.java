package br.org.rpf.cagef.entity.projection;

import java.time.LocalDate;

import org.springframework.beans.factory.annotation.Value;

public interface VolunteerReportProjection {
	
	public Long getId();

	
	public String getName();

	@Value("#{target.city.id}")
	public String getCity();

	@Value("#{target.city.state}")
	public String getState();

	@Value("#{target.ministryOrPosition.description}")
	public String getMinistryOrPosition();
	
	public LocalDate getDateOfBirth();

}
