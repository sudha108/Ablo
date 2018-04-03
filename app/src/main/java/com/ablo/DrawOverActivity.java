package com.ablo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class DrawOverActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_draw_over);
    }

    @Override
    public void onBackPressed(){
        Intent intent= new Intent(this,BlockService.class);
        stopService(intent);
        
        super.onBackPressed();
    }
}
