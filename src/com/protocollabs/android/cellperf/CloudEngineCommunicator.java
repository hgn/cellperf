package com.protocollabs.android.cellperf;


import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.apache.http.client.ResponseHandler;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.client.HttpResponseException;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;

import java.net.URI;

import java.text.SimpleDateFormat;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import android.os.Bundle;


public class CloudEngineCommunicator {

    private static final String url = "http://foo.appspot.com";
    private ArrayList<MeasurementItem> measurementItems;


    public CloudEngineCommunicator(MeasurementItem... items) {
        measurementItems = new ArrayList<MeasurementItem>();
        for (MeasurementItem r : items) {
            measurementItems.add(r);
        }
    }


    private String currentDate() {
        SimpleDateFormat sdf =  new SimpleDateFormat("yyyy-MM-dd HH:mm:ssZ");
        sdf.setTimeZone(TimeZone.getDefault());
        return sdf.format(new Date());
    }


    private void handleResponse(HttpResponse response) {
        BasicResponseHandler responseHandler = new BasicResponseHandler();
        String strResponse = null;
        if (response != null) {
            try {
                strResponse = responseHandler.handleResponse(response);
            } catch (HttpResponseException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        try {
            String out = EntityUtils.toString(response.getEntity());
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return;
        }

    }


    public void execute() {

        URI website;

        try {
            website = new URI(url);
        } catch (Exception e) {
            e.printStackTrace();
            return;
        }

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(website);

        request.setHeader("Content-Type", "application/json");
        request.setHeader("Accept",       "application/json");

        JSONObject obj = new JSONObject();
        try {
            obj.put("reg_id",   "Registration ID sent to the server"); 
            obj.put("datetime", currentDate());
        } catch (JSONException e) {
            e.printStackTrace();
            return;
        }

        StringEntity se;
        try {
            se = new StringEntity(obj.toString());
        } catch (java.io.UnsupportedEncodingException e) {
            e.printStackTrace();
            return;
        }

        request.setEntity(se);
        HttpResponse response;
        try {
            response = client.execute(request);
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return;
        }

        handleResponse(response);

        return;
    }

}
