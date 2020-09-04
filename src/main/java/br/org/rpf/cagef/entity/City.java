package br.org.rpf.cagef.entity;

import java.io.Serializable;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "cidades")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode
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
	
	public City(Long id, String name) {
		this.id = id;
		this.name = name;
	}
}
