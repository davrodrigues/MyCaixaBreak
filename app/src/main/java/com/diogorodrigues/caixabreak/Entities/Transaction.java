package com.diogorodrigues.caixabreak.Entities;

import java.time.LocalDate;

/**
 * Created by Diogo Rodrigues on 24/03/2018.
 */

public class Transaction {
    private LocalDate lData;
    private LocalDate dataValor;
    private String description;
    private Double debit; //subtractions
    private Double credit; //additions

    public Transaction() {
    }

    public Transaction(LocalDate lData, LocalDate dataValor, String description) {
        this.lData = lData;
        this.dataValor = dataValor;
        this.description = description;
    }

    public LocalDate getlData() {
        return lData;
    }

    public void setlData(LocalDate lData) {
        this.lData = lData;
    }

    public LocalDate getDataValor() {
        return dataValor;
    }

    public void setDataValor(LocalDate dataValor) {
        this.dataValor = dataValor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Double getDebit() {
        return debit;
    }

    public void setDebit(Double debit) {
        this.debit = debit;
    }

    public Double getCredit() {
        return credit;
    }

    public void setCredit(Double credit) {
        this.credit = credit;
    }

}
