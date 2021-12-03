package com.example.projetoalimentostcc.ui.classes;

public class Produto {
    //Atributos
    private int id;
    private String codigo;  //Código de barras do produto
    private String descricao;
    private String categoria;
    private String unidade;
    private String urlDaImagem;
    private String validade;
    private double quantidade;
    private String localArmaz;
    private double preco;
    private String lista;

    public Produto(){

    }

    public Produto(String descricao, String categoria) {
        this.descricao = descricao;
        this.categoria = categoria;
    }

    //Construtor para CasaFragment
    public Produto(String descricao, String urlDaImagem, String validade, double quantidade,
                   String localArmaz) {
        this.descricao = descricao;
        this.urlDaImagem = urlDaImagem;
        this.validade = validade;
        this.quantidade = quantidade;
        this.localArmaz = localArmaz;
    }

    //Construtor para tabela PRODUTOXDESPENSA
    public Produto(int id, String validade, double quantidade, String localArmaz) {
        this.id = id;
        this.validade = validade;
        this.quantidade = quantidade;
        this.localArmaz = localArmaz;
    }

    //Construtor para tabela PRODUTO
    public Produto(int id, String codigo, String descricao, String categoria, String unidade,
                   String urlDaImagem) {
        this.id = id;
        this.codigo = codigo;
        this.descricao = descricao;
        this.categoria = categoria;
        this.unidade = unidade;
        this.urlDaImagem = urlDaImagem;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCodigo() {
        return codigo;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getUrlDaImagem() {
        return urlDaImagem;
    }

    public void setUrlDaImagem(String urlDaImagem) {
        this.urlDaImagem = urlDaImagem;
    }

    public String getValidade() {
        return validade;
    }

    public void setValidade(String validade) {
        this.validade = validade;
    }

    public double getQuantidade() {
        return quantidade;
    }

    public void setQuantidade(double quantidade) {
        this.quantidade = quantidade;
    }

    public String getLocalArmaz() {
        return localArmaz;
    }

    public void setLocalArmaz(String localArmaz) {
        this.localArmaz = localArmaz;
    }

    public double getPreco() {
        return preco;
    }

    public void setPreco(double preco) {
        this.preco = preco;
    }

    public String getLista() {
        return lista;
    }

    public void setLista(String lista) {
        this.lista = lista;
    }
}