/**
 * Classe que representa um Endereço, podendo ser comercial ou residencial.
 */
public class Endereco {
  private int id;
  private int pessoaId;
  private String logradouro;
  private String numero;
  private String complemento;
  private String bairro;
  private String cidade;
  private String estado;
  private String cep;

  public Endereco(int id, int pessoaId, String logradouro, String numero, String complemento, String bairro,
      String cidade, String estado,
      String cep) {
    this.id = id;
    this.pessoaId = pessoaId;
    this.logradouro = logradouro;
    this.numero = numero;
    this.complemento = complemento;
    this.bairro = bairro;
    this.cidade = cidade;
    this.estado = estado;
    this.cep = cep;
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public int getPessoaId() {
    return pessoaId;
  }

  public void setPessoaId(int pessoaId) {
    this.pessoaId = pessoaId;
  }

  public String getLogradouro() {
    return logradouro;
  }

  public void setLogradouro(String logradouro) {
    this.logradouro = logradouro;
  }

  public String getNumero() {
    return numero;
  }

  public void setNumero(String numero) {
    this.numero = numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getBairro() {
    return bairro;
  }

  public void setBairro(String bairro) {
    this.bairro = bairro;
  }

  public String getCidade() {
    return cidade;
  }

  public void setCidade(String cidade) {
    this.cidade = cidade;
  }

  public String getEstado() {
    return estado;
  }

  public void setEstado(String estado) {
    this.estado = estado;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  @Override
  public String toString() {
    return String.format(
        "Endereco{id=%d, pessoaId=%d, logradouro='%s', numero='%s', complemento='%s', bairro='%s', cidade='%s', estado='%s', cep='%s'}",
        id, pessoaId, logradouro, numero, complemento, bairro, cidade, estado, cep);
  }
}
