package br.org.rpf.cagef.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="cidade")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "cidades")
public class Cidade implements Serializable {

	private static final long serialVersionUID = -3120546348144717013L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cidade")
	@XmlAttribute(name="id")
	private Long idCidade;
	
	@Basic(optional = false)
    @Column(name = "nome")
	@XmlElement
	private String nome;
	
	@Basic(optional = false)
    @Column(name = "estado")
	@XmlElement
	private String estado;
	
	@Basic(optional = false)
    @Column(name = "regional")
	@XmlElement
	private Boolean regional;
	
	public Cidade() {
		super();
	}
	
	public Cidade(Long idCidade, String nome, String estado, Boolean regional) {
		super();
		this.idCidade = idCidade;
		this.nome = nome;
		this.estado = estado;
		this.regional = regional;
	}
	
	public Cidade(String nome, String estado) {
		super();
		this.nome = nome;
		this.estado = estado;
	}
	
	public Cidade(String nome) {
		super();
		this.nome = nome;
	}

	public Long getIdCidade() {
		return idCidade;
	}

	public void setIdCidade(Long idCidade) {
		this.idCidade = idCidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEstado() {
		return estado;
	}

	public void setEstado(String estado) {
		this.estado = estado;
	}
	
	public Boolean getRegional() {
		return regional;
	}

	public void setRegional(Boolean regional) {
		this.regional = regional;
	}

	@Override
	public String toString() {
		return idCidade + ": " + nome + " - " + estado;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((estado == null) ? 0 : estado.hashCode());
		result = prime * result
				+ ((idCidade == null) ? 0 : idCidade.hashCode());
		result = prime * result + ((nome == null) ? 0 : nome.hashCode());
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
		Cidade other = (Cidade) obj;
		if (estado == null) {
			if (other.estado != null)
				return false;
		} else if (!estado.equals(other.estado))
			return false;
		if (idCidade == null) {
			if (other.idCidade != null)
				return false;
		} else if (!idCidade.equals(other.idCidade))
			return false;
		if (nome == null) {
			if (other.nome != null)
				return false;
		} else if (!nome.equals(other.nome))
			return false;
		return true;
	}
}
