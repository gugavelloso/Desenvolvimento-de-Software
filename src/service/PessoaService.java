import java.io.*;
import java.util.*;

public class PessoaService {
    private static final String ARQUIVO_PESSOAS = "pessoas.txt";
    private static final String ARQUIVO_ENDERECOS = "enderecos.txt";
    private static final String HEADER_PESSOAS = "// codigo;nome;tipo\n";
    private static final String HEADER_ENDERECOS = "// id;idPessoa;logradouro;numero;complemento;bairro;cidade;estado;cep\n";

    public static List<Pessoa> lerPessoasDoArquivo() {
        List<Pessoa> pessoas = new ArrayList<>();
        List<Endereco> enderecos = lerEnderecosDoArquivo();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_PESSOAS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("//"))
                    continue;
                pessoas.add(Pessoa.fromFileLine(linha, enderecos));
            }
        } catch (IOException e) {
            System.out.println("Arquivo de pessoas não encontrado. Será criado ao salvar.");
        }
        return pessoas;
    }

    public static void salvarPessoasNoArquivo(List<Pessoa> pessoas) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_PESSOAS))) {
            bw.write(HEADER_PESSOAS);
            for (Pessoa p : pessoas) {
                bw.write(p.toFileLine());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar pessoas: " + e.getMessage());
        }
    }

    public static List<Endereco> lerEnderecosDoArquivo() {
        List<Endereco> enderecos = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_ENDERECOS))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("//"))
                    continue;
                String[] partes = linha.split(";");
                int id = Integer.parseInt(partes[0]);
                int pessoaId = Integer.parseInt(partes[1]);
                String logradouro = partes[2];
                String numero = partes[3];
                String complemento = partes[4];
                String bairro = partes[5];
                String cidade = partes[6];
                String estado = partes[7];
                String cep = partes[8];
                enderecos.add(new Endereco(id, pessoaId, logradouro, numero, complemento, bairro, cidade, estado, cep));
            }
        } catch (IOException e) {
            System.out.println("Arquivo de endereços não encontrado. Será criado ao salvar.");
        }
        Logger.registrar("Listagem de endereços: total=" + enderecos.size());
        return enderecos;
    }

    public static void salvarEnderecosNoArquivo(List<Endereco> enderecos) {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(ARQUIVO_ENDERECOS))) {
            bw.write(HEADER_ENDERECOS);
            for (Endereco e : enderecos) {
                bw.write(e.getId() + ";" + e.getPessoaId() + ";" + e.getLogradouro() + ";" + e.getNumero() + ";"
                        + e.getComplemento() + ";" + e.getBairro() + ";" + e.getCidade() + ";" + e.getEstado() + ";"
                        + e.getCep());
                bw.newLine();
            }
        } catch (IOException e) {
            System.out.println("Erro ao salvar endereços: " + e.getMessage());
        }
        Logger.registrar("Endereços salvos: total=" + enderecos.size());
    }

    public int gerarProximoCodigo() {
        return lerPessoasDoArquivo().stream().mapToInt(Pessoa::getCodigo).max().orElse(0) + 1;
    }

    public int gerarProximoEnderecoId() {
        return lerEnderecosDoArquivo().stream().mapToInt(Endereco::getId).max().orElse(0) + 1;
    }

    public void criar(Pessoa p) {
        List<Pessoa> lista = lerPessoasDoArquivo();
        lista.add(p);
        salvarPessoasNoArquivo(lista);
        // Salva os endereços da pessoa
        List<Endereco> enderecos = lerEnderecosDoArquivo();
        enderecos.addAll(p.getEnderecos());
        salvarEnderecosNoArquivo(enderecos);
        Logger.registrar("Pessoa criada: " + p);
    }

    public List<Pessoa> listar() {
        List<Pessoa> pessoas = lerPessoasDoArquivo();
        Logger.registrar("Listagem de pessoas: total=" + pessoas.size());
        return pessoas;
    }

    public boolean atualizar(int codigo, String nome, TipoPessoa tipo, List<Endereco> novosEnderecos) {
        List<Pessoa> lista = lerPessoasDoArquivo();
        boolean atualizado = false;
        Pessoa pessoaAntiga = null;
        for (int i = 0; i < lista.size(); i++) {
            if (lista.get(i).getCodigo() == codigo) {
                pessoaAntiga = lista.get(i);
                Pessoa p = lista.get(i);
                p.setNome(nome);
                p.setTipo(tipo);
                p.setEnderecos(novosEnderecos);
                atualizado = true;
                break;
            }
        }
        if (atualizado) {
            salvarPessoasNoArquivo(lista);
            // Atualiza endereços
            List<Endereco> enderecos = lerEnderecosDoArquivo();
            // Remove endereços antigos da pessoa
            enderecos.removeIf(e -> e.getPessoaId() == codigo);
            // Adiciona os novos
            enderecos.addAll(novosEnderecos);
            salvarEnderecosNoArquivo(enderecos);
            Logger.registrar("Pessoa atualizada: código=" + codigo + ", antes=" + pessoaAntiga + ", depois="
                    + lista.stream().filter(p -> p.getCodigo() == codigo).findFirst().orElse(null));
        }
        return atualizado;
    }

    public boolean deletar(int codigo) {
        List<Pessoa> lista = lerPessoasDoArquivo();
        Pessoa pessoaRemovida = lista.stream().filter(p -> p.getCodigo() == codigo).findFirst().orElse(null);
        boolean removed = lista.removeIf(p -> p.getCodigo() == codigo);
        if (removed) {
            salvarPessoasNoArquivo(lista);
            // Remove endereços da pessoa
            List<Endereco> enderecos = lerEnderecosDoArquivo();
            enderecos.removeIf(e -> e.getPessoaId() == codigo);
            salvarEnderecosNoArquivo(enderecos);
            Logger.registrar("Pessoa deletada: " + pessoaRemovida);
        }
        return removed;
    }

    public Optional<Pessoa> buscar(int codigo) {
        Optional<Pessoa> pessoa = lerPessoasDoArquivo().stream().filter(p -> p.getCodigo() == codigo).findFirst();
        Logger.registrar("Busca de pessoa: codigo=" + codigo + ", resultado="
                + (pessoa.isPresent() ? pessoa.get() : "não encontrada"));
        return pessoa;
    }
}
