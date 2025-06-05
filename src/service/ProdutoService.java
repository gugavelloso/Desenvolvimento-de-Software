import java.io.*;
import java.util.*;

/**
 * Serviço para gerenciar operações de CRUD de produtos.
 */
public class ProdutoService {
    private static final String ARQUIVO_PRODUTOS = "produtos.txt";
    private static final String HEADER = "// codigo;nome;preco;fornecedorCodigo\n";

    public static List<Produto> lerProdutosDoArquivo() {
        List<Produto> produtos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PRODUTOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("//"))
                    continue;
                produtos.add(Produto.fromFileLine(linha));
            }
        } catch (IOException e) {
            System.out.println("Arquivo de produtos não encontrado. Será criado ao salvar.");
        }
        Logger.registrar("Listagem de produtos: total=" + produtos.size());
        return produtos;
    }

    public static void salvarProdutosNoArquivo(List<Produto> produtos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_PRODUTOS))) {
            bw.write(HEADER);
            for (Produto p : produtos) {
                bw.write(p.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar produtos: " + e.getMessage());
        }
        Logger.registrar("Produtos salvos: total=" + produtos.size());
    }

    public int gerarProximoCodigo() {
        return lerProdutosDoArquivo().stream().mapToInt(Produto::getCodigo).max().orElse(0) + 1;
    }

    public void criar(Produto p) {
        List<Produto> lista = lerProdutosDoArquivo();
        lista.add(p);
        salvarProdutosNoArquivo(lista);
        Logger.registrar("Produto criado: " + p);
    }

    public List<Produto> listar() {
        return lerProdutosDoArquivo();
    }

    public boolean atualizar(int codigo, String nome, double preco) {
        List<Produto> lista = lerProdutosDoArquivo();
        Produto produtoAntigo = null;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo() == codigo) {
                produtoAntigo = lista.get(i);
                Produto p = lista.get(i);
                p.setNome(nome);
                p.setPreco(preco);
                salvarProdutosNoArquivo(lista);
                Logger.registrar("Produto atualizado: código=" + codigo + ", antes=" + produtoAntigo + ", depois=" + p);
                return true;
            }
        }
        return false;
    }

    public boolean deletar(int codigo) {
        List<Produto> lista = lerProdutosDoArquivo();
        Produto produtoRemovido = lista.stream().filter(p -> p.getCodigo() == codigo).findFirst().orElse(null);
        boolean removed = lista.removeIf(p -> p.getCodigo() == codigo);
        if (removed) {
            salvarProdutosNoArquivo(lista);
            Logger.registrar("Produto deletado: " + produtoRemovido);
        }
        return removed;
    }

    public Optional<Produto> buscar(int codigo) {
        Optional<Produto> produto = lerProdutosDoArquivo().stream().filter(p -> p.getCodigo() == codigo).findFirst();
        Logger.registrar("Busca de produto: codigo=" + codigo + ", resultado="
                + (produto.isPresent() ? produto.get() : "não encontrado"));
        return produto;
    }
}
