package com.diogorodrigues.caixabreak.Entities;

import java.util.List;

/**
 * Created by Diogo Rodrigues on 24/03/2018.
 */

public class Movement {
    private List<Transaction> transactions;
    private String totalDebit;
    private String totalCredit;

    public Movement() {
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public String getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(String totalDebit) {
        this.totalDebit = totalDebit;
    }

    public String getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(String totalCredit) {
        this.totalCredit = totalCredit;
    }
}
