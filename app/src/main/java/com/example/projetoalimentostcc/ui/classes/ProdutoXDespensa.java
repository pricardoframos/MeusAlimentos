package com.example.projetoalimentostcc.ui.classes;

public class ProdutoXDespensa {
    private int idRegistro;
    private int idProduto;
    private double estoqueProduto;
    private String validProduto;
    private int idDespensa;

    public ProdutoXDespensa() {
    }

    public ProdutoXDespensa(int idRegistro) {
        this.idRegistro = idRegistro;
    }

    public ProdutoXDespensa(int idRegistro, int idProduto, double estoqueProduto, String validProduto, int idDespensa) {
        this.idRegistro = idRegistro;
        this.idProduto = idProduto;
        this.estoqueProduto = estoqueProduto;
        this.validProduto = validProduto;
        this.idDespensa = idDespensa;
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

    public double getEstoqueProduto() {
        return estoqueProduto;
    }

    public void setEstoqueProduto(double estoqueProduto) {
        this.estoqueProduto = estoqueProduto;
    }

    public String getValidProduto() {
        return validProduto;
    }

    public void setValidProduto(String validProduto) {
        this.validProduto = validProduto;
    }

    public int getIdDespensa() {
        return idDespensa;
    }

    public void setIdDespensa(int idDespensa) {
        this.idDespensa = idDespensa;
    }
}