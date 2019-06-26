package br.org.rpf.cagef.controller;

import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.service.VolunteerService;

@RestController
@RequestMapping("/volunteers")
public class VolunteerController {
	
	@Autowired
	private VolunteerService volunteerService;

	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Page<Volunteer>> findAll(
			@RequestParam(value="id", required=false) Long id,
			@RequestParam(value="name", required=false) String name,
			@RequestParam(value="city.id",  required=false) Long[] cityIds,
			@RequestParam(value="city.name", required=false) String cityName,
			@RequestParam(value="ministryOrPosition.id", required=false) Long[] ministryOrPositionIds,
			@RequestParam(value="ministryOrPosition.description", required=false) String ministryOrPosition,
			@RequestParam(value="offset", defaultValue="0") Integer offset, 
			@RequestParam(value="limit", defaultValue="10") Integer limit, 
			@RequestParam(value="orderBy", defaultValue="id") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.ok(this.volunteerService.findAll(id, name, cityIds, cityName, ministryOrPosition, ministryOrPositionIds, offset, limit, orderBy, direction));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@RequestMapping(value="/{id}", method=RequestMethod.GET)
	public ResponseEntity<Volunteer> findById(@PathVariable Long id) {
		return ResponseEntity.ok(this.volunteerService.byId(id));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.POST, RequestMethod.OPTIONS})
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<Volunteer> save(@RequestBody VolunteerDTO volunteerDTO){
		return ResponseEntity.of(Optional.of(this.volunteerService.save(volunteerDTO)));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.PUT, RequestMethod.OPTIONS})
	@RequestMapping(value="/{id}", method=RequestMethod.PUT)
	public ResponseEntity<Volunteer> update(@PathVariable Long id, @Valid @RequestBody VolunteerDTO volunteerDTO){
		return ResponseEntity.of(Optional.of(this.volunteerService.update(id, volunteerDTO)));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.DELETE, RequestMethod.OPTIONS})
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable Long id){
		this.volunteerService.remove(id);
		return ResponseEntity.noContent().build();
	}
}
