package com.example.baselife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.adapter.GroupAdapter;
import com.example.baselife.util.CheckInternet;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CreateGroupActivity extends AppCompatActivity implements View.OnClickListener{
private TextView txtSetParent;
    private TextView txtDone;
    private ProgressDialog progress;
    private EditText edtGrpName;
    private String groupName="";
    private AlertDialog alertDialog;
    private String access_token;
    private ImageView ivBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_group);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);
        txtDone=(TextView)findViewById(R.id.txtdone);
        txtDone.setOnClickListener(this);
        txtSetParent=(TextView)findViewById(R.id.tvsetparent);
        txtSetParent.setOnClickListener(this);
        edtGrpName=(EditText)findViewById(R.id.edtgroupname);

        alertDialog = new AlertDialog.Builder(
                CreateGroupActivity.this).create();
        alertDialog.setTitle("BaseLife");
        ivBack=(ImageView)findViewById(R.id.imgback);
        ivBack.setOnClickListener(this);

    }
    private class GSAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(CreateGroupActivity.this, "Base Life",
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
                Toast.makeText(CreateGroupActivity.this, "group created\n" + result1, Toast.LENGTH_LONG).show();
                // startActivity(new Intent(GroupSubcribeActivity.this,HomeActivity.class));
            } else {
        //        Toast.makeText(CreateGroupActivity.this, "please try again", Toast.LENGTH_LONG).show();
            }
            CreateGroupActivity.this.finish();
        }
        public String buildPostParameters(Object content) {
            String output = null;
            if ((content instanceof String) ||
                    (content instanceof JSONObject) ||
                    (content instanceof JSONArray)) {
                output = content.toString();
            } else if (content instanceof Map) {
                Uri.Builder builder = new Uri.Builder();
                HashMap hashMap = (HashMap) content;
                if (hashMap != null) {
                    Iterator entries = hashMap.entrySet().iterator();
                    while (entries.hasNext()) {
                        Map.Entry entry = (Map.Entry) entries.next();
                        builder.appendQueryParameter(entry.getKey().toString(), entry.getValue().toString());
                        entries.remove(); // avoids a ConcurrentModificationException
                    }
                    output = builder.build().getEncodedQuery();
                }
            }

            return output;
        }

        public URLConnection makeRequest(String method, String apiAddress, String accessToken, String mimeType, String requestBody) throws IOException {
            URL url = new URL(apiAddress);
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();

            urlConnection.setDoInput(true);
            urlConnection.setDoOutput(!method.equals("GET"));
            urlConnection.setRequestMethod(method);

            urlConnection.setRequestProperty("Authorization: Token", access_token);

            urlConnection.setRequestProperty("Content-Type", mimeType);
            OutputStream outputStream = new BufferedOutputStream(urlConnection.getOutputStream());
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(outputStream, "utf-8"));
            writer.write(requestBody);
            writer.flush();
            writer.close();
            outputStream.close();

            urlConnection.connect();

            return urlConnection;
        }

        public String postData() {
            // Create a new HttpClient and Post Header
            JSONObject jsonBody;
            String response = "";
            String requestBody;
            HttpURLConnection urlConnection = null;
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/api/1.0/groups");


                jsonBody = new JSONObject();
                jsonBody.put("name", groupName);
                jsonBody.put("parent_group", GroupAdapter.mySelectedGroup.getId());

                requestBody = buildPostParameters(jsonBody);
                urlConnection = (HttpURLConnection) makeRequest("POST", url.toString(), null, "application/json", requestBody);
                // the same logic to case #1
                InputStream inputStream;

                // get stream
                if (urlConnection.getResponseCode() < HttpURLConnection.HTTP_BAD_REQUEST) {
                    inputStream = urlConnection.getInputStream();
                } else {
                    inputStream = urlConnection.getErrorStream();
                }
                // parse stream
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String temp;
                while ((temp = bufferedReader.readLine()) != null) {
                    response += temp;
                }
                return response;
            } catch (JSONException | IOException e) {
                e.printStackTrace();
                return e.toString();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
            }
        }


    }
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtdone:
                groupName= edtGrpName.getText().toString();
                if(txtSetParent.getText().toString().equalsIgnoreCase("Set Parent..."))
                {
                    alertDialog.setMessage("Select Parent Group");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // here you can add functions

                                }
                            });
                    alertDialog.show();
                }
                else
                if(groupName.toString().length()<1) {
                    alertDialog.setMessage("Enter GroupName.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // here you can add functions

                                }
                            });
                    alertDialog.show();
                }else
                {



                    if (CheckInternet.isNetworkAvailable(CreateGroupActivity.this)) {
                        // code here
                        new GSAsyncTask().execute();
                    } else {
                        // code

                        Toast.makeText(CreateGroupActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                    }

                }

                break;
            case R.id.tvsetparent:
                Intent intent = new Intent(view.getContext(), GroupActivity.class);


                startActivityForResult(intent, 1);
                break;
            case R.id.imgback:
                CreateGroupActivity.this.finish();
                break;



        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                txtSetParent.setText(result);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        }
    }
}
