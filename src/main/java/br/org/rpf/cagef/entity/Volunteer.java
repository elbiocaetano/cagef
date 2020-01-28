package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

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

import br.org.rpf.cagef.dto.musician.MusicianDTO;
import br.org.rpf.cagef.dto.volunteer.VolunteerDTO;

/**
 *
 * @author elbio.caetano
 */
@Entity
@Table(name = "voluntarios")
@Inheritance(strategy = InheritanceType.JOINED)
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
	private List<MinistryOrPosition> ministryOrPosition;

	public Volunteer() {
	}

	public Volunteer(Long id) {
		this.id = id;
	}

	public Volunteer(Long id, String name, City city) {
		this.id = id;
		this.name = name;
		this.city = city;
	}

	public Volunteer(Long id, String name, City city, List<MinistryOrPosition> ministryOrPosition) {
		this.id = id;
		this.name = name;
		this.city = city;
		this.ministryOrPosition = ministryOrPosition;
	}
	
	public Volunteer (Long id, String name, City city, String phoneNumber, String celNumber, String email, LocalDate dateOfBirth, LocalDate updatedAt, LocalDate createdAt, PrayingHouse prayingHouse,
			List<MinistryOrPosition> ministryOrPosition) {
		super();
		this.id = id;
		this.name = name;
		this.city = city;
		this.phoneNumber = phoneNumber;
		this.celNumber = celNumber;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.updatedAt = updatedAt;
		this.createdAt = createdAt;
		this.prayingHouse = prayingHouse;
		this.ministryOrPosition = ministryOrPosition;
	}

	public Volunteer(Long id, String name, String address, String district, City city, String zipCode,
			String phoneNumber, String celNumber, String email, LocalDate dateOfBirth, City naturalness, LocalDate dateOfBaptism,
			String cpf, String rg, String maritalStatus, LocalDate ministryApresentationDate, String timeInCity,
			String promise, LocalDate updatedAt, LocalDate createdAt, PrayingHouse prayingHouse,
			List<MinistryOrPosition> ministryOrPosition) {
		super();
		this.id = id;
		this.name = name;
		this.address = address;
		this.district = district;
		this.city = city;
		this.zipCode = zipCode;
		this.phoneNumber = phoneNumber;
		this.celNumber = celNumber;
		this.email = email;
		this.dateOfBirth = dateOfBirth;
		this.naturalness = naturalness;
		this.dateOfBaptism = dateOfBaptism;
		this.cpf = cpf;
		this.rg = rg;
		this.maritalStatus = maritalStatus;
		this.ministryApresentationDate = ministryApresentationDate;
		this.timeInCity = timeInCity;
		this.promise = promise;
		this.updatedAt = updatedAt;
		this.createdAt = createdAt;
		this.prayingHouse = prayingHouse;
		this.ministryOrPosition = ministryOrPosition;
	}

	public Volunteer(VolunteerDTO volunteerDTO, City city, City naturalness, PrayingHouse prayingHouse,
			List<MinistryOrPosition> ministryOrPositions) {
		this.id = volunteerDTO.getId();
		this.name = volunteerDTO.getName();
		this.address = volunteerDTO.getAddress();
		this.district = volunteerDTO.getDistrict();
		this.city = city;
		this.zipCode = volunteerDTO.getZipCode();
		this.phoneNumber = volunteerDTO.getPhoneNumber();
		this.celNumber = volunteerDTO.getCelNumber();
		this.email = volunteerDTO.getEmail();
		this.dateOfBirth = volunteerDTO.getDateOfBirth();
		this.naturalness = naturalness;
		this.dateOfBaptism = volunteerDTO.getDateOfBaptism();
		this.cpf = volunteerDTO.getCpf();
		this.rg = volunteerDTO.getRg();
		this.maritalStatus = volunteerDTO.getMaritalStatus() != null ? volunteerDTO.getMaritalStatus().getDescription()
				: null;
		this.ministryApresentationDate = volunteerDTO.getMinistryApresentationDate();
		this.timeInCity = volunteerDTO.getTimeInCity();
		this.promise = volunteerDTO.getPromise();
		this.updatedAt = LocalDate.now();
		this.createdAt = LocalDate.now();
		this.prayingHouse = prayingHouse;
		this.ministryOrPosition = ministryOrPositions;
	}
	
	public Volunteer(MusicianDTO volunteerDTO, City city, City naturalness, PrayingHouse prayingHouse,
			List<MinistryOrPosition> ministryOrPositions) {
		this.id = volunteerDTO.getId();
		this.name = volunteerDTO.getName();
		this.city = city;
		this.phoneNumber = volunteerDTO.getPhoneNumber();
		this.celNumber = volunteerDTO.getCelNumber();
		this.email = volunteerDTO.getEmail();
		this.dateOfBirth = volunteerDTO.getDateOfBirth();
		this.naturalness = naturalness;
		this.updatedAt = LocalDate.now();
		this.prayingHouse = prayingHouse;
		this.ministryOrPosition = ministryOrPositions;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getDistrict() {
		return district;
	}

	public void setDistrict(String district) {
		this.district = district;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phonenNumber) {
		this.phoneNumber = phonenNumber;
	}

	public String getCelNumber() {
		return celNumber;
	}

	public void setCelNumber(String celNumber) {
		this.celNumber = celNumber;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public LocalDate getDateOfBirth() {
		return dateOfBirth;
	}

	public void setDateOfBirth(LocalDate dateOfBirth) {
		this.dateOfBirth = dateOfBirth;
	}

	public City getNaturalness() {
		return naturalness;
	}

	public void setNaturalness(City naturalness) {
		this.naturalness = naturalness;
	}

	public LocalDate getDateOfBaptism() {
		return dateOfBaptism;
	}

	public void setDateOfBaptism(LocalDate dateOfBaptism) {
		this.dateOfBaptism = dateOfBaptism;
	}

	public String getMaritalStatus() {
		return maritalStatus;
	}

	public void setMaritalStatus(String maritalStatus) {
		this.maritalStatus = maritalStatus;
	}

	public LocalDate getMinistryApresentationDate() {
		return ministryApresentationDate;
	}

	public void setMinistryApresentationDate(LocalDate ministryApresentationDate) {
		this.ministryApresentationDate = ministryApresentationDate;
	}

	public String getTimeInCity() {
		return timeInCity;
	}

	public void setTimeInCity(String timeInCity) {
		this.timeInCity = timeInCity;
	}

	public String getPromise() {
		return promise;
	}

	public void setPromise(String promise) {
		this.promise = promise;
	}

	public List<MinistryOrPosition> getMinistryOrPosition() {
		return ministryOrPosition;
	}

	public void setMinistryOrPosition(List<MinistryOrPosition> ministryOrPosition) {
		this.ministryOrPosition = ministryOrPosition;
	}

	public PrayingHouse getPrayingHouse() {
		return prayingHouse;
	}

	/*public Integer getTempoDataNascimento() {
		if (this.dateOfBirth != null) {
			return getTimeInYears(this.dateOfBirth);
		}

		return null;
	}*/

	public Integer getTempoBatismo() {
		if (this.dateOfBaptism != null) {
			return getTimeInYears(this.dateOfBaptism);
		}

		return null;
	}

	public String getCpf() {
		return cpf;
	}

	public void setCpf(String cpf) {
		this.cpf = cpf;
	}

	public String getRg() {
		return rg;
	}

	public void setRg(String rg) {
		this.rg = rg;
	}

	public Integer getTimeInMinistryOrPosition() {
		if (ministryApresentationDate != null) {
			return getTimeInYears(ministryApresentationDate);
		}

		return null;
	}

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

	private Integer getTimeInYears(LocalDate date) {
		/*Calendar a = getCalendar(date);
		Calendar b = getCalendar(new LocalDate(System.currentTimeMillis()));
		date.
		int diff = b.get(Calendar.YEAR) - a.get(Calendar.YEAR);
		if (a.get(Calendar.MONTH) > b.get(Calendar.MONTH)
				|| (a.get(Calendar.MONTH) == b.get(Calendar.MONTH) && a.get(Calendar.DATE) > b.get(Calendar.DATE))) {
			diff--;
		}
		return diff;*/
		return 0;
	}

	public static Calendar getCalendar(LocalDate date) {
		Calendar cal = Calendar.getInstance(Locale.getDefault());
		// cal.setTime(date.);
		return cal;
	}

	public void addMinistryOrPosition(MinistryOrPosition ministryOrPosition) {
		if (!this.getMinistryOrPosition().contains(ministryOrPosition)) {
			this.ministryOrPosition.add(ministryOrPosition);
		}
	}

	public void removeMinistryOrPosition(final MinistryOrPosition ministryOrPosition) {
		if (this.getMinistryOrPosition().contains(ministryOrPosition)) {
			this.ministryOrPosition.remove(ministryOrPosition);
		}
	}

	public LocalDate getUpdatedAt() {
		return updatedAt;
	}

	public void setUpdatedAt(LocalDate updatedAt) {
		this.updatedAt = updatedAt;
	}

	public LocalDate getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(LocalDate createdAt) {
		this.createdAt = createdAt;
	}

}
