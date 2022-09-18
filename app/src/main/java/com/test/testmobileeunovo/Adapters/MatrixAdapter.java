package com.test.testmobileeunovo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testmobileeunovo.Listeners.ItemMatrixListener;
import com.test.testmobileeunovo.Models.Matrix;
import com.test.testmobileeunovo.R;

import java.util.ArrayList;

public class MatrixAdapter extends RecyclerView.Adapter<MatrixAdapter.MatrixHolder> {

    private ArrayList<Matrix> list_data;
    private ItemMatrixListener itemMatrixListener;
    private Context context;

    public MatrixAdapter(ArrayList<Matrix> list_data, ItemMatrixListener itemMatrixListener, Context context) {
        this.list_data = list_data;
        this.itemMatrixListener = itemMatrixListener;
        this.context = context;
    }

    @NonNull
    @Override
    public MatrixHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_matrix_item, parent, false);
        return new MatrixHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MatrixHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class MatrixHolder extends RecyclerView.ViewHolder {
        private ConstraintLayout layout_matrix_item;
        private TextView txt_min_range, txt_max_range, txt_num_matrix;
        private ApprovalAdapter adapter;
        private RecyclerView rcl_Approval;
        public MatrixHolder(@NonNull View itemView) {
            super(itemView);
            layout_matrix_item = (ConstraintLayout) itemView.findViewById(R.id.layout_matrix_item);
            txt_min_range = (TextView) itemView.findViewById(R.id.txt_min_range);
            txt_max_range = (TextView) itemView.findViewById(R.id.txt_max_range);
            txt_num_matrix = (TextView) itemView.findViewById(R.id.txt_num_matrix);
            rcl_Approval = (RecyclerView) itemView.findViewById(R.id.rcl_Approval);
        }

        public void bindView(int position){
            layout_matrix_item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemMatrixListener.onClick(list_data.get(position));
                }
            });
            txt_min_range.setText(list_data.get(position).getMin_Range() + "");
            txt_max_range.setText(list_data.get(position).getMax_Range() + "");
            txt_num_matrix.setText(list_data.get(position).getList_approval().size() + "");

            adapter = new ApprovalAdapter(list_data.get(position).getList_approval());
            rcl_Approval.setLayoutManager(new LinearLayoutManager(context));
            rcl_Approval.setAdapter(adapter);
        }
    }
}
