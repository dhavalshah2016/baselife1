package com.example.baselife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.adapter.AddGroupAdapter;
import com.example.baselife.adapter.GroupAdapter;
import com.example.baselife.model.GroupModel;
import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.DividerItemDecoration;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class AddGroupActivity extends AppCompatActivity implements View.OnClickListener  {
    private ProgressDialog progress;
    private ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
    private RecyclerView recyclerView;
    private AddGroupAdapter mAdapter;
    private ImageView imgBack;
    private TextView txtDone;
    private EditText edtSearch;
    private boolean searchFlag=false;
    private String searchKeyWord="";
    public static LinearLayout layout1,layout2;
    public static TextView searchresulttext1,searchresulttext2;
    private TextView txtcreateGroup;
    private String access_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);

        txtcreateGroup= (TextView)findViewById(R.id.txtcreatgrp);
        txtcreateGroup.setOnClickListener(this);
        layout1 =(LinearLayout)findViewById(R.id.layout1);
        layout2 =(LinearLayout)findViewById(R.id.layout2);
        searchresulttext1=(TextView)findViewById(R.id.tvgrpname);
        searchresulttext2=(TextView)findViewById(R.id.tvsrchgrpname);
        AddGroupActivity.layout1.setVisibility(View.VISIBLE);
        AddGroupActivity.layout2.setVisibility(View.GONE);
        edtSearch=(EditText)findViewById(R.id.edtsearch);
        edtSearch.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence cs, int arg1, int arg2, int arg3) {
                // When user changed the Text
                AddGroupActivity.layout1.setVisibility(View.VISIBLE);
                AddGroupActivity.layout2.setVisibility(View.GONE);
                searchFlag = true;
                searchKeyWord=cs.toString();



                if (CheckInternet.isNetworkAvailable(AddGroupActivity.this)) {
                    // code here
                    new DisplayGroupAsyncTask().execute();
                } else {
                    // code

                    Toast.makeText(AddGroupActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                }



            }

            @Override
            public void beforeTextChanged(CharSequence arg0, int arg1, int arg2,
                                          int arg3) {
                // TODO Auto-generated method stub
                searchFlag = true;
            }

            @Override
            public void afterTextChanged(Editable arg0) {
                // TODO Auto-generated method stub
                searchFlag = true;
            }
        });
        imgBack=(ImageView)findViewById(R.id.imgback);
        imgBack.setOnClickListener(this);
        txtDone=(TextView)findViewById(R.id.txtdone);
        txtDone.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        searchFlag=false;




        if (CheckInternet.isNetworkAvailable(AddGroupActivity.this)) {
            // code here
            new DisplayGroupAsyncTask().execute();
        } else {
            // code

            Toast.makeText(AddGroupActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }

    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.imgback:

                AddGroupActivity.this.finish();
                break;
            case R.id.txtdone:
                if(layout2.isShown()) {



                    if (CheckInternet.isNetworkAvailable(AddGroupActivity.this)) {
                        // code here
                        new GSAsyncTask().execute();
                    } else {
                        // code

                        Toast.makeText(AddGroupActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                    }


                }
                break;
            case R.id.txtcreatgrp:
                startActivity(new Intent(AddGroupActivity.this,CreateGroupActivity.class));
                AddGroupActivity.this.finish();
                break;
        }
    }
    private class DisplayGroupAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(AddGroupActivity.this, "Base Life",
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

                try {
                    JSONArray jsonArray = new JSONArray(result1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject= jsonArray.getJSONObject(i);
                        GroupModel groupModel = new GroupModel();
                        if(!jsonObject.isNull("id"))
                            groupModel.setId(jsonObject.getInt("id"));
                        if(!jsonObject.isNull("name"))
                            groupModel.setName(jsonObject.getString("name"));
                        if(!jsonObject.isNull("is_subscribed_to"))
                            groupModel.setIsSubscribedTo(jsonObject.getInt("is_subscribed_to"));
                        if(!jsonObject.isNull("parent_group"))
                            groupModel.setParentGroup(jsonObject.getInt("parent_group"));
                        groupList.add(groupModel);
                    }
                    mAdapter = new AddGroupAdapter(groupList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.addItemDecoration(new DividerItemDecoration(AddGroupActivity.this, DividerItemDecoration.VERTICAL_LIST));

                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                    Toast.makeText(AddGroupActivity.this, groupList.get(0).getName(), Toast.LENGTH_LONG).show();
                    searchFlag=false;
                }catch(Exception e)
                {
        //            Toast.makeText(AddGroupActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            } else {

        //        Toast.makeText(AddGroupActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            URL url=null;

            String response = "";
            try {
                if(searchFlag) {
                    groupList.clear();
                    // String url = "http://baselife-dev.herokuapp.com/api/1.0/threads";
                    url = new URL("http://baselife-dev.herokuapp.com/api/1.0/groups" + "?search=" + searchKeyWord);

                }
                else
                {
                    groupList.clear();
                    url = new URL("http://baselife-dev.herokuapp.com/api/1.0/groups");
                }
                HttpURLConnection http_conn = (HttpURLConnection) url.openConnection();


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
                Log.e("dhaval", response);
            }

            return response;


        }


    }
    private class GSAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(AddGroupActivity.this, "Base Life",
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
                Toast.makeText(AddGroupActivity.this, "subscribe success\n" + result1, Toast.LENGTH_LONG).show();
                // startActivity(new Intent(GroupSubcribeActivity.this,HomeActivity.class));
            } else {
          //      Toast.makeText(AddGroupActivity.this, "please try again", Toast.LENGTH_LONG).show();
            }
            AddGroupActivity.this.finish();
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            String response = "";
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/api/1.0/groups/"+AddGroupAdapter.mySelectedGroup.getId()+"/subscribe");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Authorization: Token", access_token);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               /* List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", "dhaval"));
                params.add(new BasicNameValuePair("password", "processor"));
               params.add(new BasicNameValuePair("grant_type", "password"));
                params.add(new BasicNameValuePair("client_id", "9QvbXgbcZpwsOE1moIMtX0Tw1qJu4thx20WSWy0u"));
*/
               /* JSONObject reqObj = new JSONObject();
               *//* reqObj.put("username", edtUserName.getText().toString());
                reqObj.put("first_name", edtFirstName.getText().toString());
                reqObj.put("last_name", edtLastName.getText().toString());
                reqObj.put("rank", edtRank.getText().toString());
                reqObj.put("email", edtEmailAddress.getText().toString());
                reqObj.put("password", edtPassword.getText().toString());*//*


                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(reqObj.toString());
                *//*OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));*//*
                writer.flush();
                writer.close();*/
                //   os.close();
                int responseCode = conn.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;

                    }

                } else {
                    response = "";

                }

                conn.connect();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dhaval", response);
            }

            return response;
        }


    }
}
