package br.org.rpf.cagef.controller;

import java.net.URI;

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

import br.org.rpf.cagef.dto.http.request.city.MusicianRequestParamsDTO;
import br.org.rpf.cagef.dto.musician.MusicianDTO;
import br.org.rpf.cagef.entity.Musician;
import br.org.rpf.cagef.entity.Volunteer;
import br.org.rpf.cagef.service.MusicianService;

@RestController
@RequestMapping("/musicians")
public class MusicianController {

	@Autowired
	@Qualifier("musicianService")
	private MusicianService musicianService;

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@GetMapping
	public ResponseEntity<Page<Musician>> findAll(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "city.id", required = false) Long[] cityIds,
			@RequestParam(value = "city.name", required = false) String cityName,
			@RequestParam(value = "ministryOrPosition.id", required = false) Long[] ministryOrPositionIds,
			@RequestParam(value = "ministryOrPosition.description", required = false) String ministryOrPositionDescription,
			@RequestParam(value = "instrument.id", required = false) Long[] instrumentIds,
			@RequestParam(value = "instrument.description", required = false) String instrumentDescription,
			@RequestParam(value = "prayingHouse.district", required = false) String prayingHouseDistrict,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		return ResponseEntity.ok(this.musicianService.findAll(MusicianRequestParamsDTO.builder().id(id).name(name)
				.cityIds(cityIds).cityName(cityName).ministryOrPositionDescription(ministryOrPositionDescription)
				.ministryOrPositionIds(ministryOrPositionIds).instrumentIds(instrumentIds)
				.instrumentDescription(instrumentDescription).prayingHouseDistrict(prayingHouseDistrict)
				.offset(offset).limit(limit).orderBy(orderBy)
				.direction(direction).build()));
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@GetMapping(value = "/{id}")
	public ResponseEntity<Volunteer> findById(@PathVariable Long id) {
		return ResponseEntity.ok(this.musicianService.byId(id));
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.OPTIONS })
	@PostMapping
	public ResponseEntity<Void> save(@RequestBody MusicianDTO musicianDTO) {
		Volunteer musician = this.musicianService.save(musicianDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(musician.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.PUT, RequestMethod.OPTIONS })
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @Validated @RequestBody MusicianDTO musicianDTO) {
		this.musicianService.update(id, musicianDTO);
		return ResponseEntity.noContent().build();
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.DELETE, RequestMethod.OPTIONS })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> remove(@PathVariable Long id) {
		this.musicianService.remove(id);
		return ResponseEntity.noContent().build();
	}
}
