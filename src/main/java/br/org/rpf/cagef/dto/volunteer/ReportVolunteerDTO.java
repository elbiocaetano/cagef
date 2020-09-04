package br.org.rpf.cagef.dto.volunteer;

import java.io.Serializable;
import java.time.LocalDate;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class ReportVolunteerDTO implements Serializable{

	private static final long serialVersionUID = 7990558118429712855L;
	
	private Long id;
	
	private String name;
	
	private String city;
	
	private String state;
	
	private String ministryOrPosition;
	
	private LocalDate dateOfBirth;
}
