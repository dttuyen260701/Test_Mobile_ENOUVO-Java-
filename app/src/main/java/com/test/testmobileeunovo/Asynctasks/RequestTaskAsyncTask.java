package com.test.testmobileeunovo.Asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.test.testmobileeunovo.Listeners.RequestTaskListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.Utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class RequestTaskAsyncTask extends AsyncTask<String, String, Boolean> {
    private boolean value;
    private RequestTaskListener listener;
    private JSONObject jsonParams;
    private boolean for_add_matrix;
    private int next_id = -1;

    public RequestTaskAsyncTask(JSONObject jsonParams, boolean for_add_matrix, RequestTaskListener listener) {
        this.listener = listener;
        this.jsonParams = jsonParams;
        this.for_add_matrix = for_add_matrix;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        listener.onPre();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            JSONObject data = Methods.getInstance().make_Request(
                    strings[0],
                    strings[1],
                    jsonParams);
            JSONObject jsonObject = data.getJSONObject("data");
            if(for_add_matrix){
                next_id = jsonObject.getInt("value");
                value = (next_id != -1);
            } else {
                value = jsonObject.getBoolean("value");
            }
        } catch (Exception e){
            Log.e("ERR", "doInBackground: " + e );
            value = false;
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onEnd(value, aBoolean, next_id);
    }
}
