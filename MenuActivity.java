package com.example.baselife;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.model.ProfileModel;
import com.example.baselife.util.CheckInternet;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import javax.net.ssl.HttpsURLConnection;

public class MenuActivity extends AppCompatActivity implements View.OnClickListener {
    public static int groupId;
    private TextView tvPnn;
    private int id;
    private ProgressDialog progress;
    public static ArrayList<ProfileModel> profileList = new ArrayList<ProfileModel>();
    private RelativeLayout userLayout;
    private TextView tvUserName;
    private TextView tvBaseName;
    public static String groupName;
    private String access_token;
    private String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);
        username = prefs.getString("username", null);
        tvPnn = (TextView) findViewById(R.id.pnn);
        userLayout = (RelativeLayout) findViewById(R.id.userlayout);
        userLayout.setOnClickListener(this);
        tvBaseName = (TextView) findViewById(R.id.tvbasename);
        tvUserName = (TextView) findViewById(R.id.tvusername);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getInt("group");
            groupName = extras.getString("groupName");
            //The key argument here must match that used in the other activity
            SharedPreferences.Editor editor = getSharedPreferences("baselife", MODE_PRIVATE).edit();
            editor.putInt("groupId", groupId);
            editor.putString("groupName", "" + groupName);
            editor.commit();
        }
        tvBaseName.setText(groupName);

        tvUserName.setText(username);


        tvPnn.setOnClickListener(this);



        if (CheckInternet.isNetworkAvailable(MenuActivity.this)) {
            // code here
            new ProfileAsyncTask().execute();
        } else {
            // code

            Toast.makeText(MenuActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.pnn:
                Intent intent = new Intent(view.getContext(), DisplayThreadActivity.class);
                intent.putExtra("group", groupId);

                view.getContext().startActivity(intent);
                break;
            case R.id.userlayout:
                SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
                if (prefs.getBoolean("isLogin", false)) {
                    finish();
                    Intent intentUserLayout = new Intent(view.getContext(), ProfileActivity.class);
                    //intent.putExtra("group", groupId);

                    view.getContext().startActivity(intentUserLayout);
                }

                break;
        }
    }

    private class ProfileAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(MenuActivity.this, "Base Life",
                    "loading ... please wait", true);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String myResponse = postDataProfile();
            return myResponse;
        }

        @Override
        protected void onPostExecute(String result1) {
            progress.dismiss();
            Log.e("dhatushah", result1);
            if (result1.toString().length() > 1) {
                // Toast.makeText(MenuActivity.this, result1, Toast.LENGTH_LONG).show();
                try {

                    JSONArray jsonArray = new JSONArray(result1);
                    for (int i = 0; i < jsonArray.length(); i++) {
                        ProfileModel profileModel = new ProfileModel();
                        JSONObject profileObject = jsonArray.getJSONObject(i);
                        if (!profileObject.isNull("rank")) {
                            profileModel.setRank(profileObject.getString("rank"));
                        }


                        if (!profileObject.isNull("id")) {
                            profileModel.setId(profileObject.getInt("id"));
                        }
                        if (!profileObject.isNull("username")) {
                            profileModel.setUserName(profileObject.getString("username"));
                        }
                        if (!profileObject.isNull("is_verified")) {
                            profileModel.setIsVerified(profileObject.getBoolean("is_verified"));
                        }
                        if (!profileObject.isNull("display_name")) {
                            profileModel.setDisplayName(profileObject.getString("display_name"));
                        }
                        if (!profileObject.isNull("image_url")) {
                            profileModel.setImgUrl(profileObject.getString("image_url"));
                        }

                        if (!profileObject.isNull("first_name")) {
                            profileModel.setFirstName(profileObject.getString("first_name"));
                        }
                        if (!profileObject.isNull("last_name")) {
                            profileModel.setLastName(profileObject.getString("last_name"));
                        }
                        profileList.add(profileModel);
                    }

                } catch (Exception e) {
                    //new DisplayThreadAsyncTask().execute();
                }
            }

        }

        public String postDataProfile() {
            // Create a new HttpClient and Post Header
            URL url = null;

            String response = "";
            try {
               /* if (searchFlag) {
                    groupList.clear();*/
                // String url = "http://baselife-dev.herokuapp.com/api/1.0/threads";
                //    url = new URL("http://baselife-dev.herokuapp.com/api/1.0/groups" + "?search=" + searchKeyWord);

                // } else {
                // groupList.clear();
                url = new URL("http://baselife-dev.herokuapp.com/api/1.0/profiles");
                //   }
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
