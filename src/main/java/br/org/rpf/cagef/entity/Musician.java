package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.org.rpf.cagef.dto.musician.MusicianDTO;
import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;

/**
 *
 * @author elbio.caetano
 */
@Entity
@Table(name = "musician")
public class Musician extends Volunteer implements Serializable {

	private static final long serialVersionUID = 9200081341764819062L;
	
	@JoinColumn(name = "instrument_id", nullable = true)
	@ManyToOne(cascade = CascadeType.MERGE)
	private Instrument instrument;
	@Column(name = "oficialization_date")
	private LocalDate oficializationDate;

	public Musician() {
	}
	
	public Musician(Instrument instrument, LocalDate oficializationDate) {
		this.instrument = instrument;
		this.oficializationDate = oficializationDate;
	}

	public Musician(Long id, String name, City city, List<MinistryOrPosition> ministryOrPosition) {
	}

	public Musician(Long id, String name, City city, String phoneNumber, String celNumber, String email,
			LocalDate dateOfBirth, LocalDate updatedAt, LocalDate createdAt, PrayingHouse prayingHouse,
			List<MinistryOrPosition> ministryOrPosition, Instrument instrument, LocalDate oficializationDate) {
		super(id, name, city, phoneNumber, celNumber, email, dateOfBirth, updatedAt, createdAt, prayingHouse,
				ministryOrPosition);
		this.instrument = instrument;
		this.oficializationDate = oficializationDate;
	}


	public Musician(VolunteerDTO volunteerDTO, City city, City naturalness, PrayingHouse prayingHouse,
			List<MinistryOrPosition> ministryOrPositions, Instrument instrument, LocalDate oficializationDate) {
		super(volunteerDTO, city, naturalness, prayingHouse, ministryOrPositions);
		this.instrument = instrument;
		this.oficializationDate = oficializationDate;
	}
	
	public Musician(MusicianDTO musicianDTO, City city, City naturalness, PrayingHouse prayingHouse,
			List<MinistryOrPosition> ministryOrPositions, Instrument instrument, LocalDate oficializationDate) {
		super(musicianDTO, city, naturalness, prayingHouse, ministryOrPositions);
		this.instrument = instrument;
		this.oficializationDate = oficializationDate;
	}

	public Instrument getInstrument() {
		return instrument;
	}

	public void setInstrument(Instrument instrument) {
		this.instrument = instrument;
	}

	public LocalDate getOficializationDate() {
		return oficializationDate;
	}

	public void setOficializationDate(LocalDate oficializationDate) {
		this.oficializationDate = oficializationDate;
	}
}
