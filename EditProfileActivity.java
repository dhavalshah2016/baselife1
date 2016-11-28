package com.example.baselife;

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
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.HttpPatch;
import com.example.baselife.util.Validate;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.SocketTimeoutException;
import java.net.URL;

import org.apache.http.message.AbstractHttpMessage;
import org.apache.http.client.methods.HttpRequestBase;
import org.apache.http.client.methods.HttpEntityEnclosingRequestBase;

import javax.net.ssl.HttpsURLConnection;

public class EditProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtUserName;
    private EditText edtEmailAddress;
    private EditText edtRank;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtPassword;
    private AlertDialog alertDialog;
    private TextView txtSave;
    private ImageView imgBack;
    private ProgressDialog progress;
    private boolean flagEmail = false, flagUserName = false, flagPassword = false;
    private String access_token;
    private JSONObject profileObject;
    private String rank;
    private String userName;
    private String displayName;
    private String email;
    private String firstName;
    private String lastName;
    private String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);
        password = prefs.getString("password", null);
        Bundle extras = getIntent().getExtras();
        getInitialised();
        if (extras != null) {
            try {
                profileObject = new JSONObject(extras.getString("profile"));
                if ((!profileObject.isNull("rank")) && (profileObject.has("rank"))) {
                    rank = profileObject.getString("rank");

                }
                if ((!profileObject.isNull("username") && (profileObject.has("username")))) {
                    userName = profileObject.getString("username");

                }
                if ((!profileObject.isNull("display_name")) && (profileObject.has("display_name"))) {
                    displayName = profileObject.getString("display_name");

                }
                if ((!profileObject.isNull("email")) && (profileObject.has("email"))) {
                    email = profileObject.getString("email");

                }
                if ((!profileObject.isNull("first_name")) && (profileObject.has("first_name"))) {
                    firstName = profileObject.getString("first_name");

                }
                if ((!profileObject.isNull("last_name")) && (profileObject.has("last_name"))) {
                    lastName = profileObject.getString("last_name");

                }
                edtFirstName.setText(firstName);
                edtLastName.setText(lastName);
                edtPassword.setText(password);
                edtRank.setText(rank);
                edtUserName.setText(userName);
            } catch (Exception e) {

            }

            //The key argument here must match that used in the other activity
        }
    }

    public void getInitialised() {
        txtSave = (TextView) findViewById(R.id.txtsave);

        txtSave.setOnClickListener(this);
        imgBack = (ImageView) findViewById(R.id.imgback);
        imgBack.setOnClickListener(this);
        edtUserName = (EditText) findViewById(R.id.edtusername);
        edtEmailAddress = (EditText) findViewById(R.id.edtemail);
        edtRank = (EditText) findViewById(R.id.edtrank);
        edtFirstName = (EditText) findViewById(R.id.edtfirstname);
        edtLastName = (EditText) findViewById(R.id.edtlastname);
        edtPassword = (EditText) findViewById(R.id.edtpassword);
        flagEmail = false;
        flagUserName = false;
        flagPassword = false;
        alertDialog = new AlertDialog.Builder(
                EditProfileActivity.this).create();
        alertDialog.setTitle("BaseLife");

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.imgback:
                EditProfileActivity.this.finish();
                break;

            case R.id.txtsave:
                if ((edtUserName.getText()
                        .toString().length() > 0) && (edtUserName.getText()
                        .toString().length() < 3)) {
                    flagUserName = false;
                    alertDialog
                            .setMessage("Username must be 3  characters long");
                    alertDialog
                            .setButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                            // here
                                            // you
                                            // can
                                            // add
                                            // functions

                                        }
                                    });
                    alertDialog.show();

                } else {
                    flagUserName = true;
                }

                if ((Validate.checkEmail(edtEmailAddress) == false) && (edtEmailAddress.getText().toString().length() != 0)) {
                    flagEmail = false;
                    alertDialog.setMessage("Enter Valid Email.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {

                                    // here
                                    // you
                                    // can
                                    // add
                                    // functions

                                }
                            });
                    alertDialog.show();

                } else {
                    flagEmail = true;
                }
                if ((edtPassword.getText()
                        .toString().length() > 0) && (edtPassword.getText()
                        .toString().length() < 6)) {
                    flagPassword = false;
                    alertDialog
                            .setMessage("Password must be 6 characters long");
                    alertDialog
                            .setButton(
                                    "OK",
                                    new DialogInterface.OnClickListener() {
                                        public void onClick(
                                                DialogInterface dialog,
                                                int which) {

                                            // here
                                            // you
                                            // can
                                            // add
                                            // functions

                                        }
                                    });
                    alertDialog.show();

                } else {
                    flagPassword = true;
                }

                if (flagPassword && flagEmail && flagUserName)

                {
                    if (CheckInternet.isNetworkAvailable(EditProfileActivity.this)) {
                        // code here
                        new EditProfileAsyncTask().execute();
                    } else {
                        // code

                        Toast.makeText(EditProfileActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                    }
                }


                break;
          /*  JSONObject reqObj = new JSONObject();
            if(edtUserName.getText().toString().length()>0) {
                reqObj.put("username", edtUserName.getText().toString());
            }
            if(edtFirstName.getText().toString().length()>0) {
                reqObj.put("first_name", edtFirstName.getText().toString());
            }
            if(edtLastName.getText().toString().length()>0) {
                reqObj.put("last_name", edtLastName.getText().toString());
            }
            if(edtRank.getText().toString().length()>0) {
                reqObj.put("rank", edtRank.getText().toString());
            }
            if(edtEmailAddress.getText().toString().length()>0) {
                reqObj.put("email", edtEmailAddress.getText().toString());
            }
            if(edtPassword.getText().toString().length()>0) {
                reqObj.put("password", edtPassword.getText().toString());
            }*/

        }
    }

    private class EditProfileAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(EditProfileActivity.this, "Base Life",
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
            if ((result1.toString().equalsIgnoreCase("Invalid")) || (result1.toString().length() < 1)) {

                Toast.makeText(EditProfileActivity.this, result1.toString(), Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(EditProfileActivity.this, result1, Toast.LENGTH_LONG).show();

                EditProfileActivity.this.finish();

            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            InputStream is = null;
            HttpClient httpClient = new DefaultHttpClient();
            String strUrl = "http://baselife-dev.herokuapp.com/api/1.0/profiles/me";
            try {
                HttpPatch request = new HttpPatch(strUrl);
                request.setHeader("Authorization: Token", access_token);
                //  request.setHeader("X-HTTP-Method-Override", "PATCH");
                JSONObject reqObj = new JSONObject();
                if (edtUserName.getText().toString().length() > 0) {
                    reqObj.put("username", edtUserName.getText().toString());
                }
                if (edtFirstName.getText().toString().length() > 0) {
                    reqObj.put("first_name", edtFirstName.getText().toString());
                }
                if (edtLastName.getText().toString().length() > 0) {
                    reqObj.put("last_name", edtLastName.getText().toString());
                }
                if (edtRank.getText().toString().length() > 0) {
                    reqObj.put("rank", edtRank.getText().toString());
                }
                if (edtEmailAddress.getText().toString().length() > 0) {
                    reqObj.put("email", edtEmailAddress.getText().toString());
                }
                if (edtPassword.getText().toString().length() > 0) {
                    reqObj.put("password", edtPassword.getText().toString());
                }
                StringEntity params = new StringEntity(reqObj.toString(), "UTF-8");
                //request.addHeader("content-type", "application/x-www-form-urlencoded");
                params.setContentType("application/json");
                request.setEntity(params);
                HttpResponse response = httpClient.execute(request);

                HttpEntity entity1 = response.getEntity();
                is = entity1.getContent();
                int resopnceStatus = response.getStatusLine().getStatusCode();


                if (resopnceStatus != 200) {
                    return "Invalid";
                }
            } catch (IllegalArgumentException timeout) {
                timeout.printStackTrace();
                return "Invalid";
            } catch (SocketTimeoutException timeout) {
                timeout.printStackTrace();
                return "Invalid";
            } catch (Exception e) {
                e.printStackTrace();
                return "Invalid";
            }

            String response = "";
            String s = "";
            try {
                BufferedReader buffer = new BufferedReader(new InputStreamReader(is));
                while ((s = buffer.readLine()) != null) {
                    response += s;
                }
            } catch (Exception e) {

            }
            return response;
        }

    }
}
