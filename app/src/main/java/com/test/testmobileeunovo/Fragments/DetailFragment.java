package com.test.testmobileeunovo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.test.testmobileeunovo.Activities.MainActivity;
import com.test.testmobileeunovo.Adapters.ChosenItemAdapter;
import com.test.testmobileeunovo.Adapters.ListChoiceItemAdapter;
import com.test.testmobileeunovo.Asynctasks.LoadFeatureApprovalAsyncTask;
import com.test.testmobileeunovo.Asynctasks.RequestTaskAsyncTask;
import com.test.testmobileeunovo.Listeners.ForDetailListener;
import com.test.testmobileeunovo.Listeners.ItemChoiceListener;
import com.test.testmobileeunovo.Listeners.ItemChosenListener;
import com.test.testmobileeunovo.Listeners.LoadFeatureApprovalListener;
import com.test.testmobileeunovo.Listeners.OnChoiceFragHide;
import com.test.testmobileeunovo.Listeners.RequestTaskListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.Utils.Methods;
import com.test.testmobileeunovo.databinding.FragmentDetailBinding;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DetailFragment extends Fragment {
    private FragmentDetailBinding binding;
    private Matrix matrix;
    private ForDetailListener forDetailListener;
    private ArrayList<Feature_Approval> list_feature, list_feature_chosen, list_approval, list_approval_chosen;
    private ChosenItemAdapter feature_chosen_adapter, approval_chosen_adapter;
    private int feature_id_chosen = -1;
    private ChoiceFragment choiceFragment;

    public DetailFragment(Matrix matrix, ForDetailListener forDetailListener) {
        this.matrix = matrix;
        this.forDetailListener = forDetailListener;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentDetailBinding.inflate(inflater, container, false);
        View view = binding.getRoot();
        list_feature = new ArrayList<>();
        list_feature_chosen = new ArrayList<>();
        list_approval = new ArrayList<>();
        list_approval_chosen = new ArrayList<>();
        setUp();
        loadData();
        return view;
    }

    private void setUp(){

        binding.btnBackHome.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                forDetailListener.backFrag();
            }
        });

        //Feature
        binding.btnShowFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChoiceShow(list_feature, true);
            }
        });

        feature_chosen_adapter = new ChosenItemAdapter(list_feature_chosen, false,
                getContext(), new ItemChosenListener() {
            @Override
            public void onClick() {
                onChoiceShow(list_feature, true);
            }
        });

        binding.rclFeature.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rclFeature.setAdapter(feature_chosen_adapter);

        //Approval
        binding.btnShowApproval.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChoiceShow(list_approval, false);
            }
        });

        approval_chosen_adapter = new ChosenItemAdapter(list_approval_chosen, false,
                getContext(), new ItemChosenListener() {
            @Override
            public void onClick() {
                onChoiceShow(list_approval, false);
            }
        });

        binding.rclApproval.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rclApproval.setAdapter(approval_chosen_adapter);

        binding.btnDelMatrix.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                JSONObject jsonParam = new JSONObject();
                try {
                    jsonParam.put("id_matrix", matrix.getId());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                delApproval(jsonParam);
            }
        });

        binding.btnAddUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(checkValidData()){
                    JSONObject jsonParam = new JSONObject();
                    try {
                        jsonParam.put("alias", binding.edtAlias.getText().toString());
                        jsonParam.put("min_Range", binding.edtMinRange.getText().toString());
                        jsonParam.put("max_Range", binding.edtMaxRange.getText().toString());
                        jsonParam.put("feature_id", feature_id_chosen);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    if(matrix != null){
                        //update
                        try {
                            jsonParam.put("id", matrix.getId());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        updateMatrix(jsonParam);
                    } else {
                        //add
                        addMatrix(jsonParam);
                    }
                } else {
                    if(getContext() != null) {
                        Toast.makeText(getContext(), "Incorrect data, check again", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        binding.btnReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                initialState();
            }
        });
    }

    private void onChoiceShow(ArrayList<Feature_Approval> list_data, boolean for_feature){
        choiceFragment = new ChoiceFragment(list_data, new ItemChoiceListener() {
            @Override
            public void onItemChoice(int id, boolean b) {
                if (for_feature) {
                    getFeatureChosen(id, b);
                } else {
                    getApprovalChosen(id, b);
                }
            }
        }, for_feature ? "Choice Feature" : "Choice Approval", new OnChoiceFragHide() {
            @Override
            public void onFragHide() {

            }
        });
        choiceFragment.show(getActivity().getSupportFragmentManager(), choiceFragment.getTag());
    }

    private void getFeatureChosen(int id, boolean b){
        for (Feature_Approval choice: list_feature) {
            if(choice.getId() == id){
                choice.setCheck(true);
                feature_id_chosen = id;
                list_feature_chosen.clear();
                list_feature_chosen.add(choice);
            } else {
                choice.setCheck(false);
            }
        }
        feature_chosen_adapter.notifyDataSetChanged();
        choiceFragment.dismiss();
    }

    private void getApprovalChosen(int id, boolean b){
        list_approval_chosen.clear();
        for (Feature_Approval choice: list_approval) {
            if(choice.getId() == id){
                choice.setCheck(b);
            }
        }
        for (Feature_Approval choice: list_approval) {
            if(choice.isCheck()){
                list_approval_chosen.add(choice);
            }
        }
        approval_chosen_adapter.notifyDataSetChanged();
    }

    private void initialState(){
        list_feature_chosen.clear();
        list_approval_chosen.clear();
        if(matrix != null){
            for (Feature_Approval choice: list_feature){
                if(choice.getId() == matrix.getFeature_id()){
                    list_feature_chosen.add(choice);
                    feature_id_chosen = choice.getId();
                    choice.setCheck(true);
                } else {
                    choice.setCheck(false);
                }
            }
            for (Feature_Approval choice: list_approval){
                choice.setCheck(false);
                for (Feature_Approval user_choice: matrix.getList_approval()){
                    if(choice.getId() == user_choice.getId()){
                        choice.setCheck(true);
                    }
                }
            }
            if(matrix.getList_approval().size() == 0 ){
                list_approval_chosen.add(new Feature_Approval(-1, "Default"));
            } else {
                list_approval_chosen.addAll((matrix.getList_approval()));
            }
        } else {
            Feature_Approval test = new Feature_Approval(-1, "Default");
            test.setCheck(true);
            list_feature_chosen.add(test);
            list_approval_chosen.add(test);
            for (Feature_Approval choice: list_approval){
                choice.setCheck(false);
            }
            for (Feature_Approval choice: list_feature){
                choice.setCheck(false);
            }
        }
        approval_chosen_adapter.notifyDataSetChanged();
        feature_chosen_adapter.notifyDataSetChanged();

        if (matrix != null) {
            binding.txtTitleDetail.setText(matrix.getAlias());
            binding.edtAlias.setText(matrix.getAlias());
            binding.edtMinRange.setText(matrix.getMin_Range() + "");
            binding.edtMaxRange.setText(matrix.getMax_Range() + "");
            binding.btnDelMatrix.setVisibility(View.VISIBLE);
            binding.btnAddUpdate.setText("Update");
        } else {
            binding.edtAlias.setText("");
            binding.edtMinRange.setText("");
            binding.edtMaxRange.setText("");
            binding.btnDelMatrix.setVisibility(View.GONE);
            binding.btnAddUpdate.setText("ADD TO LIST");
        }
    }

    private boolean checkValidData(){
        if(list_approval_chosen.size() == 0
                || list_feature_chosen.size() == 0
                || binding.edtAlias.getText().toString().length() == 0
                || binding.edtMinRange.getText().toString().length() == 0
                || binding.edtMaxRange.getText().toString().length() == 0
        )
            return false;
        return true;
    }

    private void loadData(){
        LoadFeatureApprovalAsyncTask loadFeatureApprovalAsyncTask = new LoadFeatureApprovalAsyncTask(new LoadFeatureApprovalListener() {
            @Override
            public void onPre() {
                if(getContext() != null)
                    if (!Methods.getInstance().isNetworkConnected(getContext())) {
                        Toast.makeText(getContext(), "Please connect Internet", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onEnd(ArrayList<Feature_Approval> list_result, boolean done) {
                binding.layoutLoadData.setVisibility(View.GONE);
                if(done){
                    list_feature.addAll(list_result);
                } else {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
                initialState();
            }
        });


        LoadFeatureApprovalAsyncTask loadFeatureApprovalAsyncTask_approval = new LoadFeatureApprovalAsyncTask(new LoadFeatureApprovalListener() {
            @Override
            public void onPre() {
                if(getContext() != null)
                    if (!Methods.getInstance().isNetworkConnected(getContext())) {
                        Toast.makeText(getContext(), "Please connect Internet", Toast.LENGTH_SHORT).show();
                    }
                binding.layoutLoadData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(ArrayList<Feature_Approval> list_result, boolean done) {
                loadFeatureApprovalAsyncTask.execute("http://tuanpc.pw/TuyenTest/api/feature/getAll.php?page=1&step=10&search_txt=");
                if(done){
                    list_approval.addAll(list_result);
                } else {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadFeatureApprovalAsyncTask_approval.execute("http://tuanpc.pw/TuyenTest/api/approval/getAll.php?page=1&step=10&search_txt=");
    }

    private void addMatrix(JSONObject js_param){
        RequestTaskAsyncTask requestTaskAsyncTask = new RequestTaskAsyncTask(js_param, true, new RequestTaskListener() {
            @Override
            public void onPre() {
                if(getContext() != null)
                    if (!Methods.getInstance().isNetworkConnected(getContext())) {
                        Toast.makeText(getContext(), "Please connect Internet", Toast.LENGTH_SHORT).show();
                    }
                binding.layoutLoadData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean value, boolean done, int next_id) {
                if(done){
                    if(value){
                        String id_approval = "";
                        for(Feature_Approval item: list_approval_chosen){
                            if(item.getId() != -1)
                                id_approval += item.getId() + " ";
                        }
                        JSONObject jsonParam = new JSONObject();
                        try {
                            jsonParam.put("id_matrix", next_id);
                            jsonParam.put("id_approval", id_approval);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addApproval(jsonParam);
                    } else {
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Error when Add matrix", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestTaskAsyncTask.execute("http://tuanpc.pw/TuyenTest/api/matrix/insertMatrix.php", "POST");
    }

    private void delMatrix(JSONObject js_param){
        RequestTaskAsyncTask requestTaskAsyncTask = new RequestTaskAsyncTask(js_param, false, new RequestTaskListener() {
            @Override
            public void onPre() {
                if(getContext() != null)
                    if (!Methods.getInstance().isNetworkConnected(getContext())) {
                        Toast.makeText(getContext(), "Please connect Internet", Toast.LENGTH_SHORT).show();
                    }
                binding.layoutLoadData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean value, boolean done, int next_id) {
                if(done){
                    if(value){
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Delete Matrix Success", Toast.LENGTH_SHORT).show();
                        forDetailListener.onDone();
                        forDetailListener.backFrag();
                    } else {
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Error when Del matrix", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestTaskAsyncTask.execute("http://tuanpc.pw/TuyenTest/api/matrix/deleteMatrix.php", "DELETE");
    }

    private void updateMatrix(JSONObject js_param){

        JSONObject jsonParam = new JSONObject();
        try {
            jsonParam.put("id_matrix", matrix.getId());
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestTaskAsyncTask requestTaskAsyncTask_del_Approval = new RequestTaskAsyncTask(jsonParam, false, new RequestTaskListener() {
            @Override
            public void onPre() {
                if(getContext() != null)
                    if (!Methods.getInstance().isNetworkConnected(getContext())) {
                        Toast.makeText(getContext(), "Please connect Internet", Toast.LENGTH_SHORT).show();
                    }
                binding.layoutLoadData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean value, boolean done, int next_id) {
                if(done){
                    if(value){
                        String id_approval = "";
                        for(Feature_Approval item: list_approval_chosen){
                            if(item.getId() != -1)
                                id_approval += item.getId() + " ";
                        }
                        JSONObject jsonParam = new JSONObject();
                        try {
                            jsonParam.put("id_matrix", matrix.getId());
                            jsonParam.put("id_approval", id_approval);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        addApproval(jsonParam);
                    } else {
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Error when Update matrix", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        RequestTaskAsyncTask requestTaskAsyncTask = new RequestTaskAsyncTask(js_param, false, new RequestTaskListener() {
            @Override
            public void onPre() {
                if(getContext() != null)
                    if (!Methods.getInstance().isNetworkConnected(getContext())) {
                        Toast.makeText(getContext(), "Please connect Internet", Toast.LENGTH_SHORT).show();
                    }
                binding.layoutLoadData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean value, boolean done, int next_id) {
                if(done){
                    Log.e("DDD", "onEnd: " + value);
                    if(value){
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Update Success", Toast.LENGTH_SHORT).show();
                        requestTaskAsyncTask_del_Approval.execute("http://tuanpc.pw/TuyenTest/api/matrix_approval/deleteByMatrixID.php", "DELETE");
                    } else {
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Error when Del matrix", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestTaskAsyncTask.execute("http://tuanpc.pw/TuyenTest/api/matrix/updateMatrix.php", "PUT");
    }

    private void addApproval(JSONObject js_param) {
        RequestTaskAsyncTask requestTaskAsyncTask_1 = new RequestTaskAsyncTask(js_param, false, new RequestTaskListener() {
            @Override
            public void onPre() {
                if(getContext() != null)
                    if (!Methods.getInstance().isNetworkConnected(getContext())) {
                        Toast.makeText(getContext(), "Please connect Internet", Toast.LENGTH_SHORT).show();
                    }
            }

            @Override
            public void onEnd(boolean value, boolean done, int next_id) {
                binding.layoutLoadData.setVisibility(View.GONE);
                if(done){
                    if(value){
                        forDetailListener.onDone();
                        forDetailListener.backFrag();
                    } else {
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Error when Add Approval", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestTaskAsyncTask_1.execute("http://tuanpc.pw/TuyenTest/api/matrix_approval/insertMatrix_Approval.php", "POST");
    }

    private void delApproval(JSONObject js_param){
        RequestTaskAsyncTask requestTaskAsyncTask = new RequestTaskAsyncTask(js_param, false, new RequestTaskListener() {
            @Override
            public void onPre() {
                if(getContext() != null)
                    if (!Methods.getInstance().isNetworkConnected(getContext())) {
                        Toast.makeText(getContext(), "Please connect Internet", Toast.LENGTH_SHORT).show();
                    }
                binding.layoutLoadData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(boolean value, boolean done, int next_id) {
                if(done){
                    Log.e("DDD", "onEnd: " + value);
                    if(value){
                        delMatrix(js_param);
                    } else {
                        if(getContext() != null)
                            Toast.makeText(getContext(), "Error when Del Approval", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(getContext() != null)
                        Toast.makeText(getContext(), "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        requestTaskAsyncTask.execute("http://tuanpc.pw/TuyenTest/api/matrix_approval/deleteByMatrixID.php", "DELETE");
    }
}