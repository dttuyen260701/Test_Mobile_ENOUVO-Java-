package com.test.testmobileeunovo.Adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.test.testmobileeunovo.Listeners.ItemChosenListener;
import com.test.testmobileeunovo.Models.Feature_Approval;
import com.test.testmobileeunovo.R;

import java.util.ArrayList;

public class ChosenItemAdapter extends RecyclerView.Adapter<ChosenItemAdapter.ChoiceHolder> {

    private ArrayList<Feature_Approval> list_data;
    private boolean for_home_scr;
    private Context context;
    private ItemChosenListener itemChosenListener;

    public ChosenItemAdapter(ArrayList<Feature_Approval> list_data, boolean for_home_scr, Context context, ItemChosenListener itemChosenListener) {
        this.list_data = list_data;
        this.for_home_scr = for_home_scr;
        this.context = context;
        this.itemChosenListener = itemChosenListener;
    }

    @NonNull
    @Override
    public ChoiceHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.feature_choice_item, parent, false);
        return new ChoiceHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ChoiceHolder holder, int position) {
        holder.bindView(position);
    }

    @Override
    public int getItemCount() {
        return list_data.size();
    }

    public class ChoiceHolder extends RecyclerView.ViewHolder{
        private TextView txt_feature_name;
        private ConstraintLayout bg_option, item_chosen;
        public ChoiceHolder(@NonNull View itemView) {
            super(itemView);
            txt_feature_name = (TextView) itemView.findViewById(R.id.txt_feature_name);
            bg_option = (ConstraintLayout) itemView.findViewById(R.id.bg_option);
            item_chosen = (ConstraintLayout) itemView.findViewById(R.id.item_chosen);
        }

        public void bindView(int index){
            item_chosen.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    itemChosenListener.onClick();
                }
            });
            txt_feature_name.setText(list_data.get(index).getName());
            txt_feature_name.setTextColor((for_home_scr) ?
                    context.getColor(R.color.primary) :
                    context.getColor(R.color.black));
            if (index != list_data.size() - 1){
                bg_option.setVisibility(View.VISIBLE);
                bg_option.setBackgroundColor((for_home_scr) ?
                        context.getColor(R.color.primary) :
                        context.getColor(R.color.black));
            } else {
                bg_option.setVisibility(View.GONE);
            }
            if(list_data.get(index).getId() == -1 && !for_home_scr){
                txt_feature_name.setTextColor(context.getColor(R.color.border_color));
            }
        }
    }
}
