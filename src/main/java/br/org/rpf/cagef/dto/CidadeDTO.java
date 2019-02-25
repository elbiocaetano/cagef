package br.org.rpf.cagef.dto;

public class CidadeDTO {
	
	private Long[] idCidade;
	
	private String nome;
	
	private String bairro;
	
	public CidadeDTO() {
		super();
	}

	public CidadeDTO(Long[] idCidade, String nome, String bairro) {
		super();
		this.idCidade = idCidade;
		this.nome = nome;
		this.bairro = bairro;
	}

	public Long[] getIdCidade() {
		return idCidade;
	}

	public void setIdCidade(Long[] idCidade) {
		this.idCidade = idCidade;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getBairro() {
		return bairro;
	}

	public void setBairro(String bairro) {
		this.bairro = bairro;
	}

}
