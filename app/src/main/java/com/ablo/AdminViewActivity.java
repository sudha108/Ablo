package com.ablo;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;
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
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class AdminViewActivity extends AppCompatActivity {

    String url2 = "http://ablo.000webhostapp.com/SetDate.php";
    String url3 = "http://ablo.000webhostapp.com/SetOnlineStatus.php";
    String url11 = "https://ablo.000webhostapp.com/ListRunTime.php";

    int id;
    String name;

    final int isonline = 1;
    final int isonline1 = 0;

    ListView RunTimeList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_view);

        final TextView tvadmin = (TextView) findViewById(R.id.tvadmintitle);

        Intent i = getIntent();
        name = i.getStringExtra("name");
        id = i.getIntExtra("id", 0);

        String message = name + "'s Admin View";
        tvadmin.setText(message);

        SetDate(Integer.toString(id));
        OnlineStatus(Integer.toString(isonline), Integer.toString(id));

        RunTimeList = (ListView) findViewById(R.id.runlist);

        progressBar = (ProgressBar) findViewById(R.id.progressBar4);

        new GetHttpResponse(AdminViewActivity.this).execute();

    }

    @Override
    public void onBackPressed() {
        return;
    }

    public void SetDate(final String id) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String IdHolder = id;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", IdHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url2);

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

                // Toast.makeText(LoginActivity.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(id);
    }

    public void OnlineStatus(final String isonline, final String id) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = isonline;
                String IdHolder = id;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("isonline", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("id", IdHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url3);

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

                // Toast.makeText(LoginActivity.this, "Password Changed Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(isonline, id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.adminviewmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.imanageusers:
                Intent i2 = new Intent(AdminViewActivity.this, ManageUsersActivity.class);
                i2.putExtra("id", id);
                startActivity(i2);
        }
        switch (item.getItemId()){
            case R.id.iaddnewuser:
                Intent i1 = new Intent(AdminViewActivity.this, UserCreateActivity.class);
                i1.putExtra("id", id);
                i1.putExtra("name", name);
                AdminViewActivity.this.startActivity(i1);
        }
        switch (item.getItemId()){
            case R.id.isetrules:
                Intent i3 = new Intent(AdminViewActivity.this, AppListActivity.class);
                startActivity(i3);
        }
        switch (item.getItemId()){
            case R.id.isettimings:
                Intent i5= new Intent(AdminViewActivity.this,SetDayActivity.class);
                i5.putExtra("name", name);
                i5.putExtra("id", id);
                startActivity(i5);
        }
        switch (item.getItemId()){
            case R.id.iprofileadmin:

        }
        switch (item.getItemId()){
            case R.id.optoutadmin:
                OnlineStatus(Integer.toString(isonline1), Integer.toString(id));
                SetDate(Integer.toString(id));
                Intent i4 = new Intent(AdminViewActivity.this, LoginActivity.class);
                startActivity(i4);
                SharedPreferences preferences = getSharedPreferences("userdetails", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = preferences.edit();
                editor.clear();
                editor.commit();
        }
        return super.onOptionsItemSelected(item);
    }

    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;

        String JSonResult;

        List<RunApps> runAppsList;

        public GetHttpResponse(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... arg0) {
            // Passing HTTP URL to HttpServicesClass Class.
            HttpServiceClass httpServicesClass = new HttpServiceClass(url11);
            try {
                httpServicesClass.ExecutePostRequest();

                if (httpServicesClass.getResponseCode() == 200) {

                    JSonResult = httpServicesClass.getResponse();
                    if (JSonResult != null) {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            RunApps runApps;

                            runAppsList = new ArrayList<RunApps>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                runApps = new RunApps();

                                jsonObject = jsonArray.getJSONObject(i);
                                runApps.runappname = jsonObject.getString("name"); ///
                                runApps.runtime = jsonObject.getString("time");
                                runAppsList.add(runApps);

                            }
                        } catch (JSONException e) {
                            // TODO Auto-generated catch block
                            e.printStackTrace();
                        }
                    }
                } else {
                    Toast.makeText(context, httpServicesClass.getErrorMessage(), Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result)

        {
            progressBar.setVisibility(View.GONE);

            RunTimeList.setVisibility(View.VISIBLE);

            ListAdapterClass3 adapter = new ListAdapterClass3(runAppsList, context);

            RunTimeList.setAdapter(adapter);

        }
    }

}
