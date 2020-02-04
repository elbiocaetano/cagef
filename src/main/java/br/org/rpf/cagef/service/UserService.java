package br.org.rpf.cagef.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;

import br.org.rpf.cagef.dto.user.UserDTO;
import br.org.rpf.cagef.entity.User;

public interface UserService extends UserDetailsService{
	public Page<User> findAll(Long id, String name, String email, String city, String role, int offset, int limit, String orderBy,
			String direction);
	
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
		return authenticated().getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()));
	}
	
	public default boolean isAnyAdmin() {
		return authenticated().getAuthorities().stream().anyMatch(role -> "ROLE_ADMIN".equals(role.getAuthority()) || "ROLE_ADMIN_MUSICA".equals(role.getAuthority()));
	}
}
