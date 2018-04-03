package com.ablo;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final EditText etname = (EditText) findViewById(R.id.etname);
        final EditText etuname = (EditText) findViewById(R.id.etuname);
        final EditText etpass = (EditText) findViewById(R.id.etpass);
        final EditText etmail = (EditText) findViewById(R.id.etmail);


        final Button breg2 = (Button) findViewById(R.id.breg2);

        breg2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etname.getText().toString();
                final String username = etuname.getText().toString();
                final String password = etpass.getText().toString();
                final String mailid = etmail.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");

                            if(success){
                                Intent intent= new Intent(RegisterActivity.this, LoginActivity.class);
                                RegisterActivity.this.startActivity(intent);
                            }
                            else{
                                AlertDialog.Builder builder= new AlertDialog.Builder(RegisterActivity.this);
                                builder.setMessage("Registration Failed").setNegativeButton("Retry",null).create().show();

                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                };
                RegisterRequest regreq = new RegisterRequest(name, username, password,mailid, responseListener);
                RequestQueue queue= Volley.newRequestQueue(RegisterActivity.this);
                queue.add(regreq);
                finish();
            }
        });
    }
}
