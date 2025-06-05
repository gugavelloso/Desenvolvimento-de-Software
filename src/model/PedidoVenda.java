
import java.util.List;

public class PedidoVenda {
  private int numero;
  private Pessoa cliente;
  private Endereco enderecoEntrega;
  private List<Produto> produtos;
  private double montanteTotal;

  public PedidoVenda(int numero, Pessoa cliente, Endereco enderecoEntrega, List<Produto> produtos) {
    this.numero = numero;
    this.cliente = cliente;
    this.enderecoEntrega = enderecoEntrega;
    this.produtos = produtos;
    this.montanteTotal = calcularMontanteTotal();
  }

  public int getNumero() {
    return numero;
  }

  public Pessoa getCliente() {
    return cliente;
  }

  public Endereco getEnderecoEntrega() {
    return enderecoEntrega;
  }

  public List<Produto> getProdutos() {
    return produtos;
  }

  public double getMontanteTotal() {
    return montanteTotal;
  }

  public void setProdutos(List<Produto> produtos) {
    this.produtos = produtos;
    this.montanteTotal = calcularMontanteTotal();
  }

  private double calcularMontanteTotal() {
    return produtos.stream().mapToDouble(Produto::getPreco).sum();
  }

  @Override
  public String toString() {
    return "PedidoVenda{" +
        "numero=" + numero +
        ", cliente=" + cliente.getNome() +
        ", enderecoEntrega=" + enderecoEntrega +
        ", produtos=" + produtos +
        ", montanteTotal=R$" + String.format("%.2f", montanteTotal) +
        '}';
  }

  public String toFileLine() {
    StringBuilder sb = new StringBuilder();
    sb.append(numero).append(";")
        .append(cliente.getCodigo()).append(";")
        .append(enderecoEntrega != null ? enderecoEntrega.toString() : "").append(";");
    for (int i = 0; i < produtos.size(); i++) {
      sb.append(produtos.get(i).getCodigo());
      if (i < produtos.size() - 1)
        sb.append(",");
    }
    sb.append(";").append(montanteTotal);
    return sb.toString();
  }

  public static PedidoVenda fromFileLine(String linha, List<Pessoa> pessoas, List<Produto> produtosDisponiveis) {
    String[] partes = linha.split(";");
    int numero = Integer.parseInt(partes[0]);
    int codigoCliente = Integer.parseInt(partes[1]);
    Pessoa cliente = pessoas.stream().filter(p -> p.getCodigo() == codigoCliente).findFirst().orElse(null);
    Endereco enderecoEntrega = null;
    if (cliente != null && cliente.getEnderecoResidencial() != null)
      enderecoEntrega = cliente.getEnderecoResidencial();
    List<Produto> produtosPedido = new java.util.ArrayList<>();
    if (partes.length > 3 && !partes[3].isEmpty()) {
      String[] codigos = partes[3].split(",");
      for (String cod : codigos) {
        int codInt = Integer.parseInt(cod);
        produtosDisponiveis.stream().filter(prod -> prod.getCodigo() == codInt).findFirst()
            .ifPresent(produtosPedido::add);
      }
    }
    return new PedidoVenda(numero, cliente, enderecoEntrega, produtosPedido);
  }
}
