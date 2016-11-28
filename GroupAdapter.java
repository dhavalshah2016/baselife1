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
import com.example.baselife.MenuActivity;
import com.example.baselife.R;
import com.example.baselife.model.BaseModel;
import com.example.baselife.model.GroupModel;

import java.util.List;

public class GroupAdapter extends RecyclerView.Adapter<GroupAdapter.MyViewHolder> {
    public static GroupModel mySelectedGroup;
    private List<GroupModel> groupList;
    private Activity activity;
    private List<GroupModel> totalGroupList;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView groupName;
        private RelativeLayout groupLayout;
        private TextView tvSubgroup;
        public MyViewHolder(View view, Activity mActivity) {
            super(view);
            groupName = (TextView) view.findViewById(R.id.tvgroupname);
            tvSubgroup = (TextView) view.findViewById(R.id.tvsubgroup);
            groupLayout = (RelativeLayout) view.findViewById(R.id.grouplayout);
            groupLayout.setOnClickListener(this);
            activity = mActivity;
        }

        @Override
        public void onClick(View view) {
            mySelectedGroup = groupList.get(this.getPosition());
            Intent intent = new Intent();
            intent.putExtra("result", mySelectedGroup.getName());
            intent.putExtra("groupId", mySelectedGroup.getId());
            activity.setResult(activity.RESULT_OK, intent);
            activity.finish();
        }
    }


    public GroupAdapter(List<GroupModel> groupList, List<GroupModel> totalGroupList) {
        this.groupList = groupList;
        this.totalGroupList = totalGroupList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_grouplist, parent, false);
        Activity activity = (Activity) itemView.getContext();
        return new MyViewHolder(itemView, activity);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        holder.groupName.setText(groupList.get(position).getName());
        for (int i = 0; i < totalGroupList.size(); i++) {
            if (totalGroupList.get(i).getId() == groupList.get(position).getParentGroup()) {
                holder.tvSubgroup.setText(totalGroupList.get(i).getName());
                break;
            }
        }

    }

    @Override
    public int getItemCount() {
        return groupList.size();
    }
}
