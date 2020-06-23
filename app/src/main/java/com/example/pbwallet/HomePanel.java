package com.example.pbwallet;

import android.database.Cursor;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class HomePanel extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_homepanel);

        if(savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.nav_frameLayout, new HomeFragment()).commit();
        }

        BottomNavigationView navbar = findViewById(R.id.nav_bar);
        navbar.setOnNavigationItemSelectedListener(navigationlistener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navigationlistener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selected = null;
                    switch(item.getItemId()) {
                        case R.id.nav_home:
                            selected = new HomeFragment();
                            break;

                        case R.id.nav_fund:
                            selected = new FundsFragment();
                            break;

                        case R.id.nav_stats:
                            selected = new StatsFragment();
                            break;
                    }

                    assert selected != null;
                    getSupportFragmentManager().beginTransaction().replace(R.id.nav_frameLayout, selected).commit();
                    return true;
                }
            };

}
