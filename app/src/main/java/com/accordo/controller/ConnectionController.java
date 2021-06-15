package com.accordo.controller;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ConnectionController {

    private final RequestQueue requestQueue;
    private final String SID = "sid";
    private final String CTITLE = "ctitle";


    public ConnectionController(Context context) { this.requestQueue = Volley.newRequestQueue(context); }

    public void getChannel(String sid, String cTitle, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
            jsonBody.put(CTITLE,cTitle);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getChannel.php";

        JsonObjectRequest listRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                responseListener, errorListener
        );
        requestQueue.add(listRequest);
    }

    public void getProfile(String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getProfile.php";

        JsonObjectRequest listRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                responseListener, errorListener
        );
        requestQueue.add(listRequest);
    }

    public void getWall(String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {

        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getWall.php";

        JsonObjectRequest listRequest = new JsonObjectRequest(
                Request.Method.POST, url, jsonBody,
                responseListener, errorListener
        );
        requestQueue.add(listRequest);
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