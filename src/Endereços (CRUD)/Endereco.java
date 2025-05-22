public class Endereco {
  private String cep;
  private String endereco;
  private int numero;
  private String complemento;
  private String tipoDeEndereco;

  public Endereco(String cep, String endereco, int numero, String complemento, String tipoDeEndereco) {
    this.cep = cep;
    this.endereco = endereco;
    this.numero = numero;
    this.complemento = complemento;
    this.tipoDeEndereco = tipoDeEndereco;
  }

  public String getCep() {
    return cep;
  }

  public void setCep(String cep) {
    this.cep = cep;
  }

  public String getEndereco() {
    return endereco;
  }

  public void setEndereco(String endereco) {
    this.endereco = endereco;
  }

  public int getNumero() {
    return numero;
  }

  public void setNumero(int numero) {
    this.numero = numero;
  }

  public String getComplemento() {
    return complemento;
  }

  public void setComplemento(String complemento) {
    this.complemento = complemento;
  }

  public String getTipoDeEndereco() {
    return tipoDeEndereco;
  }

  public void setTipoDeEndereco(String tipoDeEndereco) {
    this.tipoDeEndereco = tipoDeEndereco;
  }

  @Override
  public String toString() {
    return String.format("CEP: %s | Endereço: %s, %d | Complemento: %s | Tipo: %s", cep, endereco, numero, complemento,
        tipoDeEndereco);
  }

  public String toFileLine() {
    return cep + ";" + endereco + ";" + numero + ";" + complemento + ";" + tipoDeEndereco;
  }

  public static Endereco fromFileLine(String linha) {
    String[] partes = linha.split(";");
    String cep = partes.length > 0 ? partes[0] : "";
    String endereco = partes.length > 1 ? partes[1] : "";
    int numero = (partes.length > 2 && !partes[2].isEmpty()) ? Integer.parseInt(partes[2]) : 0;
    String complemento = partes.length > 3 ? partes[3] : "";
    String tipoDeEndereco = partes.length > 4 ? partes[4] : "";
    return new Endereco(cep, endereco, numero, complemento, tipoDeEndereco);
  }
}
