package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.util.ArrayList;
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

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
@Table(name = "ministeriocargos")
public class MinistryOrPosition implements Serializable {

	private static final long serialVersionUID = -6524153139410485852L;
	
	@Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id_ministeriocargo")
    private Long id;
    @Column(name = "descricao", unique = true)
    private String description;
    
    @ManyToMany(fetch=FetchType.LAZY, mappedBy = "ministryOrPosition")
    @JsonIgnore
    private List<Volunteer> volunteers;
    
    public MinistryOrPosition(Long id) {
    	this.id = id;
    }
    
    public MinistryOrPosition(Long id, String description) {
    	this.id = id;
    	this.description = description;
    }
    
    public MinistryOrPosition() {
    	
    }

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public List<Volunteer> getVolunteers() {
		return volunteers == null ? new ArrayList<Volunteer>() : volunteers;
	}

	public void setVolunteers(List<Volunteer> volunteers) {
		this.volunteers = volunteers;
	}
	
	public void addVolunteers(List<Volunteer> volunteers) {
		this.getVolunteers().addAll(volunteers);
	}
}
