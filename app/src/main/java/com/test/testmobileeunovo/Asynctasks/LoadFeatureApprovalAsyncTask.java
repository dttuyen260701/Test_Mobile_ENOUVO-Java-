package com.test.testmobileeunovo.Asynctasks;

import android.os.AsyncTask;
import android.util.Log;

import com.test.testmobileeunovo.Listeners.LoadFeatureApprovalListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.Utils.Methods;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

public class LoadFeatureApprovalAsyncTask extends AsyncTask<String, String, Boolean> {

    private ArrayList<Feature_Approval> list_result;
    private LoadFeatureApprovalListener listener;

    public LoadFeatureApprovalAsyncTask(LoadFeatureApprovalListener listener) {
        this.listener = listener;
        this.list_result = new ArrayList<>();
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
                    "GET",
                    null);
            JSONArray jsonArray = data.getJSONArray("data");
            for(int i = 0; i < jsonArray.length(); ++i){
                JSONObject object = jsonArray.getJSONObject(i);
                Feature_Approval feature_approval = new Feature_Approval(
                        object.getInt("id"),
                        object.getString("name")
                );
                list_result.add(feature_approval);
            }
        } catch (Exception e){
            Log.e("ERR_", "doInBackground: " + e );
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
