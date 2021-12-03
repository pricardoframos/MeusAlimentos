package com.example.projetoalimentostcc.ui.classes;

public class Despensa {
    //Declaração das variáveis relacionadas aos atributos da despensa
    private int id;
    private String descricao;

    public Despensa(int id, String descricao) {
        this.id = id;
        this.descricao = descricao;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }
}