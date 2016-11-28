package com.example.baselife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.model.ProfileModel;
import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.Validate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    private TextView txtskip;
    private ProgressDialog progress;
    private TextView txtCreateAccount;
    private TextView txtLogin;
    private AlertDialog alertDialog;
    private EditText edtUserName;
    private EditText edtPassword;
    private String access_token;
    private String userName;
    private String password;
    private TextView tvForgotPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        setContentView(R.layout.activity_login);

        tvForgotPassword = (TextView) findViewById(R.id.txtfgpwd);
        tvForgotPassword.setOnClickListener(this);
        edtUserName = (EditText) findViewById(R.id.edtusername);
        edtPassword = (EditText) findViewById(R.id.edtpassword);
        txtskip = (TextView) findViewById(R.id.txtskip);
        txtskip.setOnClickListener(this);
        txtCreateAccount = (TextView) findViewById(R.id.txtcreateacc);
        txtCreateAccount.setOnClickListener(this);
        txtLogin = (TextView) findViewById(R.id.txtlogin);
        txtLogin.setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(
                LoginActivity.this).create();
        alertDialog.setTitle("BaseLife");
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        if (prefs.getBoolean("isLogin", false)) {
            finish();
            startActivity(new Intent(LoginActivity.this, MenuActivity.class));
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtfgpwd:

                startActivity(new Intent(LoginActivity.this, ForgotPasswordActivity.class));
                break;
            case R.id.txtskip:

                startActivity(new Intent(LoginActivity.this, BaseActivity.class));
                break;
            case R.id.txtcreateacc:

                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
                break;
            case R.id.txtlogin:
                if (Validate.Check_Lengh(edtUserName)) {

                    alertDialog.setMessage("Enter username.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // here you can add functions

                                }
                            });
                    alertDialog.show();
                } else if (Validate.Check_Lengh(edtPassword)) {

                    //
                    alertDialog
                            .setMessage("Enter Password");
                    alertDialog
                            .setButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                        }
                                    });
                    alertDialog.show();

                } else {


                    if (CheckInternet.isNetworkAvailable(LoginActivity.this)) {
                        // code here
                        new LoginAsyncTask().execute();
                    } else {
                        // code

                        Toast.makeText(LoginActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                    }

                }
                break;
        }
    }

    private class LoginAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(LoginActivity.this, "Base Life",
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
                Toast.makeText(LoginActivity.this, "Login success\n" + result1, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result1);
                    access_token = jsonObject.getString("access_token");
                    SharedPreferences.Editor editor = getSharedPreferences("baselife", MODE_PRIVATE).edit();
                    editor.putString("access_token", access_token);
                    editor.putString("username", userName);
                    editor.putString("password", password);
                    editor.putBoolean("isLogin",true);
                    editor.commit();
                } catch (Exception e) {
                    e.printStackTrace();

                }
                startActivity(new Intent(LoginActivity.this, HomeActivity.class));
                LoginActivity.this.finish();

            } else {
                Toast.makeText(LoginActivity.this, "Username and password do not match", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            String response = "";
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/oauth/token/");

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
                reqObj.put("username", edtUserName.getText().toString().trim());

                reqObj.put("password", edtPassword.getText().toString().trim());
                reqObj.put("grant_type", "password");

                reqObj.put("client_id", "9QvbXgbcZpwsOE1moIMtX0Tw1qJu4thx20WSWy0u");

                userName = edtUserName.getText().toString().trim();
                password = edtPassword.getText().toString().trim();
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
