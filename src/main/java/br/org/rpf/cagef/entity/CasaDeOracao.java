package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 *
 * @author elbio.caetano
 */
@Entity
@Table(name = "casasoracao")
public class CasaDeOracao implements Serializable {

	private static final long serialVersionUID = 5359458823838538383L;

	@Id
	@Column(name = "cod_relatorio", unique = true)
	private String codRelatorio;
	@OneToOne
	@JoinColumn(name = "id_cidade")
	private Cidade cidade;
	@Column(name = "bairro")
	private String bairro;
	@JsonIgnore
	@Column(name = "updated_at")
	private Date updatedAt;
	@JsonIgnore
	@Column(name = "created_at")
	private Date createdAt;
	@JsonIgnore
	@OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.MERGE, mappedBy = "casaDeOracao")
	private Collection<Voluntario> voluntariosCollection;

	public CasaDeOracao() {
	}
	
	public CasaDeOracao(String codRelatorio, String bairro, Cidade cidade) {
		this.codRelatorio = codRelatorio;
		this.bairro = bairro;
		this.cidade = cidade;
	}
	
	public CasaDeOracao(String bairro, Cidade cidade) {
		this.bairro = bairro;
		this.cidade = cidade;
	}

	public String getCodRelatorio() {
		return codRelatorio;
	}

	public void setCodRelatorio(String codRelatorio) {
		this.codRelatorio = codRelatorio;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Date getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(Date updatedAt) {
		this.updatedAt = updatedAt;
	}

	public Date getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Date createdAt) {
		this.createdAt = createdAt;
	}

	public Collection<Voluntario> getVoluntariosCollection() {
		return new ArrayList<Voluntario>(voluntariosCollection);
	}

	public void addVoluntario(Voluntario voluntario) {
		if (voluntariosCollection.contains(voluntario))
			return;
		voluntariosCollection.add(voluntario);
		voluntario.setCasaDeOracao(this);
	}

	public void removeVoluntario(Voluntario voluntario) {
		if (!voluntariosCollection.contains(voluntario))
			return;
		voluntariosCollection.remove(voluntario);
		voluntario.setCasaDeOracao(null);
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (codRelatorio != null ? codRelatorio.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof CasaDeOracao)) {
			return false;
		}
		CasaDeOracao other = (CasaDeOracao) object;
		if ((this.codRelatorio == null && other.codRelatorio != null)
				|| (this.codRelatorio != null && !this.codRelatorio
						.equals(other.codRelatorio))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return codRelatorio + ": " + bairro + " - " + cidade;
	}

}
