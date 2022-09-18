package com.test.testmobileeunovo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.R;

import java.util.ArrayList;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.ApprovalHolder> {

    private ArrayList<Feature_Approval> list_data;

    public ApprovalAdapter(ArrayList<Feature_Approval> list_data) {
        this.list_data = list_data;
    }

    @NonNull
    @Override
    public ApprovalHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.layout_approval_item, parent, false);
        return new ApprovalHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovalHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class ApprovalHolder extends RecyclerView.ViewHolder {
        private TextView txt_Stt_Approval, txt_name_Approval;
        public ApprovalHolder(@NonNull View itemView) {
            super(itemView);
            txt_Stt_Approval = (TextView) itemView.findViewById(R.id.txt_Stt_Approval);
            txt_name_Approval = (TextView) itemView.findViewById(R.id.txt_name_Approval);
        }

        public void bindView(int position){
            txt_Stt_Approval.setText("Approver " + (position + 1));
            txt_name_Approval.setText(list_data.get(position).getName());
        }
    }
}
