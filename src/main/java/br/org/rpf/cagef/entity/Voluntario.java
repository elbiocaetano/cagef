package br.org.rpf.cagef.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.Period;
import java.util.Date;
import java.util.List;

import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.springframework.util.StringUtils;

/**
 *
 * @author elbio.caetano
 */
@XmlRootElement(name="voluntario")
@XmlAccessorType(XmlAccessType.NONE)
@Entity
@Table(name = "voluntarios")
public class Voluntario implements Serializable {

	private static final long serialVersionUID = 8909322380615594035L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Basic(optional = false)
	@Column(name = "id")
	@XmlAttribute(name="id")
	private Long id;
	@XmlElement
	@Column(name = "nome")
	private String nome;
	@Column(name = "endereco")
	private String endereco;
	@Column(name = "bairro")
	private String bairro;
	@OneToOne
    @JoinColumn(name="id_cidade")
	@XmlElement
	private Cidade cidade;
	@Column(name = "cep")
	private String cep;
	@Column(name = "telefone")
	private String telefone;
	@Column(name = "celular")
	private String celular;
	// @Pattern(regexp="[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*@(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?",
	// message="E-mail inválido")//if the field contains email address consider
	// using this annotation to enforce field validation
	@Column(name = "email")
	private String email;
	@Column(name = "data_nascimento")
	@XmlElement
	private String dataNascimento;
	@OneToOne
    @JoinColumn(name="naturalidade")
	//@Column(name = "naturalidade")
	private Cidade naturalidade;
	@Column(name = "data_batismo")
	private String dataBatismo;
	@Column(name = "cpf")
	private String cpf;
	@Column(name = "rg")
	private String rg;
	@Column(name = "estado_civil")
	private String estadoCivil;
	@Column(name = "data_apres_minis_cargo")
	private String dataApresMinisCargo;
	@Column(name = "tempo_cidade_atual")
	private String tempoCidadeAtual;
	@Column(name = "promessa")
	private String promessa;
	@Column(name = "updated_at")
	private Date updatedAt;
	@Column(name = "created_at")
	private Date createdAt;
	@Column(name = "codigo_carteirinha")
	@XmlElement
	private String codigoCarteirinha;
	@JoinColumn(name = "cod_relatorio")
	@ManyToOne(cascade = CascadeType.MERGE)
	@XmlElement
	private CasaDeOracao casaDeOracao;
	@ManyToMany(fetch = FetchType.EAGER)
	@JoinTable(name="voluntarios_ministeriocargos", joinColumns = { @JoinColumn(name = "id") }, inverseJoinColumns = { @JoinColumn(name = "id_ministeriocargo") })
	@XmlElement
	private List<MinisterioOuCargo> ministerioCargo;

	public Voluntario() {
	}

