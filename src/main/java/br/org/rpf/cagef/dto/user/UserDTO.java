package br.org.rpf.cagef.dto.user;

import java.io.Serializable;
import java.util.Base64;

import br.org.rpf.cagef.dto.city.CityInnerDTO;

public class UserDTO implements Serializable {
	private static final long serialVersionUID = 5115148653761791475L;

	private Long id;
	private String name;
	private CityInnerDTO city;
	private String email;
	private String password;
	private String role;

	public UserDTO() {
		super();
	}

	public UserDTO(Long id, String name, CityInnerDTO city, String email, String password, String role) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
		this.email = email;
		this.password = password;
		this.role = role;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public CityInnerDTO getCity() {
		return city;
	}

	public void setCity(CityInnerDTO city) {
		this.city = city;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = new String(Base64.getDecoder().decode(password.getBytes()));
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}
}
