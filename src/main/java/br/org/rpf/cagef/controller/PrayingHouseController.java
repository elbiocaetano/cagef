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

import br.org.rpf.cagef.dto.http.request.city.PrayingHouseRequestParamsDTO;
import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseDTO;
import br.org.rpf.cagef.entity.PrayingHouse;
import br.org.rpf.cagef.service.PrayingHouseService;

@RestController
@RequestMapping("/prayinghouses")
public class PrayingHouseController {
	
	@Autowired
	private PrayingHouseService prayingHouseService;

	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@GetMapping
	public ResponseEntity<Page<PrayingHouse>> findAll(
			@RequestParam(value="reportCode", required=false) String reportCode,
			@RequestParam(value="city.id", required=false) Long cityId,
			@RequestParam(value="city.name", required=false) String cityName,
			@RequestParam(value="district", required=false) String district,
			@RequestParam(value="offset", defaultValue="0") Integer offset, 
			@RequestParam(value="limit", defaultValue="10") Integer limit, 
			@RequestParam(value="orderBy", defaultValue="reportCode") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		
		return ResponseEntity.ok(this.prayingHouseService
				.findAll(PrayingHouseRequestParamsDTO.builder().reportCode(reportCode).cityId(cityId).cityName(cityName)
						.district(district).offset(offset).limit(limit).orderBy(orderBy).direction(direction).build()));
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@GetMapping(value="/{reportCode}")
	public ResponseEntity<PrayingHouse> byId(@PathVariable String reportCode){
		return ResponseEntity.ok(this.prayingHouseService.byId(reportCode));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.POST, RequestMethod.OPTIONS})
	@PostMapping
	public ResponseEntity<PrayingHouse> save(@RequestBody PrayingHouseDTO prayingHouseDTO){
		PrayingHouse prayingHouse = this.prayingHouseService.save(prayingHouseDTO);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{reportCode}").buildAndExpand(prayingHouse.getReportCode()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.PUT, RequestMethod.OPTIONS})
	@PutMapping(value="/{reportCode}")
	public ResponseEntity<PrayingHouse> update(@PathVariable String reportCode, @Validated @RequestBody PrayingHouseDTO prayingHouseDTO){
		this.prayingHouseService.update(reportCode, prayingHouseDTO);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.DELETE, RequestMethod.OPTIONS})
	@DeleteMapping(value="/{reportCode}")
	public ResponseEntity<Void> remove(@PathVariable String reportCode){
		this.prayingHouseService.remove(reportCode);
		return ResponseEntity.noContent().build();
	}
}
