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
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.adapter.AddGroupAdapter;
import com.example.baselife.adapter.GroupAdapter;
import com.example.baselife.adapter.MyGroupAdapter;
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

public class MyGroupActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progress;
    private ArrayList<GroupModel> groupList = new ArrayList<GroupModel>();
    private RecyclerView recyclerView;
    private MyGroupAdapter mAdapter;
    private ImageView ivBack;
    private ImageView ivPlus;
    private TextView txtFind;
    private TextView txtCreate;
    private String access_token;
    private ArrayList<GroupModel> totalGroupList = new ArrayList<GroupModel>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_group);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        ivBack = (ImageView) findViewById(R.id.imgback);
        ivBack.setOnClickListener(this);
        ivPlus = (ImageView) findViewById(R.id.ivplus);
        ivPlus.setOnClickListener(this);
        txtFind = (TextView) findViewById(R.id.txtfind);
        txtFind.setOnClickListener(this);
        txtCreate = (TextView) findViewById(R.id.txtcreate);
        txtCreate.setOnClickListener(this);
        try {

            if (CheckInternet.isNetworkAvailable(MyGroupActivity.this)) {
                // code here
                new DisplayGroupAsyncTask().execute();
            } else {
                // code

                Toast.makeText(MyGroupActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
            }



        }catch(NullPointerException e)
        {

        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgback:
                MyGroupActivity.this.finish();
                break;

            case R.id.ivplus:
                Intent intentcreate1 = new Intent(view.getContext(), CreateGroupActivity.class);

                view.getContext().startActivity(intentcreate1);
                break;

            case R.id.txtcreate:
                Intent intentcreate2 = new Intent(view.getContext(), CreateGroupActivity.class);

                view.getContext().startActivity(intentcreate2);
                break;

            case R.id.txtfind:
                Intent intent = new Intent(view.getContext(), AddGroupActivity.class);

                view.getContext().startActivity(intent);
                break;

        }
    }

    private class DisplayMyGroupAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(MyGroupActivity.this, "Base Life",
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
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        GroupModel groupModel = new GroupModel();
                        if (!jsonObject.isNull("id"))
                            groupModel.setId(jsonObject.getInt("id"));
                        if (!jsonObject.isNull("name"))
                            groupModel.setName(jsonObject.getString("name"));
                        if (!jsonObject.isNull("is_subscribed_to"))
                            groupModel.setIsSubscribedTo(jsonObject.getInt("is_subscribed_to"));
                        if (!jsonObject.isNull("parent_group"))
                            groupModel.setParentGroup(jsonObject.getInt("parent_group"));
                        groupList.add(groupModel);
                    }

                    mAdapter = new MyGroupAdapter(groupList,totalGroupList);
                    RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                    recyclerView.addItemDecoration(new DividerItemDecoration(MyGroupActivity.this, DividerItemDecoration.VERTICAL_LIST));

                    recyclerView.setLayoutManager(mLayoutManager);
                    recyclerView.setItemAnimator(new DefaultItemAnimator());
                    recyclerView.setAdapter(mAdapter);
                  //  Toast.makeText(MyGroupActivity.this, result1, Toast.LENGTH_LONG).show();
                } catch (Exception e) {
      //              Toast.makeText(MyGroupActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            } else {

    //            Toast.makeText(MyGroupActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header


            String response = "";
            try {
                // String url = "http://baselife-dev.herokuapp.com/api/1.0/threads";
                URL url = new URL("http://baselife-dev.herokuapp.com/api/1.0/groups/me");
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
    private class DisplayGroupAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(MyGroupActivity.this, "Base Life",
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
                        totalGroupList.add(groupModel);
                    }

                }catch(Exception e)
                {
                    //            Toast.makeText(AddGroupActivity.this, "Try again", Toast.LENGTH_LONG).show();
                }
            } else {

                //        Toast.makeText(AddGroupActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }
            if (CheckInternet.isNetworkAvailable(MyGroupActivity.this)) {
                // code here
                new DisplayMyGroupAsyncTask().execute();
            } else {
                // code

                Toast.makeText(MyGroupActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            URL url=null;

            String response = "";
            try {

                {
                    totalGroupList.clear();
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
}
