import java.io.*;
import java.util.*;

public class PessoaService {
    private static final String ARQUIVO_PESSOAS = "pessoas.txt";
    private static final String HEADER = "// Este arquivo simula o banco de dados de pessoas.\n// Formato: codigo;nome;tipo;cep;endereco;numero;complemento;tipoDeEndereco\n";

    // Utilitário para ler todas as pessoas do arquivo
    public static List<Pessoa> lerPessoasDoArquivo() {
        List<Pessoa> pessoas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PESSOAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("//"))
                    continue;
                pessoas.add(Pessoa.fromFileLine(linha));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler pessoas: " + e.getMessage());
        }
        return pessoas;
    }

    public static void salvarPessoasNoArquivo(List<Pessoa> pessoas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_PESSOAS, false))) {
            bw.write(HEADER);
            for (Pessoa p : pessoas) {
                bw.write(p.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pessoas: " + e.getMessage());
        }
    }

    public int gerarProximoCodigo() {
        return lerPessoasDoArquivo().stream()
                .mapToInt(Pessoa::getCodigo)
                .max()
                .orElse(0) + 1;
    }

    public void criar(Pessoa p) {
        if (p.getCodigo() == 0) {
            p.setCodigo(gerarProximoCodigo());
        }
        List<Pessoa> pessoas = lerPessoasDoArquivo();
        pessoas.add(p);
        salvarPessoasNoArquivo(pessoas);
    }

    public List<Pessoa> listar() {
        return lerPessoasDoArquivo();
    }

    public boolean atualizar(int codigo, String novoNome, TipoPessoa novoTipo, Endereco novoEndereco) {
        List<Pessoa> pessoas = lerPessoasDoArquivo();
        boolean atualizado = pessoas.stream().anyMatch(p -> {
            if (p.getCodigo() == codigo) {
                p.setNome(novoNome);
                p.setTipo(novoTipo);
                p.setEndereco(novoEndereco);
                return true;
            }
            return false;
        });
        if (atualizado) {
            salvarPessoasNoArquivo(pessoas);
        }
        return atualizado;
    }

    public boolean deletar(int codigo) {
        List<Pessoa> pessoas = lerPessoasDoArquivo();
        boolean removido = pessoas.removeIf(p -> p.getCodigo() == codigo);
        if (removido) {
            salvarPessoasNoArquivo(pessoas);
        }
        return removido;
    }

    public Optional<Pessoa> buscar(int codigo) {
        return lerPessoasDoArquivo().stream()
                .filter(p -> p.getCodigo() == codigo)
                .findFirst();
    }
}
