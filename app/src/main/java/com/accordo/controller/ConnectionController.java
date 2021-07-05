package com.accordo.controller;

import android.content.Context;
import android.util.Log;

import com.android.volley.NetworkError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import androidx.appcompat.app.AlertDialog;

import static com.accordo.data.AccordoValues.CTITLE;
import static com.accordo.data.AccordoValues.NAME;
import static com.accordo.data.AccordoValues.PICTURE;
import static com.accordo.data.AccordoValues.PID;
import static com.accordo.data.AccordoValues.SID;
import static com.accordo.data.AccordoValues.UID;

public class ConnectionController {

    private final RequestQueue requestQueue;

    public ConnectionController(Context context) { this.requestQueue = Volley.newRequestQueue(context); }

    public void addChannel(String sid, String cTitle, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
            jsonBody.put(CTITLE,cTitle);
        } catch (JSONException e) { e.printStackTrace(); }
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/addChannel.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener));
    }

    public void getChannel(String sid, String cTitle, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
            jsonBody.put(CTITLE,cTitle);
        } catch (JSONException e) { e.printStackTrace(); }
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getChannel.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener));
    }

    public void getPostImage(String sid, String pid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
            jsonBody.put(PID,pid);
        } catch (JSONException e) { e.printStackTrace(); }
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getPostImage.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener));
    }

    public void getProfile(String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try { jsonBody.put(SID,sid); }
        catch (JSONException e) { e.printStackTrace(); }
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getProfile.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener));
    }

    public void getUserPicture(String sid, String uid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
            jsonBody.put(UID,uid);
        } catch (JSONException e) { e.printStackTrace(); }
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getUserPicture.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener));
    }

    public void getWall(String sid, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try { jsonBody.put(SID,sid); }
        catch (JSONException e) { e.printStackTrace(); }
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/getWall.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener));
    }

    public void handleVolleyError(VolleyError error, Context context, String tag) {
        String message = null;
        if (error instanceof NetworkError) {
            message = "Cannot connect to Internet...Please check your connection!";
        } else if (error instanceof ServerError) {
            message = "The server could not be found. Please try again after some time!!";
        } else if (error instanceof ParseError) {
            message = "Parsing error! Please try again after some time!!";
        } else if (error instanceof TimeoutError) {
            message = "Connection TimeOut! Please check your internet connection.";
        }
        Log.e(tag, error.toString());
        if(message != null){
            AlertDialog.Builder builder = new AlertDialog.Builder(context);
            builder.setMessage(message)
                    .setTitle("ERROR")
                    .setNegativeButton("Ok", (dialog, id) -> {})
                    .create()
                    .show();
        }
    }

    public void register(Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/register.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, new JSONObject(), responseListener, errorListener));
    }

    public void setProfileName(String sid, String name, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
            jsonBody.put(NAME,name);
        } catch (JSONException e) { e.printStackTrace(); }
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/setProfile.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener));
    }

    public void setProfilePicture(String sid, String picture, Response.Listener<JSONObject> responseListener, Response.ErrorListener errorListener) {
        final JSONObject jsonBody = new JSONObject();
        try {
            jsonBody.put(SID,sid);
            jsonBody.put(PICTURE,picture);
        } catch (JSONException e) { e.printStackTrace(); }
        final String url = "https://ewserver.di.unimi.it/mobicomp/accordo/setProfile.php";
        requestQueue.add(new JsonObjectRequest(Request.Method.POST, url, jsonBody, responseListener, errorListener));
    }
}
