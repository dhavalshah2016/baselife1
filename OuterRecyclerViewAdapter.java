package com.example.baselife.adapter;

/**
 * Created by dhaval on 10/9/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baselife.BaseActivity;
import com.example.baselife.MenuActivity;
import com.example.baselife.R;
import com.example.baselife.model.BaseModel;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by dhaval on 22/8/16.
 */
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.baselife.CreateThreadActivity;
import com.example.baselife.DisplayThreadActivity;
import com.example.baselife.MenuActivity;
import com.example.baselife.R;
import com.example.baselife.model.BaseModel;
import com.example.baselife.model.MessageBoardModel;
import com.example.baselife.util.CustomLinearLayoutManager;
import com.example.baselife.util.DividerItemDecoration;
import com.example.baselife.util.ImageLoader1;

import java.util.List;

public class OuterRecyclerViewAdapter extends RecyclerView.Adapter<OuterRecyclerViewAdapter.MyViewHolder> {

    private ArrayList<MessageBoardModel> messageBoardList;
    private int groupId;
    ImageLoader1 imageLoader;
    private Context mContext;
    private String groupName;
    private RelativeLayout bottomLayout;
    private MessageBoardAdapter mAdapter;

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /* private TextView baseName;
         private TextView baseAddress;*/
        private RecyclerView SecondaryRecylerView;

        public MyViewHolder(View view) {
            super(view);
            SecondaryRecylerView = (RecyclerView) view.findViewById(R.id.secondary_recycler_view);

        }

        @Override
        public void onClick(View view) {
            /*groupId=baseList.get(this.getPosition()).getGroup();

            Intent intent = new Intent(view.getContext(), MenuActivity.class);
            intent.putExtra("group",groupId);

            view.getContext().startActivity(intent);*/
        }
    }


    public OuterRecyclerViewAdapter(ArrayList<MessageBoardModel> msgboardList, Context myContext) {
        this.messageBoardList = msgboardList;
        mContext = myContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_recyclerview, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {

        String userfp, userlr, iconfp = "", iconlr = "";
        try {



            Activity activity = (Activity) mContext;
            mAdapter = new MessageBoardAdapter(messageBoardList.get(position),mContext);
            CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager( mContext);
            holder.SecondaryRecylerView.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL_LIST));
            holder.SecondaryRecylerView.setHasFixedSize(true);
            holder.SecondaryRecylerView.setLayoutManager(mLayoutManager);
            holder.SecondaryRecylerView.setItemAnimator(new DefaultItemAnimator());
            holder.SecondaryRecylerView.setAdapter(mAdapter);
        }catch(Exception e)
        {

        }
    }

    @Override
    public int getItemCount() {
        return messageBoardList.size();
    }
}
