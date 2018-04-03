package com.diogorodrigues.caixabreak.Entities;

import java.io.Serializable;

/**
 * Created by Diogo Rodrigues on 22/03/2018.
 */

public class Card implements Serializable {
    private static final long serialVersionUID = 666;
    private String nome ="generico";
    private String user = "6272293";
    private String username;
    private String pwd = "14528";
    private String balance = "0,00 €";
    private Movement movement;


    public Card(String nome) {
        this.nome = nome;
    }

    public Card() {
    }

    public Card(String nome, String user, String pwd) {
        this.nome = nome;
        this.user = user;
        this.pwd = pwd;
    }

    public String getNome() {
        return nome;
    }
    public void setNome(String nome) {
        this.nome = nome;
    }
    public String getUser() {
        return user;
    }
    public void setUser(String user) {
        this.user = user;
    }
    public String getPwd() {
        return pwd;
    }
    public void setPwd(String pwd) {
        this.pwd = pwd;
    }
    public String getUsername() {
        return username;
    }
    public void setUsername() {
        this.username = "PPP"+user;
    }
    public String getBalance() {
        return balance;
    }
    public void setBalance(String balance) {
        this.balance = balance;
    }

    public Movement getMovement() {
        return movement;
    }

    public void setMovement(Movement movement) {
        this.movement = movement;
    }
}