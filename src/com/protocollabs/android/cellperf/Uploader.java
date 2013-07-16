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


public class Uploader {

    private static final String url = "http://foo.appspot.com";


    public static void ping(String url) {

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
        request.setHeader("Accept", "application/json");

        String currentDateTimeString = new SimpleDateFormat("HH:mm:ss").format(new Date());

        JSONObject obj = new JSONObject();
        try {
            obj.put("reg_id","Registration ID sent to the server"); 
            obj.put("datetime",currentDateTimeString);
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

        try {
            String out = EntityUtils.toString(response.getEntity());
        } catch (java.io.IOException e) {
            e.printStackTrace();
            return;
        }

        return;
    }

}
