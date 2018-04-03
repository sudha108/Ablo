package com.ablo;

import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ChangePasswordActivity extends AppCompatActivity {

    String url9 = "http://ablo.000webhostapp.com/ChangePassword.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_password);

        final EditText etchangepassword= (EditText)findViewById(R.id.etchangepassword);
        final Button baddpassword =(Button) findViewById(R.id.bchange);
        final Button bgoback3= (Button) findViewById(R.id.bgoback3);

        Intent intent= getIntent();
        final int id= intent.getIntExtra("id",0);
        final String name = intent.getStringExtra("name");

        baddpassword.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                final String password = etchangepassword.getText().toString();
                UpdatePassword(password,Integer.toString(id));
                Intent intent= new Intent(ChangePasswordActivity.this,UserViewActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });

        bgoback3.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent= new Intent(ChangePasswordActivity.this,UserViewActivity.class);
                intent.putExtra("id",id);
                intent.putExtra("name",name);
                startActivity(intent);
            }
        });
    }

    public void UpdatePassword(final String password, final String id) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = password;
                String IdHolder = id;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("password", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("id", IdHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url9);

                    httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

                    HttpResponse httpResponse = httpClient.execute(httpPost);

                    HttpEntity httpEntity = httpResponse.getEntity();


                } catch (ClientProtocolException e) {

                } catch (IOException e) {

                }
                return "Data Inserted Successfully";
            }

            @Override
            protected void onPostExecute(String result) {

                super.onPostExecute(result);

                Toast.makeText(ChangePasswordActivity.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(password,id);
    }

}
