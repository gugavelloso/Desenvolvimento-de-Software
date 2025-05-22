import java.util.Arrays;

public class Pessoa {
    private int codigo;
    private String nome;
    private TipoPessoa tipo;
    private Endereco endereco;

    public Pessoa(int codigo, String nome, TipoPessoa tipo, Endereco endereco) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.endereco = endereco;
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }

    @Override
    public String toString() {
        return String.format("Código: %d | Nome: %s | Tipo: %s\n%s", codigo, nome, tipo, endereco);
    }

    // Serializa para linha de arquivo
    public String toFileLine() {
        return codigo + ";" + nome + ";" + tipo + ";" + endereco.toFileLine();
    }

    // Cria Pessoa a partir de uma linha do arquivo
    public static Pessoa fromFileLine(String linha) {
        String[] partes = linha.split(";");
        int codigo = Integer.parseInt(partes[0]);
        String nome = partes[1];
        TipoPessoa tipo = TipoPessoa.valueOf(partes[2]);
        String enderecoStr = String.join(";", Arrays.copyOfRange(partes, 3, partes.length));
        Endereco endereco = Endereco.fromFileLine(enderecoStr);
        return new Pessoa(codigo, nome, tipo, endereco);
    }
}
