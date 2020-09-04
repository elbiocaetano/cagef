package br.org.rpf.cagef.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

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
import br.org.rpf.cagef.entity.enums.MaritalStatusEnum;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.InstrumentRepository;
import br.org.rpf.cagef.repository.MinistryOrPositionRepository;
import br.org.rpf.cagef.repository.MusicianRepository;
import br.org.rpf.cagef.repository.PrayingHouseRepository;
import br.org.rpf.cagef.repository.VolunteerRepository;
import br.org.rpf.cagef.service.DefaultVolunteerService;

public abstract class DefaultVolunteerServiceImpl implements DefaultVolunteerService {
	
	protected static final List<Long> MUSIC_MINISTERIES = List.of(28l, 29l, 30l, 32l, 33l, 49l);

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
		return volunteerRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));
	}

	@Transactional
	public Volunteer update(Long id, BaseVolunteerDTO baseDTO) {
		Volunteer v = this.volunteerRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));

		baseDTO.setId(v.getId());
		if(!hasMusicMinistery(baseDTO)) {
			this.musicianRepository.removeRelationship(v.getId());
		}
		if (baseDTO instanceof VolunteerDTO) {
			this.musicianRepository.findById(v.getId()).ifPresentOrElse(m -> 
				this.musicianRepository.save(fromDTO((VolunteerDTO) baseDTO, m))
			, () -> {
				if(hasMusicMinistery(baseDTO)) {
					this.musicianRepository.createMusician(null, null, v.getId());
					this.musicianRepository.save(fromDTO((VolunteerDTO) baseDTO, new Musician()));
				} else {
					this.save(baseDTO);
				}
			});
		} else {
			this.musicianRepository.findById(v.getId()).ifPresentOrElse(m ->
				this.save(baseDTO)
			, () -> {
				this.musicianRepository.createMusician(((MusicianDTO) baseDTO).getInstrument().getId(),
						((MusicianDTO) baseDTO).getOficializationDate(), v.getId());
				this.save(baseDTO);
			});
		}

		return v;
	}

	protected Volunteer fromDTO(VolunteerDTO volunteerDTO) {
		PrayingHouse prayingHouse = null;
		City naturalness = null;
		City city = cityRepository.findById(volunteerDTO.getCity().getId()).orElseThrow(
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

		return fromDTO(volunteerDTO, prayingHouse, naturalness, city, ministryOrPositions);
	}

	protected Volunteer fromDTO(VolunteerDTO volunteerDTO, PrayingHouse prayingHouse, City naturalness, City city,
			List<MinistryOrPosition> ministryOrPositions) {
		return Volunteer.builder().id(volunteerDTO.getId()).name(volunteerDTO.getName())
				.address(volunteerDTO.getAddress()).district(volunteerDTO.getDistrict()).city(city)
				.zipCode(volunteerDTO.getZipCode()).phoneNumber(volunteerDTO.getPhoneNumber())
				.celNumber(volunteerDTO.getCelNumber()).email(volunteerDTO.getEmail())
				.dateOfBirth(volunteerDTO.getDateOfBirth()).naturalness(naturalness)
				.dateOfBaptism(volunteerDTO.getDateOfBaptism()).cpf(volunteerDTO.getCpf()).rg(volunteerDTO.getRg())
				.maritalStatus(Optional.ofNullable(volunteerDTO.getMaritalStatus())
						.map(MaritalStatusEnum::getDescription).orElse(null))
				.ministryApresentationDate(volunteerDTO.getMinistryApresentationDate())
				.timeInCity(volunteerDTO.getTimeInCity()).promise(volunteerDTO.getPromise()).updatedAt(LocalDate.now())
				.createdAt(LocalDate.now()).prayingHouse(prayingHouse).ministryOrPosition(ministryOrPositions).build();
	}

	protected Musician fromDTO(VolunteerDTO volunteerDTO, Musician m) {
		PrayingHouse prayingHouse = null;
		City naturalness = null;
		City city = cityRepository.findById(volunteerDTO.getCity().getId()).orElseThrow(
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

		return Musician.builder().id(volunteerDTO.getId()).name(volunteerDTO.getName())
				.address(volunteerDTO.getAddress()).district(volunteerDTO.getDistrict()).city(city)
				.zipCode(volunteerDTO.getZipCode()).phoneNumber(volunteerDTO.getPhoneNumber())
				.celNumber(volunteerDTO.getCelNumber()).email(volunteerDTO.getEmail())
				.dateOfBirth(volunteerDTO.getDateOfBirth()).naturalness(naturalness)
				.dateOfBaptism(volunteerDTO.getDateOfBaptism()).cpf(volunteerDTO.getCpf()).rg(volunteerDTO.getRg())
				.maritalStatus(Optional.ofNullable(volunteerDTO.getMaritalStatus())
						.map(MaritalStatusEnum::getDescription).orElse(null))
				.ministryApresentationDate(volunteerDTO.getMinistryApresentationDate())
				.timeInCity(volunteerDTO.getTimeInCity()).promise(volunteerDTO.getPromise()).updatedAt(LocalDate.now())
				.createdAt(LocalDate.now()).prayingHouse(prayingHouse).ministryOrPosition(ministryOrPositions)
				.instrument(m.getInstrument()).oficializationDate(m.getOficializationDate())
				.rehearsalDate(m.getRehearsalDate()).rjmExamDate(m.getRjmExamDate())
				.oficialCultExamDate(m.getOficialCultExamDate()).observation(m.getObservation()).build();
	}

	protected Musician fromDTO(MusicianDTO musicianDTO) {
		PrayingHouse prayingHouse = null;
		City city = cityRepository.findById(musicianDTO.getCity().getId()).orElseThrow(
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

		return Musician.builder().id(musicianDTO.getId()).name(musicianDTO.getName()).city(city)
				.phoneNumber(musicianDTO.getPhoneNumber()).celNumber(musicianDTO.getCelNumber())
				.email(musicianDTO.getEmail()).dateOfBirth(musicianDTO.getDateOfBirth()).updatedAt(LocalDate.now())
				.createdAt(LocalDate.now()).prayingHouse(prayingHouse).ministryOrPosition(ministryOrPositions)
				.instrument(instrument).oficializationDate(musicianDTO.getOficializationDate())
				.rehearsalDate(musicianDTO.getRehearsalDate()).rjmExamDate(musicianDTO.getRjmExamDate())
				.oficialCultExamDate(musicianDTO.getOficialCultExamDate()).observation(musicianDTO.getObservation())
				.build();
	}
	
	protected boolean hasMusicMinistery(BaseVolunteerDTO baseDTO) {
		return baseDTO.getMinistryOrPosition().getIds().stream().anyMatch(MUSIC_MINISTERIES::contains);
	}

	@Override
	public void remove(Long id) {
		Volunteer volunteer = this.volunteerRepository.findById(id)
				.orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));
		this.volunteerRepository.delete(volunteer);
	}
}
