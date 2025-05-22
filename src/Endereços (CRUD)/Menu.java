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
                case "6":
                    executarSubmenu(scanner);
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
        System.out.println("6 - Submenu");
    }

    private void criarPessoa(Scanner scanner) {
        try {
            // Não pede mais o código ao usuário, será gerado automaticamente
            int codigo = 0; // placeholder para gerar automaticamente

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

            // Coleta os dados do endereço
            System.out.print("Digite o CEP: ");
            String cep = scanner.nextLine();
            System.out.print("Digite o endereço (logradouro): ");
            String enderecoStr = scanner.nextLine();
            System.out.print("Digite o número: ");
            int numero = Integer.parseInt(scanner.nextLine());
            System.out.print("Digite o complemento: ");
            String complemento = scanner.nextLine();
            System.out.print("Digite o tipo de endereço: ");
            String tipoDeEndereco = scanner.nextLine();
            Endereco endereco = new Endereco(cep, enderecoStr, numero, complemento, tipoDeEndereco);

            Pessoa p = new Pessoa(codigo, nome, tipo, endereco); // código será gerado automaticamente
            pessoaService.criar(p);
            Logger.registrar(
                    "Pessoa criada: código=" + p.getCodigo() + ", nome=" + nome + ", tipo=" + tipo + ", endereco="
                            + endereco);
            System.out.println("Pessoa criada com sucesso! Código gerado: " + p.getCodigo());

        } catch (NumberFormatException e) {
            System.out.println("Código ou número inválido. Deve ser um número inteiro.");
            Logger.registrar("Erro ao criar pessoa: código ou número inválido");
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
            lista.forEach(p -> System.out.println(p.toString()));
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

            // Coleta os dados do novo endereço
            System.out.print("Digite o novo CEP: ");
            String cep = scanner.nextLine();
            System.out.print("Digite o novo endereço (logradouro): ");
            String enderecoStr = scanner.nextLine();
            System.out.print("Digite o novo número: ");
            int numero = Integer.parseInt(scanner.nextLine());
            System.out.print("Digite o novo complemento: ");
            String complemento = scanner.nextLine();
            System.out.print("Digite o novo tipo de endereço: ");
            String tipoDeEndereco = scanner.nextLine();
            Endereco novoEndereco = new Endereco(cep, enderecoStr, numero, complemento, tipoDeEndereco);

            boolean atualizado = pessoaService.atualizar(codigo, novoNome, novoTipo, novoEndereco);
            if (atualizado) {
                Logger.registrar(
                        "Pessoa atualizada: código=" + codigo + ", nomeAnterior=" + pessoaAntiga.getNome()
                                + ", tipoAnterior=" + pessoaAntiga.getTipo() + ", enderecoAnterior="
                                + pessoaAntiga.getEndereco() + ", novoNome=" + novoNome + ", novoTipo="
                                + novoTipo + ", novoEndereco=" + novoEndereco);
                System.out.println("Pessoa atualizada com sucesso!");
            } else {
                Logger.registrar("Falha ao atualizar pessoa: código=" + codigo);
                System.out.println("Falha ao atualizar pessoa.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Código ou número inválido. Deve ser um número inteiro.");
            Logger.registrar("Erro ao atualizar pessoa: código ou número inválido");
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
                            + pessoa.getTipo() + ", endereco=" + pessoa.getEndereco());
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

    private void executarSubmenu(Scanner scanner) {
        boolean submenuAtivo = true;
        while (submenuAtivo) {
            exibirSubmenu();
            System.out.print("Digite a opção do submenu: ");
            String opcao = scanner.nextLine();
            switch (opcao) {
                case "1":
                    buscarPessoaPorCodigo(scanner);
                    break;
                case "2":
                    submenuAtivo = false;
                    break;
                case "3":
                    exibirTotalPessoas();
                    break;
                case "4":
                    listarPessoasPorTipo(scanner);
                    break;
                case "5":
                    System.out.println("Saindo do programa...");
                    System.exit(0);
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private void exibirSubmenu() {
        try (BufferedReader br = new BufferedReader(new FileReader("submenu.txt"))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                System.out.println(linha);
            }
        } catch (IOException e) {
            System.out.println("Erro ao ler o arquivo de submenu: " + e.getMessage());
        }
    }

    private void buscarPessoaPorCodigo(Scanner scanner) {
        System.out.print("Digite o código da pessoa: ");
        try {
            int codigo = Integer.parseInt(scanner.nextLine());
            var pessoaOpt = pessoaService.buscar(codigo);
            if (pessoaOpt.isPresent()) {
                System.out.println("Pessoa encontrada: " + pessoaOpt.get());
            } else {
                System.out.println("Pessoa não encontrada.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Código inválido.");
        }
    }

    private void exibirTotalPessoas() {
        int total = pessoaService.listar().size();
        System.out.println("Total de pessoas cadastradas: " + total);
    }

    private void listarPessoasPorTipo(Scanner scanner) {
        System.out.print("Digite o tipo de pessoa (Cliente, Fornecedor ou Ambos): ");
        String tipoStr = scanner.nextLine();
        try {
            TipoPessoa tipo = TipoPessoa.fromString(tipoStr);
            var pessoas = pessoaService.listar();
            pessoas.stream().filter(p -> p.getTipo() == tipo)
                    .forEach(System.out::println);
        } catch (IllegalArgumentException e) {
            System.out.println("Tipo inválido.");
        }
    }
}
