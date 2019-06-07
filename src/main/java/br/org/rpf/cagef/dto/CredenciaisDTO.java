package br.org.rpf.cagef.dto;

import java.io.Serializable;
import java.util.Base64;

public class CredenciaisDTO implements Serializable {
	private static final long serialVersionUID = 1L;
	
	private String username;
	private String password;
	
	public CredenciaisDTO() {
	}
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = new String(Base64.getDecoder().decode(password.getBytes()));
	}
}
