package br.org.rpf.cagef.controller;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.org.rpf.cagef.entity.User;
import br.org.rpf.cagef.security.JWTUtil;
import br.org.rpf.cagef.service.UserService;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

	@Autowired
	private JWTUtil jwtUtil;
	
	@PostMapping(value = "/refresh_token")
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		User user = UserService.authenticated();
		if (user != null) {
			String token = jwtUtil.generateToken(user);
			response.addHeader("Authorization", "Bearer " + token);
			response.addHeader("access-control-expose-headers", "Authorization");
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
	}
}
