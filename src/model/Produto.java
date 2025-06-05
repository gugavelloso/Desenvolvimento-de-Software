/**
 * Classe que representa um Produto com código, nome e preço.
 */
public class Produto {
    private int codigo;
    private String nome;
    private double preco;
    private int fornecedorCodigo;

    public Produto(int codigo, String nome, double preco, int fornecedorCodigo) {
        this.codigo = codigo;
        this.nome = nome;
        this.preco = preco;
        this.fornecedorCodigo = fornecedorCodigo;
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

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public int getFornecedorCodigo() {
        return fornecedorCodigo;
    }

    public void setFornecedorCodigo(int fornecedorCodigo) {
        this.fornecedorCodigo = fornecedorCodigo;
    }

    @Override
    public String toString() {
        return String.format("Produto{codigo=%d, nome='%s', preco=%.2f, fornecedorCodigo=%d}", codigo, nome, preco,
                fornecedorCodigo);
    }

    public String toFileLine() {
        return codigo + ";" + nome + ";" + preco + ";" + fornecedorCodigo;
    }

    public static Produto fromFileLine(String linha) {
        String[] partes = linha.split(";");
        int codigo = Integer.parseInt(partes[0]);
        String nome = partes[1];
        double preco = Double.parseDouble(partes[2]);
        int fornecedorCodigo = partes.length > 3 ? Integer.parseInt(partes[3]) : -1;
        return new Produto(codigo, nome, preco, fornecedorCodigo);
    }
}
