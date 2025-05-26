package model;

public class Produto {
    // atributos
    private int id;
    private String nome;
    private double preco;
    private int quantidade;
    private String categoria;

    public Produto(int id, double preco,int quantidade, String categoria, String nome) {
        this.id = id;
        this.quantidade = quantidade;
        this.preco = preco;
        this.categoria = categoria;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    @Override
    public String toString() {
        return "Produto{" +
                "id=" + id +
                ", nome='" + nome + '\'' +
                ", preco=" + preco +
                ", quantidade=" + quantidade +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
