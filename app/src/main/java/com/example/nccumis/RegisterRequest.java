package com.example.nccumis;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;


import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private  static String REGISTER_REQUEST_URL="https://nccugo105306.000webhostapp.com/Register.php";
    private Map<String,String> params;

    public RegisterRequest(String member_name,String member_id,String member_phone,String member_email,String member_password,String member_personal_id,String member_birthday, Response.Listener<String> listener){
        super(Method.POST, REGISTER_REQUEST_URL, listener, null);
        params = new HashMap<>();
        params.put("member_name", member_name);
        params.put("member_id", member_id);
        params.put("member_phone",  member_phone);
        params.put("member_email", member_email);
        params.put("member_password", member_password);
        params.put("member_personal_id", member_personal_id);
        params.put("member_birthday", member_birthday);
    }

    @Override
    public Map<String, String> getParams() {
        return params;
    }


}
