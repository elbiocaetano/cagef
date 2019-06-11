package br.org.rpf.cagef.controller;

import java.net.URI;

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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.org.rpf.cagef.dto.prayinghouse.PrayingHouseDTO;
import br.org.rpf.cagef.entity.PrayingHouse;
import br.org.rpf.cagef.service.PrayingHouseService;

@RestController
@RequestMapping("/prayinghouses")
public class PrayingHouseController {
	
	@Autowired
	private PrayingHouseService prayingHouseService;

	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@RequestMapping(method=RequestMethod.GET)
	public ResponseEntity<Page<PrayingHouse>> findAll(
			@RequestParam(value="reportCode", required=false) String reportCode,
			@RequestParam(value="city.id", required=false) Long cityId,
			@RequestParam(value="city.name", required=false) String cityName,
			@RequestParam(value="district", required=false) String district,
			@RequestParam(value="offset", defaultValue="0") Integer offset, 
			@RequestParam(value="limit", defaultValue="10") Integer limit, 
			@RequestParam(value="orderBy", defaultValue="reportCode") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.ok(this.prayingHouseService.findAll(reportCode, cityId, cityName, district, offset, limit, orderBy, direction));
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@RequestMapping(value="/{reportCode}", method = RequestMethod.GET)
	public ResponseEntity<PrayingHouse> byId(@PathVariable String reportCode){
		return ResponseEntity.ok(this.prayingHouseService.byId(reportCode));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.POST, RequestMethod.OPTIONS})
	@RequestMapping(method=RequestMethod.POST)
	public ResponseEntity<PrayingHouse> save(@RequestBody PrayingHouseDTO prayingHouseDTO){
		PrayingHouse prayingHouse = this.prayingHouseService.save(prayingHouseDTO);
		
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{reportCode}").buildAndExpand(prayingHouse.getReportCode()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.PUT, RequestMethod.OPTIONS})
	@RequestMapping(value="/{reportCode}", method=RequestMethod.PUT)
	public ResponseEntity<PrayingHouse> update(@PathVariable String reportCode, @Valid @RequestBody PrayingHouseDTO prayingHouseDTO){
		this.prayingHouseService.update(reportCode, prayingHouseDTO);
		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.DELETE, RequestMethod.OPTIONS})
	@RequestMapping(value="/{reportCode}", method=RequestMethod.DELETE)
	public ResponseEntity<Void> remove(@PathVariable String reportCode){
		this.prayingHouseService.remove(reportCode);
		return ResponseEntity.noContent().build();
	}
}
