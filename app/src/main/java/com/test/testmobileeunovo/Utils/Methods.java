package com.test.testmobileeunovo.Utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

public class Methods {
    private static Methods Instance;

    private Methods(){

    }

    public static Methods getInstance(){
        if(Instance == null)
            Instance = new Methods();
        return Instance;
    }


    public boolean isNetworkConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null && cm.getActiveNetworkInfo().isConnected();
    }




//    JSONObject jsonParam = new JSONObject();
//    jsonParam.put("AuditScheduleDetailID", param1);
//    jsonParam.put("AuditAnswerId", param2);
//    jsonParam.put("LocalFindingID", param3);
//    jsonParam.put("LocalMediaID", param4);
//    jsonParam.put("Files", param5);
//    jsonParam.put("ExtFiles", param6);
    public JSONObject make_Request(String Url, String method, JSONObject js_params) throws IOException, JSONException {
        JSONObject data = null;
        URL url = new URL(Url);
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod(method);
        urlConnection.setRequestProperty("Content-Type", "application/json; charset=utf-8");
        urlConnection.setRequestProperty("Accept", "application/json");
        if(!method.equals("GET")){
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            DataOutputStream os = new DataOutputStream(urlConnection.getOutputStream());
//            os.writeBytes(URLEncoder.encode(js_params.toString(), "UTF-8"));
            os.writeBytes(js_params.toString());
        }
        try {
            InputStream in = new BufferedInputStream(urlConnection.getInputStream());
            BufferedReader bin = new BufferedReader(new InputStreamReader(in));
            data = new JSONObject(bin.readLine());
        } catch (JSONException e) {
            Log.e("E", "make_Request: " + e);
        } finally {
            urlConnection.disconnect();
        }
        return data;
    }
}
