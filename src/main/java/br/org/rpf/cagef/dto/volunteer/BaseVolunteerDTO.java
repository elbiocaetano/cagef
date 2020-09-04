package br.org.rpf.cagef.dto.volunteer;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import br.org.rpf.cagef.dto.city.CityInnerDTO;
import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionInnerDTO;
import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseInnerDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@SuperBuilder
public class BaseVolunteerDTO implements Serializable{
	private static final long serialVersionUID = 1082142461710612322L;
	
	@Min(1)
	private Long id;
	@NotNull
	@Size(min=1, max=255)
	private String name;
	@Valid
	@NotNull
	private CityInnerDTO city;
	@Size(min=1, max=45)
	private String phoneNumber;
	@Size(min=1, max=45)
	private String celNumber;
	@Size(min=1, max=255)
	private String email;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate dateOfBirth;
	@Valid
	private PrayingHouseInnerDTO prayingHouse;
	@Valid
	@NotNull
	private MinistryOrPositionInnerDTO ministryOrPosition;

	public BaseVolunteerDTO(Long id, String name, CityInnerDTO city,
			MinistryOrPositionInnerDTO ministryOrPosition) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.ministryOrPosition = ministryOrPosition;
	}
}
