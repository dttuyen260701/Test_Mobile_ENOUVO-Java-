package com.test.testmobileeunovo.Asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.google.gson.JsonObject;
import com.test.testmobileeunovo.Listeners.LoadMatrixListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.Utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class LoadMatrixAsynctask extends AsyncTask<String, String, Boolean> {
    private ArrayList<Matrix> list_result;
    private LoadMatrixListener listener;

    public LoadMatrixAsynctask(LoadMatrixListener listener) {
        this.listener = listener;
        this.list_result = new ArrayList<>();
    }

    @Override
    protected void onPreExecute() {
        listener.onPre();
        super.onPreExecute();
    }

    @Override
    protected Boolean doInBackground(String... strings) {
        try {
            JSONObject data = Methods.getInstance().make_Request(
                    strings[0],
                    "GET",
                    null);
            JSONArray jsonArray = data.getJSONArray("data");
            for(int i = 0; i < jsonArray.length(); ++i){
                JSONObject object = jsonArray.getJSONObject(i);
                ArrayList<Feature_Approval> list_Approval = new ArrayList<>();
                JSONArray jsonArray_approval = object.getJSONArray("approvals");
                for(int j = 0; j < jsonArray_approval.length(); ++j){
                    JSONObject object_approval = jsonArray_approval.getJSONObject(j);
                    list_Approval.add(new Feature_Approval(object_approval.getInt("id"), object_approval.getString("name")));
                }
                Matrix matrix = new Matrix(
                        object.getInt("id"),
                        object.getString("alias"),
                        object.getInt("min_Range"),
                        object.getInt("max_Range"),
                        object.getInt("feature_id"),
                        list_Approval
                );
                list_result.add(matrix);
            }
        } catch (Exception e){
            Log.e("ERR", "doInBackground: " + e );
            return false;
        }
        return true;
    }

    @Override
    protected void onPostExecute(Boolean aBoolean) {
        super.onPostExecute(aBoolean);
        listener.onEnd(list_result, aBoolean);
    }
}
