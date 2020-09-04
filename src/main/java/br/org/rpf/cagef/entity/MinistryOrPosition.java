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

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "ministeriocargos")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
public class MinistryOrPosition implements Serializable {
	private static final long serialVersionUID = -6524153139410485852L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id_ministeriocargo")
	private Long id;
	@Column(name = "descricao", unique = true)
	private String description;

	@ManyToMany(fetch = FetchType.LAZY, mappedBy = "ministryOrPosition")
	@JsonIgnore
	@Builder.Default
	private List<Volunteer> volunteers = new ArrayList<>();

	public MinistryOrPosition(Long id) {
		this.id = id;
	}

	public MinistryOrPosition(Long id, String description) {
		this.id = id;
		this.description = description;
	}
}
