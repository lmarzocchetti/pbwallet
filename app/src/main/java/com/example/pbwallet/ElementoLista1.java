package com.example.pbwallet;

/**
 * This class defines an object which is used to create
 * the list of all budget
 */
public class ElementoLista1 {
    private String type, subtype, cash, bound, date;

    /**
     * The constructor that initializes each param with the passed param
     * @param type String indicating which type the budget belongs to
     * @param subtype String indicating which subtype the budget belongs to
     * @param cash String indicating how much the budget is spent
     * @param bound String indicating the limit of the budget
     * @param date  String indicating the budget and date
     */
    public ElementoLista1(String type, String subtype, String cash, String bound, String date){
        this.type = type;
        this.subtype = subtype;
        this.cash = cash;
        this.bound = bound;
        this.date = date;
    }

    /**
     * Get of type
     * @return String type
     */
    public String getType() {
        return type;
    }

    /**
     * Get of cash
     * @return String cash
     */
    public String getCash() {
        return cash;
    }

    /**
     * Get of bound
     * @return String bound
     */
    public String getBound() {
        return bound;
    }

    /**
     * Get of subtype
     * @return String subtype
     */
    public String getSubtype() {
        return subtype;
    }

    /**
     * Get of date
     * @return String date
     */
    public String getDate() {
        return date;
    }
}
