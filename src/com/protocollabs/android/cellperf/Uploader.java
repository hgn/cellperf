package com.protocollabs.android.cellperf;

import java.util.Locale;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


import android.os.Bundle;


public class Pinger {

    public static String ping(String url) {

        URI website = new URI("http://foo.appspot.com");

        HttpClient client = new DefaultHttpClient();
        HttpPost request = new HttpPost(website);

        request.setHeader("Content-Type", "application/json");

        String currentDateTimeString = DateFormat.getDateTimeInstance().format(new Date());

        JSONObject obj = new JSONObject();
        obj.put("reg_id","Registration ID sent to the server"); 
        obj.put("datetime",currentDateTimeString);

        StringEntity se = new StringEntity(obj.toString());
        request.setEntity(se);
        HttpResponse response = client.execute(request);

        String out = EntityUtils.toString(response.getEntity());
    }
}
