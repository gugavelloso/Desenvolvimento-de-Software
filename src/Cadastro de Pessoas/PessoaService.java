import java.io.*;
import java.util.*;

public class PessoaService {
    private static final String ARQUIVO_PESSOAS = "pessoas.txt";

    // Cria uma nova pessoa e salva no arquivo
    public void criar(Pessoa p) {
        try (FileWriter fw = new FileWriter(ARQUIVO_PESSOAS, true);
                BufferedWriter bw = new BufferedWriter(fw)) {
            bw.write(p.getCodigo() + ";" + p.getNome() + ";" + p.getTipo());
            bw.newLine();
        } catch (IOException e) {
            System.out.println("Erro ao salvar pessoa: " + e.getMessage());
        }
    }

    // Retorna lista de todas as pessoas lendo do arquivo
    public List<Pessoa> listar() {
        List<Pessoa> pessoas = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PESSOAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("//"))
                    continue;
                String[] partes = linha.split(";");
                int codigo = Integer.parseInt(partes[0]);
                String nome = partes[1];
                TipoPessoa tipo = TipoPessoa.valueOf(partes[2]);
                pessoas.add(new Pessoa(codigo, nome, tipo));
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler pessoas: " + e.getMessage());
        }
        return pessoas;
    }

    // Atualiza uma pessoa pelo código
    public boolean atualizar(int codigo, String novoNome, TipoPessoa novoTipo) {
        List<Pessoa> pessoas = listar();
        boolean atualizado = false;
        for (Pessoa p : pessoas) {
            if (p.getCodigo() == codigo) {
                p.setNome(novoNome);
                p.setTipo(novoTipo);
                atualizado = true;
                break;
            }
        }
        if (atualizado) {
            salvarListaNoArquivo(pessoas);
        }
        return atualizado;
    }

    // Remove pessoa pelo código
    public boolean deletar(int codigo) {
        List<Pessoa> pessoas = listar();
        boolean removido = pessoas.removeIf(p -> p.getCodigo() == codigo);
        if (removido) {
            salvarListaNoArquivo(pessoas);
        }
        return removido;
    }

    // Busca pessoa pelo código
    public Optional<Pessoa> buscar(int codigo) {
        return listar().stream()
                .filter(p -> p.getCodigo() == codigo)
                .findFirst();
    }

    // Salva a lista completa no arquivo (sobrescreve)
    private void salvarListaNoArquivo(List<Pessoa> pessoas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_PESSOAS, false))) {
            bw.write("// Este arquivo simula o banco de dados de pessoas.\n// Formato: codigo;nome;tipo\n");
            for (Pessoa p : pessoas) {
                bw.write(p.getCodigo() + ";" + p.getNome() + ";" + p.getTipo());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pessoas: " + e.getMessage());
        }
    }
}
