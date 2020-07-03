package com.example.pbwallet;

public class ElementoLista1 {
    private String type, subtype, cash, bound, date;

    public ElementoLista1(String type, String subtype, String cash, String bound, String date){
        this.type = type;
        this.subtype = subtype;
        this.cash = cash;
        this.bound = bound;
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getBound() {
        return bound;
    }

    public void setBound(String bound) {
        this.bound = bound;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
