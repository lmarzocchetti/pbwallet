package com.example.pbwallet;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        prova();
    }

    public void prova() {
        System.out.println("prova");
    }

    public void ciao(int miao){
        System.out.println(miao);
    }
}