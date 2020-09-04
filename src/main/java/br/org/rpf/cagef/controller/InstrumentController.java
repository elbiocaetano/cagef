package br.org.rpf.cagef.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
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

import br.org.rpf.cagef.dto.http.request.city.InstrumentRequestParamsDTO;
import br.org.rpf.cagef.dto.instrument.InstrumentDTO;
import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.entity.InstrumentCategory;
import br.org.rpf.cagef.service.InstrumentService;

@RestController
@RequestMapping("/instruments")
public class InstrumentController {

	@Autowired
	private InstrumentService instrumentService;

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@GetMapping
	public ResponseEntity<Page<Instrument>> findAll(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "category.id", required = false) Long[] categoryIds,
			@RequestParam(value = "category.description", required = false) String categoryName,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		return ResponseEntity.ok(this.instrumentService.findAll(InstrumentRequestParamsDTO.builder().id(id)
				.description(description).categoryIds(categoryIds).categoryName(categoryName).offset(offset)
				.limit(limit).orderBy(orderBy).direction(direction).build()));
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@GetMapping(value = "/categories")
	public ResponseEntity<Page<InstrumentCategory>> findAll(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "description", required = false) String description,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		return ResponseEntity.ok(
				this.instrumentService.findAllCategories(id, description, offset, limit, orderBy, direction));
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@GetMapping(value = "/{id}")
	public ResponseEntity<Instrument> findById(@PathVariable Long id) {
		return ResponseEntity.ok(this.instrumentService.byId(id));
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.OPTIONS })
	@PostMapping
	public ResponseEntity<Void> save(@RequestBody InstrumentDTO instrumentDTO) {
		Instrument instrument = this.instrumentService.save(instrumentDTO);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(instrument.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.PUT, RequestMethod.OPTIONS })
	@PutMapping(value = "/{id}")
	public ResponseEntity<Void> update(@PathVariable Long id, @Validated @RequestBody InstrumentDTO instrumentDTO) {
		this.instrumentService.update(id, instrumentDTO);
		return ResponseEntity.noContent().build();
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.DELETE, RequestMethod.OPTIONS })
	@DeleteMapping(value = "/{id}")
	public ResponseEntity<Void> remove(@PathVariable Long id) {
		this.instrumentService.remove(id);
		return ResponseEntity.noContent().build();
	}
}
