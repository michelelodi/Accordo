package com.example.accordo.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

public class ConnectionController {

    final private Context context;
    final private RequestQueue requestQueue;

    public ConnectionController(Context context) {
        this.context = context;
        this.requestQueue = Volley.newRequestQueue(context);
    }

    public void register(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/register.php";

        JsonObjectRequest listRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                responseListener, errorListener
        );
        requestQueue.add(listRequest);
    }


}
