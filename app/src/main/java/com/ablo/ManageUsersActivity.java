package com.ablo;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ManageUsersActivity extends AppCompatActivity {

    ListView UserListView;
    ProgressBar progressBar;
    String url4 = "https://ablo.000webhostapp.com/UserList.php"; ///

    int adminid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_users);


        Intent i = getIntent();
        adminid = i.getIntExtra("id", 0); ///

        UserListView = (ListView) findViewById(R.id.adminuserlist);

        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        new GetHttpResponse(ManageUsersActivity.this).execute();


    }

    // JSON parse class started from here.
    private class GetHttpResponse extends AsyncTask<Void, Void, Void> {
        public Context context;

        String JSonResult;

        List<User> userlist;

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
            HttpServiceClass httpServicesClass = new HttpServiceClass(url4);
            try {
                httpServicesClass.ExecutePostRequest();

                if (httpServicesClass.getResponseCode() == 200) {

                    JSonResult = httpServicesClass.getResponse();
                    if (JSonResult != null) {
                        JSONArray jsonArray = null;

                        try {
                            jsonArray = new JSONArray(JSonResult);

                            JSONObject jsonObject;

                            User user;

                            userlist = new ArrayList<User>();

                            for (int i = 0; i < jsonArray.length(); i++) {
                                user = new User();


                                jsonObject = jsonArray.getJSONObject(i);
                                user.usersName = jsonObject.getString("name"); ///
                                user.isonline = jsonObject.getString("isonline");
                                userlist.add(user);

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

            UserListView.setVisibility(View.VISIBLE);

            ListAdapterClass adapter = new ListAdapterClass(userlist, context);

            UserListView.setAdapter(adapter);

        }
    }
}
