package com.example.pbwallet;

public class ElementoLista {
    private String datecard, cash, subtype;
    private int margintop;

    public ElementoLista(String datecard, String cash, String subtype, int margintop){
        this.datecard = datecard;
        this.cash = cash;
        this.subtype = subtype;
        this.margintop = margintop;
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

    public int getMargintop() {
        return margintop;
    }

    public void setMargintop(int margintop) {
        this.margintop = margintop;
    }
}
