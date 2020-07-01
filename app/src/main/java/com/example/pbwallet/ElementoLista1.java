package com.example.pbwallet;

public class ElementoLista1 {
    private String type, subtype, cash, bound;

    public ElementoLista1(String type, String subtype, String cash, String bound){
        this.type = type;
        this.subtype = subtype;
        this.cash = cash;
        this.bound = bound;
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
}
