package com.example.baselife.adapter;

/**
 * Created by dhaval on 22/8/16.
 */

import android.app.Activity;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baselife.CreateThreadActivity;
import com.example.baselife.DisplayThreadActivity;
import com.example.baselife.GroupThreadDisplayActivity;
import com.example.baselife.MenuActivity;
import com.example.baselife.R;
import com.example.baselife.model.BaseModel;
import com.example.baselife.model.GroupModel;

import java.util.List;

public class MyGroupAdapter extends RecyclerView.Adapter<MyGroupAdapter.MyViewHolder> {
    public static GroupModel mySelectedGroup;
    private List<GroupModel> groupList;
    private Activity activity;
    private List<GroupModel> totalGroupList;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView group1, group2, group3;
        private RelativeLayout groupLayout;
        private int groupId;

        public MyViewHolder(View view, Activity mActivity) {
            super(view);
            group1 = (TextView) view.findViewById(R.id.tvgroup1);
            group2 = (TextView) view.findViewById(R.id.tvgroup2);
            group3 = (TextView) view.findViewById(R.id.tvgroup3);
            groupLayout = (RelativeLayout) view.findViewById(R.id.grouplayout);
            groupLayout.setOnClickListener(this);
            activity = mActivity;
        }

        @Override
        public void onClick(View view) {
            groupId = groupList.get(this.getPosition()).getId();
            Intent intent = new Intent(view.getContext(), GroupThreadDisplayActivity.class);
            intent.putExtra("groupId", groupId);
            intent.putExtra("groupName", groupList.get(this.getPosition()).getName());
            view.getContext().startActivity(intent);
        }
    }


    public MyGroupAdapter(List<GroupModel> groupList, List<GroupModel> totalGroupList) {
        this.groupList = groupList;
        this.totalGroupList = totalGroupList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_mygroup, parent, false);
        Activity activity = (Activity) itemView.getContext();
        return new MyViewHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.group1.setText(groupList.get(position).getName());

        for (int i = 0; i < totalGroupList.size(); i++) {
            if (totalGroupList.get(i).getId() == groupList.get(position).getParentGroup()) {
                holder.group2.setText(totalGroupList.get(i).getName());
                break;
            }
        }
    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
