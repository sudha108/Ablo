package com.ablo;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class ProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        int id = intent.getIntExtra("id", 0);

        Intent i = new Intent(ProfileActivity.this, ChangePasswordActivity.class);
        i.putExtra("id", id);
        i.putExtra("name",name);
        startActivity(i);
    }
}
