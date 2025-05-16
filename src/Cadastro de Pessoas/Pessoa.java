public class Pessoa {
  private int codigo;
  private String nome;
  private TipoPessoa tipo;

  public Pessoa(int codigo, String nome, TipoPessoa tipo) {
      this.codigo = codigo;
      this.nome = nome;
      this.tipo = tipo;
  }

  public int getCodigo() {
      return codigo;
  }

  public void setCodigo(int codigo) {
      this.codigo = codigo;
  }

  public String getNome() {
      return nome;
  }

  public void setNome(String nome) {
      this.nome = nome;
  }

  public TipoPessoa getTipo() {
      return tipo;
  }

  public void setTipo(TipoPessoa tipo) {
      this.tipo = tipo;
  }

  @Override
  public String toString() {
      return String.format("Código: %d | Nome: %s | Tipo: %s", codigo, nome, tipo);
  }
}
