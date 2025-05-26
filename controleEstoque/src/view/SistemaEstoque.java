package view;
import controller.Estoque;
import model.MovimentacaoEstoque;
import model.Produto;
import model.RelatorioEstoque; 

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.InputMismatchException; 

public class SistemaEstoque {

    private Estoque estoque;
    private RelatorioEstoque relatorioEstoque;
    private List<MovimentacaoEstoque> movimentacoes;
    private Scanner scanner;

    public SistemaEstoque() {
        this.estoque = new Estoque();
        this.relatorioEstoque = new RelatorioEstoque();
        this.movimentacoes = new ArrayList<>();
        this.scanner = new Scanner(System.in);
    }


    public void executar() {
        int opcao;
        do {
            exibirMenu();
            opcao = lerOpcao();

            switch (opcao) {
                case 1:
                    cadastrarProduto();
                    break;
                case 2:
                    atualizarProduto();
                    break;
                case 3:
                    registrarEntrada();
                    break;
                case 4:
                    registrarSaida();
                    break;
                case 5:
                    buscarProduto();
                    break;
                case 6:
                    listarTodosProdutos();
                    break;
                case 7:
                    listarProdutosBaixoEstoque();
                    break;
                case 8:
                    excluirProduto();
                    break;
                case 9:
                    gerarRelatorios();
                    break;
                case 0:
                    System.out.println("Saindo do sistema...");
                    break;
                default:
                    System.out.println("Opção inválida. Tente novamente.");
            }
        } while (opcao != 0);
        scanner.close();
    }

    private void exibirMenu() {
        System.out.println("\n--- Sistema de Controle de Estoque ---");
        System.out.println("1. Cadastrar Produto");
        System.out.println("2. Atualizar Produto");
        System.out.println("3. Registrar Entrada de Estoque");
        System.out.println("4. Registrar Saída de Estoque");
        System.out.println("5. Buscar Produto");
        System.out.println("6. Listar Todos os Produtos");
        System.out.println("7. Listar Produtos com Baixo Estoque");
        System.out.println("8. Excluir Produto");
        System.out.println("9. Gerar Relatórios");
        System.out.println("0. Sair");
        System.out.print("Escolha uma opção: ");
    }


    private int lerOpcao() {
        int opcao = -1;
        try {
            opcao = scanner.nextInt();
        } catch (InputMismatchException e) {
            System.out.println("Entrada inválida. Por favor, digite um número inteiro.");
        } finally {
            scanner.nextLine();
        }
        return opcao;
    }

    private double lerDouble(String prompt) {
        double valor = 0;
        boolean valido = false;
        while(!valido){
            System.out.print(prompt);
            try {
                valor = scanner.nextDouble();
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número decimal (use vírgula ou ponto).");
            } finally {
                scanner.nextLine();
            }
        }
        return valor;
    }

    private int lerInt(String prompt) {
        int valor = 0;
        boolean valido = false;
        while(!valido){
            System.out.print(prompt);
            try {
                valor = scanner.nextInt();
                valido = true;
            } catch (InputMismatchException e) {
                System.out.println("Entrada inválida. Digite um número inteiro.");
            } finally {
                scanner.nextLine();
            }
        }
        return valor;
    }

    private String lerString(String prompt) {
        System.out.print(prompt);
        return scanner.nextLine();
    }


    private void cadastrarProduto() {
        System.out.println("\n-- Cadastro de Produto --");
        String nome = lerString("Nome: ");
        // Verifica se produto já existe
        if (estoque.buscarProdutoPorNome(nome) != null) {
            System.out.println("Erro: Já existe um produto com o nome '" + nome + "'.");
            return;
        }
        String categoria = lerString("Categoria: ");
        double preco = lerDouble("Preço: ");
        int quantidade = lerInt("Quantidade Inicial: ");
        if (quantidade < 0) {
            System.out.println("Quantidade inicial não pode ser negativa.");
            return;
        }

        Produto novoProduto = estoque.adicionarProduto(nome, preco, quantidade, categoria);
        if(novoProduto != null) {
            System.out.println("Produto '" + nome + "' cadastrado com sucesso com ID " + novoProduto.getId() + "!");
            if (quantidade > 0) {
                registrarMovimentacao(LocalDate.now(), "Entrada Inicial", quantidade, novoProduto);
            }
        } else {
            System.out.println("Falha ao cadastrar o produto.");
        }
    }

    private void atualizarProduto() {
        System.out.println("\n-- Atualizar Produto --");
        String nome = lerString("Nome do produto a atualizar: ");

        Produto produto = estoque.buscarProdutoPorNome(nome);
        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        System.out.println("Deixe em branco para não alterar o valor atual.");
        System.out.print("Novo Preço (" + produto.getPreco() + "): ");
        String novoPrecoStr = scanner.nextLine();
        System.out.print("Nova Categoria (" + produto.getCategoria() + "): ");
        String novaCategoria = scanner.nextLine();

        Double novoPreco = null;
        if (!novoPrecoStr.trim().isEmpty()) {
            try {
                novoPreco = Double.parseDouble(novoPrecoStr.replace(',', '.'));
                if (novoPreco < 0) {
                    System.out.println("Preço não pode ser negativo. Mantendo o valor original.");
                    novoPreco = produto.getPreco();
                }
            } catch (NumberFormatException e) {
                System.out.println("Formato de preço inválido. Mantendo o preço original.");
                novoPreco = produto.getPreco();
            }
        }

        String categoriaFinal = produto.getCategoria();
        if (!novaCategoria.trim().isEmpty()) {
            categoriaFinal = novaCategoria;
        }

        // Chamar o método atualizado de Estoque
        boolean atualizado = estoque.atualizarProduto(nome, novoPreco, categoriaFinal);

        if (atualizado) {
            System.out.println("Produto '" + nome + "' atualizado com sucesso.");
        } else {
            System.out.println("Não foi possível atualizar o produto (pode já ter sido removido).");
        }
    }


