package com.test.testmobileeunovo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testmobileeunovo.Listeners.ItemChosenListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.R;

import java.util.ArrayList;

public class ApprovalAdapter extends RecyclerView.Adapter<ApprovalAdapter.ApprovalHolder> {

    private ArrayList<Feature_Approval> list_data;
    private ItemChosenListener listener;

    public ApprovalAdapter(ArrayList<Feature_Approval> list_data, ItemChosenListener listener) {
        this.list_data = list_data;
        this.listener = listener;
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
        private LinearLayout layout_Item_Approval;
        public ApprovalHolder(@NonNull View itemView) {
            super(itemView);
            txt_Stt_Approval = (TextView) itemView.findViewById(R.id.txt_Stt_Approval);
            txt_name_Approval = (TextView) itemView.findViewById(R.id.txt_name_Approval);
            layout_Item_Approval = (LinearLayout) itemView.findViewById(R.id.layout_Item_Approval);
        }

        public void bindView(int position){
            txt_Stt_Approval.setText("Approver " + (position + 1));
            txt_name_Approval.setText(list_data.get(position).getName());
            layout_Item_Approval.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onClick();
                }
            });
        }
    }
}
