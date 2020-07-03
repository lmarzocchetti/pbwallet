package com.example.pbwallet;

import java.util.HashMap;

public class AccessImage {
    private HashMap<String, Integer> accimg = new HashMap<>();

    public AccessImage() {

    }

    public int get(String s) {
        return accimg.get(s).intValue();
    }
}
