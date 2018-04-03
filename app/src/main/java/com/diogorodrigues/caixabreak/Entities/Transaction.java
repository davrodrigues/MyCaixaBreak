package com.diogorodrigues.caixabreak.Entities;

import java.time.LocalDate;
import java.util.Date;

/**
 * Created by Diogo Rodrigues on 24/03/2018.
 */

public class Transaction {
    private Date lData;
    private Date lDataValor;
    private String description;
    private String debit; //subtractions
    private String credit; //additions

    public Transaction() {
    }

    public Transaction(Date lData, Date dataValor, String description) {
        this.lData = lData;
        this.lDataValor = dataValor;
        this.description = description;
    }

    public Date getlData() {
        return lData;
    }

    public void setlData(Date lData) {
        this.lData = lData;
    }

    public Date getlDataValor() {
        return lDataValor;
    }

    public void setlDataValor(Date lDataValor) {
        this.lDataValor = lDataValor;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    @Override
    public String toString() {
        return "Transaction{" +
                "lData=" + lData +
                ", lDataValor=" + lDataValor +
                ", description='" + description + '\'' +
                ", debit=" + debit +
                ", credit=" + credit +
                '}';
    }

}
