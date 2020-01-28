package br.org.rpf.cagef.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.ObjectUtils;

import br.org.rpf.cagef.dto.musician.MusicianDTO;
import br.org.rpf.cagef.dto.volunteer.BaseVolunteerDTO;
import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.City;
import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.entity.MinistryOrPosition;
import br.org.rpf.cagef.entity.Musician;
import br.org.rpf.cagef.entity.PrayingHouse;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.InstrumentRepository;
import br.org.rpf.cagef.repository.MinistryOrPositionRepository;
import br.org.rpf.cagef.repository.MusicianRepository;
import br.org.rpf.cagef.repository.PrayingHouseRepository;
import br.org.rpf.cagef.repository.VolunteerRepository;
import br.org.rpf.cagef.service.DefaultVolunteerService;

public abstract class DefaultVolunteerServiceImpl implements DefaultVolunteerService {

	@Autowired
	private CityRepository cityRepository;

	@Autowired
	private MinistryOrPositionRepository ministryOrPositionRepository;

	@Autowired
	private PrayingHouseRepository prayingHouseRepository;

	@Autowired
	private InstrumentRepository instrumentRepository;

	@Autowired
	protected VolunteerRepository volunteerRepository;

	@Autowired
	protected MusicianRepository musicianRepository;

	public Volunteer byId(Long id) {
		Volunteer v = volunteerRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));
		if (v instanceof Musician) {
			return (Musician) v;
		}
		return v;
	}

	public abstract Volunteer save(BaseVolunteerDTO volunteerDTO);

	@Transactional
	public Volunteer update(Long id, BaseVolunteerDTO baseDTO) {
		Volunteer v = this.volunteerRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));

		baseDTO.setId(v.getId());
		if (baseDTO instanceof VolunteerDTO) {
			this.musicianRepository.findById(v.getId()).ifPresentOrElse(m -> {
				this.musicianRepository.save(fromDTO((VolunteerDTO) baseDTO, m));
			}, () -> {
				this.save((VolunteerDTO) baseDTO);
			});
		} else {
			this.musicianRepository.findById(v.getId()).ifPresentOrElse(m -> {
				this.save((MusicianDTO) baseDTO);
			}, () -> {
				this.musicianRepository.createMusician(((MusicianDTO) baseDTO).getInstrument().getId(),
						((MusicianDTO) baseDTO).getOficializationDate(), v.getId());
				this.save((MusicianDTO) baseDTO);
			});
		}

		return v;
	}

	protected Volunteer fromDTO(VolunteerDTO volunteerDTO) {
		PrayingHouse prayingHouse = null;
		City naturalness = null;
		City cidade = cityRepository.findById(volunteerDTO.getCity().getId()).orElseThrow(
				() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getCity().getId(), City.class.getName()));

		if (!ObjectUtils.isEmpty(volunteerDTO.getNaturalness())) {
			naturalness = cityRepository.findById(volunteerDTO.getNaturalness().getId())
					.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getNaturalness().getId(),
							City.class.getName()));
		}

		if (volunteerDTO.getPrayingHouse() != null && volunteerDTO.getPrayingHouse().getReportCode() != null) {
			prayingHouse = prayingHouseRepository.findById(volunteerDTO.getPrayingHouse().getReportCode()).orElseThrow(
					() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getPrayingHouse().getReportCode(),
							PrayingHouse.class.getName()));
		}
		List<MinistryOrPosition> ministryOrPositions = this.ministryOrPositionRepository
				.findAllById(volunteerDTO.getMinistryOrPosition().getIds());

		return new Volunteer(volunteerDTO, cidade, naturalness, prayingHouse, ministryOrPositions);
	}

	public Musician fromDTO(VolunteerDTO volunteerDTO, Musician m) {
		PrayingHouse prayingHouse = null;
		City naturalness = null;
		City cidade = cityRepository.findById(volunteerDTO.getCity().getId()).orElseThrow(
				() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getCity().getId(), City.class.getName()));

		if (!ObjectUtils.isEmpty(volunteerDTO.getNaturalness())) {
			naturalness = cityRepository.findById(volunteerDTO.getNaturalness().getId())
					.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getNaturalness().getId(),
							City.class.getName()));
		}

		if (volunteerDTO.getPrayingHouse() != null && volunteerDTO.getPrayingHouse().getReportCode() != null) {
			prayingHouse = prayingHouseRepository.findById(volunteerDTO.getPrayingHouse().getReportCode()).orElseThrow(
					() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getPrayingHouse().getReportCode(),
							PrayingHouse.class.getName()));
		}
		List<MinistryOrPosition> ministryOrPositions = this.ministryOrPositionRepository
				.findAllById(volunteerDTO.getMinistryOrPosition().getIds());

		return new Musician(volunteerDTO, cidade, naturalness, prayingHouse, ministryOrPositions, m.getInstrument(),
				m.getOficializationDate());
	}

	protected Musician fromDTO(MusicianDTO musicianDTO) {
		PrayingHouse prayingHouse = null;
		City cidade = cityRepository.findById(musicianDTO.getCity().getId()).orElseThrow(
				() -> new org.hibernate.ObjectNotFoundException(musicianDTO.getCity().getId(), City.class.getName()));

		Instrument instrument = instrumentRepository.findById(musicianDTO.getInstrument().getId())
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(musicianDTO.getInstrument().getId(),
						Instrument.class.getName()));

		if (musicianDTO.getPrayingHouse() != null && musicianDTO.getPrayingHouse().getReportCode() != null) {
			prayingHouse = prayingHouseRepository.findById(musicianDTO.getPrayingHouse().getReportCode()).orElseThrow(
					() -> new org.hibernate.ObjectNotFoundException(musicianDTO.getPrayingHouse().getReportCode(),
							PrayingHouse.class.getName()));
		}
		List<MinistryOrPosition> ministryOrPositions = this.ministryOrPositionRepository
				.findAllById(musicianDTO.getMinistryOrPosition().getIds());

		return new Musician(musicianDTO, cidade, null, prayingHouse, ministryOrPositions, instrument,
				musicianDTO.getOficializationDate());
	}

	@Override
	public void remove(Long id) {
		Volunteer volunteer = this.volunteerRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));
		this.volunteerRepository.delete(volunteer);
	}
}
