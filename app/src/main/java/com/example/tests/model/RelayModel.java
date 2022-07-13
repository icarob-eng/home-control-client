package com.example.tests.model;

import com.example.tests.MainActivity;

public class RelayModel {
    private int id;
    private String nome;

    public RelayModel(int id, String nome) {
        this.id = id;
        this.nome = nome;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
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
