package com.ablo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sudhams on 20/02/18.
 */

public class UserCreateRequest   extends StringRequest {
    private static  final String CREATE_USER_REQUEST_URL = "http://ablo.000webhostapp.com/UserCreate.php";
    private Map<String,String> params;

    public UserCreateRequest(String name, String username, String mailid, String adminid, Response.Listener<String> listener){
        super(Method.POST, CREATE_USER_REQUEST_URL, listener, null );
        params= new HashMap<>();
        params.put("name",name);
        params.put("username",username);
        params.put("mailid",mailid);
        params.put("adminid",adminid);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}
