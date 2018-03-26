package com.diogorodrigues.caixabreak.Entities;

import java.util.List;

/**
 * Created by Diogo Rodrigues on 24/03/2018.
 */

public class Movement {
    private List<Transaction> transactions;
    private Double totalDebit;
    private Double totalCredit;

    public Movement() {
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public Double getTotalDebit() {
        return totalDebit;
    }

    public void setTotalDebit(Double totalDebit) {
        this.totalDebit = totalDebit;
    }

    public Double getTotalCredit() {
        return totalCredit;
    }

    public void setTotalCredit(Double totalCredit) {
        this.totalCredit = totalCredit;
    }
}
