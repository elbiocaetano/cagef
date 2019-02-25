package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name="ministerio")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "ministeriocargos")
public class MinisterioOuCargo implements Serializable {

	private static final long serialVersionUID = -6524153139410485852L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ministeriocargo")
	@XmlAttribute(name="id")
    private Long idMinisterioOuCargo;
    @Column(name = "descricao", unique = true)
    @XmlElement
    private String descricao;
    
    @ManyToMany(fetch = FetchType.EAGER, mappedBy = "ministerioCargo")
    private List<Voluntario> voluntarios;
    
	public Long getIdMinisterioOuCargo() {
		return idMinisterioOuCargo;
	}
	public void setIdMinisterioOuCargo(Long idMinisterioOuCargo) {
		this.idMinisterioOuCargo = idMinisterioOuCargo;
	}
	public String getDescricao() {
		return descricao;
	}
	public void setDescricao(String descricao) {
		this.descricao = descricao;
	}
	public List<Voluntario> getVoluntario() {
		return voluntarios;
	}
	public void setVoluntario(List<Voluntario> voluntarios) {
		this.voluntarios = voluntarios;
	}

	@Override
	public String toString() {
		return idMinisterioOuCargo + ": " + descricao;
	}
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((descricao == null) ? 0 : descricao.hashCode());
		result = prime
				* result
				+ ((idMinisterioOuCargo == null) ? 0 : idMinisterioOuCargo
						.hashCode());
		result = prime * result
				+ ((voluntarios == null) ? 0 : voluntarios.hashCode());
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
		MinisterioOuCargo other = (MinisterioOuCargo) obj;
		if (descricao == null) {
			if (other.descricao != null)
				return false;
		} else if (!descricao.equals(other.descricao))
			return false;
		if (idMinisterioOuCargo == null) {
			if (other.idMinisterioOuCargo != null)
				return false;
		} else if (!idMinisterioOuCargo.equals(other.idMinisterioOuCargo))
			return false;
		if (voluntarios == null) {
			if (other.voluntarios != null)
				return false;
		} else if (!voluntarios.equals(other.voluntarios))
			return false;
		return true;
	}
}
