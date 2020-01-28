package br.org.rpf.cagef.dto.volunteer;

import java.io.Serializable;
import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import br.org.rpf.cagef.dto.city.CityInnerDTO;
import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionInnerDTO;
import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseInnerDTO;

public class BaseVolunteerDTO implements Serializable{
	private static final long serialVersionUID = 1082142461710612322L;
	
	@Min(1)
	private Long id;
	@NotNull
	@Length(min=1, max=255)
	private String name;
	@Valid
	@NotNull
	private CityInnerDTO city;
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
	private PrayingHouseInnerDTO prayingHouse;
	@Valid
	@NotNull
	private MinistryOrPositionInnerDTO ministryOrPosition;
	
	public BaseVolunteerDTO() {
		super();
	}

	public BaseVolunteerDTO(@Min(1) Long id, @NotNull @Length(min = 1, max = 255) String name,
			@Valid @NotNull CityInnerDTO city, @Length(min = 1, max = 45) String phoneNumber,
			@Length(min = 1, max = 45) String celNumber, @Length(min = 1, max = 255) String email,
			LocalDate dateOfBirth, @Valid PrayingHouseInnerDTO prayingHouse,
			@Valid @NotNull MinistryOrPositionInnerDTO ministryOrPosition) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
		this.phoneNumber = phoneNumber;
		this.celNumber = celNumber;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.prayingHouse = prayingHouse;
		this.ministryOrPosition = ministryOrPosition;
	}

	public BaseVolunteerDTO(Long id, String name, CityInnerDTO city,
			MinistryOrPositionInnerDTO ministryOrPosition) {
		this.id = id;
		this.name = name;
		this.city = city;
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

	public CityInnerDTO getCity() {
		return city;
	}

	public void setCity(CityInnerDTO city) {
		this.city = city;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
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
