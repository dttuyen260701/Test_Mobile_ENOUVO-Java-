package com.test.testmobileeunovo.Adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testmobileeunovo.Listeners.ItemChoiceListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.R;

import java.util.ArrayList;

public class ListChoiceItemAdapter extends RecyclerView.Adapter<ListChoiceItemAdapter.ListChoiceHolder> {

    private ArrayList<Feature_Approval> list_data;
    private ItemChoiceListener itemChoiceListener;

    public ListChoiceItemAdapter(ArrayList<Feature_Approval> list_data, ItemChoiceListener itemChoiceListener) {
        this.list_data = list_data;
        this.itemChoiceListener = itemChoiceListener;
    }

    public void setList_data(ArrayList<Feature_Approval> list_data) {
        this.list_data = list_data;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ListChoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.feature_list_item, parent, false);
        return new ListChoiceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListChoiceHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    class ListChoiceHolder extends RecyclerView.ViewHolder {
        private TextView txt_name_item;
        private CheckBox cb_item;
        public ListChoiceHolder(@NonNull View itemView) {
            super(itemView);
            txt_name_item = (TextView) itemView.findViewById(R.id.txt_name_item);
            cb_item = (CheckBox) itemView.findViewById(R.id.cb_item);
        }

        public void bindView(int position){
            txt_name_item.setText(list_data.get(position).getName());
            cb_item.setChecked(list_data.get(position).isCheck());

            cb_item.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    itemChoiceListener.onItemChoice(list_data.get(position).getId(), b);
                }
            });
        }
    }
}
