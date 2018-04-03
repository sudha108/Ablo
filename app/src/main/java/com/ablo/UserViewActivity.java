package com.ablo;

import android.app.ActivityManager;
import android.app.AppOpsManager;
import android.app.usage.UsageStats;
import android.app.usage.UsageStatsManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.provider.Settings;
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
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class UserViewActivity extends AppCompatActivity {

    String userid;
    String url3 = "http://ablo.000webhostapp.com/SetOnlineStatus.php";
    String url2 = "https://ablo.000webhostapp.com/SetDate.php";
    String url11 = "https://ablo.000webhostapp.com/ListRunTime.php";
    String url12= "https://ablo.000webhostapp.com/GetRunTime.php";

    final int isonline = 1;
    final int isonline1 = 0;
    String name;
    int id;

    ListView RunTimeList;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_view);

        if (!isAccessGranted()) {
            Intent intent = new Intent(Settings.ACTION_USAGE_ACCESS_SETTINGS);
            startActivity(intent);
        }

        final TextView tvuserView = findViewById(R.id.tvUserViewtitle);

        Intent intent = getIntent();
        name = intent.getStringExtra("name");
        id = intent.getIntExtra("id", 0);

        userid= Integer.toString(id);
        String message = name + "'s User View";
        tvuserView.setText(message);

        SetDate(Integer.toString(id));
        OnlineStatus(Integer.toString(isonline), Integer.toString(id));
        myMethod();

        RunTimeList = (ListView) findViewById(R.id.runlist);

        progressBar = (ProgressBar) findViewById(R.id.progressBar4);

        new GetHttpResponse(UserViewActivity.this).execute();


    }

    @Override
    public void onBackPressed() {
        return;
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
            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(isonline, id);
    }

    public void SetDate( final String id) {

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

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.userviewmenu,menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        switch (item.getItemId()){
            case R.id.iprofileuser:

                Intent i = new Intent(UserViewActivity.this, ProfileActivity.class);
                i.putExtra("id", id);
                i.putExtra("name",name);
                startActivity(i);
        }
        switch (item.getItemId()){
            case R.id.optoutuser:
                OnlineStatus(Integer.toString(isonline1), Integer.toString(id));
                SetDate(Integer.toString(id));
                Intent i1 = new Intent(UserViewActivity.this, LoginActivity.class);
                startActivity(i1);
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
                                runApps.runappname = jsonObject.getString("name");
                                runApps.runtime = jsonObject.getString("runningtime");
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

    public void myMethod() {
//        List<String> test = new ArrayList<>();
        PackageManager pm = this.getPackageManager();
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            UsageStatsManager usm = (UsageStatsManager) this.getSystemService(Context.USAGE_STATS_SERVICE);
            long time = System.currentTimeMillis();
            List<UsageStats> appList = usm.queryUsageStats(UsageStatsManager.INTERVAL_DAILY, time - 1000 * 1000, time);
            if (appList != null && appList.size() > 0) {
                SortedMap<Long, UsageStats> mySortedMap = new TreeMap<Long, UsageStats>();
                for (UsageStats usageStats : appList) {
                    mySortedMap.put(usageStats.getLastTimeUsed(), usageStats);
                }
                if (mySortedMap != null && !mySortedMap.isEmpty()) {
                    for (Map.Entry<Long, UsageStats> e : mySortedMap.entrySet()) {
                        try {
                            String s = e.getValue().getPackageName();
                            Long s1 = e.getValue().getTotalTimeInForeground();
                            String appname = (pm.getApplicationLabel(pm.getApplicationInfo(s, PackageManager.GET_META_DATA))).toString();
//                            test.add(appname);
                            InsertData(appname,s1.toString(),userid);
                        } catch (Exception e1) {
                        }
                    }
                }
            }

        } else {
            ActivityManager am = (ActivityManager) this.getSystemService(Context.ACTIVITY_SERVICE);
            List<ActivityManager.RunningAppProcessInfo> tasks = am.getRunningAppProcesses();
            for (ActivityManager.RunningAppProcessInfo task : tasks) {
//                test.add(task.processName);
            }
        }
//        return test;
    }

    private boolean isAccessGranted() {
        try {
            PackageManager packageManager = getPackageManager();
            ApplicationInfo applicationInfo = packageManager.getApplicationInfo(getPackageName(), 0);
            AppOpsManager appOpsManager = (AppOpsManager) getSystemService(Context.APP_OPS_SERVICE);
            int mode = 0;
            if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.KITKAT) {
                mode = appOpsManager.checkOpNoThrow(AppOpsManager.OPSTR_GET_USAGE_STATS,
                        applicationInfo.uid, applicationInfo.packageName);
            }
            return (mode == AppOpsManager.MODE_ALLOWED);

        } catch (PackageManager.NameNotFoundException e) {
            return false;
        }
    }


    public void InsertData(final String name, final String time, final String userid) {

        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name;
                String IdHolder = userid;
                String TimeHolder = time;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("userid", IdHolder));
                nameValuePairs.add(new BasicNameValuePair("time", TimeHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url12);

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

                Toast.makeText(UserViewActivity.this, "Data Submit Successfully", Toast.LENGTH_LONG).show();

            }
        }

        SendPostReqAsyncTask sendPostReqAsyncTask = new SendPostReqAsyncTask();

        sendPostReqAsyncTask.execute(name, time, userid);
    }

}



