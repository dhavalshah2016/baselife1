package com.example.baselife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.adapter.MessageBoardAdapter;
import com.example.baselife.adapter.OuterRecyclerViewAdapter;
import com.example.baselife.model.MessageBoardModel;
import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.CustomLinearLayoutManager;
import com.example.baselife.util.DividerItemDecoration;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class GroupThreadDisplayActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progress;
    private MessageBoardModel messageBoardModel;
    private RecyclerView recyclerView;
    private MessageBoardAdapter mAdapter;
    private int groupId;
    private LinearLayout createThreadLayout;
    private ImageView imgMore;
    private ImageView imgBack;
    private TextView tvGroupName;
    private String groupName;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private int totalCount = 1, currentPage = 1;
    private boolean countsaved = false;
    private String access_token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_thread_display);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipeToRefresh);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                //  Toast.makeText(DisplayThreadActivity.this,countsaved+"",Toast.LENGTH_SHORT).show();
                if (countsaved) {
                    if (totalCount > currentPage) {
                        currentPage++;


                        if (CheckInternet.isNetworkAvailable(GroupThreadDisplayActivity.this)) {
                            // code here
                            new DisplayThreadAsyncTask().execute();
                        } else {
                            // code

                            Toast.makeText(GroupThreadDisplayActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                        }

                    }
                }
                mSwipeRefreshLayout.setRefreshing(false);
            }
        });
        createThreadLayout = (LinearLayout) findViewById(R.id.createthreadlayout);
        createThreadLayout.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        imgMore = (ImageView) findViewById(R.id.imgmore);
        imgMore.setOnClickListener(this);
        imgBack = (ImageView) findViewById(R.id.imgback);
        imgBack.setOnClickListener(this);
        tvGroupName = (TextView) findViewById(R.id.tvgroupname);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getInt("groupId");
            groupName = extras.getString("groupName");
            //The key argument here must match that used in the other activity
        }
        tvGroupName.setText(groupName);
        countsaved = false;
        // Toast.makeText(GroupThreadDisplayActivity.this, groupId + "", Toast.LENGTH_LONG).show();


        if (CheckInternet.isNetworkAvailable(GroupThreadDisplayActivity.this)) {
            // code here
            new DisplayThreadAsyncTask().execute();
        } else {
            // code

            Toast.makeText(GroupThreadDisplayActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.createthreadlayout:
                Intent intent = new Intent(view.getContext(), CreateThreadActivity.class);
                intent.putExtra("group", groupId);

                view.getContext().startActivity(intent);
                break;
            case R.id.imgmore:
                Intent intentMyGroup = new Intent(view.getContext(), MyGroupActivity.class);
                intentMyGroup.putExtra("group", groupId);

                view.getContext().startActivity(intentMyGroup);


                GroupThreadDisplayActivity.this.finish();
                break;
            case R.id.imgback:

                GroupThreadDisplayActivity.this.finish();
                break;

        }
    }

    private class DisplayThreadAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(GroupThreadDisplayActivity.this, "Base Life",
                    "loading ... please wait", true);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String myResponse = postData();
            return myResponse;
        }

        @Override
        protected void onPostExecute(String result1) {
            progress.dismiss();
            Log.e("result", result1);
            if (result1.toString().length() > 1) {
                // Toast.makeText(DisplayThreadActivity.this, result1, Toast.LENGTH_LONG).show();

                try {
                    Gson gson = new GsonBuilder().create();
                    messageBoardModel = gson.fromJson(result1, MessageBoardModel.class);

                   /* MessageBoardModel messageBoardModel = new MessageBoardModel();
                    Type listType = new TypeToken<ArrayList<MessageBoardModel>>(){}.getType();
                    messageBoardList = new GsonBuilder().create().fromJson(result1, listType);
*/
                    /*MessageBoardModel.Meta meta = new MessageBoardModel.Meta();

                    JSONObject mainObject = new JSONObject(result1);
                    if (!mainObject.isNull("meta")) {
                        countsaved = true;
                        JSONObject metaObj = mainObject.getJSONObject("meta");
                        if (!metaObj.isNull("count")) {
                            int count = metaObj.getInt("count");
                            meta.setCount(count);
                            totalCount = count;
                        }
                        if (!metaObj.isNull("page")) {
                            int page = metaObj.getInt("page");

                            meta.setPage(page);
                            currentPage = page;
                        }
                    }
                    if (!mainObject.isNull("threads")) {
                        JSONArray threadArray = mainObject.getJSONArray("threads");
                        for (int i = 0; i < threadArray.length(); i++) {
                            MessageBoardModel.Thread threadClassObj = new MessageBoardModel.Thread();
                            JSONObject threadObject = threadArray.getJSONObject(i);
                            if (!threadObject.isNull("id")) {
                                int threadId = threadObject.getInt("id");
                                threadClassObj.setId(threadId);
                            }
                            if (!threadObject.isNull("posted_by_profile")) {
                                int posted_by_profile = threadObject.getInt("posted_by_profile");
                                threadClassObj.setPosted_by_profile(posted_by_profile);
                            }
                            if (!threadObject.isNull("posted_anonymously")) {
                                boolean posted_anonymously = threadObject.getBoolean("posted_anonymously");
                                threadClassObj.setPosted_anonymously(posted_anonymously);
                            }
                            if (!threadObject.isNull("group")) {
                                int group = threadObject.getInt("group");
                                threadClassObj.setGroup(group);
                            }
                            MessageBoardModel.Thread.LastReply lastReplyClassObj = new MessageBoardModel.Thread.LastReply();
                            ArrayList<MessageBoardModel.Thread.LastReply> lastReplyList = new ArrayList<MessageBoardModel.Thread.LastReply>();
                            if (!threadObject.isNull("last_reply")) {
                                JSONObject last_reply = threadObject.getJSONObject("last_reply");
                                if (!last_reply.isNull("id")) {
                                    int lastReplyId = last_reply.getInt("id");

                                    lastReplyClassObj.setId(lastReplyId);
                                }
                                if (!last_reply.isNull("created_date")) {
                                    String created_date = last_reply.getString("created_date");

                                    lastReplyClassObj.setCreated_date(created_date);
                                }
                                if (!last_reply.isNull("posted_by_profile")) {
                                    int lastReplyPostedByProfile = last_reply.getInt("posted_by_profile");
                                    lastReplyClassObj.setPosted_by_profile(lastReplyPostedByProfile);
                                }
                                if (!last_reply.isNull("posted_anonymously")) {
                                    boolean last_replyposted_anonymously = last_reply.getBoolean("posted_anonymously");
                                    lastReplyClassObj.setPosted_anonymously(last_replyposted_anonymously);
                                }
                                if (!last_reply.isNull("thread")) {
                                    int lastReplyThread = last_reply.getInt("thread");
                                    lastReplyClassObj.setThread(lastReplyThread);
                                }
                                if (!last_reply.isNull("text")) {
                                    String text = last_reply.getString("text");
                                    lastReplyClassObj.setText(text);
                                }
                                if (!last_reply.isNull("media_objects")) {
                                    JSONArray mediaObjectArray = last_reply.getJSONArray("media_objects");
                                    ArrayList<MessageBoardModel.Thread.MediaObjects> mediaList = new ArrayList<MessageBoardModel.Thread.MediaObjects>();
                                    for (int j = 0; j < mediaObjectArray.length(); j++) {
                                        MessageBoardModel.Thread.MediaObjects mediaObjectCLassObj = new MessageBoardModel.Thread.MediaObjects();

                                        JSONObject mediaObject = mediaObjectArray.getJSONObject(j);
                                        if (!mediaObject.isNull("id")) {
                                            int mediaId = mediaObject.getInt("id");
                                            mediaObjectCLassObj.setId(mediaId);
                                        }
                                        if (!mediaObject.isNull("url")) {
                                            String url = mediaObject.getString("url");
                                            mediaObjectCLassObj.setUrl(url);
                                        }
                                        if (!mediaObject.isNull("media_type")) {
                                            String media_type = mediaObject.getString("media_type");
                                            mediaObjectCLassObj.setMedia_type(media_type);
                                        }
                                        mediaList.add(mediaObjectCLassObj);
                                    }
                                    lastReplyClassObj.setMediaList(mediaList);
                                }
                                if (last_reply.isNull("media")) {

                                } else {
                                    if (last_reply.getJSONArray("media").length() > 0) {
                                        int media = Integer.parseInt(last_reply.getJSONArray("media").get(0).toString());
                                        lastReplyClassObj.setMedia(media);
                                    }
                                }
                                lastReplyList.add(lastReplyClassObj);
                                threadClassObj.setLastReplyList(lastReplyList);
                            }
                            ArrayList<MessageBoardModel.Thread.FirstPost> firstPostList = new ArrayList<MessageBoardModel.Thread.FirstPost>();
                            MessageBoardModel.Thread.FirstPost firstPostClassObj = new MessageBoardModel.Thread.FirstPost();
                            if (!threadObject.isNull("first_post")) {
                                JSONObject first_post = threadObject.getJSONObject("first_post");
                                if (!first_post.isNull("id")) {
                                    int first_postId = first_post.getInt("id");
                                    firstPostClassObj.setId(first_postId);
                                }
                                if (!first_post.isNull("created_date")) {
                                    String created_date_first_post = first_post.getString("created_date");
                                    firstPostClassObj.setCreated_date(created_date_first_post);
                                }
                                if (!first_post.isNull("posted_by_profile")) {
                                    int firstPostPostedByProfile = first_post.getInt("posted_by_profile");
                                    firstPostClassObj.setPosted_by_profile(firstPostPostedByProfile);
                                }
                                if (!first_post.isNull("posted_anonymously")) {
                                    boolean firstPostposted_anonymously = first_post.getBoolean("posted_anonymously");
                                    firstPostClassObj.setPosted_anonymously(firstPostposted_anonymously);
                                }
                                if (!first_post.isNull("thread")) {
                                    int firstPostThread = first_post.getInt("thread");
                                    firstPostClassObj.setThread(firstPostThread);
                                }
                                if (!first_post.isNull("text")) {
                                    String textFirstPost = first_post.getString("text");
                                    firstPostClassObj.setText(textFirstPost);
                                }
                                ArrayList<MessageBoardModel.Thread.MediaObjects> mediaListFP = new ArrayList<MessageBoardModel.Thread.MediaObjects>();
                                if (!first_post.isNull("media_objects")) {
                                    JSONArray mediaObjectArrayFirstPost = first_post.getJSONArray("media_objects");
                                    for (int j = 0; j < mediaObjectArrayFirstPost.length(); j++) {
                                        MessageBoardModel.Thread.MediaObjects mediaObjectCLassObjFP = new MessageBoardModel.Thread.MediaObjects();

                                        JSONObject mediaObjectFirstPost = mediaObjectArrayFirstPost.getJSONObject(j);
                                        if (!mediaObjectFirstPost.isNull("id")) {


                                            int mediaIdFirstPost = mediaObjectFirstPost.getInt("id");
                                            mediaObjectCLassObjFP.setId(mediaIdFirstPost);
                                        }
                                        if (!mediaObjectFirstPost.isNull("url")) {
                                            String urlFirstPost = mediaObjectFirstPost.getString("url");
                                            mediaObjectCLassObjFP.setUrl(urlFirstPost);
                                        }
                                        if (!mediaObjectFirstPost.isNull("media_type")) {
                                            String media_typeFirstPost = mediaObjectFirstPost.getString("media_type");

                                            mediaObjectCLassObjFP.setMedia_type(media_typeFirstPost);
                                        }
                                        mediaListFP.add(mediaObjectCLassObjFP);
                                    }
                                    firstPostClassObj.setMediaList(mediaListFP);
                                }
                                if (!first_post.isNull("media")) {
                                    if (first_post.getJSONArray("media").length() > 0) {
                                        int mediaFP = Integer.parseInt(first_post.getJSONArray("media").get(0).toString());
                                        firstPostClassObj.setMedia(mediaFP);
                                    }
                                }
                                firstPostList.add(firstPostClassObj);
                                threadClassObj.setFirstPostList(firstPostList);
                            }
                            if (!threadObject.isNull("number_of_replies")) {
                                int number_of_replies = threadObject.getInt("number_of_replies");
                                threadClassObj.setNumber_of_replies(number_of_replies);
                            }
                            ArrayList<MessageBoardModel.Thread> myThreadList = new ArrayList<MessageBoardModel.Thread>();
                            myThreadList.add(threadClassObj);
                            messageBoardModel.setThreadList(myThreadList);
                        }
                    }
                    ArrayList<MessageBoardModel.Profiles> profileList = new ArrayList<MessageBoardModel.Profiles>();
                    if (!mainObject.isNull("profiles")) {
                        JSONArray profileArray = mainObject.getJSONArray("profiles");
                        for (int k = 0; k < profileArray.length(); k++) {
                            MessageBoardModel.Profiles profileObj = new MessageBoardModel.Profiles();
                            JSONObject profileObject = profileArray.getJSONObject(k);
                            if (!profileObject.isNull("id")) {
                                int id = profileObject.getInt("id");
                                profileObj.setId(id);
                            }
                            if (!profileObject.isNull("username")) {
                                String username = profileObject.getString("username");
                                profileObj.setUserName(username);
                            }
                            if (!profileObject.isNull("is_verified")) {
                                boolean is_verified = profileObject.getBoolean("is_verified");
                                profileObj.setIsVerified(is_verified);
                            }
                            if (!profileObject.isNull("display_name")) {
                                String display_name = profileObject.getString("display_name");
                                profileObj.setDisplayName(display_name);
                            }
                            if (!profileObject.isNull("image_url")) {
                                String image_url = profileObject.getString("image_url");
                                profileObj.setImgUrl(image_url);
                            }
                            if (!profileObject.isNull("first_name")) {
                                String first_name = profileObject.getString("first_name");
                                profileObj.setFirstName(first_name);
                            }
                            if (!profileObject.isNull("last_name")) {
                                String last_name = profileObject.getString("last_name");
                                profileObj.setLastName(last_name);
                            }
                            if (!profileObject.isNull("rank")) {
                                String rank = profileObject.getString("rank");
                                profileObj.setRank(rank);
                            }
                            profileList.add(profileObj);
                        }
                        messageBoardModel.setProfileList(profileList);
                    }
                    messageBoardList.add(messageBoardModel);*/
                } catch (Exception e) {
                    Log.e("error", e.getMessage());
                    //        Toast.makeText(GroupThreadDisplayActivity.this, e.toString(), Toast.LENGTH_LONG).show();
                }
                //  Toast.makeText(GroupThreadDisplayActivity.this, messageBoardList.get(0).toString(), Toast.LENGTH_LONG).show();

                mAdapter = new MessageBoardAdapter(messageBoardModel, GroupThreadDisplayActivity.this);
                CustomLinearLayoutManager mLayoutManager = new CustomLinearLayoutManager(GroupThreadDisplayActivity.this);
                recyclerView.addItemDecoration(new DividerItemDecoration(GroupThreadDisplayActivity.this, DividerItemDecoration.VERTICAL_LIST));

                recyclerView.setLayoutManager(mLayoutManager);
                //recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            } else {

                //        Toast.makeText(GroupThreadDisplayActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header


            String response = "";
            try {
                // String url = "http://baselife-dev.herokuapp.com/api/1.0/threads";
                URL url = null;
                // String url = "http://baselife-dev.herokuapp.com/api/1.0/threads";
                if (countsaved) {

                    url = new URL("http://baselife-dev.herokuapp.com/api/1.0/threads?group=" + groupId + "&page=" + currentPage);
                } else {

                    url = new URL("http://baselife-dev.herokuapp.com/api/1.0/threads?group=" + groupId);
                }
                HttpURLConnection http_conn = (HttpURLConnection) url.openConnection();
                //http_conn.setRequestProperty("group", groupId + "");

                http_conn.setRequestProperty("Authorization: Token", access_token);
                //  http_conn.setRequestProperty(new BasicHeader("Authorization: Token", LoginActivity.access_token).toString());
                http_conn.setFollowRedirects(true);
                http_conn.setConnectTimeout(100000);
                http_conn.setReadTimeout(100000);
                http_conn.setInstanceFollowRedirects(true);
                int responseCode = http_conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(http_conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;

                    }

                } else {
                    response = "";

                }
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dhaval", e.getMessage());
            }

            return response;


        }


    }

}
