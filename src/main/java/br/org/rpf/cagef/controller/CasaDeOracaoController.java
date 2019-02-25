package br.org.rpf.cagef.controller;

import java.util.Optional;

import javax.websocket.server.PathParam;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.org.rpf.cagef.entity.CasaDeOracao;
import br.org.rpf.cagef.service.CasaDeOracaoService;

@RestController
@RequestMapping("/casasdeoracao")
public class CasaDeOracaoController {
	
	@Autowired
	private CasaDeOracaoService casaDeOracaoService;

	@CrossOrigin(origins = "*", methods={RequestMethod.GET, RequestMethod.OPTIONS})
	@RequestMapping(value="/", method=RequestMethod.GET)
	public ResponseEntity<Page<CasaDeOracao>> buscar(
			@RequestParam(value="codRelatorio", required=false) String codRelatorio,
			@RequestParam(value="cidade", required=false) String cidade,
			@RequestParam(value="bairro", required=false) String bairro,
			@RequestParam(value="offset", defaultValue="0") Integer offset, 
			@RequestParam(value="limit", defaultValue="24") Integer limit, 
			@RequestParam(value="orderBy", defaultValue="codRelatorio") String orderBy, 
			@RequestParam(value="direction", defaultValue="ASC") String direction) {
		return ResponseEntity.ok(this.casaDeOracaoService.buscar(codRelatorio, cidade, bairro, offset,limit,orderBy,direction));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.POST, RequestMethod.OPTIONS})
	@RequestMapping(value="/", method=RequestMethod.POST)
	public ResponseEntity<CasaDeOracao> salvar(@RequestBody CasaDeOracao casaDeOracao){
		return ResponseEntity.of(Optional.of(this.casaDeOracaoService.salvar(casaDeOracao)));
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.PUT, RequestMethod.OPTIONS})
	@RequestMapping(value="/{codRelatorio}", method=RequestMethod.POST)
	public ResponseEntity<CasaDeOracao> atualizar(@PathParam(value="codRelatorio") String codRelatorio, @RequestBody CasaDeOracao casaDeOracao){
		casaDeOracao.setCodRelatorio(codRelatorio);
		return ResponseEntity.of(Optional.of(this.casaDeOracaoService.salvar(casaDeOracao)));
	}
}
