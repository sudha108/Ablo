package com.ablo;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.wdullaer.materialdatetimepicker.time.RadialPickerLayout;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

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
import java.util.Calendar;
import java.util.List;

public class AppListActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    ListView AppListView;
    ProgressBar progressBar;

    String url5 = "https://ablo.000webhostapp.com/GetAppList.php";
    String url6 = "https://ablo.000webhostapp.com/SetLimit.php";

    String name;
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    int CalendarHour, CalendarMinute;

    List<String> NameList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_list);

        AppListView = (ListView) findViewById(R.id.applist);
        progressBar = (ProgressBar) findViewById(R.id.progressBar1);

        calendar = Calendar.getInstance();

        new GetHttpResponse(AppListActivity.this).execute();

        //Adding ListView Item click Listener.
        AppListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // TODO Auto-generated method stub

                name = NameList.get(position).toString();
                CalendarHour = calendar.get(Calendar.HOUR_OF_DAY);
                CalendarMinute = calendar.get(Calendar.MINUTE);

                timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(AppListActivity.this, CalendarHour, CalendarMinute, true);

                timePickerDialog.setThemeDark(false);

                timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

                    @Override
                    public void onCancel(DialogInterface dialogInterface) {

                        Toast.makeText(AppListActivity.this, "Time Not Selected", Toast.LENGTH_SHORT).show();
                    }
                });
                timePickerDialog.show(getFragmentManager(), "Material Design TimePicker Dialog");

            }
        });
    }

    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;

        String JSonResult;

        List<Applications> applist;

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
            HttpServiceClass httpServicesClass = new HttpServiceClass(url5);
            try {
                httpServicesClass.ExecutePostRequest();

                if (httpServicesClass.getResponseCode() == 200) {
                    JSonResult = httpServicesClass.getResponse();

                    if (JSonResult != null) {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            Applications applications;

                            applist = new ArrayList<Applications>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                applications = new Applications();

                                jsonObject = jsonArray.getJSONObject(i);

                                // Adding Student Id TO IdList Array.
                                NameList.add(jsonObject.getString("name"));

                                //Adding Student Name.
                                applications.appname = jsonObject.getString("name"); ///
                                applications.limittime = jsonObject.getString("limittime"); ///
                                applist.add(applications);

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

            AppListView.setVisibility(View.VISIBLE);

            ListAdapterClass2 adapter = new ListAdapterClass2(applist, context);

            AppListView.setAdapter(adapter);

        }
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {

        int Hour = 0;

        if (hourOfDay > 12) {
            switch (hourOfDay) {
                case 13:
                    Hour = 1;
                    break;

                case 14:
                    Hour = 2;
                    break;

                case 15:
                    Hour = 3;
                    break;

                case 16:
                    Hour = 4;
                    break;

                case 17:
                    Hour = 5;
                    break;

                case 18:
                    Hour = 6;
                    break;

                case 19:
                    Hour = 7;
                    break;

                case 20:
                    Hour = 8;
                    break;

                case 21:
                    Hour = 9;
                    break;

                case 22:
                    Hour = 10;
                    break;

                case 23:
                    Hour = 11;
                    break;

                case 24:
                    Hour = 12;
                    break;
            }

        } else {

            Hour = hourOfDay;
        }
        String SelectedTime = "Selected Time is " + Hour + " : " + minute;

        Toast.makeText(AppListActivity.this, SelectedTime, Toast.LENGTH_LONG).show();

        SetLimitTime(Integer.toString(Hour), Integer.toString(minute), name);

        Intent i= new Intent(this,AppListActivity.class);
        startActivity(i);

    }

    public void SetLimitTime(final String hour, final String minute, final String name) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = name;
                String HourHolder = hour;
                String MinHolder = minute;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("name", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("hour", HourHolder));
                nameValuePairs.add(new BasicNameValuePair("min", MinHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url6);

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

        sendPostReqAsyncTask.execute(hour, minute, name);
    }

}

