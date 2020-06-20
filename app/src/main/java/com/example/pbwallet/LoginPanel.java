package com.example.pbwallet;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;


public class LoginPanel extends Activity{

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loginpanel);

        Button enter = findViewById(R.id.login_button);

        enter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent homepage = new Intent(LoginPanel.this ,LoginPanel.class);
                startActivity(homepage);
                finish();
            }
        });
    }
}
