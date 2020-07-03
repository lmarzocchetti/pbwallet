package com.example.pbwallet;

import java.util.HashMap;

public class AccessImage {
    private HashMap<String, Integer> accimg = new HashMap<>();

    public AccessImage() {
        accimg.put("Home", R.drawable.ic_baseline_work_24);
        accimg.put("Work", R.drawable.ic_baseline_home_24);
        accimg.put("Shopping", R.drawable.ic_baseline_shopping_cart_24);
        accimg.put("Hobby", R.drawable.ic_baseline_videogame_asset_24);
        accimg.put("Other", R.drawable.ic_baseline_weekend_24);
    }

    public int get(String s) {
        return accimg.get(s).intValue();
    }
}
