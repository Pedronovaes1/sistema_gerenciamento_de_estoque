package model;

import controller.Estoque; // Estoque está no pacote controller

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class RelatorioEstoque {


    public RelatorioEstoque() {
    }


    public void gerarRelatorioProdutos(Estoque estoque, String nomeArquivo) {
        try (PrintWriter relatorio = new PrintWriter(new FileWriter(nomeArquivo))) {
            relatorio.println("=== Relatório de Produtos em Estoque ===");
            if (estoque.getProdutos().isEmpty()) {
                relatorio.println("Nenhum produto cadastrado no estoque.");
            } else {
                for (Produto produto : estoque.getProdutos()) {
                    relatorio.println(produto.toString());
                }
            }
            System.out.println("Relatório de produtos gerado com sucesso em: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório de produtos: " + e.getMessage());
        }
    }


    public void gerarRelatorioBaixoEstoque(Estoque estoque, int quantidadeMinima, String nomeArquivo) {
        try (PrintWriter relatorio = new PrintWriter(new FileWriter(nomeArquivo))) {
            relatorio.println("=== Relatório de Produtos com Estoque Baixo (Abaixo de " + quantidadeMinima + " unidades) ===");
            List<Produto> produtosBaixoEstoque = estoque.listarProdutosComEstoqueBaixo(quantidadeMinima);

            if (produtosBaixoEstoque.isEmpty()) {
                relatorio.println("Nenhum produto com estoque baixo encontrado.");
            } else {
                for (Produto produto : produtosBaixoEstoque) {
                    relatorio.println(produto.toString() + " - Quantidade Atual: " + produto.getQuantidade());
                }
            }
            System.out.println("Relatório de baixo estoque gerado com sucesso em: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório de baixo estoque: " + e.getMessage());
        }
    }

    public void gerarHistoricoMovimentacoes(List<MovimentacaoEstoque> movimentacoes, String nomeArquivo) {
        try (PrintWriter relatorio = new PrintWriter(new FileWriter(nomeArquivo))) {
            relatorio.println("=== Histórico de Movimentações de Estoque ===");
            if (movimentacoes == null || movimentacoes.isEmpty()) {
                relatorio.println("Nenhuma movimentação registrada.");
            } else {
                for (MovimentacaoEstoque mov : movimentacoes) {
                    relatorio.println(mov.toString());
                }
            }
            System.out.println("Relatório de histórico de movimentações gerado com sucesso em: " + nomeArquivo);
        } catch (IOException e) {
            System.err.println("Erro ao gerar relatório de histórico de movimentações: " + e.getMessage());
        }
    }
}