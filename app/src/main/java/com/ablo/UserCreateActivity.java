package com.ablo;

import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

public class UserCreateActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_create);

        Intent i = getIntent();
        final String name = i.getStringExtra("name");
        final int id= i.getIntExtra("id",0);

        final EditText etcname = (EditText) findViewById(R.id.etcname);
        final EditText etcusername = (EditText) findViewById(R.id.etcusername);
        final EditText etcmail = (EditText) findViewById(R.id.etcmail);
        final Button bgoback= (Button) findViewById(R.id.bgoback) ;
        final Button bsubmit= (Button) findViewById(R.id.bsubmit);

        bsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String name = etcname.getText().toString();
                final String username = etcusername.getText().toString();
                final String mailid = etcmail.getText().toString();
                final String adminid = Integer.toString(id);
                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try{
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success=jsonResponse.getBoolean("success");
                            if(success){
                                String mailid = jsonResponse.getString("mailid");
                                String password = jsonResponse.getString("password");
                                String username = jsonResponse.getString("username");
                                Intent intent= new Intent(UserCreateActivity.this, ManageUsersActivity.class);
                                UserCreateActivity.this.startActivity(intent);
                                if(true) {
                                    SendMail s = new SendMail(username, password, mailid);
                                    Toast.makeText(UserCreateActivity.this, "User Information Mail sent to user Successfully", Toast.LENGTH_LONG).show();
                                }

                            }
                            else{
                                AlertDialog.Builder builder= new AlertDialog.Builder(UserCreateActivity.this);
                                builder.setMessage("Creation Failed").setNegativeButton("Retry",null).create().show();

                            }
                        }catch(JSONException e){
                            e.printStackTrace();
                        }

                    }
                };
                UserCreateRequest regreq = new UserCreateRequest(name, username, mailid, adminid, responseListener);
                RequestQueue queue= Volley.newRequestQueue(UserCreateActivity.this);
                queue.add(regreq);
            }
        });

        bgoback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(UserCreateActivity.this, AdminViewActivity.class);
                i.putExtra("name",name);
                i.putExtra("id",id);
                UserCreateActivity.this.startActivity(i);
            }
        });;
    }

}

