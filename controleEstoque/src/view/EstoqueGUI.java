package view;

import controller.Estoque;
import model.MovimentacaoEstoque;
import model.Produto;
import model.RelatorioEstoque;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;


public class EstoqueGUI extends JFrame {


    private JButton btnCadastrarProduto, btnAtualizarProduto, btnRegistrarEntrada, btnRegistrarSaida;
    private JButton btnBuscarProduto, btnListarTodos, btnListarBaixoEstoque, btnExcluirProduto, btnGerarRelatorios;
    private JTextArea areaExibicao;


    private Estoque estoque;
    private RelatorioEstoque relatorioEstoque;
    private List<MovimentacaoEstoque> movimentacoes;


    public EstoqueGUI() {
        this.estoque = new Estoque();
        this.relatorioEstoque = new RelatorioEstoque();
        this.movimentacoes = new ArrayList<>();

        setTitle("Sistema de Controle de Estoque");
        setSize(800, 600);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        inicializarComponentes();
        adicionarComponentes();
        adicionarListeners();


        areaExibicao.setText("Bem-vindo ao Sistema de Controle de Estoque!\nSelecione uma operação no menu acima.\n");
    }

    private void inicializarComponentes() {
        btnCadastrarProduto = new JButton("Cadastrar Produto");
        btnAtualizarProduto = new JButton("Atualizar Produto");
        btnRegistrarEntrada = new JButton("Registrar Entrada");
        btnRegistrarSaida = new JButton("Registrar Saída");
        btnBuscarProduto = new JButton("Buscar Produto");
        btnListarTodos = new JButton("Listar Todos");
        btnListarBaixoEstoque = new JButton("Baixo Estoque");
        btnExcluirProduto = new JButton("Excluir Produto");
        btnGerarRelatorios = new JButton("Gerar Relatórios");

        areaExibicao = new JTextArea();
        areaExibicao.setEditable(false);
        areaExibicao.setFont(new Font("Monospaced", Font.PLAIN, 14));
        areaExibicao.setMargin(new Insets(10, 10, 10, 10));
    }

    private void adicionarComponentes() {
        JPanel painelBotoes = new JPanel();
        painelBotoes.setLayout(new GridLayout(0, 3, 10, 10));
        painelBotoes.setBorder(BorderFactory.createEmptyBorder(10, 10, 10, 10));

        painelBotoes.add(btnCadastrarProduto);
        painelBotoes.add(btnAtualizarProduto);
        painelBotoes.add(btnRegistrarEntrada);
        painelBotoes.add(btnRegistrarSaida);
        painelBotoes.add(btnBuscarProduto);
        painelBotoes.add(btnListarTodos);
        painelBotoes.add(btnListarBaixoEstoque);
        painelBotoes.add(btnExcluirProduto);
        painelBotoes.add(btnGerarRelatorios);

        add(painelBotoes, BorderLayout.NORTH);
        add(new JScrollPane(areaExibicao), BorderLayout.CENTER);
    }

    private void adicionarListeners() {
        btnCadastrarProduto.addActionListener(e -> cadastrarProdutoGUI());
        btnAtualizarProduto.addActionListener(e -> atualizarProdutoGUI());
        btnRegistrarEntrada.addActionListener(e -> registrarEntradaGUI());
        btnRegistrarSaida.addActionListener(e -> registrarSaidaGUI());
        btnBuscarProduto.addActionListener(e -> buscarProdutoGUI());
        btnListarTodos.addActionListener(e -> listarTodosProdutosGUI());
        btnListarBaixoEstoque.addActionListener(e -> listarProdutosBaixoEstoqueGUI());
        btnExcluirProduto.addActionListener(e -> excluirProdutoGUI());
        btnGerarRelatorios.addActionListener(e -> gerarRelatoriosGUI());
    }

    private void registrarMovimentacao(LocalDate data, String tipo, int quantidade, Produto produto) {
        MovimentacaoEstoque mov = new MovimentacaoEstoque(data, tipo, quantidade, produto);
        this.movimentacoes.add(mov);

    }

