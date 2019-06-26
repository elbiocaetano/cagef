package br.org.rpf.cagef.dto.volunteer;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

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

public class VolunteerDTO implements Serializable{
	
	private static final long serialVersionUID = 2679554574289800806L;

	@Min(1)
	private Long id;
	
	@NotNull
	@Length(min=1, max=255)
	private String name;
	
	@Length(min=1, max=255)
	private String address;

	@Length(min=1, max=45)
	private String district;

	@Valid
	@NotNull
	private CityInnerDTO city;

	@Length(min=1, max=45)
	private String zipCode;

	@Length(min=1, max=45)
	private String phoneNumber;

	@Length(min=1, max=45)
	private String celNumber;

	@Length(min=1, max=255)
	private String email;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate dateOfBirth;

	@Valid
	private CityInnerDTO naturalness;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate dateOfBaptism;

	@CPF
	private String cpf;

	@Length(min=1, max=45)
	private String rg;

	private String maritalStatus;

	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using=LocalDateDeserializer.class)
	@DateTimeFormat(pattern="dd/MM/yyyy")
	private LocalDate ministryApresentationDate;

	@Length(min=1, max=45)
	private String timeInCity;

	@Length(min=1, max=3)
	private String promise;

	@Valid
	private PrayingHouseInnerDTO prayingHouse;
	
	@Valid
	private MinistryOrPositionInnerDTO ministryOrPosition;

	public VolunteerDTO() {
		super();
	}
	
	public VolunteerDTO(Long id, String name, CityInnerDTO city, MinistryOrPositionInnerDTO ministryOrPosition) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
		this.ministryOrPosition = ministryOrPosition;
	}
	
	public VolunteerDTO(Long id, String name, String address, String district, CityInnerDTO city, String zipCode,
			String phonenNumber, String celNumber, String email, LocalDate dateOfBirth, CityInnerDTO naturalness,
			LocalDate dateOfBaptism, String cpf, String rg, MaritalStatusEnum maritalStatus, LocalDate ministryApresentationDate,
			String timeInCity, String promise, String updatedAt, String createdAt, PrayingHouseInnerDTO prayingHouse,
			MinistryOrPositionInnerDTO ministryOrPosition) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.district = district;
		this.city = city;
		this.zipCode = zipCode;
		this.phoneNumber = phonenNumber;
		this.celNumber = celNumber;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.naturalness = naturalness;
		this.dateOfBaptism = dateOfBaptism;
		this.cpf = cpf;
		this.rg = rg;
		this.maritalStatus = maritalStatus != null ? maritalStatus.getDescription() : null;
		this.ministryApresentationDate = ministryApresentationDate;
		this.timeInCity = timeInCity;
		this.promise = promise;
		this.prayingHouse = prayingHouse;
		this.ministryOrPosition = ministryOrPosition;
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

	public CityInnerDTO getCity() {
		return city;
	}

	public void setCity(CityInnerDTO city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phonenNumber) {
		this.phoneNumber = phonenNumber;
	}

	public String getCelNumber() {
		return celNumber;
	}

	public void setCelNumber(String celNumber) {
		this.celNumber = celNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public CityInnerDTO getNaturalness() {
		return naturalness;
	}

	public void setNaturalness(CityInnerDTO naturalness) {
		this.naturalness = naturalness;
	}

	public LocalDate getDateOfBaptism() {
		return dateOfBaptism;
	}

	public void setDateOfBaptism(LocalDate dateOfBaptism) {
		this.dateOfBaptism = dateOfBaptism;
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
		this.maritalStatus = maritalStatus != null ? MaritalStatusEnum.toEnum(maritalStatus).getDescription() : maritalStatus;
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

	public PrayingHouseInnerDTO getPrayingHouse() {
		return prayingHouse;
	}

	public void setPrayingHouse(PrayingHouseInnerDTO prayingHouse) {
		this.prayingHouse = prayingHouse;
	}

	public MinistryOrPositionInnerDTO getMinistryOrPosition() {
		return ministryOrPosition;
	}

	public void setMinistryOrPosition(MinistryOrPositionInnerDTO ministryOrPosition) {
		this.ministryOrPosition = ministryOrPosition;
	}
}
