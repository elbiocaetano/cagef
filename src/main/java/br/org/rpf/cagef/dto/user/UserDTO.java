package br.org.rpf.cagef.dto.user;

import java.io.Serializable;
import java.util.Base64;

import br.org.rpf.cagef.dto.city.CityInnerDTO;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class UserDTO implements Serializable {
	private static final long serialVersionUID = 5115148653761791475L;

	private Long id;
	private String name;
	private CityInnerDTO city;
	private String email;
	private String password;
	private String role;

	public void setPassword(String password) {
		this.password = new String(Base64.getDecoder().decode(password.getBytes()));
	}
}