    private void cadastrarProdutoGUI() {
        String nome = JOptionPane.showInputDialog(this, "Nome do Produto:", "Cadastrar Produto", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) return;

        if (estoque.buscarProdutoPorNome(nome) != null) {
            JOptionPane.showMessageDialog(this, "Erro: Já existe um produto com o nome '" + nome + "'.", "Erro ao Cadastrar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String categoria = JOptionPane.showInputDialog(this, "Categoria do Produto:", "Cadastrar Produto", JOptionPane.PLAIN_MESSAGE);
        if (categoria == null || categoria.trim().isEmpty()) {
            categoria = "Geral";
        }


        double preco;
        try {
            String precoStr = JOptionPane.showInputDialog(this, "Preço do Produto (ex: 10.99):", "Cadastrar Produto", JOptionPane.PLAIN_MESSAGE);
            if (precoStr == null) return;
            preco = Double.parseDouble(precoStr.replace(',', '.'));
            if (preco < 0) {
                JOptionPane.showMessageDialog(this, "Preço não pode ser negativo.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Preço inválido. Use ponto como separador decimal.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantidade;
        try {
            String qtdStr = JOptionPane.showInputDialog(this, "Quantidade Inicial:", "Cadastrar Produto", JOptionPane.PLAIN_MESSAGE);
            if (qtdStr == null) return;
            quantidade = Integer.parseInt(qtdStr);
            if (quantidade < 0) {
                JOptionPane.showMessageDialog(this, "Quantidade inicial não pode ser negativa.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Deve ser um número inteiro.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        Produto novoProduto = estoque.adicionarProduto(nome, preco, quantidade, categoria); //
        if (novoProduto != null) {
            areaExibicao.setText("Produto '" + nome + "' cadastrado com sucesso com ID " + novoProduto.getId() + "!\n");
            if (quantidade > 0) {
                registrarMovimentacao(LocalDate.now(), "Entrada Inicial", quantidade, novoProduto); //
                areaExibicao.append("Movimentação de entrada inicial registrada para '" + nome + "'.\n");
            }
            listarTodosProdutosGUI();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao cadastrar o produto.", "Erro ao Cadastrar", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void atualizarProdutoGUI() {
        String nome = JOptionPane.showInputDialog(this, "Nome do produto a atualizar:", "Atualizar Produto", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) return;

        Produto produto = estoque.buscarProdutoPorNome(nome); //
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Produto '" + nome + "' não encontrado.", "Erro ao Atualizar", JOptionPane.ERROR_MESSAGE);
            return;
        }

        String novoPrecoStr = JOptionPane.showInputDialog(this, "Novo Preço (atual: " + produto.getPreco() + ") - Deixe em branco ou cancele para não alterar:", "Atualizar Produto", JOptionPane.PLAIN_MESSAGE);
        Double novoPreco = null;
        if (novoPrecoStr != null && !novoPrecoStr.trim().isEmpty()) {
            try {
                novoPreco = Double.parseDouble(novoPrecoStr.replace(',', '.'));
                if (novoPreco < 0) {
                    JOptionPane.showMessageDialog(this, "Preço não pode ser negativo. Mantendo o valor original.", "Aviso", JOptionPane.WARNING_MESSAGE);
                    novoPreco = produto.getPreco();
                }
            } catch (NumberFormatException e) {
                JOptionPane.showMessageDialog(this, "Formato de preço inválido. Mantendo o preço original.", "Aviso", JOptionPane.WARNING_MESSAGE);
                novoPreco = produto.getPreco();
            }
        }

        String novaCategoria = JOptionPane.showInputDialog(this, "Nova Categoria (atual: " + produto.getCategoria() + ") - Deixe em branco ou cancele para não alterar:", "Atualizar Produto", JOptionPane.PLAIN_MESSAGE);
        String categoriaFinal = produto.getCategoria();
        if (novaCategoria != null && !novaCategoria.trim().isEmpty()) {
            categoriaFinal = novaCategoria;
        }


        boolean precoMudou = novoPreco != null && produto.getPreco() != novoPreco;
        boolean categoriaMudou = !categoriaFinal.equals(produto.getCategoria());

        if(novoPreco == null && (novaCategoria == null || novaCategoria.trim().isEmpty())){
            areaExibicao.setText("Nenhuma alteração solicitada para o produto '" + nome + "'.\n");
            return;
        }


        boolean atualizado = estoque.atualizarProduto(nome, novoPreco, categoriaFinal); //

        if (atualizado) {
            areaExibicao.setText("Produto '" + nome + "' atualizado com sucesso.\n");
            listarTodosProdutosGUI();
        } else {

            areaExibicao.setText("Nenhuma alteração efetiva foi feita no produto '" + nome + "' (os novos valores podem ser iguais aos atuais ou o produto não foi encontrado).\n");
        }
    }

    private void registrarEntradaGUI() {
        String nome = JOptionPane.showInputDialog(this, "Nome do produto para entrada:", "Registrar Entrada", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) return;

        Produto produto = estoque.buscarProdutoPorNome(nome);
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Produto '" + nome + "' não encontrado.", "Erro na Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantidade;
        try {
            String qtdStr = JOptionPane.showInputDialog(this, "Quantidade a adicionar:", "Registrar Entrada", JOptionPane.PLAIN_MESSAGE);
            if (qtdStr == null) return;
            quantidade = Integer.parseInt(qtdStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade para entrada deve ser positiva.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Deve ser um número inteiro positivo.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sucesso = estoque.registrarEntrada(nome, quantidade); //
        if (sucesso) {
            registrarMovimentacao(LocalDate.now(), "Entrada", quantidade, produto); //
            areaExibicao.setText("Entrada de " + quantidade + " unidades de '" + nome + "' registrada.\n");
            areaExibicao.append("Nova quantidade: " + produto.getQuantidade() + "\n");
            listarTodosProdutosGUI();
        } else {
            JOptionPane.showMessageDialog(this, "Falha ao registrar entrada para o produto '" + nome + "'.", "Erro na Entrada", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void registrarSaidaGUI() {
        String nome = JOptionPane.showInputDialog(this, "Nome do produto para saída:", "Registrar Saída", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) return;

        Produto produto = estoque.buscarProdutoPorNome(nome);
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Produto '" + nome + "' não encontrado.", "Erro na Saída", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int quantidade;
        try {
            String qtdStr = JOptionPane.showInputDialog(this, "Quantidade a remover:", "Registrar Saída", JOptionPane.PLAIN_MESSAGE);
            if (qtdStr == null) return;
            quantidade = Integer.parseInt(qtdStr);
            if (quantidade <= 0) {
                JOptionPane.showMessageDialog(this, "Quantidade para saída deve ser positiva.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade inválida. Deve ser um número inteiro positivo.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        boolean sucesso = estoque.registrarSaida(nome, quantidade); //
        if (sucesso) {
            registrarMovimentacao(LocalDate.now(), "Saida", quantidade, produto); //
            areaExibicao.setText("Saída de " + quantidade + " unidades de '" + nome + "' registrada.\n");
            areaExibicao.append("Nova quantidade: " + produto.getQuantidade() + "\n");
            listarTodosProdutosGUI();
        } else {

            JOptionPane.showMessageDialog(this, "Falha ao registrar saída para o produto '" + nome + "'.\nVerifique o console para detalhes (ex: estoque insuficiente).", "Erro na Saída", JOptionPane.ERROR_MESSAGE);
        }
    }

    private void buscarProdutoGUI() {
        String nome = JOptionPane.showInputDialog(this, "Nome do produto a buscar:", "Buscar Produto", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) return;

        Produto produto = estoque.buscarProdutoPorNome(nome); //
        if (produto != null) {
            areaExibicao.setText("--- Produto Encontrado ---\n");
            areaExibicao.append(produto.toString() + "\n");
            areaExibicao.append("--------------------------\n");
        } else {
            areaExibicao.setText("Nenhum produto encontrado com o nome '" + nome + "'.\n");
        }
    }

    private void listarTodosProdutosGUI() {
        List<Produto> produtos = estoque.getProdutos(); //
        areaExibicao.setText("--- Lista de Todos os Produtos ---\n");
        if (produtos.isEmpty()) {
            areaExibicao.append("Nenhum produto cadastrado no estoque.\n");
        } else {
            for (Produto p : produtos) {
                areaExibicao.append(p.toString() + "\n");
            }
        }
        areaExibicao.append("----------------------------------\n");
    }

    private void listarProdutosBaixoEstoqueGUI() {
        int qtdMinima;
        try {
            String qtdMinStr = JOptionPane.showInputDialog(this, "Informe a quantidade mínima para alerta:", "Baixo Estoque", JOptionPane.PLAIN_MESSAGE);
            if (qtdMinStr == null) return;
            qtdMinima = Integer.parseInt(qtdMinStr);
            if (qtdMinima < 0) {
                JOptionPane.showMessageDialog(this, "Quantidade mínima não pode ser negativa.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
        } catch (NumberFormatException ex) {
            JOptionPane.showMessageDialog(this, "Quantidade mínima inválida. Deve ser um número inteiro.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
            return;
        }

        List<Produto> baixos = estoque.listarProdutosComEstoqueBaixo(qtdMinima); //
        areaExibicao.setText("--- Produtos com Estoque Baixo (igual ou inferior a " + qtdMinima + ") ---\n");
        if (baixos.isEmpty()) {
            areaExibicao.append("Nenhum produto com estoque baixo (<= " + qtdMinima + ") encontrado.\n");
        } else {
            for (Produto p : baixos) {
                areaExibicao.append(p.toString() + " - Quantidade Atual: " + p.getQuantidade() + "\n");
            }
        }
        areaExibicao.append("------------------------------------------------------------\n");
    }

    private void excluirProdutoGUI() {
        String nome = JOptionPane.showInputDialog(this, "Nome do produto a excluir:", "Excluir Produto", JOptionPane.PLAIN_MESSAGE);
        if (nome == null || nome.trim().isEmpty()) return;

        Produto produto = estoque.buscarProdutoPorNome(nome);
        if (produto == null) {
            JOptionPane.showMessageDialog(this, "Produto '" + nome + "' não encontrado para exclusão.", "Erro ao Excluir", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int confirm = JOptionPane.showConfirmDialog(this,
                "Tem certeza que deseja excluir o produto '" + nome + "' (ID: " + produto.getId() + ")?",
                "Confirmar Exclusão",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE);

        if (confirm == JOptionPane.YES_OPTION) {
            boolean excluido = estoque.removerProdutoPorNome(nome); //
            if (excluido) {
                areaExibicao.setText("Produto '" + nome + "' excluído com sucesso.\n");

                listarTodosProdutosGUI();
            } else {

                areaExibicao.setText("Produto '" + nome + "' não encontrado para exclusão (pode ter sido removido por outra ação).\n");
            }
        } else {
            areaExibicao.setText("Exclusão do produto '" + nome + "' cancelada.\n");
        }
    }

    private void gerarRelatoriosGUI() {
        String[] opcoesRelatorio = {"1. Produtos em Estoque", "2. Produtos com Baixo Estoque", "3. Histórico de Movimentações"};
        String escolha = (String) JOptionPane.showInputDialog(this,
                "Escolha o relatório para gerar:",
                "Gerar Relatórios",
                JOptionPane.QUESTION_MESSAGE,
                null,
                opcoesRelatorio,
                opcoesRelatorio[0]);

        if (escolha == null) return;

        String mensagemConsole = "Verifique o console para mensagens de sucesso/erro da geração do arquivo.\n";

        if (escolha.startsWith("1.")) {
            relatorioEstoque.gerarRelatorioProdutos(estoque, "relatorio_produtos_gui.txt"); //
            areaExibicao.setText("Solicitado: Relatório de produtos.\nArquivo: 'relatorio_produtos_gui.txt'.\n" + mensagemConsole);
        } else if (escolha.startsWith("2.")) {
            int qtdMinima;
            try {
                String qtdMinStr = JOptionPane.showInputDialog(this, "Informe a quantidade mínima para o relatório de baixo estoque:", "Baixo Estoque - Relatório", JOptionPane.PLAIN_MESSAGE);
                if (qtdMinStr == null) return;
                qtdMinima = Integer.parseInt(qtdMinStr);
                if (qtdMinima < 0) {
                    JOptionPane.showMessageDialog(this, "Quantidade mínima não pode ser negativa.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                    return;
                }
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Quantidade mínima inválida.", "Erro de Entrada", JOptionPane.ERROR_MESSAGE);
                return;
            }
            relatorioEstoque.gerarRelatorioBaixoEstoque(estoque, qtdMinima, "relatorio_baixo_estoque_gui.txt"); //
            areaExibicao.setText("Solicitado: Relatório de baixo estoque (<= " + qtdMinima + ").\nArquivo: 'relatorio_baixo_estoque_gui.txt'.\n" + mensagemConsole);
        } else if (escolha.startsWith("3.")) {
            relatorioEstoque.gerarHistoricoMovimentacoes(movimentacoes, "relatorio_movimentacoes_gui.txt"); //
            areaExibicao.setText("Solicitado: Histórico de movimentações.\nArquivo: 'relatorio_movimentacoes_gui.txt'.\n" + mensagemConsole);
        } else {
            JOptionPane.showMessageDialog(this, "Tipo de relatório inválido.", "Erro", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Método principal para iniciar a aplicação GUI
    public static void main(String[] args) {
        SwingUtilities.invokeLater(new Runnable() {
            @Override
            public void run() {

                try {
                    UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
                } catch (Exception e) {
                    System.err.println("Não foi possível definir o Look and Feel do sistema: " + e);
                }
                new EstoqueGUI().setVisible(true);
            }
        });
    }
}