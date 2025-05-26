package repository;

import java.util.List;
import java.util.Optional;

import model.Produto;

public interface EstoqueRepositoy {
    Produto adicionarProduto(String nome, double preco, int quantidade, String categoria);

    void listarProdutos();

    boolean removerProdutoPorNome(String nome);

    boolean atualizarProduto(String nome, Double novoPreco, String novaCategoria);

    Produto buscarProdutoPorNome(String nome);

    Optional<Produto> buscarProdutoPorId(int id);

    boolean registrarEntrada(String nome, int quantidade);

    boolean registrarSaida(String nome, int quantidade);

    List<Produto> listarProdutosComEstoqueBaixo(int quantidadeMinima);
}
