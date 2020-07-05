package com.example.pbwallet;

/**
 * This class defines an object which is used to create
 * the list of all transactions
 */
public class ElementoLista {
    private String datecard, cash, subtype;

    /**
     * The constructor that initializes each param with the passed param
     * @param datecard String indicating when the transaction occurred
     * @param cash String indicating how much the transaction is
     * @param subtype String indicating wich subtype the transaction belongs to
     */
    public ElementoLista(String datecard, String cash, String subtype){
        this.datecard = datecard;
        this.cash = cash;
        this.subtype = subtype;
    }

    /**
     * Get of datecard
     * @return String datecard
     */
    public String getDatecard() {
        return datecard;
    }

    /**
     * Get of cash
     * @return String cash
     */
    public String getCash() {
        return cash;
    }

    /**
     * Get of subtype
     * @return String dsubtype
     */
    public String getSubtype() {
        return subtype;
    }

}
