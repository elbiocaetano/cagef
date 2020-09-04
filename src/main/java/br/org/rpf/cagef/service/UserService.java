package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import br.org.rpf.cagef.dto.http.request.city.UserRequestParamsDTO;
import br.org.rpf.cagef.dto.user.UserDTO;
import br.org.rpf.cagef.entity.User;

public interface UserService extends UserDetailsService{
	public Page<User> findAll(UserRequestParamsDTO requestParams);
	
	public User byId(Long id);
	
	public User save(UserDTO user);
	
	public User update(Long id, UserDTO userDTO);
	
	public void remove(Long id);
	
	public static User authenticated() {
		try {
			return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		}
		catch (Exception e) {
			return null;
		}
	}
	
	public default boolean isAdmin() {
		User authenticated = authenticated();
		return authenticated != null && authenticated.getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()));
	}
	
	public default boolean isAnyAdmin() {
		User authenticated = authenticated();
		return authenticated != null && authenticated.getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()) || "ROLE_ADMIN_MUSICA".equals(role.getAuthority()));
	}
}
