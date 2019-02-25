package br.org.rpf.cagef.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.rpf.cagef.entity.Cidade;
import br.org.rpf.cagef.service.CidadeService;

@RestController
@RequestMapping("/cidades")
public class CidadeController {
	
	@Autowired
	private CidadeService cidadeService;

	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ResponseEntity<Page<Cidade>> buscar(
			@RequestParam(value="id", required=false) Long id,
			@RequestParam(value="nome", required=false) String nome,
			@RequestParam(value="estado", required=false) String estado,
			@RequestParam(value="regional", defaultValue="false") Boolean regional,
			@RequestParam(value="offset", defaultValue="0") Integer offset, 
			@RequestParam(value="limit", defaultValue="24") Integer limit, 
			@RequestParam(value="orderBy", defaultValue="idCidade") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.ok(this.cidadeService.buscar(id, nome, estado, regional, offset,limit,orderBy,direction));
	}
}
