package com.example.nccumis;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {
    private static final String LOGIN_REQUEST_URL = "https://nccugo105306.000webhostapp.com/Login.php";
    private Map<String, String> params;
//
    public LoginRequest(String member_id, String member_password, Response.Listener<String> listener) {
        super(Method.POST, LOGIN_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("member_id", member_id);
        params.put("member_password", member_password);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }
}