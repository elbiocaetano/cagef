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

import br.org.rpf.cagef.dto.instrument.InstrumentDTO;
import br.org.rpf.cagef.entity.Instrument;
import br.org.rpf.cagef.service.InstrumentService;

@RestController
@RequestMapping("/instruments")
public class InstrumentController {

	@Autowired
	private InstrumentService instrumentService;

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<Page<Instrument>> findAll(@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "category.id", required = false) Long[] categoryIds,
			@RequestParam(value = "category.description", required = false) String categoryName,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "10") Integer limit,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		return ResponseEntity.ok(
				this.instrumentService.findAll(id, name, categoryIds, categoryName, offset, limit, orderBy, direction));
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)
	public ResponseEntity<Instrument> findById(@PathVariable Long id) {
		return ResponseEntity.ok(this.instrumentService.byId(id));
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.OPTIONS })
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Instrument> save(@RequestBody InstrumentDTO instrumentDTO) {
		return ResponseEntity.of(Optional.of(this.instrumentService.save(instrumentDTO)));
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.PUT, RequestMethod.OPTIONS })
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Instrument> update(@PathVariable Long id, @Valid @RequestBody InstrumentDTO instrumentDTO) {
		return ResponseEntity.of(Optional.of(this.instrumentService.update(id, instrumentDTO)));
	}

	@CrossOrigin(origins = "*", methods = { RequestMethod.DELETE, RequestMethod.OPTIONS })
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable Long id) {
		this.instrumentService.remove(id);
		return ResponseEntity.noContent().build();
	}
}
