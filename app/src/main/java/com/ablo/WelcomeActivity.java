package com.ablo;

import android.annotation.SuppressLint;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Binder;
import android.os.Build;
import android.os.Build.VERSION;
import android.os.Build.VERSION_CODES;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;

public class WelcomeActivity extends AppCompatActivity {

    public final static int REQUEST_CODE = 99;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        final Button bstart = (Button) findViewById(R.id.bstart);

        bstart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                    if (VERSION.SDK_INT >= VERSION_CODES.M) {
                        if (!Settings.canDrawOverlays(WelcomeActivity.this)) {
                            Toast.makeText(WelcomeActivity.this, "Enable the setting to continue! If already enabled, close and reopen!", Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                                    Uri.parse("package:" + getPackageName()));
                            startActivityForResult(intent, REQUEST_CODE);

                        } else {

                            SharedPreferences userDetails = getApplicationContext().getSharedPreferences("userdetails", MODE_PRIVATE);
                            int id = userDetails.getInt("id", 0);
                            int isadmin = userDetails.getInt("isadmin", 0);
                            String name = userDetails.getString("name", "");
                            if (id == 0) {
                                startActivity(new Intent(WelcomeActivity.this, LoginActivity.class));
                            } else if (isadmin == 1) {
                                Intent intent = new Intent(WelcomeActivity.this, AdminViewActivity.class);
                                intent.putExtra("name", name);
                                intent.putExtra("id", id);
                                startActivity(intent);
                            } else if (isadmin == 0) {
                                Intent intent2 = new Intent(WelcomeActivity.this, UserViewActivity.class);
                                intent2.putExtra("name", name);
                                intent2.putExtra("id", id);
                                startActivity(intent2);
                            }
                        }
                    }
                    }


        });

    }

    public String getAndroidVersion() {
        String release = Build.VERSION.RELEASE;
        int sdkVersion = Build.VERSION.SDK_INT;
        return "Android SDK: " + sdkVersion + " (" + release +")";
    }
}
