import java.util.Scanner;
import java.util.List;
import java.util.Map;
import java.util.LinkedHashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Classe que gerencia o menu e submenus da aplicação.
 */
public class Menu {
    private final PessoaService pessoaService = new PessoaService();
    private final ProdutoService produtoService = new ProdutoService();
    private final PedidoVendaService pedidoVendaService = new PedidoVendaService();
    private final Map<String, String> opcoesMenuPrincipal;
    private final Map<String, Map<String, String>> opcoesSubmenus;

    public Menu() {
        opcoesMenuPrincipal = carregarMenuPrincipal("app/menu_opcoes.txt");
        opcoesSubmenus = carregarSubmenus("app/submenu_opcoes.txt");
    }

    private Map<String, String> carregarMenuPrincipal(String caminho) {
        Map<String, String> menu = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("//"))
                    continue;
                String[] partes = linha.split(";");
                if (partes.length >= 3 && "principal".equals(partes[0])) {
                    menu.put(partes[1], partes[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar menu principal: " + e.getMessage());
        }
        return menu;
    }

    private Map<String, Map<String, String>> carregarSubmenus(String caminho) {
        Map<String, Map<String, String>> submenus = new LinkedHashMap<>();
        try (BufferedReader br = new BufferedReader(new FileReader(caminho))) {
            String linha;
            while ((linha = br.readLine()) != null) {
                if (linha.trim().isEmpty() || linha.startsWith("//"))
                    continue;
                String[] partes = linha.split(";");
                if (partes.length >= 3) {
                    if (!submenus.containsKey(partes[0]))
                        submenus.put(partes[0], new LinkedHashMap<>());
                    submenus.get(partes[0]).put(partes[1], partes[2]);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao carregar submenus: " + e.getMessage());
        }
        return submenus;
    }

    private void exibirMenu(Map<String, String> opcoes, String titulo) {
        System.out.println("\n--- " + titulo + " ---");
        opcoes.forEach((k, v) -> System.out.println(k + " - " + v));
    }

    public void executar() {
        try (Scanner scanner = new Scanner(System.in)) {
            boolean executando = true;
            while (executando) {
                exibirMenu(opcoesMenuPrincipal, "Menu Principal");
                System.out.print("Digite a opção desejada: ");
                String opcao = scanner.nextLine();
                switch (opcao) {
                    case "1":
                        executarSubmenu(scanner, "pessoas");
                        break;
                    case "2":
                        executarSubmenu(scanner, "produtos");
                        break;
                    case "3":
                        executarSubmenu(scanner, "pedidos");
                        break;
                    case "4":
                        executando = false;
                        System.out.println("Saindo da aplicação. Obrigado!");
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            }
        }
    }

    private void executarSubmenu(Scanner scanner, String submenu) {
        boolean voltar = false;
        Map<String, String> opcoes = opcoesSubmenus.get(submenu);
        while (!voltar) {
            exibirMenu(opcoes, "Cadastro de " + capitalize(submenu));
            System.out.print("Digite a opção do menu de " + capitalize(submenu) + ": ");
            String opcao = scanner.nextLine();
            switch (opcao) {
                case "1":
                    if ("pessoas".equals(submenu))
                        criarPessoa(scanner);
                    else if ("produtos".equals(submenu))
                        criarProduto(scanner);
                    else if ("pedidos".equals(submenu))
                        criarPedidoVenda(scanner);
                    break;
                case "2":
                    if ("pessoas".equals(submenu))
                        listarPessoas();
                    else if ("produtos".equals(submenu))
                        listarProdutos();
                    else if ("pedidos".equals(submenu))
                        listarPedidosVenda();
                    break;
                case "3":
                    if ("pessoas".equals(submenu))
                        atualizarPessoa(scanner);
                    else if ("produtos".equals(submenu))
                        atualizarProduto(scanner);
                    else if ("pedidos".equals(submenu))
                        atualizarPedidoVenda(scanner);
                    break;
                case "4":
                    if ("pessoas".equals(submenu))
                        deletarPessoa(scanner);
                    else if ("produtos".equals(submenu))
                        deletarProduto(scanner);
                    else if ("pedidos".equals(submenu))
                        deletarPedidoVenda(scanner);
                    break;
                case "5":
                    voltar = true;
                    break;
                case "6":
                    if ("pedidos".equals(submenu))
                        voltar = true;
                    else
                        System.out.println("Opção inválida! Tente novamente.");
                    break;
                default:
                    System.out.println("Opção inválida! Tente novamente.");
            }
        }
    }

    private String capitalize(String s) {
        return (s == null || s.isEmpty()) ? "" : s.substring(0, 1).toUpperCase() + s.substring(1);
    }

    private String formatarEndereco(Endereco endereco) {
        if (endereco == null)
            return "(não cadastrado)";
        return String.format("Rua: %s, Nº: %s, Complemento: %s, Bairro: %s, Cidade: %s, Estado: %s, CEP: %s",
                endereco.getLogradouro(),
                endereco.getNumero(),
                endereco.getComplemento(),
                endereco.getBairro(),
                endereco.getCidade(),
                endereco.getEstado(),
                endereco.getCep());
    }

    private String formatarEnderecos(List<Endereco> enderecos) {
        if (enderecos == null || enderecos.isEmpty())
            return "(nenhum cadastrado)";
        StringBuilder sb = new StringBuilder();
        for (Endereco e : enderecos) {
            sb.append(String.format(
                    "[ID: %d] Rua: %s, Nº: %s, Complemento: %s, Bairro: %s, Cidade: %s, Estado: %s, CEP: %s\n",
                    e.getId(),
                    e.getLogradouro(),
                    e.getNumero(),
                    e.getComplemento(),
                    e.getBairro(),
                    e.getCidade(),
                    e.getEstado(),
                    e.getCep()));
        }
        return sb.toString();
    }

    private void listarPessoas() {
        System.out.println("\n===== Lista de Pessoas =====");
        for (Pessoa p : pessoaService.listar()) {
            System.out.println("Código: " + p.getCodigo());
            System.out.println("Nome: " + p.getNome());
            System.out.println("Tipo: " + p.getTipo());
            System.out.println("Endereços:");
            System.out.print(formatarEnderecos(p.getEnderecos()));
            System.out.println("------------------------------");
        }
    }

    private void atualizarPessoa(Scanner scanner) {
        try {
            System.out.print("Digite o código da pessoa a atualizar: ");
            int codigo = Integer.parseInt(scanner.nextLine());
            Optional<Pessoa> pessoaOpt = pessoaService.buscar(codigo);
            if (!pessoaOpt.isPresent()) {
                System.out.println("Pessoa não encontrada com código " + codigo);
                return;
            }
            Pessoa pessoa = pessoaOpt.get();
            boolean atualizar = true;
            while (atualizar) {
                System.out.println("\n--- O que deseja atualizar? ---");
                System.out.println("1 - Nome");
                System.out.println("2 - Tipo");
                System.out.println("3 - Endereços");
                System.out.println("4 - Voltar");
                System.out.print("Escolha uma opção: ");
                String opcao = scanner.nextLine();
                switch (opcao) {
                    case "1":
                        System.out.print("Digite o novo nome: ");
                        String novoNome = scanner.nextLine();
                        pessoa.setNome(novoNome);
                        System.out.println("Nome atualizado!");
                        break;
                    case "2":
                        System.out.println("Escolha o novo tipo da pessoa:");
                        System.out.println("1 - CLIENTE");
                        System.out.println("2 - FORNECEDOR");
                        System.out.println("3 - AMBOS");
                        System.out.print("Digite o número correspondente ao tipo: ");
                        int tipoOpcao = Integer.parseInt(scanner.nextLine());
                        TipoPessoa tipo;
                        switch (tipoOpcao) {
                            case 1:
                                tipo = TipoPessoa.CLIENTE;
                                break;
                            case 2:
                                tipo = TipoPessoa.FORNECEDOR;
                                break;
                            case 3:
                                tipo = TipoPessoa.AMBOS;
                                break;
                            default:
                                System.out.println("Opção inválida! Usando CLIENTE como padrão.");
                                tipo = TipoPessoa.CLIENTE;
                        }
                        pessoa.setTipo(tipo);
                        System.out.println("Tipo atualizado!");
                        break;
                    case "3":
                        List<Endereco> novosEnderecos = new ArrayList<>();
                        boolean adicionarMais = true;
                        while (adicionarMais) {
                            Endereco endereco = lerEndereco(scanner, codigo, pessoaService.gerarProximoEnderecoId());
                            novosEnderecos.add(endereco);
                            System.out.print("Deseja adicionar outro endereço? (s/n): ");
                            String resp = scanner.nextLine().trim().toLowerCase();
                            adicionarMais = resp.equals("s") || resp.equals("sim");
                        }
                        pessoa.setEnderecos(novosEnderecos);
                        System.out.println("Endereços atualizados!");
                        break;
                    case "4":
                        atualizar = false;
                        break;
                    default:
                        System.out.println("Opção inválida! Tente novamente.");
                }
            }
            // Salva as alterações
            boolean atualizado = pessoaService.atualizar(pessoa.getCodigo(), pessoa.getNome(), pessoa.getTipo(),
                    pessoa.getEnderecos());
            if (atualizado) {
                Logger.registrar("Pessoa atualizada: código=" + codigo);
                System.out.println("Pessoa atualizada com sucesso!");
            } else {
                System.out.println("Erro ao atualizar pessoa.");
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar pessoa: " + e.getMessage());
            Logger.registrar("Erro ao atualizar pessoa: " + e.getMessage());
        }
    }

    private void deletarPessoa(Scanner scanner) {
        try {
            System.out.print("Digite o código da pessoa a deletar: ");
            int codigo = Integer.parseInt(scanner.nextLine());
            Optional<Pessoa> pessoaOpt = pessoaService.buscar(codigo);
            if (pessoaOpt.isPresent()) {
                Pessoa pessoa = pessoaOpt.get();
                System.out.println("\n===== Confirmação de Remoção =====");
                System.out.println("Código: " + pessoa.getCodigo());
                System.out.println("Nome: " + pessoa.getNome());
                System.out.println("Tipo: " + pessoa.getTipo());
                System.out.println("Endereços:");
                System.out.print(formatarEnderecos(pessoa.getEnderecos()));
                System.out.println("------------------------------");
                System.out.print("Tem certeza que deseja deletar esta pessoa? (s/n): ");
                String confirmacao = scanner.nextLine().trim().toLowerCase();
                if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
                    System.out.println("Operação de deleção cancelada pelo usuário.");
                    return;
                }
                boolean removido = pessoaService.deletar(codigo);
                if (removido) {
                    Logger.registrar("Pessoa deletada: código=" + codigo);
                    System.out.println("Pessoa deletada com sucesso!");
                    System.out.println("\n===== Pessoa Removida =====");
                    System.out.println("Código: " + pessoa.getCodigo());
                    System.out.println("Nome: " + pessoa.getNome());
                    System.out.println("Tipo: " + pessoa.getTipo());
                    System.out.println("Endereços:");
                    System.out.print(formatarEnderecos(pessoa.getEnderecos()));
                    System.out.println("------------------------------\n");
                } else {
                    System.out.println("Erro ao deletar pessoa.");
                }
            } else {
                System.out.println("Pessoa não encontrada com código " + codigo);
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar pessoa: " + e.getMessage());
            Logger.registrar("Erro ao deletar pessoa: " + e.getMessage());
        }
    }

    private Endereco selecionarEnderecoEntrega(Scanner scanner, Pessoa cliente) {
        List<Endereco> enderecos = cliente.getEnderecos();
        if (enderecos == null || enderecos.isEmpty()) {
            System.out.println("Cliente não possui endereço cadastrado.");
            return null;
        }
        System.out.println("Selecione o endereço de entrega pelo ID:");
        for (Endereco e : enderecos) {
            System.out.printf("%d - %s, Nº %s, %s, %s, %s - %s, CEP: %s\n",
                    e.getId(), e.getLogradouro(), e.getNumero(), e.getComplemento(), e.getBairro(), e.getCidade(),
                    e.getEstado(), e.getCep());
        }
        int idSelecionado = -1;
        while (true) {
            System.out.print("Digite o ID do endereço desejado: ");
            try {
                idSelecionado = Integer.parseInt(scanner.nextLine());
                for (Endereco e : enderecos) {
                    if (e.getId() == idSelecionado) {
                        return e;
                    }
                }
                System.out.println("ID inválido. Tente novamente.");
            } catch (NumberFormatException ex) {
                System.out.println("Digite um número válido.");
            }
        }
    }

    private void criarProduto(Scanner scanner) {
        try {
            int codigo = produtoService.gerarProximoCodigo();
            // Seleção do fornecedor
            List<Pessoa> fornecedores = new ArrayList<>();
            for (Pessoa p : pessoaService.listar()) {
                if (p.getTipo() == TipoPessoa.FORNECEDOR || p.getTipo() == TipoPessoa.AMBOS) {
                    fornecedores.add(p);
                }
            }
            if (fornecedores.isEmpty()) {
                System.out.println("Nenhum fornecedor cadastrado. Cadastre um fornecedor antes de criar produtos.");
                return;
            }
            System.out.println("Selecione o fornecedor pelo código:");
            for (Pessoa f : fornecedores) {
                System.out.println(f.getCodigo() + " - " + f.getNome());
            }
            int codigoFornecedor = -1;
            Pessoa fornecedor = null;
            while (fornecedor == null) {
                System.out.print("Digite o código do fornecedor: ");
                try {
                    codigoFornecedor = Integer.parseInt(scanner.nextLine());
                    for (Pessoa f : fornecedores) {
                        if (f.getCodigo() == codigoFornecedor) {
                            fornecedor = f;
                            break;
                        }
                    }
                    if (fornecedor == null) {
                        System.out.println("Código inválido. Tente novamente.");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Digite um número válido.");
                }
            }
            System.out.print("Digite o nome do produto: ");
            String nome = scanner.nextLine();
            System.out.print("Digite o preço do produto: ");
            double preco = Double.parseDouble(scanner.nextLine());
            Produto produto = new Produto(codigo, nome, preco, fornecedor.getCodigo());
            produtoService.criar(produto);
            Logger.registrar(
                    "Produto criado: código=" + codigo + ", nome=" + nome + ", fornecedor=" + fornecedor.getNome());
            System.out.println("Produto criado com sucesso! Código gerado: " + codigo);
        } catch (NumberFormatException e) {
            System.out.println("Preço inválido. Deve ser um número.");
            Logger.registrar("Erro ao criar produto: preço inválido");
        }
    }

    private void listarProdutos() {
        System.out.println("\n===== Lista de Produtos =====");
        List<Pessoa> pessoas = pessoaService.listar();
        for (Produto p : produtoService.listar()) {
            String fornecedorNome = "(não encontrado)";
            for (Pessoa f : pessoas) {
                if (f.getCodigo() == p.getFornecedorCodigo()) {
                    fornecedorNome = f.getNome();
                    break;
                }
            }
            System.out.printf("Código: %d | Nome: %s | Preço: R$ %.2f | Fornecedor: %s\n",
                    p.getCodigo(), p.getNome(), p.getPreco(), fornecedorNome);
        }
        System.out.println("------------------------------");
    }

    private void atualizarProduto(Scanner scanner) {
        try {
            System.out.print("Digite o código do produto a atualizar: ");
            int codigo = Integer.parseInt(scanner.nextLine());
            System.out.print("Digite o novo nome: ");
            String nome = scanner.nextLine();
            System.out.print("Digite o novo preço: ");
            double preco = Double.parseDouble(scanner.nextLine());
            boolean atualizado = produtoService.atualizar(codigo, nome, preco);
            if (atualizado) {
                Logger.registrar("Produto atualizado: código=" + codigo);
                System.out.println("Produto atualizado com sucesso!");
            } else {
                System.out.println("Produto não encontrado com código " + codigo);
            }
        } catch (Exception e) {
            System.out.println("Erro ao atualizar produto: " + e.getMessage());
            Logger.registrar("Erro ao atualizar produto: " + e.getMessage());
        }
    }

    private void deletarProduto(Scanner scanner) {
        try {
            System.out.print("Digite o código do produto a deletar: ");
            int codigo = Integer.parseInt(scanner.nextLine());
            Optional<Produto> produtoOpt = produtoService.buscar(codigo);
            if (produtoOpt.isPresent()) {
                Produto produto = produtoOpt.get();
                System.out.println("\n===== Confirmação de Remoção =====");
                System.out.println("Código: " + produto.getCodigo());
                System.out.println("Nome: " + produto.getNome());
                System.out.printf("Preço: R$ %.2f\n", produto.getPreco());
                System.out.println("------------------------------");
                System.out.print("Tem certeza que deseja deletar este produto? (s/n): ");
                String confirmacao = scanner.nextLine().trim().toLowerCase();
                if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
                    System.out.println("Operação de deleção cancelada pelo usuário.");
                    return;
                }
                boolean removido = produtoService.deletar(codigo);
                if (removido) {
                    Logger.registrar("Produto deletado: código=" + codigo);
                    System.out.println("Produto deletado com sucesso!");
                } else {
                    System.out.println("Erro ao deletar produto.");
                }
            } else {
                System.out.println("Produto não encontrado com código " + codigo);
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar produto: " + e.getMessage());
            Logger.registrar("Erro ao deletar produto: " + e.getMessage());
        }
    }

    private void criarPedidoVenda(Scanner scanner) {
        List<Pessoa> pessoas = PessoaService.lerPessoasDoArquivo();
        List<Produto> produtos = ProdutoService.lerProdutosDoArquivo();
        List<PedidoVenda> pedidos = pedidoVendaService.listar();
        int numero = pedidoVendaService.gerarProximoNumero(pedidos);
        System.out.println("Selecione o cliente pelo código:");
        pessoas.forEach(p -> System.out.println(p.getCodigo() + " - " + p.getNome()));
        int codigoCliente = Integer.parseInt(scanner.nextLine());
        Pessoa cliente = pessoas.stream().filter(p -> p.getCodigo() == codigoCliente).findFirst().orElse(null);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        Endereco enderecoEntrega = selecionarEnderecoEntrega(scanner, cliente);
        if (enderecoEntrega == null) {
            return;
        }
        System.out.println("Selecione os produtos (códigos separados por vírgula):");
        produtos.forEach(p -> System.out.println(p.getCodigo() + " - " + p.getNome() + " (R$" + p.getPreco() + ")"));
        String[] codigos = scanner.nextLine().split(",");
        List<Produto> produtosSelecionados = new ArrayList<>();
        for (String cod : codigos) {
            try {
                int codInt = Integer.parseInt(cod.trim());
                produtos.stream().filter(prod -> prod.getCodigo() == codInt).findFirst()
                        .ifPresent(produtosSelecionados::add);
            } catch (NumberFormatException ignored) {
            }
        }
        if (produtosSelecionados.isEmpty()) {
            System.out.println("Nenhum produto selecionado.");
            return;
        }
        PedidoVenda pedido = new PedidoVenda(numero, cliente, enderecoEntrega, produtosSelecionados);
        pedidoVendaService.criar(pedido);
        System.out.println("Pedido criado com sucesso! Número: " + numero);
        // Resumo detalhado do pedido
        System.out.println("\n===== RESUMO DO PEDIDO =====");
        System.out.println("Número do Pedido: " + pedido.getNumero());
        System.out.println("Cliente: " + cliente.getNome() + " (Código: " + cliente.getCodigo() + ")");
        System.out.println("Endereço de Entrega: " + enderecoEntrega);
        System.out.println("Produtos:");
        for (Produto prod : produtosSelecionados) {
            System.out.printf("  - %s (Código: %d) - R$ %.2f\n", prod.getNome(), prod.getCodigo(), prod.getPreco());
        }
        System.out.printf("Montante Total: R$ %.2f\n", pedido.getMontanteTotal());
        System.out.println("============================\n");
    }

    private void listarPedidosVenda() {
        List<PedidoVenda> pedidos = pedidoVendaService.listar();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado.");
            return;
        }
        System.out.println("\n===== Lista de Pedidos de Venda =====");
        for (PedidoVenda pedido : pedidos) {
            exibirResumoPedido(pedido);
        }
        System.out.println("------------------------------");
    }

    private void exibirResumoPedido(PedidoVenda pedido) {
        System.out.println("Número do Pedido: " + pedido.getNumero());
        System.out.println(
                "Cliente: " + pedido.getCliente().getNome() + " (Código: " + pedido.getCliente().getCodigo() + ")");
        System.out.println("Endereço de Entrega: " + formatarEndereco(pedido.getEnderecoEntrega()));
        System.out.println("Produtos:");
        for (Produto prod : pedido.getProdutos()) {
            System.out.printf("  - %s (Código: %d) - R$ %.2f\n", prod.getNome(), prod.getCodigo(), prod.getPreco());
        }
        System.out.printf("Montante Total: R$ %.2f\n", pedido.getMontanteTotal());
        System.out.println("------------------------------");
    }

    private void atualizarPedidoVenda(Scanner scanner) {
        List<PedidoVenda> pedidos = pedidoVendaService.listar();
        if (pedidos.isEmpty()) {
            System.out.println("Nenhum pedido cadastrado para atualizar.");
            return;
        }
        listarPedidosVenda();
        System.out.print("Digite o número do pedido a atualizar: ");
        int numero = Integer.parseInt(scanner.nextLine());
        PedidoVenda pedido = pedidos.stream().filter(p -> p.getNumero() == numero).findFirst().orElse(null);
        if (pedido == null) {
            System.out.println("Pedido não encontrado.");
            return;
        }
        List<Pessoa> pessoas = PessoaService.lerPessoasDoArquivo();
        List<Produto> produtos = ProdutoService.lerProdutosDoArquivo();
        System.out.println("Selecione o novo cliente pelo código:");
        pessoas.forEach(p -> System.out.println(p.getCodigo() + " - " + p.getNome()));
        int codigoCliente = Integer.parseInt(scanner.nextLine());
        Pessoa cliente = pessoas.stream().filter(p -> p.getCodigo() == codigoCliente).findFirst().orElse(null);
        if (cliente == null) {
            System.out.println("Cliente não encontrado.");
            return;
        }
        Endereco enderecoEntrega = selecionarEnderecoEntrega(scanner, cliente);
        if (enderecoEntrega == null) {
            return;
        }
        System.out.println("Selecione os novos produtos (códigos separados por vírgula):");
        produtos.forEach(p -> System.out.println(p.getCodigo() + " - " + p.getNome() + " (R$" + p.getPreco() + ")"));
        String[] codigos = scanner.nextLine().split(",");
        List<Produto> produtosSelecionados = new ArrayList<>();
        for (String cod : codigos) {
            try {
                int codInt = Integer.parseInt(cod.trim());
                produtos.stream().filter(prod -> prod.getCodigo() == codInt).findFirst()
                        .ifPresent(produtosSelecionados::add);
            } catch (NumberFormatException ignored) {
            }
        }
        if (produtosSelecionados.isEmpty()) {
            System.out.println("Nenhum produto selecionado.");
            return;
        }
        boolean atualizado = pedidoVendaService.atualizar(numero, cliente, enderecoEntrega, produtosSelecionados);
        if (atualizado) {
            System.out.println("Pedido atualizado com sucesso!");
        } else {
            System.out.println("Erro ao atualizar pedido.");
        }
    }

    private void buscarPedidoVenda(Scanner scanner) {
        System.out.print("Digite o número do pedido para buscar: ");
        int numero = Integer.parseInt(scanner.nextLine());
        PedidoVenda pedido = pedidoVendaService.buscar(numero).orElse(null);
        if (pedido != null) {
            System.out.println(pedido);
        } else {
            System.out.println("Pedido não encontrado.");
        }
    }

    // Ao criar, atualizar ou deletar, exibir o resultado formatado
    // Exemplo para criarPessoa:
    private void criarPessoa(Scanner scanner) {
        try {
            int codigo = pessoaService.gerarProximoCodigo();
            System.out.print("Digite o nome da pessoa: ");
            String nome = scanner.nextLine();
            System.out.println("Escolha o tipo da pessoa:");
            System.out.println("1 - CLIENTE");
            System.out.println("2 - FORNECEDOR");
            System.out.println("3 - AMBOS");
            System.out.print("Digite o número correspondente ao tipo: ");
            int tipoOpcao = Integer.parseInt(scanner.nextLine());
            TipoPessoa tipo;
            switch (tipoOpcao) {
                case 1:
                    tipo = TipoPessoa.CLIENTE;
                    break;
                case 2:
                    tipo = TipoPessoa.FORNECEDOR;
                    break;
                case 3:
                    tipo = TipoPessoa.AMBOS;
                    break;
                default:
                    System.out.println("Opção inválida! Usando CLIENTE como padrão.");
                    tipo = TipoPessoa.CLIENTE;
            }
            List<Endereco> enderecos = new ArrayList<>();
            boolean adicionarMais = true;
            while (adicionarMais) {
                Endereco endereco = lerEndereco(scanner, codigo, pessoaService.gerarProximoEnderecoId());
                enderecos.add(endereco);
                System.out.print("Deseja adicionar outro endereço? (s/n): ");
                String resp = scanner.nextLine().trim().toLowerCase();
                adicionarMais = resp.equals("s") || resp.equals("sim");
            }
            Pessoa pessoa = new Pessoa(codigo, nome, tipo, enderecos);
            pessoaService.criar(pessoa);
            Logger.registrar("Pessoa criada: código=" + codigo + ", nome=" + nome);
            System.out.println("Pessoa criada com sucesso! Código gerado: " + codigo);
            // Exibir resultado formatado
            System.out.println("\n===== Pessoa Cadastrada =====");
            System.out.println("Código: " + pessoa.getCodigo());
            System.out.println("Nome: " + pessoa.getNome());
            System.out.println("Tipo: " + pessoa.getTipo());
            System.out.println("Endereços:");
            System.out.print(formatarEnderecos(pessoa.getEnderecos()));
            System.out.println("------------------------------\n");
        } catch (Exception e) {
            System.out.println("Erro ao criar pessoa: " + e.getMessage());
            Logger.registrar("Erro ao criar pessoa: " + e.getMessage());
        }
    }

    private Endereco lerEndereco(Scanner scanner, int pessoaId, int enderecoId) {
        String logradouro;
        while (true) {
            System.out.print("Logradouro: ");
            logradouro = scanner.nextLine();
            if (!logradouro.trim().isEmpty())
                break;
            System.out.println("Logradouro não pode ser vazio.");
        }
        String numero;
        while (true) {
            System.out.print("Número: ");
            numero = scanner.nextLine();
            if (numero.matches("\\d+"))
                break;
            System.out.println("Número deve conter apenas dígitos.");
        }
        String complemento;
        while (true) {
            System.out.print("Complemento: ");
            complemento = scanner.nextLine();
            if (!complemento.trim().isEmpty())
                break;
            System.out.println("Complemento não pode ser vazio.");
        }
        String bairro;
        while (true) {
            System.out.print("Bairro: ");
            bairro = scanner.nextLine();
            if (!bairro.trim().isEmpty())
                break;
            System.out.println("Bairro não pode ser vazio.");
        }
        String cidade;
        while (true) {
            System.out.print("Cidade: ");
            cidade = scanner.nextLine();
            if (!cidade.trim().isEmpty())
                break;
            System.out.println("Cidade não pode ser vazia.");
        }
        String estado;
        while (true) {
            System.out.print("Estado: ");
            estado = scanner.nextLine();
            if (!estado.trim().isEmpty())
                break;
            System.out.println("Estado não pode ser vazio.");
        }
        String cep;
        while (true) {
            System.out.print("CEP: ");
            cep = scanner.nextLine();
            if (cep.matches("\\d{8,11}"))
                break;
            System.out.println("CEP deve conter apenas dígitos (8 a 11 dígitos).");
        }
        String tipo;
        while (true) {
            System.out.print("Tipo do endereço (ex: Residencial, Comercial, Entrega, etc): ");
            tipo = scanner.nextLine();
            if (!tipo.trim().isEmpty())
                break;
            System.out.println("Tipo do endereço não pode ser vazio.");
        }
        // O id do endereço deve ser sempre gerado automaticamente e único
        // O método pessoaService.gerarProximoEnderecoId() já é chamado na criação de
        // cada endereço
        return new Endereco(enderecoId, pessoaId, logradouro, numero, tipo + " - " + complemento, bairro, cidade,
                estado, cep);
    }

    private void deletarPedidoVenda(Scanner scanner) {
        try {
            System.out.print("Digite o número do pedido a deletar: ");
            int numero = Integer.parseInt(scanner.nextLine());
            Optional<PedidoVenda> pedidoOpt = pedidoVendaService.buscar(numero);
            if (pedidoOpt.isPresent()) {
                PedidoVenda pedido = pedidoOpt.get();
                System.out.println("\n===== Confirmação de Remoção =====");
                System.out.println("Número do Pedido: " + pedido.getNumero());
                System.out.println("Cliente: " + pedido.getCliente().getNome());
                System.out.println("Endereço de Entrega: " + formatarEndereco(pedido.getEnderecoEntrega()));
                System.out.println("Produtos:");
                for (Produto prod : pedido.getProdutos()) {
                    System.out.printf("  - %s (Código: %d) - R$ %.2f\n", prod.getNome(), prod.getCodigo(),
                            prod.getPreco());
                }
                System.out.printf("Montante Total: R$ %.2f\n", pedido.getMontanteTotal());
                System.out.println("------------------------------");
                System.out.print("Tem certeza que deseja deletar este pedido? (s/n): ");
                String confirmacao = scanner.nextLine().trim().toLowerCase();
                if (!confirmacao.equals("s") && !confirmacao.equals("sim")) {
                    System.out.println("Operação de deleção cancelada pelo usuário.");
                    return;
                }
                boolean removido = pedidoVendaService.deletar(numero);
                if (removido) {
                    Logger.registrar("Pedido de venda deletado: número=" + numero);
                    System.out.println("Pedido de venda deletado com sucesso!");
                } else {
                    System.out.println("Erro ao deletar pedido de venda.");
                }
            } else {
                System.out.println("Pedido de venda não encontrado com número " + numero);
            }
        } catch (Exception e) {
            System.out.println("Erro ao deletar pedido de venda: " + e.getMessage());
            Logger.registrar("Erro ao deletar pedido de venda: " + e.getMessage());
        }
    }

    // Repita o padrão para atualizarPessoa, deletarPessoa, criarProduto,
    // atualizarProduto, deletarProduto, criarPedidoVenda, atualizarPedidoVenda,
    // deletarPedidoVenda
}
