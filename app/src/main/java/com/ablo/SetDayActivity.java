package com.ablo;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class SetDayActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener{

    String url7 = "https://ablo.000webhostapp.com/StartDayTime.php";
    int id;
    String name;
    Calendar calendar;
    TimePickerDialog timePickerDialog;
    int CalendarHour, CalendarMinute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_day);

        Intent i = getIntent();
        name= i.getStringExtra("name");
        id = i.getIntExtra("id", 0);

        calendar = Calendar.getInstance();

        Button bstarttime = findViewById(R.id.bstarttime);

        bstarttime.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                callCalendar(calendar);
            }
        });
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute, int second) {


        String SelectedTime = "Selected Time is " + hourOfDay + " : " + minute ;

        Toast.makeText(SetDayActivity.this, SelectedTime, Toast.LENGTH_LONG).show();
        SetStartDayTime(Integer.toString(hourOfDay), Integer.toString(minute), Integer.toString(id));

        Intent intent= new Intent(SetDayActivity.this, SetEndActivity.class);
        intent.putExtra("id",id);
        intent.putExtra("name", name);
        startActivity(intent);

    }

    public void SetStartDayTime(final String hour, final String minute, final String id) {
        class SendPostReqAsyncTask extends AsyncTask<String, Void, String> {
            @Override
            protected String doInBackground(String... params) {

                String NameHolder = id;
                String HourHolder = hour;
                String MinHolder = minute;

                List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>();

                nameValuePairs.add(new BasicNameValuePair("id", NameHolder));
                nameValuePairs.add(new BasicNameValuePair("hour", HourHolder));
                nameValuePairs.add(new BasicNameValuePair("min", MinHolder));

                try {
                    HttpClient httpClient = new DefaultHttpClient();

                    HttpPost httpPost = new HttpPost(url7);

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

        sendPostReqAsyncTask.execute(hour,minute, id);
    }

    public void callCalendar(Calendar c){
        CalendarHour = c.get(Calendar.HOUR_OF_DAY);
        CalendarMinute = c.get(Calendar.MINUTE);

        timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(SetDayActivity.this, CalendarHour, CalendarMinute, true);

        timePickerDialog.setThemeDark(false);

        timePickerDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {

            @Override
            public void onCancel(DialogInterface dialogInterface) {

                Toast.makeText(SetDayActivity.this, "Time Not Selected", Toast.LENGTH_SHORT).show();
            }
        });
        timePickerDialog.show(getFragmentManager(), "Material Design TimePicker Dialog");
    }
}
