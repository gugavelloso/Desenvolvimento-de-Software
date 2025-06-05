import java.io.*;
import java.util.*;

public class PedidoVendaService {
  private static final String ARQUIVO_PEDIDOS = "pedidos.txt";
  private static final String HEADER = "// numero;cliente;enderecoEntrega;produtos;montanteTotal\n";

  public static List<PedidoVenda> lerPedidosDoArquivo(List<Pessoa> pessoas, List<Produto> produtos) {
    List<PedidoVenda> pedidos = new ArrayList<>();
    try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PEDIDOS))) {
      String linha;
      while ((linha = br.readLine()) != null) {
        if (linha.trim().isEmpty() || linha.startsWith("//"))
          continue;
        pedidos.add(PedidoVenda.fromFileLine(linha, pessoas, produtos));
      }
    } catch (IOException e) {
      System.out.println("Arquivo de pedidos não encontrado. Será criado ao salvar.");
    }
    Logger.registrar("Listagem de pedidos de venda: total=" + pedidos.size());
    return pedidos;
  }

  public static void salvarPedidosNoArquivo(List<PedidoVenda> pedidos) {
    try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_PEDIDOS))) {
      bw.write(HEADER);
      for (PedidoVenda p : pedidos) {
        bw.write(p.toFileLine());
        bw.newLine();
      }
    } catch (IOException e) {
      System.out.println("Erro ao salvar pedidos: " + e.getMessage());
    }
    Logger.registrar("Pedidos de venda salvos: total=" + pedidos.size());
  }

  public int gerarProximoNumero(List<PedidoVenda> pedidos) {
    return pedidos.stream().mapToInt(PedidoVenda::getNumero).max().orElse(0) + 1;
  }

  public void criar(PedidoVenda pedido) {
    List<PedidoVenda> lista = lerPedidosDoArquivo(
        PessoaService.lerPessoasDoArquivo(),
        ProdutoService.lerProdutosDoArquivo());
    lista.add(pedido);
    salvarPedidosNoArquivo(lista);
    Logger.registrar("Pedido de venda criado: " + pedido);
  }

  public List<PedidoVenda> listar() {
    return lerPedidosDoArquivo(
        PessoaService.lerPessoasDoArquivo(),
        ProdutoService.lerProdutosDoArquivo());
  }

  public boolean deletar(int numero) {
    List<PedidoVenda> lista = listar();
    PedidoVenda pedidoRemovido = lista.stream().filter(p -> p.getNumero() == numero).findFirst().orElse(null);
    boolean removed = lista.removeIf(p -> p.getNumero() == numero);
    if (removed)
      salvarPedidosNoArquivo(lista);
    if (removed) {
      Logger.registrar("Pedido de venda deletado: " + pedidoRemovido);
    }
    return removed;
  }

  public Optional<PedidoVenda> buscar(int numero) {
    Optional<PedidoVenda> pedido = listar().stream().filter(p -> p.getNumero() == numero).findFirst();
    Logger.registrar("Busca de pedido de venda: numero=" + numero + ", resultado="
        + (pedido.isPresent() ? pedido.get() : "não encontrado"));
    return pedido;
  }

  public boolean atualizar(int numero, Pessoa novoCliente, Endereco novoEnderecoEntrega, List<Produto> novosProdutos) {
    List<PedidoVenda> lista = listar();
    PedidoVenda pedidoAntigo = null;
    boolean atualizado = false;
    for (int i = 0; i < lista.size(); i++) {
      PedidoVenda pedido = lista.get(i);
      if (pedido.getNumero() == numero) {
        pedidoAntigo = pedido;
        PedidoVenda novoPedido = new PedidoVenda(numero, novoCliente, novoEnderecoEntrega, novosProdutos);
        lista.set(i, novoPedido);
        atualizado = true;
        break;
      }
    }
    if (atualizado) {
      salvarPedidosNoArquivo(lista);
      Logger.registrar("Pedido de venda atualizado: número=" + numero + ", antes=" + pedidoAntigo + ", depois="
          + lista.stream().filter(p -> p.getNumero() == numero).findFirst().orElse(null));
    }
    return atualizado;
  }
}
