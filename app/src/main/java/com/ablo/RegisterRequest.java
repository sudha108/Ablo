package com.ablo;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by sudhams on 20/02/18.
 */

public class RegisterRequest extends StringRequest {

    private static  final String REGISTER_REQUEST_URL = "http://ablo.000webhostapp.com/AdminReg.php";
    private Map<String,String> params;

    public RegisterRequest(String name, String username,String password, String mailid,  Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null );
        params= new HashMap<>();
        params.put("name",name);
        params.put("username",username);
        params.put("mailid",mailid);
        params.put("password",password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}

