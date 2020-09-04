package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

/**
 *
 * @author elbio.caetano
 */
@Entity
@Table(name = "voluntarios")
@Inheritance(strategy = InheritanceType.JOINED)
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class Volunteer implements Serializable {

	private static final long serialVersionUID = 8909322380615594035L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	private Long id;
	@Column(name = "nome")
	private String name;
	@Column(name = "endereco")
	private String address;
	@Column(name = "bairro")
	private String district;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "id_cidade")
	@Fetch(FetchMode.JOIN)
	private City city;
	@Column(name = "cep")
	private String zipCode;
	@Column(name = "telefone")
	private String phoneNumber;
	@Column(name = "celular")
	private String celNumber;
	@Column(name = "email")
	private String email;
	@Column(name = "data_nascimento")
	private LocalDate dateOfBirth;
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "naturalidade")
	@Fetch(FetchMode.JOIN)
	private City naturalness;
	@Column(name = "data_batismo")
	private LocalDate dateOfBaptism;
	@Column(name = "cpf")
	private String cpf;
	@Column(name = "rg")
	private String rg;
	@Column(name = "estado_civil")
	private String maritalStatus;
	@Column(name = "data_apres_minis_cargo")
	private LocalDate ministryApresentationDate;
	@Column(name = "tempo_cidade_atual")
	private String timeInCity;
	@Column(name = "promessa")
	private String promise;
	@JsonIgnore
	@Column(name = "updated_at")
	private LocalDate updatedAt;
	@JsonIgnore
	@Column(name = "created_at")
	private LocalDate createdAt;
	@JoinColumn(name = "cod_relatorio")
	@ManyToOne(cascade = CascadeType.MERGE)
	private PrayingHouse prayingHouse;
	@ManyToMany(fetch = FetchType.LAZY)
	@JoinTable(name = "voluntarios_ministeriocargos", joinColumns = @JoinColumn(name = "id_voluntario", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "id_ministeriocargos", referencedColumnName = "id_ministeriocargo"))
	@Fetch(FetchMode.JOIN)
	@Builder.Default
	private List<MinistryOrPosition> ministryOrPosition = new ArrayList<>();

	public void setPrayingHouse(PrayingHouse prayingHouse) {
		if (sameAsFormer(prayingHouse))
			return;
		PrayingHouse oldPrayingHouse = this.prayingHouse;
		this.prayingHouse = prayingHouse;
		if (oldPrayingHouse != null)
			oldPrayingHouse.removeVolunteer(this);
		if (prayingHouse != null)
			prayingHouse.addVolunteer(this);
	}

	private boolean sameAsFormer(PrayingHouse newPrayingHouse) {
		return prayingHouse == null ? newPrayingHouse == null : prayingHouse.equals(newPrayingHouse);
	}
}
