import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

public class Menu {

    private static final String ARQUIVO_MENU = "menu.txt"; // arquivo que contem o menu a exibir

    private PessoaService pessoaService = new PessoaService();

    public void executar() {
        Scanner scanner = new Scanner(System.in);
        boolean executando = true;

        while (executando) {
            exibirMenu();

            System.out.print("Digite a opção desejada: ");
            String opcao = scanner.nextLine();

            switch (opcao) {
                case "1":
                    criarPessoa(scanner);
                    break;
                case "2":
                    listarPessoas();
                    break;
                case "3":
                    atualizarPessoa(scanner);
                    break;
                case "4":
                    deletarPessoa(scanner);
                    break;
                case "5":
                    System.out.println("Saindo do programa...");
                    executando = false;
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
        scanner.close();
    }

    private void exibirMenu() {
        try (BufferedReader br = new BufferedReader(new FileReader(ARQUIVO_MENU))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de menu: " + e.getMessage());
            System.out.println("Menu padrão será exibido:");
            exibirMenuPadrao();
        }
    }

    private void exibirMenuPadrao() {
        System.out.println("1 - Criar Pessoa");
        System.out.println("2 - Listar Pessoas");
        System.out.println("3 - Atualizar Pessoa");
        System.out.println("4 - Deletar Pessoa");
        System.out.println("5 - Sair");
    }

    private void criarPessoa(Scanner scanner) {
        try {
            System.out.print("Digite o código: ");
            int codigo = Integer.parseInt(scanner.nextLine());

            if (pessoaService.buscar(codigo).isPresent()) {
                System.out.println("Código já existente. Tente novamente.");
                Logger.registrar("Tentativa de criar pessoa com código já existente: " + codigo);
                return;
            }

            System.out.print("Digite o nome: ");
            String nome = scanner.nextLine();

            System.out.print("Digite o tipo de pessoa (Cliente, Fornecedor ou Ambos): ");
            String tipoStr = scanner.nextLine();

            TipoPessoa tipo = TipoPessoa.fromString(tipoStr);

            Pessoa p = new Pessoa(codigo, nome, tipo);
            pessoaService.criar(p);
            Logger.registrar("Pessoa criada: código=" + codigo + ", nome=" + nome + ", tipo=" + tipo);
            System.out.println("Pessoa criada com sucesso!");

        } catch (NumberFormatException e) {
            System.out.println("Código inválido. Deve ser um número inteiro.");
            Logger.registrar("Erro ao criar pessoa: código inválido");
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de pessoa inválido.");
            Logger.registrar("Erro ao criar pessoa: tipo inválido");
        }
    }

    private void listarPessoas() {
        var lista = pessoaService.listar();
        Logger.registrar("Listagem de pessoas. Total: " + lista.size());
        if (lista.isEmpty()) {
            System.out.println("Nenhuma pessoa cadastrada.");
        } else {
            System.out.println("Lista de pessoas cadastradas:");
            lista.forEach(System.out::println);
        }
    }

    private void atualizarPessoa(Scanner scanner) {
        try {
            System.out.print("Digite o código da pessoa a ser atualizada: ");
            int codigo = Integer.parseInt(scanner.nextLine());

            var pessoaOpt = pessoaService.buscar(codigo);
            if (pessoaOpt.isEmpty()) {
                System.out.println("Pessoa não encontrada.");
                Logger.registrar("Tentativa de atualizar pessoa não encontrada: código=" + codigo);
                return;
            }
            Pessoa pessoaAntiga = pessoaOpt.get();

            System.out.print("Digite o novo nome: ");
            String novoNome = scanner.nextLine();

            System.out.print("Digite o novo tipo de pessoa (Cliente, Fornecedor ou Ambos): ");
            String tipoStr = scanner.nextLine();

            TipoPessoa novoTipo = TipoPessoa.fromString(tipoStr);

            boolean atualizado = pessoaService.atualizar(codigo, novoNome, novoTipo);
            if (atualizado) {
                Logger.registrar(
                        "Pessoa atualizada: código=" + codigo + ", nomeAnterior=" + pessoaAntiga.getNome()
                                + ", tipoAnterior=" + pessoaAntiga.getTipo() + ", novoNome=" + novoNome + ", novoTipo="
                                + novoTipo);
                System.out.println("Pessoa atualizada com sucesso!");
            } else {
                Logger.registrar("Falha ao atualizar pessoa: código=" + codigo);
                System.out.println("Falha ao atualizar pessoa.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Código inválido. Deve ser um número inteiro.");
            Logger.registrar("Erro ao atualizar pessoa: código inválido");
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo de pessoa inválido.");
            Logger.registrar("Erro ao atualizar pessoa: tipo inválido");
        }
    }

    private void deletarPessoa(Scanner scanner) {
        try {
            System.out.print("Digite o código da pessoa a ser deletada: ");
            int codigo = Integer.parseInt(scanner.nextLine());

            var pessoaOpt = pessoaService.buscar(codigo);
            if (pessoaOpt.isPresent()) {
                Pessoa pessoa = pessoaOpt.get();
                boolean removido = pessoaService.deletar(codigo);
                if (removido) {
                    Logger.registrar("Pessoa removida: código=" + codigo + ", nome=" + pessoa.getNome() + ", tipo="
                            + pessoa.getTipo());
                    System.out.println("Pessoa removida com sucesso!");
                } else {
                    Logger.registrar("Falha ao remover pessoa: código=" + codigo);
                    System.out.println("Falha ao remover pessoa.");
                }
            } else {
                Logger.registrar("Tentativa de remover pessoa não encontrada: código=" + codigo);
                System.out.println("Pessoa não encontrada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Código inválido. Deve ser um número inteiro.");
            Logger.registrar("Erro ao deletar pessoa: código inválido");
        }
    }
}
