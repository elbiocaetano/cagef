package br.org.rpf.cagef.dto.volunteer;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Size;

import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import br.org.rpf.cagef.dto.city.CityInnerDTO;
import br.org.rpf.cagef.entity.enums.MaritalStatusEnum;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

@Data
@NoArgsConstructor
@AllArgsConstructor
@SuperBuilder
@EqualsAndHashCode(callSuper = false)
public class VolunteerDTO extends BaseVolunteerDTO {

	private static final long serialVersionUID = 2679554574289800806L;

	@Size(min = 1, max = 255)
	private String address;

	@Size(min = 1, max = 45)
	private String district;

	@Size(min = 1, max = 45)
	private String zipCode;

	@Valid
	private CityInnerDTO naturalness;

	@CPF
	private String cpf;

	@Size(min = 1, max = 45)
	private String rg;

	private String maritalStatus;
	
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate dateOfBaptism;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate ministryApresentationDate;

	@Size(min = 1, max = 45)
	private String timeInCity;

	@Size(min = 1, max = 3)
	private String promise;

	public MaritalStatusEnum getMaritalStatus() {
		return MaritalStatusEnum.toEnum(maritalStatus);
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus != null ? MaritalStatusEnum.toEnum(maritalStatus).getDescription()
				: maritalStatus;
	}
}
