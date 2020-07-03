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


    public String getCash() {
        return cash;
    }


    public String getSubtype() {
        return subtype;
    }

}
