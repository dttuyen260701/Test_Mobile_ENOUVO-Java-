package com.test.testmobileeunovo.Activities;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.test.testmobileeunovo.Adapters.ChosenItemAdapter;
import com.test.testmobileeunovo.Adapters.MatrixAdapter;
import com.test.testmobileeunovo.Asynctasks.LoadFeatureApprovalAsyncTask;
import com.test.testmobileeunovo.Asynctasks.LoadMatrixAsynctask;
import com.test.testmobileeunovo.Fragments.ChoiceFragment;
import com.test.testmobileeunovo.Fragments.DetailFragment;
import com.test.testmobileeunovo.Listeners.ForDetailListener;
import com.test.testmobileeunovo.Listeners.ItemChoiceListener;
import com.test.testmobileeunovo.Listeners.ItemChosenListener;
import com.test.testmobileeunovo.Listeners.ItemMatrixListener;
import com.test.testmobileeunovo.Listeners.LoadFeatureApprovalListener;
import com.test.testmobileeunovo.Listeners.LoadMatrixListener;
import com.test.testmobileeunovo.Listeners.OnChoiceFragHide;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.R;
import com.test.testmobileeunovo.Utils.Methods;
import com.test.testmobileeunovo.databinding.ActivityMainBinding;

import java.lang.reflect.Method;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ChosenItemAdapter adapter_choice;
    private MatrixAdapter adapter_matrix;
    private static ArrayList<Feature_Approval> list_choice;
    private ArrayList<Feature_Approval> list_chosen;
    private static ArrayList<Matrix> list_matrix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        if(list_choice == null)
            list_choice = new ArrayList<>();
        if(list_matrix == null)
            list_matrix = new ArrayList<>();
        list_chosen = new ArrayList<>();
        setUp();
        loadData();
    }

    private void setUp(){
        ConstraintLayout.LayoutParams btn_add_Params = new ConstraintLayout.LayoutParams(
                (getResources().getDisplayMetrics().widthPixels-60)/2, ConstraintLayout.LayoutParams.MATCH_CONSTRAINT);
        binding.btnAddChild.setLayoutParams(btn_add_Params);
        binding.btnAddChild.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DetailFragment detailFragment = new DetailFragment(null, new ForDetailListener() {
                    @Override
                    public void onDone() {
                        loadData();
                    }

                    @Override
                    public void backFrag() {
                        getSupportFragmentManager().popBackStack();
                    }
                });
                addFragment(detailFragment);
            }
        });

        adapter_choice = new ChosenItemAdapter(list_chosen, true, MainActivity.this, new ItemChosenListener() {
            @Override
            public void onClick() {
                onChoiceShow();
            }
        });

        binding.rclFeature.setLayoutManager(new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false));
        binding.rclFeature.setAdapter(adapter_choice);

        binding.btnShowFeature.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onChoiceShow();
            }
        });

        binding.swipRefeshHome.setColorSchemeColors(getColor(R.color.primary));
        binding.swipRefeshHome.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadData();
                binding.swipRefeshHome.setRefreshing(false);
            }
        });

        adapter_matrix = new MatrixAdapter(list_matrix, new ItemMatrixListener() {
            @Override
            public void onClick(Matrix matrix) {
                DetailFragment detailFragment = new DetailFragment(matrix, new ForDetailListener() {
                    @Override
                    public void onDone() {
                        loadData();
                    }

                    @Override
                    public void backFrag() {
                        getSupportFragmentManager().popBackStack();
                    }
                });
                addFragment(detailFragment);
            }
        }, MainActivity.this);

        binding.rclMatrix.setLayoutManager(new LinearLayoutManager(MainActivity.this));
        binding.rclMatrix.setAdapter(adapter_matrix);
    }

    private void addFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        //add để trạng thái trước được lưu
        transaction.add(R.id.main_layout, fragment, "Detail");
        transaction.addToBackStack("main");
        transaction.commit();
    }

    private void onChoiceShow(){
        ChoiceFragment choiceFragment = new ChoiceFragment(list_choice, new ItemChoiceListener() {
            @Override
            public void onItemChoice(int id, boolean b) {
                for (Feature_Approval choice : list_choice) {
                    if (choice.getId() == id) {
                        choice.setCheck(b);
                    }
                }
                getChoices();
            }
        }, "Choice Feature", new OnChoiceFragHide() {
            @Override
            public void onFragHide() {
                loadMatrixByFeatureID();
            }
        });
        choiceFragment.show(getSupportFragmentManager(), choiceFragment.getTag());
    }

    private void getChoices(){
        list_chosen.clear();
        for (Feature_Approval choice: list_choice) {
            if(choice.isCheck()){
                list_chosen.add(choice);
            }
        }
        if(list_chosen.size() == 0){
            Feature_Approval test = new Feature_Approval(-1, "Default");
            test.setCheck(true);
            list_chosen.add(test);
        }
        adapter_choice.notifyDataSetChanged();
    }

    private void loadData(){
        list_matrix.clear();
        list_choice.clear();
        list_chosen.clear();
        Feature_Approval test = new Feature_Approval(-1, "Default");
        test.setCheck(true);
        list_chosen.add(test);
        adapter_choice.notifyDataSetChanged();
        LoadMatrixAsynctask loadMatrixAsynctask = new LoadMatrixAsynctask(new LoadMatrixListener() {
            @Override
            public void onPre() {
            }

            @Override
            public void onEnd(ArrayList<Matrix> list_result, boolean done) {
                binding.layoutLoadData.setVisibility(View.GONE);
                if(done){
                    list_matrix.addAll(list_result);
                    adapter_matrix.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        LoadFeatureApprovalAsyncTask loadFeatureApprovalAsyncTask = new LoadFeatureApprovalAsyncTask(new LoadFeatureApprovalListener() {
            @Override
            public void onPre() {
                if (!Methods.getInstance().isNetworkConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "Please connect Internet", Toast.LENGTH_SHORT).show();
                }
                binding.layoutLoadData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(ArrayList<Feature_Approval> list_result, boolean done) {
                loadMatrixAsynctask.execute("http://tuanpc.pw/TuyenTest/api/matrix/getAll.php?page=1&step=10&search_txt=");
                if(done){
                    list_choice.addAll(list_result);
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });

        loadFeatureApprovalAsyncTask.execute("http://tuanpc.pw/TuyenTest/api/feature/getAll.php?page=1&step=10&search_txt=");
    }

    private void loadMatrixByFeatureID(){
        list_matrix.clear();
        String feature_id = "";
        for(Feature_Approval item: list_chosen){
            if(item.getId() != -1)
                feature_id += item.getId() + " ";
        }
        feature_id = feature_id.trim();
        LoadMatrixAsynctask loadMatrixAsynctask = new LoadMatrixAsynctask(new LoadMatrixListener() {
            @Override
            public void onPre() {
                if (!Methods.getInstance().isNetworkConnected(MainActivity.this)) {
                    Toast.makeText(MainActivity.this, "Please connect Internet", Toast.LENGTH_SHORT).show();
                }
                binding.layoutLoadData.setVisibility(View.VISIBLE);
            }

            @Override
            public void onEnd(ArrayList<Matrix> list_result, boolean done) {
                binding.layoutLoadData.setVisibility(View.GONE);
                if(done){
                    list_matrix.addAll(list_result);
                    adapter_matrix.notifyDataSetChanged();
                } else {
                    Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
                }
            }
        });
        loadMatrixAsynctask.execute("http://tuanpc.pw/TuyenTest/api/matrix/getByFeatureId.php?page=1&step=50&search_txt=&feature_id=" + feature_id);
    }
}