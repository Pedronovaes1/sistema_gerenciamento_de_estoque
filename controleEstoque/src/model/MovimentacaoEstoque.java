package model;

import java.time.LocalDate;

public class MovimentacaoEstoque {
    private LocalDate data;
    private String tipo;
    private int quantidade;
    private Produto produto;

    public MovimentacaoEstoque(LocalDate data, String tipo, int quantidade, Produto produto) {
        this.data = data;
        this.tipo = tipo;
        this.quantidade = quantidade;
        this.produto = produto;
    }


    public LocalDate getData() {
        return data;
    }

    public String getTipo() {
        return tipo;
    }

    public int getQuantidade() {
        return quantidade;
    }

    public Produto getProduto() {
        return produto;
    }

    public void setData(LocalDate data) {
        this.data = data;
    }

    public void setTipo(String tipo) {
        this.tipo = tipo;
    }

    public void setQuantidade(int quantidade) {
        this.quantidade = quantidade;
    }

    public void setProduto(Produto produto) {
        this.produto = produto;
    }

    @Override
    public String toString() {

        return "MovimentacaoEstoque{" +
                "data=" + data +
                ", tipo='" + tipo + '\'' +
                ", quantidade=" + quantidade + //
                ", produto=" + (produto != null ? produto.getNome() : "N/A") +
                '}';
    }
}