package com.diogorodrigues.caixabreak.Entities;

/**
 * Created by Diogo Rodrigues on 22/03/2018.
 */

public class Card {
    private String nome ="generico";
    private String user = "6272293";
    private String username ="PPP"+user;
    private String pwd = "14528";
    private String balance;
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
    public void setUsername(String username) {
        this.username = username;
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