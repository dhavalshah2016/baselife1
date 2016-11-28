package com.example.baselife;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.widget.Toast;

import com.example.baselife.adapter.BaseAdapter;
import com.example.baselife.adapter.SettingsBaseAdapter;
import com.example.baselife.model.BaseModel;
import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.DividerItemDecoration;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class SettingsBaseActivity extends AppCompatActivity {
    private ProgressDialog progress;
    public  ArrayList<BaseModel> baseModelList = new ArrayList<BaseModel>();
    private RecyclerView recyclerView;
    private SettingsBaseAdapter mAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_base);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);




        if (CheckInternet.isNetworkAvailable(SettingsBaseActivity.this)) {
            // code here
            new BaseAsyncTask().execute();
        } else {
            // code

            Toast.makeText(SettingsBaseActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }

    }
    private class BaseAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(SettingsBaseActivity.this, "Base Life",
                    "loading ... please wait", true);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String myResponse=     postData();
            return myResponse;
        }

        @Override
        protected void onPostExecute(String result1) {
            progress.dismiss();
            Log.e("dhaval response", result1);
            if(result1.toString().length()>1) {
                try {
                    JSONArray jsonArray = new JSONArray(result1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        BaseModel baseModel = new BaseModel();
                        baseModel.setBackground_photo_url(jsonArray.getJSONObject(i).getString("background_photo_url"));
                        baseModel.setCity(jsonArray.getJSONObject(i).getString("city"));
                        baseModel.setState(jsonArray.getJSONObject(i).getString("state"));
                        baseModel.setId(Integer.parseInt(jsonArray.getJSONObject(i).getString("id")));
                        baseModel.setName(jsonArray.getJSONObject(i).getString("name"));
                        baseModel.setLat(Double.parseDouble(jsonArray.getJSONObject(i).getString("geo_latitude")));
                        baseModel.setLng(Double.parseDouble(jsonArray.getJSONObject(i).getString("geo_longitude")));
                        baseModel.setGroup(Integer.parseInt(jsonArray.getJSONObject(i).getString("group")));
                        baseModelList.add(baseModel);
                    }
                }catch(Exception e)
                {

                }
                mAdapter = new SettingsBaseAdapter(baseModelList,SettingsBaseActivity.this);
                RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getApplicationContext());
                recyclerView.addItemDecoration(new DividerItemDecoration(SettingsBaseActivity.this, DividerItemDecoration.VERTICAL_LIST));

                recyclerView.setLayoutManager(mLayoutManager);
                recyclerView.setItemAnimator(new DefaultItemAnimator());
                recyclerView.setAdapter(mAdapter);
            }
            else
            {

        //        Toast.makeText(SettingsBaseActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            String response = "";
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/api/1.0/bases");

                HttpURLConnection http_conn = (HttpURLConnection)url.openConnection();
                http_conn.setFollowRedirects(true);
                http_conn.setConnectTimeout(100000);
                http_conn.setReadTimeout(100000);
                http_conn.setInstanceFollowRedirects(true);
                // conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               /* List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", "dhaval"));
                params.add(new BasicNameValuePair("password", "processor"));
               params.add(new BasicNameValuePair("grant_type", "password"));
                params.add(new BasicNameValuePair("client_id", "9QvbXgbcZpwsOE1moIMtX0Tw1qJu4thx20WSWy0u"));
*/
                /*JSONObject reqObj = new JSONObject();
                reqObj.put("username",edtUserName.getText().toString().trim());

                reqObj.put("password",edtPassword.getText().toString().trim());
                reqObj.put("grant_type","password");

                reqObj.put("client_id", "9QvbXgbcZpwsOE1moIMtX0Tw1qJu4thx20WSWy0u");*/


              /*  OutputStreamWriter writer= new OutputStreamWriter(conn.getOutputStream());
                writer.write(reqObj.toString());
                *//*OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));*//*
                writer.flush();
                writer.close();*/
                //   os.close();
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

                //  http_conn.connect();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("dhaval", response);
            }

            return response;
        }



    }

}
