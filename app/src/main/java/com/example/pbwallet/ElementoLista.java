package com.example.pbwallet;

public class ElementoLista {
    private String datecard, cash, subtype;

    public ElementoLista(String datecard, String cash, String subtype){
        this.datecard = datecard;
        this.cash = cash;
        this.subtype = subtype;
    }

    public String getDatecard() {
        return datecard;
    }

    public void setDatecard(String datecard) {
        this.datecard = datecard;
    }

    public String getCash() {
        return cash;
    }

    public void setCash(String cash) {
        this.cash = cash;
    }

    public String getSubtype() {
        return subtype;
    }

    public void setSubtype(String subtype) {
        this.subtype = subtype;
    }
}
