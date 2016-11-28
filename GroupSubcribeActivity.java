package com.example.baselife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.baselife.util.CheckInternet;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class GroupSubcribeActivity extends AppCompatActivity {
    private ProgressDialog progress;
    private String access_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_subcribe);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);



        if (CheckInternet.isNetworkAvailable(GroupSubcribeActivity.this)) {
            // code here
            new GSAsyncTask().execute();
        } else {
            // code

            Toast.makeText(GroupSubcribeActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }

    }
    private class GSAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(GroupSubcribeActivity.this, "Base Life",
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
                Toast.makeText(GroupSubcribeActivity.this, "subscribe success\n" + result1, Toast.LENGTH_LONG).show();
               // startActivity(new Intent(GroupSubcribeActivity.this,HomeActivity.class));
            } else {
        //        Toast.makeText(GroupSubcribeActivity.this, "please try again", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            String response = "";
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/api/1.0/groups/101/subscribe");

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
