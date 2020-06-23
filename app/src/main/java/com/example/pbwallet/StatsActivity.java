package com.example.pbwallet;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class StatsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.stats);

        BottomNavigationView navbar = findViewById(R.id.nav_bar);
        navbar.setSelectedItemId(R.id.nav_stats);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;
                    switch(item.getItemId()) {
                        case R.id.nav_home:
                            startActivity(new Intent(getApplicationContext(),HomeActivity.class));
                            overridePendingTransition(0,0);
                            break;

                        case R.id.nav_fund:
                            startActivity(new Intent(getApplicationContext(),FundsActivity.class));
                            overridePendingTransition(0,0);
                            break;

                        case R.id.nav_stats:
                            break;
                    }
                    return true;
                }
            };
}
