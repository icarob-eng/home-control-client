package com.example.tests.model;


import com.example.tests.activities.MainActivity;

import java.io.Serializable;

public class RelayModel implements Serializable {
    private Long id;
    private String nome;

    public RelayModel() {}
    public RelayModel(Long id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void turnRelay(int position) {
        MainActivity.getSocketClient().actionRelay(position);
    }
}
