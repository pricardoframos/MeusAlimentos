package com.example.projetoalimentostcc.ui.classes;

public class ProdutoXLista {
    private int idRegistro;
    private int idProduto;
    private double quantidadeProduto;
    private double precoProduto;
    private int idLista;
    private int status;

    public ProdutoXLista(int idRegistro, int idProduto, double quantidadeProduto, double precoProduto, int idLista, int status) {
        this.idRegistro = idRegistro;
        this.idProduto = idProduto;
        this.quantidadeProduto = quantidadeProduto;
        this.precoProduto = precoProduto;
        this.idLista = idLista;
        this.status = status;
    }

    public int getIdRegistro() {
        return idRegistro;
    }

    public void setIdRegistro(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public int getIdProduto() {
        return idProduto;
    }

    public void setIdProduto(int idProduto) {
        this.idProduto = idProduto;
    }

    public double getQuantidadeProduto() {
        return quantidadeProduto;
    }

    public void setQuantidadeProduto(double quantidadeProduto) {
        this.quantidadeProduto = quantidadeProduto;
    }

    public double getPrecoProduto() {
        return precoProduto;
    }

    public void setPrecoProduto(double precoProduto) {
        this.precoProduto = precoProduto;
    }

    public int getIdLista() {
        return idLista;
    }

    public void setIdLista(int idLista) {
        this.idLista = idLista;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}