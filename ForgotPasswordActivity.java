package com.example.baselife;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.Validate;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

import javax.net.ssl.HttpsURLConnection;

public class ForgotPasswordActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtUsername;
    private Button btnSend;
    private AlertDialog alertDialog;
    private ProgressDialog progress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        edtUsername=(EditText)findViewById(R.id.edtusername);
        btnSend=(Button)findViewById(R.id.btnsend);
        btnSend.setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(
                ForgotPasswordActivity.this).create();
        alertDialog.setTitle("BaseLife");
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.btnsend:
                if (Validate.Check_Lengh(edtUsername)) {

                    alertDialog.setMessage("Enter username.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // here you can add functions

                                }
                            });
                    alertDialog.show();
                }  else {

                    if (CheckInternet.isNetworkAvailable(ForgotPasswordActivity.this)) {
                        // code here
                        new ForgotPasswordAsyncTask().execute();
                    } else {
                        // code

                        Toast.makeText(ForgotPasswordActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                    }
                }
                break;
        }
    }
    private class ForgotPasswordAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(ForgotPasswordActivity.this, "Base Life",
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
                Toast.makeText(ForgotPasswordActivity.this, result1, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result1);

                } catch (Exception e) {
                    e.printStackTrace();

                }


            } else {
        //        Toast.makeText(ForgotPasswordActivity.this, "Username ", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            String response = "";
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/login/forgot");

                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(10000);
                conn.setConnectTimeout(15000);
                conn.setRequestMethod("POST");
                conn.setDoInput(true);
                conn.setDoOutput(true);
                conn.setRequestProperty("Content-Type", "application/json; charset=UTF-8");
               /* List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("username", "dhaval"));
                params.add(new BasicNameValuePair("password", "processor"));
               params.add(new BasicNameValuePair("grant_type", "password"));
                params.add(new BasicNameValuePair("client_id", "9QvbXgbcZpwsOE1moIMtX0Tw1qJu4thx20WSWy0u"));
*/
                JSONObject reqObj = new JSONObject();
                reqObj.put("username", edtUsername.getText().toString().trim());


                reqObj.put("grant_type", "username");

                reqObj.put("client_id", "9QvbXgbcZpwsOE1moIMtX0Tw1qJu4thx20WSWy0u");


                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream());
                writer.write(reqObj.toString());
                /*OutputStream os = conn.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));*/
                writer.flush();
                writer.close();
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
