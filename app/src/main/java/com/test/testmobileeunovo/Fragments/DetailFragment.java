package com.test.testmobileeunovo.Fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.test.testmobileeunovo.Adapters.ChosenItemAdapter;
import com.test.testmobileeunovo.Adapters.ListChoiceItemAdapter;
import com.test.testmobileeunovo.Listeners.ForDetailListener;
import com.test.testmobileeunovo.Listeners.ItemChoiceListener;
import com.test.testmobileeunovo.Listeners.ItemChosenListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.databinding.FragmentDetailBinding;

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
        return view;
    }

    private void setUp(){
        list_feature.add(new Feature_Approval(5, "Feature D"));
        list_feature.add(new Feature_Approval(4, "Feature C"));
        list_feature.add(new Feature_Approval(2, "Feature B"));
        list_feature.add(new Feature_Approval(3, "Feature A"));

        list_approval.add(new Feature_Approval(5, "Feature D"));
        list_approval.add(new Feature_Approval(4, "Feature C"));
        list_approval.add(new Feature_Approval(2, "Feature B"));
        list_approval.add(new Feature_Approval(3, "Feature A"));

        initialState();
        if (matrix != null) {
            binding.txtTitleDetail.setText(matrix.getAlias());
            binding.edtAlias.setText(matrix.getAlias());
            binding.edtMinRange.setText(matrix.getMin_Range() + "");
            binding.edtMaxRange.setText(matrix.getMax_Range() + "");
            binding.btnDelMatrix.setVisibility(View.VISIBLE);
        } else {
            binding.btnDelMatrix.setVisibility(View.GONE);
        }

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
                onChoiceShow(list_approval, true);
            }
        });

        binding.rclApproval.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false));
        binding.rclApproval.setAdapter(approval_chosen_adapter);

    }

    private void onChoiceShow(ArrayList<Feature_Approval> list_data, boolean for_feature){
        choiceFragment = new ChoiceFragment(list_data, new ItemChoiceListener() {
            @Override
            public void onItemChoice(int id, boolean b) {
                if(for_feature){
                    getFeatureChosen(id, b);
                } else {
                    getApprovalChosen(id, b);
                }
            }
        }, "Choice Feature");
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
        }
    }
}