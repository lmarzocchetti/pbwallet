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


    public String getCash() {
        return cash;
    }


    public String getBound() {
        return bound;
    }


    public String getSubtype() {
        return subtype;
    }


    public String getDate() {
        return date;
    }
}
