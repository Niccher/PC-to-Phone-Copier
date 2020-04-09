package com.niccher.pctophonecopier;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class Spla extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_spla);

        Handler hdl=new Handler();
        hdl.postDelayed(new Runnable() {
            @Override
            public void run() {
                startActivity(new Intent(Spla.this,MainActivity.class));
                finish();
            }
        }, 2500);

    }
}
