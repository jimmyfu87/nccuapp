package com.example.nccumis;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class PasswordCheckEmailRequest extends StringRequest {
    private  static String REGISTER_REQUEST_URL="https://nccugo105306087.000webhostapp.com/forgetpassword.php";
    private Map<String,String> params;

    public PasswordCheckEmailRequest(String member_email, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("member_email", member_email);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}