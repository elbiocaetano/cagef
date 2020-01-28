package br.org.rpf.cagef.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.rpf.cagef.dto.ministryorposition.MinistryOrPositionDTO;
import br.org.rpf.cagef.entity.MinistryOrPosition;
import br.org.rpf.cagef.service.MinistryOrPositionService;

@RestController
@RequestMapping("/ministeriesOrPositions")
public class MinistryOrPositionController {
	
	@Autowired
	private MinistryOrPositionService ministryOrPositionService;

	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Page<MinistryOrPosition>> buscar(
			@RequestParam(value="id", required=false) Long id,
			@RequestParam(value="id.in", required=false) Long[] idIn,
			@RequestParam(value="description", required=false) String description,
			@RequestParam(value="offset", defaultValue="0") Integer offset, 
			@RequestParam(value="limit", defaultValue="24") Integer limit, 
			@RequestParam(value="orderBy", defaultValue="id") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.ok(this.ministryOrPositionService.findAll(id, idIn, description, offset, limit, orderBy, direction));
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@RequestMapping(value="/{id}", method = RequestMethod.GET)
	public ResponseEntity<MinistryOrPosition> byId(@PathVariable Long id){
		return ResponseEntity.ok(this.ministryOrPositionService.byId(id));
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.OPTIONS })
	@RequestMapping(method = RequestMethod.POST)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> save(@RequestBody MinistryOrPositionDTO ministryOrPositionDTO) {
		MinistryOrPosition ministryOrPosition = this.ministryOrPositionService.save(ministryOrPositionDTO);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(ministryOrPosition.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.PUT, RequestMethod.OPTIONS })
	@RequestMapping(value="/{id}", method = RequestMethod.PUT)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody MinistryOrPositionDTO ministryOrPositionDTO) {
		this.ministryOrPositionService.update(id, ministryOrPositionDTO);

		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.DELETE, RequestMethod.OPTIONS})
	@RequestMapping(value="/{id}", method=RequestMethod.DELETE)
	@PreAuthorize("hasAnyRole('ADMIN')")
	public ResponseEntity<Void> remove(@PathVariable Long id){
		this.ministryOrPositionService.remove(id);
		return ResponseEntity.noContent().build();
	}
}
