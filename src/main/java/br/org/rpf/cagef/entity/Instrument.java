package br.org.rpf.cagef.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "instruments")
public class Instrument implements Serializable {

	private static final long serialVersionUID = 1761086781430473882L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;

	@Basic(optional = false)
	@Column(name = "description")
	private String description;

	@JoinColumn(name = "category_id")
	@ManyToOne(cascade = CascadeType.MERGE)
	private InstrumentCategory category;

	public Instrument() {
		super();
	}

	public Instrument(Long id, String description, InstrumentCategory category) {
		super();
		this.id = id;
		this.description = description;
		this.category = category;
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

	public InstrumentCategory getCategory() {
		return category;
	}

	public void setCategory(InstrumentCategory category) {
		this.category = category;
	}
}
