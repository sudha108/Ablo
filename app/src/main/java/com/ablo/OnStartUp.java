package com.ablo;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.os.AsyncTask;
import android.util.Log;
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

/**
 * Created by sudhams on 20/02/18.
 */

public class OnStartUp extends BroadcastReceiver {

    String url = "https://ablo.000webhostapp.com/SendAppList.php";

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Intent.ACTION_BOOT_COMPLETED)) {
            Intent i = new Intent(context, StartActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
        }

        String act = intent.getAction();
        Log.e("ee", "new install" + intent);
        if (Intent.ACTION_PACKAGE_ADDED.equals(act) || Intent.ACTION_PACKAGE_REMOVED.equals(act)) {
            /* ContentResolver contentResolver = context.getContentResolver();
            contentResolver.query()*/
        }

        SharedPreferences userDetails = context.getSharedPreferences("userdetails", context.MODE_PRIVATE);
        int id = userDetails.getInt("id", 0);

        String[] a=intent.getData().toString().split(":");
        String packageName=a[a.length-1];

        List<PackageInfo> packageInfoList = context.getPackageManager().getInstalledPackages(0);
        for (int i = 0; i < packageInfoList.size(); i++) {
            PackageInfo packageInfo = packageInfoList.get(i);
            if(packageInfo.packageName.equals(packageName)){
                String appName = packageInfo.applicationInfo.loadLabel(context.getPackageManager()).toString();
                Toast.makeText(context, "Installed new app "+appName, Toast.LENGTH_SHORT).show();
                InsertData(appName,Integer.toString(id));
            }
        }

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
}

