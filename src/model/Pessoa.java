import java.util.List;

/**
 * Classe que representa uma Pessoa com código, nome, tipo e endereços
 * residencial e comercial.
 */
public class Pessoa {
    private int codigo;
    private String nome;
    private TipoPessoa tipo;
    private List<Endereco> enderecos;

    public Pessoa(int codigo, String nome, TipoPessoa tipo, List<Endereco> enderecos) {
        this.codigo = codigo;
        this.nome = nome;
        this.tipo = tipo;
        this.enderecos = enderecos;
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

    public List<Endereco> getEnderecos() {
        return enderecos;
    }

    public void setEnderecos(List<Endereco> enderecos) {
        this.enderecos = enderecos;
    }

    @Override
    public String toString() {
        return String.format("Pessoa{codigo=%d, nome='%s', tipo=%s, enderecos=%s}",
                codigo, nome, tipo, enderecos);
    }

    public String toFileLine() {
        return codigo + ";" + nome + ";" + tipo;
    }

    public static Pessoa fromFileLine(String linha, List<Endereco> enderecos) {
        String[] partes = linha.split(";");
        int codigo = Integer.parseInt(partes[0]);
        String nome = partes[1];
        TipoPessoa tipo = TipoPessoa.valueOf(partes[2]);
        // Filtra os endereços dessa pessoa
        List<Endereco> enderecosPessoa = new java.util.ArrayList<>();
        for (Endereco e : enderecos) {
            if (e.getPessoaId() == codigo) {
                enderecosPessoa.add(e);
            }
        }
        return new Pessoa(codigo, nome, tipo, enderecosPessoa);
    }
}
