package br.org.rpf.cagef.entity;

import java.io.Serializable;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author elbio.caetano
 */
@XmlRootElement(name="contato")
@XmlAccessorType(XmlAccessType.NONE)
public class Contato implements Serializable {


	private static final long serialVersionUID = -3082177914670184155L;
	
	@XmlAttribute(name="id")
	private Long id;
	@XmlElement
	private String nome;
	@XmlElement
	private String ministerio;
	@XmlElement
	private String cidade;
	@XmlElement
	private String telefone;
	@XmlElement
	private String celular;
	@XmlElement
	private String email;

	public Contato() {
	}

	public Contato(Long id) {
		this.id = id;
	}

	public Contato(Long id, String nome, String ministerio, String cidade, String telefone, String celular, String email) {
		super();
		this.id = id;
		this.nome = nome;
		this.ministerio = ministerio;
		this.cidade = cidade;
		this.telefone = telefone;
		this.celular = celular;
		this.email = email;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		if(nome != null) {
			this.nome = nome.toUpperCase();
		} else {
			this.nome = nome;
		}
	}

	public String getMinisterio() {
		return ministerio;
	}

	public void setMinisterio(String ministerio) {
		if(ministerio != null) {
			this.ministerio = ministerio.toUpperCase();
		} else {
			this.ministerio = ministerio;
		}	}

	public String getCidade() {
		return cidade;
	}

	public void setCidade(String cidade) {
		this.cidade = cidade;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		if(email != null) {
			this.email = email.toLowerCase();
		} else {
			this.email = email;
		}
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((celular == null) ? 0 : celular.hashCode());
		result = prime * result + ((cidade == null) ? 0 : cidade.hashCode());
		result = prime * result + ((email == null) ? 0 : email.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
		result = prime * result + ((telefone == null) ? 0 : telefone.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Contato other = (Contato) obj;
		if (celular == null) {
			if (other.celular != null)
				return false;
		} else if (!celular.equals(other.celular))
			return false;
		if (cidade == null) {
			if (other.cidade != null)
				return false;
		} else if (!cidade.equals(other.cidade))
			return false;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		if (telefone == null) {
			if (other.telefone != null)
				return false;
		} else if (!telefone.equals(other.telefone))
			return false;
		return true;
	}

}
