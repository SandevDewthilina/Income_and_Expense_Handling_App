package com.example.incomeandexpensehandlingapp;

import java.util.Date;

public class Transactions {

    private String amount, desc, type;
    private Date time_stamp;

    public Transactions() {}

    public Transactions(String amount, String desc, String type, Date time_stamp) {
        this.amount = amount;
        this.desc = desc;
        this.type = type;
        this.time_stamp = time_stamp;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Date getTime_stamp() {
        return time_stamp;
    }

    public void setTime_stamp(Date time_stamp) {
        this.time_stamp = time_stamp;
    }
}
