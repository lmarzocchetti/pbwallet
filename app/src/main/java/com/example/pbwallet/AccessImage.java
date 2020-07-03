package com.example.pbwallet;

import java.util.HashMap;

public class AccessImage {
    private HashMap<String, Integer> accimg = new HashMap<>();

    public AccessImage() {
        accimg.put("Lavoro", R.drawable.ic_baseline_work_24);
        accimg.put("Casa", R.drawable.ic_baseline_home_24);
        accimg.put("Hobby", R.drawable.ic_baseline_directions_bike_24);
    }

    public int get(String s) {
        return accimg.get(s).intValue();
    }
}
