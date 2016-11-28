package com.example.baselife.adapter;

/**
 * Created by dhaval on 10/9/16.
 */

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.baselife.BaseActivity;
import com.example.baselife.MenuActivity;
import com.example.baselife.R;
import com.example.baselife.model.BaseModel;

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
import android.widget.Toast;

import com.example.baselife.CreateThreadActivity;
import com.example.baselife.DisplayThreadActivity;
import com.example.baselife.MenuActivity;
import com.example.baselife.R;
import com.example.baselife.model.BaseModel;
import com.example.baselife.model.MessageBoardModel;
import com.example.baselife.util.ImageLoader1;

import java.util.List;

public class MessageBoardAdapter extends RecyclerView.Adapter<MessageBoardAdapter.MyViewHolder> {

    private MessageBoardModel messageBoardModel;
    private int groupId;
    ImageLoader1 imageLoader;
    private Context mContext;
    private String groupName;


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        /* private TextView baseName;
         private TextView baseAddress;*/
        private ImageView icon, image, image2, iconreply;
        private TextView tvname, tvdate, tvgroup, tvmessage;
        private TextView tvname2, tvduration, tvreplymessage, tvtotalreply;
        private RelativeLayout bottomLayout;
        public MyViewHolder(View view) {
            super(view);
            tvname = (TextView) view.findViewById(R.id.tvname);
            tvdate = (TextView) view.findViewById(R.id.tvdate);
            tvmessage = (TextView) view.findViewById(R.id.tvmessage);
           /* baseName.setOnClickListener(this);
            baseAddress.setOnClickListener(this)*/
            bottomLayout = (RelativeLayout) view.findViewById(R.id.bottomlayout);
            tvgroup = (TextView) view.findViewById(R.id.tvgroup);
            tvname2 = (TextView) view.findViewById(R.id.tvname2);
            tvduration = (TextView) view.findViewById(R.id.tvduration);
            tvtotalreply = (TextView) view.findViewById(R.id.tvtotalreply);
            tvreplymessage = (TextView) view.findViewById(R.id.tvreplymessage);

            imageLoader = new ImageLoader1(mContext);
            icon = (ImageView) view.findViewById(R.id.icon);
            image = (ImageView) view.findViewById(R.id.image);
            image2 = (ImageView) view.findViewById(R.id.image2);
            iconreply = (ImageView) view.findViewById(R.id.iconreply);
        }

        @Override
        public void onClick(View view) {
            /*groupId=baseList.get(this.getPosition()).getGroup();

            Intent intent = new Intent(view.getContext(), MenuActivity.class);
            intent.putExtra("group",groupId);

            view.getContext().startActivity(intent);*/
        }
    }


    public MessageBoardAdapter(MessageBoardModel msgBoardModel, Context myContext) {
        this.messageBoardModel = msgBoardModel;
        mContext = myContext;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.row_messageboard, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder,final int position) {

        String userfp="", userlr, iconfp = "", iconlr = "";
        try {






            Activity activity = (Activity) mContext;

            if (messageBoardModel.getThreadList().size() > 0) {
                if (messageBoardModel.getThreadList().get(position).getNumber_of_replies() > 0) {
                    holder.bottomLayout.setVisibility(View.VISIBLE);
                    holder.tvtotalreply.setText(messageBoardModel.getThreadList().get(position).getNumber_of_replies() + "replies");
                }else
                {
                    holder.bottomLayout.setVisibility(View.GONE);
                }

                    for (int p = 0; p < MenuActivity.profileList.size(); p++) {
                        if (messageBoardModel.getThreadList().get(position).firstPost.getPosted_by_profile() == MenuActivity.profileList.get(p).getId()) {
                            userfp = MenuActivity.profileList.get(p).getDisplayName();
                            iconlr = MenuActivity.profileList.get(p).getImgUrl();
                            holder.tvname.setText(userfp);
                            break;
                        }

                    }


                    if (messageBoardModel.getThreadList().get(position).firstPost.getCreated_date() != null) {
                        holder.tvdate.setText(messageBoardModel.getThreadList().get(position).firstPost.getCreated_date());
                    }

                    if (messageBoardModel.getThreadList().get(position).firstPost.getText() != null) {

                        holder.tvmessage.setText(messageBoardModel.getThreadList().get(position).firstPost.getText());
                    }

                    if (messageBoardModel.getThreadList().get(position).firstPost.getMediaList().size() > 0) {
                        holder.image.setVisibility(View.VISIBLE);
                        if (messageBoardModel.getThreadList().get(position).firstPost.getMediaList().get(0).getUrl().length() > 0) {
                            imageLoader.DisplayImage(messageBoardModel.getThreadList().get(position).firstPost.getMediaList().get(0).getUrl(), activity,
                                    holder.image);
                        } else {
                            holder.image.setVisibility(View.GONE);
                        }
                    } else {
                        holder.image.setVisibility(View.GONE);
                    }





                    for (int p = 0; p < MenuActivity.profileList.size(); p++) {
                        if (messageBoardModel.getThreadList().get(position).lastReply.getPosted_by_profile() == MenuActivity.profileList.get(p).getId()) {
                            userlr = MenuActivity.profileList.get(p).getDisplayName();
                            iconlr = MenuActivity.profileList.get(p).getImgUrl();
                            holder.tvname2.setText(userlr);
                            break;
                        }

                    }

                    if (messageBoardModel.getThreadList().get(position).lastReply.getCreated_date() != null) {
                        holder.tvduration.setText(messageBoardModel.getThreadList().get(position).lastReply.getCreated_date());
                    }
                    if (messageBoardModel.getThreadList().get(position).lastReply.getText() != null) {
                        holder.tvreplymessage.setText(messageBoardModel.getThreadList().get(position).lastReply.getText());
                    }
                    if (messageBoardModel.getThreadList().get(position).lastReply.getMediaList().size() > 0) {
                        if (messageBoardModel.getThreadList().get(position).lastReply.getMediaList().get(0).getUrl().length() > 0) {
                            holder.image2.setVisibility(View.VISIBLE);

                            imageLoader.DisplayImage(messageBoardModel.getThreadList().get(position).lastReply.getMediaList().get(0).getUrl(), activity,
                                    holder.image2);
                        } else {
                            holder.image2.setVisibility(View.GONE);
                        }
                    } else {
                        holder.image2.setVisibility(View.GONE);
                    }

                for (int k = 0; k < BaseActivity.baseModelList.size(); k++) {
                    if (BaseActivity.baseModelList.get(k).getGroup() == messageBoardModel.getThreadList().get(position).getGroup()) {
                        holder.tvgroup.setText(BaseActivity.baseModelList.get(k).getName());
                        break;
                    }
                }


                if (iconfp.length() > 0) {
                    holder.icon.setVisibility(View.VISIBLE);
                    imageLoader.DisplayImage(iconfp, activity,
                            holder.icon);
                } else {
                    holder.icon.setVisibility(View.GONE);
                }
                if (iconlr.length() > 0) {
                    holder.iconreply.setVisibility(View.VISIBLE);
                    imageLoader.DisplayImage(iconlr, activity,
                            holder.iconreply);
                } else {
                    holder.iconreply.setVisibility(View.GONE);
                }
            }
        } catch (Exception e) {

        }
    }

    @Override
    public int getItemCount() {
        return messageBoardModel.getThreadList().size();
    }
}
