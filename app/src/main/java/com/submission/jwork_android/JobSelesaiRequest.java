package com.submission.jwork_android;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class JobSelesaiRequest extends StringRequest {
    private static final String URL = "http://10.0.2.2:8080/invoice/invoiceStatus/";
    private Map<String, String> params;


    public JobSelesaiRequest(String id, Response.Listener<String> listener){
        super(Method.PUT, URL+id, listener, null);
        params = new HashMap<>();
        params.put("id", id);
        params.put("status", "Finished");
    }

    @Override
    public Map<String, String> getParams() throws AuthFailureError {
        return params;
    }
}
