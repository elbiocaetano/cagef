package br.org.rpf.cagef.dto.volunteer;

import java.time.LocalDate;

import javax.validation.Valid;

import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.br.CPF;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import br.org.rpf.cagef.dto.city.CityInnerDTO;
import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionInnerDTO;
import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseInnerDTO;
import br.org.rpf.cagef.entity.enums.MaritalStatusEnum;

public class VolunteerDTO extends BaseVolunteerDTO {

	private static final long serialVersionUID = 2679554574289800806L;

	@Length(min = 1, max = 255)
	private String address;

	@Length(min = 1, max = 45)
	private String district;

	@Length(min = 1, max = 45)
	private String zipCode;

	@Valid
	private CityInnerDTO naturalness;

	@CPF
	private String cpf;

	@Length(min = 1, max = 45)
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

	@Length(min = 1, max = 45)
	private String timeInCity;

	@Length(min = 1, max = 3)
	private String promise;

	public VolunteerDTO() {
		super();
	}

	public VolunteerDTO(Long id, String name, String address, String district, CityInnerDTO city, String zipCode,
			String phoneNumber, String celNumber, String email, LocalDate dateOfBirth, CityInnerDTO naturalness,
			LocalDate dateOfBaptism, String cpf, String rg, MaritalStatusEnum maritalStatus,
			LocalDate ministryApresentationDate, String timeInCity, String promise, String updatedAt, String createdAt,
			PrayingHouseInnerDTO prayingHouse, MinistryOrPositionInnerDTO ministryOrPosition) {
		super(id, name, city, phoneNumber, celNumber, email, dateOfBirth, prayingHouse, ministryOrPosition);
		this.address = address;
		this.district = district;
		this.zipCode = zipCode;
		this.naturalness = naturalness;
		this.dateOfBaptism = dateOfBaptism;
		this.cpf = cpf;
		this.rg = rg;
		this.maritalStatus = maritalStatus != null ? maritalStatus.getDescription() : null;
		this.ministryApresentationDate = ministryApresentationDate;
		this.timeInCity = timeInCity;
		this.promise = promise;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public CityInnerDTO getNaturalness() {
		return naturalness;
	}

	public void setNaturalness(CityInnerDTO naturalness) {
		this.naturalness = naturalness;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public MaritalStatusEnum getMaritalStatus() {
		return MaritalStatusEnum.toEnum(maritalStatus);
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus != null ? MaritalStatusEnum.toEnum(maritalStatus).getDescription()
				: maritalStatus;
	}
	
	public LocalDate getDateOfBaptism() {
		return dateOfBaptism;
	}

	public void setDateOfBaptism(LocalDate dateOfBaptism) {
		this.dateOfBaptism = dateOfBaptism;
	}

	public LocalDate getMinistryApresentationDate() {
		return ministryApresentationDate;
	}

	public void setMinistryApresentationDate(LocalDate ministryApresentationDate) {
		this.ministryApresentationDate = ministryApresentationDate;
	}

	public String getTimeInCity() {
		return timeInCity;
	}

	public void setTimeInCity(String timeInCity) {
		this.timeInCity = timeInCity;
	}

	public String getPromise() {
		return promise;
	}

	public void setPromise(String promise) {
		this.promise = promise;
	}
}
