package br.org.rpf.cagef.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "cidades")
public class City implements Serializable {

	private static final long serialVersionUID = -3120546348144717013L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_cidade")
	private Long id;
	
	@Basic(optional = false)
    @Column(name = "nome")
	private String name;
	
	@Basic(optional = false)
    @Column(name = "estado")
	private String state;
	
	@Basic(optional = false)
    @Column(name = "regional")
	private Boolean regional;
	
	public City() {
		super();
	}
	
	public City(Long id, String name, String state, Boolean regional) {
		super();
		this.id = id;
		this.name = name;
		this.state = state;
		this.regional = regional;
	}

	public City(Long id, String name) {
		super();
		this.id = id;
		this.name = name;
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

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public Boolean getRegional() {
		return regional;
	}

	public void setRegional(Boolean regional) {
		this.regional = regional;
	}
}