	public Voluntario(Long id) {
		this.id = id;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getEndereco() {
		return endereco;
	}

	public void setEndereco(String endereco) {
		this.endereco = endereco;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

	public Cidade getCidade() {
		return cidade;
	}

	public void setCidade(Cidade cidade) {
		this.cidade = cidade;
	}

	public String getCep() {
		return cep;
	}

	public void setCep(String cep) {
		this.cep = cep;
	}

	public String getTelefone() {
		return telefone;
	}

	public void setTelefone(String telefone) {
		this.telefone = telefone;
	}

	public String getCelular() {
		return celular;
	}

	public void setCelular(String celular) {
		this.celular = celular;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getDataNascimento() {
		return dataNascimento;
	}

	public void setDataNascimento(String dataNascimento) {
		this.dataNascimento = dataNascimento;
	}

	public Integer getTempoDataNascimento() {
		if (!StringUtils.isEmpty(dataNascimento)) {

			return getTempoEmAnos(dataNascimento);
		}

		return null;
	}

	public Cidade getNaturalidade() {
		return naturalidade;
	}

	public void setNaturalidade(Cidade naturalidade) {
		this.naturalidade = naturalidade;
	}

	public String getDataBatismo() {
		return dataBatismo;
	}

	public void setDataBatismo(String dataBatismo) {
		this.dataBatismo = dataBatismo;
	}

	public Integer getTempoBatismo() {
		if (!StringUtils.isEmpty(dataBatismo)) {

			return getTempoEmAnos(dataBatismo);
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

	public String getEstadoCivil() {
		return estadoCivil;
	}

	public void setEstadoCivil(String estadoCivil) {
		this.estadoCivil = estadoCivil;
	}

	public String getDataApresMinisCargo() {
		return dataApresMinisCargo;
	}

	public Integer getTempoApresMinisCargo() {
		if (!StringUtils.isEmpty(dataApresMinisCargo)) {

			return getTempoEmAnos(dataApresMinisCargo);
		}

		return null;
	}

	public void setDataApresMinisCargo(String dataApresMinisCargo) {
		this.dataApresMinisCargo = dataApresMinisCargo;
	}

	public String getTempoCidadeAtual() {
		return tempoCidadeAtual;
	}

	public void setTempoCidadeAtual(String tempoCidadeAtual) {
		this.tempoCidadeAtual = tempoCidadeAtual;
	}

	public String getPromessa() {
		return promessa;
	}

	public void setPromessa(String promessa) {
		this.promessa = promessa;
	}

	public void setCasaDeOracao(CasaDeOracao casaDeOracao) {
		if (sameAsFormer(casaDeOracao))
			return;
		CasaDeOracao oldCasaDeOracao = this.casaDeOracao;
		this.casaDeOracao = casaDeOracao;
		if (oldCasaDeOracao != null)
			oldCasaDeOracao.removeVoluntario(this);
		if (casaDeOracao != null)
			casaDeOracao.addVoluntario(this);
	}

	private boolean sameAsFormer(CasaDeOracao newCasaDeOracao) {
		return casaDeOracao == null ? newCasaDeOracao == null : casaDeOracao
				.equals(newCasaDeOracao);
	}

	public CasaDeOracao getCasaDeOracao() {
		return casaDeOracao;
	}

	// public void setCasaDeOracao(CasaDeOracao casaDeOracao) {
	// this.casaDeOracao = casaDeOracao;
	// }

	public List<MinisterioOuCargo> getMinisterioCargo() {
		return ministerioCargo;
	}

	public void setMinisterioCargo(List<MinisterioOuCargo> ministerioCargo) {
		this.ministerioCargo = ministerioCargo;
	}

	private Integer getTempoEmAnos(String data) {
		String[] s = data.split("/");
		if(!StringUtils.isEmpty(s) && s.length == 3){
			LocalDate firstDate = LocalDate.of(Integer.valueOf(s[2]), Integer.valueOf(s[1]), Integer.valueOf(s[0]));
			LocalDate now = LocalDate.now();
			Period p = Period.between(firstDate, now);
			return p.getYears();
		}
		
		return null;
	}

	@Override
	public int hashCode() {
		int hash = 0;
		hash += (id != null ? id.hashCode() : 0);
		return hash;
	}

	@Override
	public boolean equals(Object object) {
		if (!(object instanceof Voluntario)) {
			return false;
		}
		Voluntario other = (Voluntario) object;
		if ((this.id == null && other.id != null)
				|| (this.id != null && !this.id.equals(other.id))) {
			return false;
		}
		return true;
	}

	@Override
	public String toString() {
		return id + ": " + nome + " - " + cidade;
	}

	public void adicionarMinisterioOuCargo(MinisterioOuCargo ministerioOuCargo) {
		if (!this.getMinisterioCargo().contains(ministerioOuCargo)) {
			this.ministerioCargo.add(ministerioOuCargo);
		}
	}

	public void removerMinisterioOuCargo(
			final MinisterioOuCargo ministerioOuCargo) {
		if (this.getMinisterioCargo().contains(ministerioOuCargo)) {
			this.ministerioCargo.remove(ministerioOuCargo);
		}
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

	public String getCodigoCarteirinha() {
		return codigoCarteirinha;
	}

	public void setCodigoCarteirinha(String codigoCarteirinha) {
		this.codigoCarteirinha = codigoCarteirinha;
	}
}
