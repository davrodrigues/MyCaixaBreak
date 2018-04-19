package com.diogorodrigues.caixabreak.Entities;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Diogo Rodrigues on 24/03/2018.
 */

public class Movement implements Serializable {
    private static final long serialVersionUID = 123;
    private List<Transaction> transactions;
    private String totalDebit = "";
    private String totalCredit = "";

    public Movement() {
    }

    public List<Transaction> getTransactions() {
        return (transactions!=null)?transactions:new ArrayList<Transaction>();
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
