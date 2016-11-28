package com.example.baselife.adapter;

/**
 * Created by dhaval on 22/8/16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baselife.BaseActivity;
import com.example.baselife.CreateThreadActivity;
import com.example.baselife.DisplayThreadActivity;
import com.example.baselife.MenuActivity;
import com.example.baselife.R;
import com.example.baselife.model.BaseModel;

import java.util.List;

public class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.MyViewHolder> {

    private List<BaseModel> baseList;
    private   int groupId;
    private Context activityContext;
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private TextView baseName;
        private TextView baseAddress;

        public MyViewHolder(View view) {
            super(view);
            baseName = (TextView) view.findViewById(R.id.tvbasename);
            baseAddress = (TextView) view.findViewById(R.id.tvbaseaddress);
            baseName.setOnClickListener(this);
            baseAddress.setOnClickListener(this);
        }
        @Override
        public void onClick(View view) {
            groupId=baseList.get(this.getPosition()).getGroup();

            Intent intent = new Intent(view.getContext(), MenuActivity.class);
            intent.putExtra("group", groupId);
            intent.putExtra("groupName",baseList.get(this.getPosition()).getName());
            view.getContext().startActivity(intent);
            ((BaseActivity)activityContext).finish();

        }
    }


    public BaseAdapter(List<BaseModel> baseList,Context myContext) {
        this.baseList = baseList;
        this.activityContext=myContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_base, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        BaseModel baseModel = baseList.get(position);
        holder.baseName.setText(baseModel.getName());
        holder.baseAddress.setText(baseModel.getCity()+","+baseModel.getState());

    }

    @Override
    public int getItemCount() {
        return baseList.size();
    }
}
