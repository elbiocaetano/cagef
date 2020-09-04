package br.org.rpf.cagef.controller;

import java.net.URI;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.rpf.cagef.dto.http.request.city.VolunteerRequestParamsDTO;
import br.org.rpf.cagef.dto.volunteer.ReportVolunteerProjection;
import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.service.VolunteerService;

@RestController
@RequestMapping("/volunteers")
public class VolunteerController {
	
	@Autowired
	@Qualifier("volunteerService")
	private VolunteerService volunteerService;

	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@GetMapping
	public ResponseEntity<Page<Volunteer>> findAll(
			@RequestParam(value="id", required=false) Long id,
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="city.id",  required=false) Long[] cityIds,
			@RequestParam(value="city.name", required=false) String cityName,
			@RequestParam(value="ministryOrPosition.id", required=false) Long[] ministryOrPositionIds,
			@RequestParam(value="ministryOrPosition.description", required=false) String ministryOrPositionDescription,
			@RequestParam(value="prayingHouse.reportCode", required=false) String[] prayingHouseReportCodes,
			@RequestParam(value="prayingHouse.district", required=false) String prayingHouseDistrict,
			@RequestParam(value="offset", defaultValue="0") Integer offset, 
			@RequestParam(value="limit", defaultValue="10") Integer limit, 
			@RequestParam(value="orderBy", defaultValue="id") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		
		return ResponseEntity.ok(this.volunteerService.findAll(VolunteerRequestParamsDTO.builder().id(id).name(name)
				.cityIds(cityIds).cityName(cityName).ministryOrPositionDescription(ministryOrPositionDescription)
				.ministryOrPositionIds(ministryOrPositionIds).prayingHouseReportCodes(prayingHouseReportCodes)
				.prayingHouseDistrict(prayingHouseDistrict).offset(offset).limit(limit).orderBy(orderBy)
				.direction(direction).build()));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@GetMapping(value="/{id}")
	public ResponseEntity<Volunteer> findById(@PathVariable Long id) {
		return ResponseEntity.ok(this.volunteerService.byId(id));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.POST, RequestMethod.OPTIONS})
	@PostMapping
	public ResponseEntity<Volunteer> save(@Validated @RequestBody VolunteerDTO volunteerDTO){
		Volunteer volunteer = this.volunteerService.save(volunteerDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(volunteer.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.PUT, RequestMethod.OPTIONS})
	@PutMapping(value="/{id}")
	public ResponseEntity<Volunteer> update(@PathVariable Long id, @Validated @RequestBody VolunteerDTO volunteerDTO){
		this.volunteerService.update(id, volunteerDTO);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.DELETE, RequestMethod.OPTIONS})
	@DeleteMapping(value="/{id}")
	public ResponseEntity<Void> remove(@PathVariable Long id){
		this.volunteerService.remove(id);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@GetMapping(value="/report")
	public ResponseEntity<List<ReportVolunteerProjection>> report(
			@RequestParam(value="city.id",  required=false) List<Long> cityIds,
			@RequestParam(value="ministryOrPosition.id", required=false) List<Long> ministryOrPositionIds) {
		return ResponseEntity.ok(this.volunteerService.reportVolunteersByCityAndMinistryOrPosition(cityIds, ministryOrPositionIds));
	}
}
