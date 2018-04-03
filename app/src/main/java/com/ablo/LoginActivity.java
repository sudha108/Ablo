package com.ablo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.SharedPreferences.*;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends AppCompatActivity {

    private PendingIntent pendingIntent;
    private AlarmManager manager;
    String url = "https://ablo.000webhostapp.com/SendAppList.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        final Button bregister = (Button) findViewById(R.id.bregister);
        final Button blogin = (Button) findViewById(R.id.blogin);
        final EditText etusername = (EditText) findViewById(R.id.etusername);
        final EditText etpassword = (EditText) findViewById(R.id.etpassword);

        bregister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this, RegisterActivity.class);
                LoginActivity.this.startActivity(i);
            }
        });

        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                final String username = etusername.getText().toString();
                final String password = etpassword.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonResponse = new JSONObject(response);
                            boolean success = jsonResponse.getBoolean("success");

                            if (success) {

//                                startAlarm(v);

                                String name = jsonResponse.getString("name");
                                int isadmin = jsonResponse.getInt("isadmin");
                                int id = jsonResponse.getInt("id");

                                SharedPreferences userDetails = getApplicationContext().getSharedPreferences("userdetails", MODE_PRIVATE);
                                Editor edit = userDetails.edit();
                                edit.clear();
                                edit.putInt("id", id);
                                edit.putInt("isadmin", isadmin);
                                edit.putString("name", name);
                                edit.commit();

                                PackageManager manager = getPackageManager();
                                Intent intent1 = new Intent(Intent.ACTION_MAIN, null);
                                intent1.addCategory(Intent.CATEGORY_LAUNCHER);

                                List<ResolveInfo> resolveInfos = manager.queryIntentActivities(intent1, 0);
                                for (ResolveInfo info : resolveInfos) {

                                    //  for (int i = 0; i < packs.size(); i++) {
                                    // PackageInfo p = packs.get(i);
                                    ApplicationInfo all_App = info.activityInfo.applicationInfo;

                                    //    if ((all_App.flags & ApplicationInfo.FLAG_SYSTEM) == 0) {
                                    String appName = all_App.loadLabel(manager).toString();
                                    InsertData(appName, Integer.toString(id));
                                    //   }
                                }

                                Intent intent;
                                if (isadmin == 1) {
                                    intent = new Intent(LoginActivity.this, AdminViewActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("id", id);
                                    LoginActivity.this.startActivity(intent);

                                } else {
                                    intent = new Intent(LoginActivity.this, UserViewActivity.class);
                                    intent.putExtra("name", name);
                                    intent.putExtra("id", id);
                                    LoginActivity.this.startActivity(intent);
                                }

                            } else {
                                AlertDialog.Builder builder = new AlertDialog.Builder(LoginActivity.this);
                                builder.setMessage("User Does Not Exist").setNegativeButton("Retry", null).create().show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(username, password, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });

//        Intent alarmIntent = new Intent(this, AlarmReceiver.class);
//        pendingIntent = PendingIntent.getBroadcast(this, 0, alarmIntent, PendingIntent.FLAG_UPDATE_CURRENT);


    }
    public void startAlarm(View view) {
        manager = (AlarmManager)getSystemService(Context.ALARM_SERVICE);
        int interval = 2500; // 10 seconds

        manager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), interval, pendingIntent);
        //alarm type, when to first trigger, intervals, action to perform

    }

    public void InsertData(final String name, final String id) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name;
                String IdHolder = id;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("userid", IdHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url);

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

                //   Toast.makeText(LoginActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.loginmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.msettings:
                Intent intent = new Intent();
                intent.setAction(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                Uri uri = Uri.fromParts("package", getPackageName(), null);
                intent.setData(uri);
                startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }


}