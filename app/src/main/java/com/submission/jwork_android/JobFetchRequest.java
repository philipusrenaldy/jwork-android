package com.submission.jwork_android;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class JobFetchRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8080/invoice/Jobseeker/";
    private Map<String, String> params;

    public JobFetchRequest(String jobseekerid, Response.Listener<String> listener){
        super(Method.GET, URL+jobseekerid, listener, null);
        params = new HashMap<>();
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
