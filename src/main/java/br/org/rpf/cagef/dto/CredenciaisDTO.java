package br.org.rpf.cagef.dto;

import java.io.Serializable;
import java.util.Base64;

import lombok.Data;

@Data
public class CredenciaisDTO implements Serializable {
	private static final long serialVersionUID = -590568645320922552L;
	
	private String username;
	private String password;
	
	public void setPassword(String password) {
		this.password = new String(Base64.getDecoder().decode(password.getBytes()));
	}
}
