package br.org.rpf.cagef.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.City;
import br.org.rpf.cagef.entity.MinistryOrPosition;
import br.org.rpf.cagef.entity.PrayingHouse;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.repository.CityRepository;
import br.org.rpf.cagef.repository.MinistryOrPositionRepository;
import br.org.rpf.cagef.repository.PrayingHouseRepository;
import br.org.rpf.cagef.repository.VolunteerRepository;
import br.org.rpf.cagef.service.UserService;
import br.org.rpf.cagef.service.VolunteerService;
import br.org.rpf.cagef.util.VolunteerSpecification;

@Service
public class VolunteerServiceImpl implements VolunteerService {

	@Autowired
	private VolunteerRepository volunteerRepository;
	
	@Autowired
	private CityRepository cityRepository;
	
	@Autowired
	private MinistryOrPositionRepository ministryOrPositionRepository;
	
	@Autowired
	private PrayingHouseRepository prayingHouseRepository;
	
	@Autowired
	private UserService userService;
	
	public Page<Volunteer> findAll(Long id, String name, Long[] cityIds, String cityName,
			String ministryOrPositionDescription, Long[] ministryOrPositionIds, int offset, int limit, String orderBy,
			String direction) {
		if (!this.userService.isAdmin()) {
			cityIds = new Long[] { UserService.authenticated().getCity().getId() };
		}

		Page<Volunteer> page = volunteerRepository.findAll(
				new VolunteerSpecification(id, name, cityIds, cityName, ministryOrPositionDescription,
						ministryOrPositionIds),
				PageRequest.of(offset, limit, Direction.fromString(direction), orderBy));
		return page;
	}

	public Volunteer byId(Long id) {
		return volunteerRepository.findById(id).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));
	}
	
	@Override
	public Volunteer save(VolunteerDTO volunteerDTO) {
		return volunteerRepository.save(fromDTO(volunteerDTO));
	}
	
	@Override
	public Volunteer update(Long id, VolunteerDTO volunteerDTO) {
		this.volunteerRepository.findById(id).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));
		volunteerDTO.setId(id);
		return volunteerRepository.save(fromDTO(volunteerDTO));
	}
	
	@Override
	public void remove(Long id) {
		Volunteer volunteer = this.volunteerRepository.findById(id).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(id, Volunteer.class.getName()));;
		this.volunteerRepository.delete(volunteer);
	}

	private Volunteer fromDTO(VolunteerDTO volunteerDTO) {
		City naturalness = null;
		PrayingHouse prayingHouse = null;
		City cidade = cityRepository.findById(volunteerDTO.getCity().getId()).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getCity().getId(), City.class.getName()));
		if(volunteerDTO.getNaturalness() != null && volunteerDTO.getNaturalness().getId() != null) {
			naturalness = cityRepository.findById(volunteerDTO.getNaturalness().getId()).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getNaturalness().getId(), City.class.getName()));
		}
		
		if(volunteerDTO.getPrayingHouse() != null && volunteerDTO.getPrayingHouse().getReportCode() != null) {
			prayingHouse = prayingHouseRepository.findById(volunteerDTO.getPrayingHouse().getReportCode()).orElseThrow(() -> new org.hibernate.ObjectNotFoundException(volunteerDTO.getPrayingHouse().getReportCode(), PrayingHouse.class.getName()));
		}
		List<MinistryOrPosition> ministryOrPositions = this.ministryOrPositionRepository.findAllById(volunteerDTO.getMinistryOrPosition().getIds());
		
		return new Volunteer(volunteerDTO, cidade, naturalness, prayingHouse, ministryOrPositions);
	}
}
