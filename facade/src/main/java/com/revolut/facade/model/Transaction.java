package com.revolut.facade.model;

/**
 * Created by azhuravel on 24.05.16.
 */
public class Transaction {
    private long fromAccountId;

    private long toAccountId;

    private long sum;

    public Transaction() {
    }

    public Transaction(long fromAccountId, long toAccountId, long sum) {
        this.fromAccountId = fromAccountId;
        this.toAccountId = toAccountId;
        this.sum = sum;
    }

    public long getFromAccountId() {
        return fromAccountId;
    }

    public void setFromAccountId(long fromAccountId) {
        this.fromAccountId = fromAccountId;
    }

    public long getToAccountId() {
        return toAccountId;
    }

    public void setToAccountId(long toAccountId) {
        this.toAccountId = toAccountId;
    }

    public long getSum() {
        return sum;
    }

    public void setSum(long sum) {
        this.sum = sum;
    }
}
