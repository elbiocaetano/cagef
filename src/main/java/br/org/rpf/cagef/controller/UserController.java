package br.org.rpf.cagef.controller;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
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

import br.org.rpf.cagef.dto.http.request.city.UserRequestParamsDTO;
import br.org.rpf.cagef.dto.user.UserDTO;
import br.org.rpf.cagef.entity.User;
import br.org.rpf.cagef.service.UserService;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserService userService;

	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@GetMapping
	public ResponseEntity<Page<User>> findAll(
			@RequestParam(value = "id", required = false) Long id,
			@RequestParam(value = "name", required = false) String name,
			@RequestParam(value = "email", required = false) String email,
			@RequestParam(value = "city.name", required = false) String city,
			@RequestParam(value = "role", required = false) String role,
			@RequestParam(value = "offset", defaultValue = "0") Integer offset,
			@RequestParam(value = "limit", defaultValue = "24") Integer limit,
			@RequestParam(value = "orderBy", defaultValue = "id") String orderBy,
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {
		
		return ResponseEntity.ok(this.userService.findAll(UserRequestParamsDTO.builder().id(id).name(name).email(email)
				.city(city).role(role).offset(offset).limit(limit).orderBy(orderBy).direction(direction).build()));
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.GET, RequestMethod.OPTIONS })
	@GetMapping(value="/{id}")
	public ResponseEntity<User> byId(@PathVariable Long id){
		return ResponseEntity.ok(this.userService.byId(id));
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.POST, RequestMethod.OPTIONS })
	@PostMapping
	@PreAuthorize("hasAnyRole('ADMIN','ADMIN_MUSICA')")
	public ResponseEntity<Void> save(@RequestBody UserDTO userDTO) {
		User user = this.userService.save(userDTO);

		URI uri = ServletUriComponentsBuilder.fromCurrentRequest().path("/{id}").buildAndExpand(user.getId()).toUri();
		return ResponseEntity.created(uri).build();
	}
	
	@CrossOrigin(origins = "*", methods = { RequestMethod.PUT, RequestMethod.OPTIONS })
	@PutMapping(value="/{id}")
	// @PreAuthorize("hasAnyRole('ADMIN','ADMIN_MUSICA')")
	public ResponseEntity<Void> update(@PathVariable Long id, @RequestBody UserDTO userDTO) {
		this.userService.update(id, userDTO);

		return ResponseEntity.noContent().build();
	}
	
	@CrossOrigin(origins = "*", methods={RequestMethod.DELETE, RequestMethod.OPTIONS})
	@DeleteMapping(value="/{id}")
	@PreAuthorize("hasAnyRole('ADMIN','ADMIN_MUSICA')")
	public ResponseEntity<Void> remove(@PathVariable Long id){
		this.userService.remove(id);
		return ResponseEntity.noContent().build();
	}
}