    private void registrarEntrada() {
        System.out.println("\n-- Registrar Entrada de Estoque --");
        String nome = lerString("Nome do produto: ");
        Produto produto = estoque.buscarProdutoPorNome(nome);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int quantidade = lerInt("Quantidade a adicionar: ");
        if (quantidade <= 0) {
            System.out.println("Quantidade para entrada deve ser positiva.");
            return;
        }

        boolean sucesso = estoque.registrarEntrada(nome, quantidade);

        if(sucesso) {
            registrarMovimentacao(LocalDate.now(), "Entrada", quantidade, produto);
            System.out.println("Entrada de " + quantidade + " unidades de '" + nome + "' registrada. Nova quantidade: " + produto.getQuantidade());
        } else {
            System.out.println("Falha ao registrar entrada para o produto '" + nome + "'.");
        }
    }

    private void registrarSaida() {
        System.out.println("\n-- Registrar Saída de Estoque --");
        String nome = lerString("Nome do produto: ");
        Produto produto = estoque.buscarProdutoPorNome(nome);

        if (produto == null) {
            System.out.println("Produto não encontrado.");
            return;
        }

        int quantidade = lerInt("Quantidade a remover: ");
        if (quantidade <= 0) {
            System.out.println("Quantidade para saída deve ser positiva.");
            return;
        }

        boolean sucesso = estoque.registrarSaida(nome, quantidade);

        if(sucesso) {
            registrarMovimentacao(LocalDate.now(), "Saida", quantidade, produto);
            System.out.println("Saída de " + quantidade + " unidades de '" + nome + "' registrada. Nova quantidade: " + produto.getQuantidade());
        } else {
        }
    }

    private void buscarProduto() {
        System.out.println("\n-- Buscar Produto --");
        String nome = lerString("Nome do produto a buscar: ");
        Produto produto = estoque.buscarProdutoPorNome(nome);

        if (produto != null) {
            System.out.println("Produto encontrado:");
            System.out.println(produto);
        } else {
            System.out.println("Nenhum produto encontrado com o nome '" + nome + "'.");
        }
    }

    private void listarTodosProdutos() {
        System.out.println("\n-- Lista de Todos os Produtos --");
        estoque.listarProdutos(); // Usa o método revisado em Estoque
    }

    private void listarProdutosBaixoEstoque() {
        System.out.println("\n-- Produtos com Baixo Estoque --");
        int qtdMinima = lerInt("Informe a quantidade mínima para alerta: ");
        List<Produto> baixos = estoque.listarProdutosComEstoqueBaixo(qtdMinima);

        if (baixos.isEmpty()) {
            System.out.println("Nenhum produto encontrado abaixo de " + qtdMinima + " unidades.");
        } else {
            System.out.println("Produtos com quantidade igual ou inferior a " + qtdMinima + ":");
            for (Produto p : baixos) {
                System.out.println(p + " - Quantidade Atual: " + p.getQuantidade());
            }
        }
    }


    private void excluirProduto() {
        System.out.println("\n-- Excluir Produto --");
        String nome = lerString("Nome do produto a excluir: ");
        boolean excluido = estoque.removerProdutoPorNome(nome);

        if(excluido) {
            System.out.println("Produto '" + nome + "' excluído com sucesso.");
        } else {
            System.out.println("Produto '" + nome + "' não encontrado para exclusão.");
        }
    }


    private void gerarRelatorios() {
        System.out.println("\n-- Gerar Relatórios --");
        System.out.println("1. Relatório de Produtos em Estoque");
        System.out.println("2. Relatório de Produtos com Baixo Estoque");
        System.out.println("3. Histórico de Movimentações");
        System.out.print("Escolha o relatório: ");
        int tipoRelatorio = lerOpcao();

        switch(tipoRelatorio) {
            case 1:
                relatorioEstoque.gerarRelatorioProdutos(estoque, "relatorio_produtos.txt");
                break;
            case 2:
                int qtdMinima = lerInt("Informe a quantidade mínima para o relatório de baixo estoque: ");


                relatorioEstoque.gerarRelatorioBaixoEstoque(this.estoque, qtdMinima, "relatorio_baixo_estoque.txt");
                break;
            case 3:
                relatorioEstoque.gerarHistoricoMovimentacoes(movimentacoes, "relatorio_movimentacoes.txt");
                break;
            default:
                System.out.println("Tipo de relatório inválido.");
        }
    }



    private void registrarMovimentacao(LocalDate data, String tipo, int quantidade, Produto produto) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque(data, tipo, quantidade, produto);
        this.movimentacoes.add(mov);
    }
    
    public static void main(String[] args) {
        SistemaEstoque sistema = new SistemaEstoque();
        sistema.executar();
    }
}