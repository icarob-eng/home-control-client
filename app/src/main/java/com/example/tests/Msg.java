package com.example.tests;

public enum Msg {
    DISCONNECT("!d"),
    LED("!l"),
    RELAY("!r:"),
    PING("!p"),
    ADD_NW("!addnw"),
    /**
     * Sintaxe de adição de rede: `!addnw-Nomedarede-senhadarede`
     */
    DEL_NW("!delnw");
    /**
     * Sintaxe re remoção de rede: `!delnw-Nomedarede`
     */

    final String txt;

    Msg (String txt) {
        this.txt = txt;
    }
}
