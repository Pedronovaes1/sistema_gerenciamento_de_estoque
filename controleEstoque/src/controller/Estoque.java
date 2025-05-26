package controller;

import model.Produto; 
import repository.EstoqueRepositoy;

import java.util.ArrayList;
import java.util.Iterator; 
import java.util.List;
import java.util.Optional; 
public class Estoque implements EstoqueRepositoy{
    private List<Produto> produtos;
    private int proximoId;

    public Estoque() {
        this.produtos = new ArrayList<>();
        this.proximoId = 1;
    }

    public List<Produto> getProdutos() {
        return new ArrayList<>(produtos);
    }

    @Override
    public Produto adicionarProduto(String nome, double preco, int quantidade, String categoria) {

        if (nome == null || nome.trim().isEmpty() || preco < 0 || quantidade < 0) {
            System.err.println("Erro: Dados inválidos para adicionar produto.");
            return null;
        }
        if (buscarProdutoPorNome(nome) != null) {
            System.err.println("Erro: Já existe um produto com o nome '" + nome + "'.");
            return null;
        }

        Produto newProduto = new Produto(proximoId, preco, quantidade, categoria, nome);
        produtos.add(newProduto);
        proximoId++;
        return newProduto;
    }

    @Override
    public void listarProdutos(){
        if(produtos.isEmpty()){
            System.out.println("Não há produtos cadastrados no estoque.");
        } else {
            System.out.println("-------------------- Lista de Produtos --------------------");
            for (Produto produto: produtos){
                System.out.println(produto);
            }
            System.out.println("---------------------------------------------------------");
        }
    }

    @Override
    public boolean removerProdutoPorNome(String nome){
        Iterator<Produto> iterator = produtos.iterator();
        while (iterator.hasNext()) {
            Produto produto = iterator.next();
            if (produto.getNome().equalsIgnoreCase(nome)) {
                iterator.remove();
                return true;
            }
        }
        return false;
    }


    @Override
    public boolean atualizarProduto(String nome, Double novoPreco, String novaCategoria) {
        Produto produto = buscarProdutoPorNome(nome); // Reutiliza a busca
        if (produto != null) {
            boolean alterado = false;
            if (novoPreco != null && novoPreco >= 0) {
                if(produto.getPreco() != novoPreco){
                    produto.setPreco(novoPreco);
                    alterado = true;
                }
            } else if (novoPreco != null && novoPreco < 0) {
                System.err.println("Aviso: Tentativa de definir preço negativo ignorada para o produto '" + nome + "'.");
            }


            if (novaCategoria != null && !novaCategoria.trim().isEmpty()) {
                if(!produto.getCategoria().equals(novaCategoria)){
                    produto.setCategoria(novaCategoria);
                    alterado = true;
                }
            }
            return alterado;
        }
        return false;
    }

    @Override
    public Produto buscarProdutoPorNome(String nome) {
        if (nome == null || nome.trim().isEmpty()){
            return null;
        }
        for (Produto produto : produtos) {
            if (produto.getNome().equalsIgnoreCase(nome)) {
                return produto;
            }
        }
        return null;
    }

    @Override
    public Optional<Produto> buscarProdutoPorId(int id) {
        for (Produto produto : produtos) {
            if (produto.getId() == id) {
                return Optional.of(produto);
            }
        }
        return Optional.empty();
    }


    @Override
    public boolean registrarEntrada(String nome, int quantidade) {
        if (quantidade <= 0) {
            System.err.println("Erro: Quantidade para entrada deve ser positiva.");
            return false;
        }
        Produto produto = buscarProdutoPorNome(nome);
        if (produto != null) {
            produto.setQuantidade(produto.getQuantidade() + quantidade);
            return true;
        } else {
            System.err.println("Erro: Produto '" + nome + "' não encontrado para registrar entrada.");
            return false;
        }
    }

    @Override
    public boolean registrarSaida(String nome, int quantidade) {
        if (quantidade <= 0) {
            System.err.println("Erro: Quantidade para saída deve ser positiva.");
            return false;
        }
        Produto produto = buscarProdutoPorNome(nome);
        if (produto != null) {
            if (produto.getQuantidade() >= quantidade) {
                produto.setQuantidade(produto.getQuantidade() - quantidade);
                return true;
            } else {
                System.err.println("Erro: Estoque insuficiente para o produto '" + nome + "'. Quantidade atual: " + produto.getQuantidade() + ", Saída solicitada: " + quantidade);
                return false;
            }
        } else {
            System.err.println("Erro: Produto '" + nome + "' não encontrado para registrar saída.");
            return false;
        }
    }

    @Override
    public List<Produto> listarProdutosComEstoqueBaixo(int quantidadeMinima) {
        List<Produto> produtosBaixoEstoque = new ArrayList<>();
        for (Produto produto : produtos) {
            if (produto.getQuantidade() <= quantidadeMinima) {
                produtosBaixoEstoque.add(produto);
            }
        }
        return produtosBaixoEstoque;
    }
}