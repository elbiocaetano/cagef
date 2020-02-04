package br.org.rpf.cagef.dto.musician;

import java.time.LocalDate;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

import org.hibernate.validator.constraints.Length;
import org.springframework.format.annotation.DateTimeFormat;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;

import br.org.rpf.cagef.dto.city.CityInnerDTO;
import br.org.rpf.cagef.dto.instrument.InstrumentInnerDTO;
import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionInnerDTO;
import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseInnerDTO;
import br.org.rpf.cagef.dto.volunteer.BaseVolunteerDTO;

public class MusicianDTO extends BaseVolunteerDTO {
	private static final long serialVersionUID = 8319437154233809176L;

	@Valid
	@NotNull
	private InstrumentInnerDTO instrument;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate oficializationDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate rehearsalDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate rjmExamDate;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy")
	@JsonDeserialize(using = LocalDateDeserializer.class)
	@DateTimeFormat(pattern = "dd/MM/yyyy")
	private LocalDate oficialCultExamDate;
	@Length(min = 1, max = 65535)
	private String observation;

	public MusicianDTO() {
		super();
	}

	public MusicianDTO(Long id, String name, CityInnerDTO city, String phoneNumber, String celNumber, String email,
			LocalDate dateOfBirth, PrayingHouseInnerDTO prayingHouse, MinistryOrPositionInnerDTO ministryOrPosition,
			InstrumentInnerDTO instrument, LocalDate oficializationDate) {
		super(id, name, city, phoneNumber, celNumber, email, dateOfBirth, prayingHouse, ministryOrPosition);
		this.instrument = instrument;
		this.oficializationDate = oficializationDate;
	}

	public InstrumentInnerDTO getInstrument() {
		return instrument;
	}

	public void setInstrument(InstrumentInnerDTO instrument) {
		this.instrument = instrument;
	}

	public LocalDate getOficializationDate() {
		return oficializationDate;
	}

	public void setOficializationDate(LocalDate oficializationDate) {
		this.oficializationDate = oficializationDate;
	}

	public LocalDate getRehearsalDate() {
		return rehearsalDate;
	}

	public void setRehearsalDate(LocalDate rehearsalDate) {
		this.rehearsalDate = rehearsalDate;
	}

	public LocalDate getRjmExamDate() {
		return rjmExamDate;
	}

	public void setRjmExamDate(LocalDate rjmExamDate) {
		this.rjmExamDate = rjmExamDate;
	}

	public LocalDate getOficialCultExamDate() {
		return oficialCultExamDate;
	}

	public void setOficialCultExamDate(LocalDate oficialCultExamDate) {
		this.oficialCultExamDate = oficialCultExamDate;
	}

	public String getObservation() {
		return observation;
	}

	public void setObservation(String observation) {
		this.observation = observation;
	}
}
