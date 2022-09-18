package com.test.testmobileeunovo.Activities;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.test.testmobileeunovo.Adapters.ChosenItemAdapter;
import com.test.testmobileeunovo.Adapters.MatrixAdapter;
import com.test.testmobileeunovo.Fragments.ChoiceFragment;
import com.test.testmobileeunovo.Fragments.DetailFragment;
import com.test.testmobileeunovo.Listeners.ForDetailListener;
import com.test.testmobileeunovo.Listeners.ItemChoiceListener;
import com.test.testmobileeunovo.Listeners.ItemChosenListener;
import com.test.testmobileeunovo.Listeners.ItemMatrixListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.R;
import com.test.testmobileeunovo.databinding.ActivityMainBinding;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;
    private ChosenItemAdapter adapter_choice;
    private MatrixAdapter adapter_matrix;
    private static ArrayList<Feature_Approval> list_choice, list_chosen;
    private static ArrayList<Matrix> list_matrix;

    public static ArrayList<Feature_Approval> getList_choice() {
        return list_choice;
    }

    public static void setList_choice(ArrayList<Feature_Approval> list_choice) {
        MainActivity.list_choice = list_choice;
    }

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
        if(list_chosen == null){
            list_chosen = new ArrayList<>();
            Feature_Approval test = new Feature_Approval(-1, "Default");
            test.setCheck(true);
            list_chosen.add(test);
        }
        setUp();
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
                        //load Data
                    }

                    @Override
                    public void backFrag() {
                        getSupportFragmentManager().popBackStack();
                    }
                });
                addFragment(detailFragment);
            }
        });
        list_choice.add(new Feature_Approval(5, "Feature D"));
        list_choice.add(new Feature_Approval(4, "Feature C"));
        list_choice.add(new Feature_Approval(2, "Feature B"));
        list_choice.add(new Feature_Approval(3, "Feature A"));

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
                binding.swipRefeshHome.setRefreshing(false);
            }
        });

        list_matrix.add(new Matrix(1, "Transfer Online", 1000, 2000, 2, list_chosen));
        list_matrix.add(new Matrix(2, "Transfer Offline", 100, 20000, 3, list_choice));
        list_matrix.add(new Matrix(3, "Transfer Test", 1000, 2000, 1, new ArrayList<>()));

        adapter_matrix = new MatrixAdapter(list_matrix, new ItemMatrixListener() {
            @Override
            public void onClick(Matrix matrix) {
                DetailFragment detailFragment = new DetailFragment(matrix, new ForDetailListener() {
                    @Override
                    public void onDone() {
                        //load Data
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
                for (Feature_Approval choice: list_choice) {
                    if(choice.getId() == id){
                        choice.setCheck(b);
                    }
                }
                getChoices();
            }
        }, "Choice Feature");
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
}