package com.diogorodrigues.caixabreak;

/**
 * Created by Diogo Rodrigues on 22/03/2018.
 */

public class Cartao {
    String nome;
    String user = "6272293";
    String username ="PPP"+user;
    String pwd = "14528";

    public Cartao() {
    }

    public Cartao(String nome, String user, String pwd) {
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


}