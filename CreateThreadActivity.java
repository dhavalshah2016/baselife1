package com.example.baselife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.adapter.BaseAdapter;
import com.example.baselife.model.BaseModel;
import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.DividerItemDecoration;
import com.example.baselife.util.MultipartUtility;
import com.example.baselife.util.Utility;

import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.net.ssl.HttpsURLConnection;

public class CreateThreadActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progress;
    private ArrayList<BaseModel> baseModelList = new ArrayList<BaseModel>();
    private RecyclerView recyclerView;
    private BaseAdapter mAdapter;
    private int groupId;
    private String threadId;
    private int posted_by_profile;
    private TextView tvSelectGroup;
    private ImageView ivcamera;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    private String userChoosenTask;
    private String resultImage;
    private String resultImageUrl = "";
    private String displayName;
    private TextView tvUserName;
    private ImageView ivImage;
    private TextView tvPost;
    private EditText edtPost;
    private TextView tvcancel;
    private String access_token;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_thread);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);
        ivImage = (ImageView) findViewById(R.id.ivuserimage);
        tvSelectGroup = (TextView) findViewById(R.id.tvselectgroup);
        tvSelectGroup.setOnClickListener(this);
        ivcamera = (ImageView) findViewById(R.id.ivcamera);
        ivcamera.setOnClickListener(this);
        tvUserName = (TextView) findViewById(R.id.tvusername);
        tvPost = (TextView) findViewById(R.id.tvpost);
        tvPost.setOnClickListener(this);
        edtPost = (EditText) findViewById(R.id.edtpost);
        tvcancel = (TextView) findViewById(R.id.textcancel);
        tvcancel.setOnClickListener(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar); // Attaching the layout to the toolbar object
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle(null);
        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            groupId = extras.getInt("group");

            //The key argument here must match that used in the other activity
        }
       // Toast.makeText(CreateThreadActivity.this, groupId + "", Toast.LENGTH_LONG).show();

        // new myCreateThreadAsyncTask().execute();



        if (CheckInternet.isNetworkAvailable(CreateThreadActivity.this)) {
            // code here
            new ProfileAsyncTask().execute();
        } else {
            // code

            Toast.makeText(CreateThreadActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }


    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tvselectgroup:
                Intent intent = new Intent(view.getContext(), GroupActivity.class);
                intent.putExtra("group", groupId);

                startActivityForResult(intent, 1);
                break;
            case R.id.ivcamera:
                selectImage();
                break;
            case R.id.tvpost:

                if (CheckInternet.isNetworkAvailable(CreateThreadActivity.this)) {
                    // code here
                    new myCreateThreadAsyncTask().execute();
                } else {
                    // code

                    Toast.makeText(CreateThreadActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                }
                break;
            case R.id.textcancel:
                CreateThreadActivity.this.finish();
                break;


        }
    }


    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Upload From Photos",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(CreateThreadActivity.this);
        builder.setTitle("Choose Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(CreateThreadActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Upload From Photos")) {
                    userChoosenTask = "Upload From Photos";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                tvSelectGroup.setText(result);
                 groupId = data.getIntExtra("groupId",groupId);
            }
            if (resultCode == Activity.RESULT_CANCELED) {
                //Write your code if there's no result
            }
        } else if (requestCode == SELECT_FILE) {
            if (resultCode == Activity.RESULT_OK)

                onSelectFromGalleryResult(data);
        } else if (requestCode == REQUEST_CAMERA) {
            if (resultCode == Activity.RESULT_OK)
                onCaptureImageResult(data);
        }
    }

    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 90, bytes);

        File destination = new File(Environment.getExternalStorageDirectory(),
                "userimage.jpg");

        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        //   resultImageUrl=getRealPathFromURI(getPhotoFileUri("userimage.jpg", destination));
      //  resultImageUrl = getPhotoFileUri("userimage.jpg", destination).toString();
        resultImageUrl= getImageUri(CreateThreadActivity.this,thumbnail).toString();
        resultImage = BitMapToString(thumbnail);
        Bitmap resized = Bitmap.createScaledBitmap(thumbnail, 100,100, true);
        ivImage.setImageBitmap(resized);
    }

    public Uri getPhotoFileUri(String fileName, File mediaStorageDir) {

        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator
                + fileName));
    }
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
    }
    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos=new  ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG,100, baos);
        byte [] b=baos.toByteArray();
        String temp=null;
        try{
            System.gc();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
        }catch(Exception e){
            e.printStackTrace();
        }catch(OutOfMemoryError e){
            baos=new  ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG,50, baos);
            b=baos.toByteArray();
            temp=Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
    }

    @SuppressWarnings("deprecation")
    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {
            try {
                final Uri selectedImageUri = data.getData();
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                //   resultImageUrl=getRealPathFromURI(selectedImageUri);
                resultImageUrl = selectedImageUri.toString();
                resultImage = BitMapToString(bm);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        Bitmap resized = Bitmap.createScaledBitmap(bm, 100,100, true);
        ivImage.setImageBitmap(resized);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Upload From Photos"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private class ProfileAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(CreateThreadActivity.this, "Base Life",
                    "loading ... please wait", true);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String myResponse = myPostData();
            return myResponse;
        }

        @Override
        protected void onPostExecute(String result1) {
            progress.dismiss();
            Log.e("dhaval response", result1);
            if (result1.toString().length() > 1) {
                try {
                    JSONObject jsonObject = new JSONObject(result1);
                    posted_by_profile = jsonObject.getInt("id");
                    displayName = jsonObject.getString("display_name");
                } catch (Exception e) {

                }
              //  Toast.makeText(CreateThreadActivity.this, result1 + "", Toast.LENGTH_LONG).show();
                tvUserName.setText(displayName);
            } else {

       //         Toast.makeText(CreateThreadActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }

        }


        private String myPostData() {
            // Create a new HttpClient and Post Header
            String response = "";
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/api/1.0/profiles/me");

                HttpURLConnection http_conn = (HttpURLConnection) url.openConnection();
                http_conn.setFollowRedirects(true);
                http_conn.setConnectTimeout(100000);
                http_conn.setReadTimeout(100000);
                http_conn.setInstanceFollowRedirects(true);
                http_conn.setRequestProperty("Authorization: Token", access_token);
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

    private class myCreateThreadAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(CreateThreadActivity.this, "Base Life",
                    "loading ... please wait", true);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String myResponse = postDataTask();
            return myResponse;
        }

        @Override
        protected void onPostExecute(String result2) {
            progress.dismiss();
            Log.e("result", result2);
            if (result2.toString().length() > 1) {
                Toast.makeText(CreateThreadActivity.this, result2, Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result2);
                    threadId = jsonObject.getString("id");


                } catch (Exception e) {

                }
            } else {

  //              Toast.makeText(CreateThreadActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }



            if (CheckInternet.isNetworkAvailable(CreateThreadActivity.this)) {
                // code here
                new myReplyCreateThreadAsyncTask().execute();
            } else {
                // code

                Toast.makeText(CreateThreadActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
            }

        }


        private String postDataTask() {

            JSONObject jsonBody;
            String requestBody;
            HttpURLConnection urlConnection = null;
            URL url;
            String response = "";
            try {
                url = new URL("http://baselife-dev.herokuapp.com/api/1.0/threads");
                jsonBody = new JSONObject();
                jsonBody.put("posted_by_profile", posted_by_profile);
                jsonBody.put("posted_anonymously", false);
                jsonBody.put("group", " " + groupId);
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

    }

    private class myReplyCreateThreadAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(CreateThreadActivity.this, "Base Life",
                    "loading ... please wait", true);
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO Auto-generated method stub
            String myResponse = postDataMethod();
            return myResponse;
        }

        @Override
        protected void onPostExecute(String result3) {
            progress.dismiss();

            Log.e("result", result3);
            if (result3.toString().length() > 1) {
                Toast.makeText(CreateThreadActivity.this, result3 + "", Toast.LENGTH_LONG).show();
                try {
                    JSONObject jsonObject = new JSONObject(result3);
                } catch (Exception e) {

                }
            } else {

     //           Toast.makeText(CreateThreadActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }




            if (CheckInternet.isNetworkAvailable(CreateThreadActivity.this)) {
                // code here
                new MyAsyncTask().execute();
            } else {
                // code

                Toast.makeText(CreateThreadActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
            }


        }


        public String postDataMethod() {
            // Create a new HttpClient and Post Header
            /*String response = "";
            URL url;
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://baselife-dev.herokuapp.com/api/1.0/threads");
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setDoOutput(true);
                urlConnection.setDoInput(true);
                urlConnection.setRequestMethod("POST");
                urlConnection.setRequestProperty("Authorization: Token", LoginActivity.access_token);
               *//* DataOutputStream wr = new DataOutputStream(urlConnection.getOutputStream ());

                try {
                    JSONObject obj = new JSONObject();
                    obj.put("posted_by_profile" , "123");
                    obj.put("posted_anonymously" , "false");
                    obj.put("group",""+groupId);
                    wr.writeBytes(obj.toString());
                    Log.e("JSON Input", obj.toString());
                    wr.flush();
                    wr.close();
                } catch (JSONException ex) {
                    ex.printStackTrace();
                }
                urlConnection.connect();

                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;

                    }

                } else {
                    response = "";

                }
          //      os.close();
*//*
                List<NameValuePair> params = new ArrayList<NameValuePair>();
                params.add(new BasicNameValuePair("posted_by_profile", "123"));
                params.add(new BasicNameValuePair("posted_anonymously", "false"));
                params.add(new BasicNameValuePair("group", ""+groupId));

                OutputStream os = urlConnection.getOutputStream();
                BufferedWriter writer = new BufferedWriter(
                        new OutputStreamWriter(os, "UTF-8"));
                writer.write(getQuery(params));
                writer.flush();
                writer.close();



                int responseCode = urlConnection.getResponseCode();

                if (responseCode == HttpsURLConnection.HTTP_OK) {
                    String line;
                    BufferedReader br = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                    while ((line = br.readLine()) != null) {
                        response += line;

                    }

                } else {
                    response = "";

                }
                urlConnection.connect();
                os.close();
            } catch (Exception e) {

            }*/
            // return response;
            JSONArray emptyArray = new JSONArray();
            JSONObject jsonBody;
            String requestBody;
            HttpURLConnection urlConnection = null;
            URL url;
            String response = "";
            try {
                url = new URL("http://baselife-dev.herokuapp.com/api/1.0/posts");
                jsonBody = new JSONObject();
                jsonBody.put("posted_by_profile", posted_by_profile);
                jsonBody.put("posted_anonymously", false);
                jsonBody.put("thread", threadId);
                jsonBody.put("text", edtPost.getText().toString());
                jsonBody.put("media", emptyArray);
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

            urlConnection.setRequestProperty("Authorization: Token",access_token);

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

    }

    public List<String> uploadImage() {
        // File uploadFile1 = new File(resultImageUrl);
        Log.e("dhaval", resultImageUrl);
        String charset = "UTF-8";

        String url;
        List<String> response = null;
        url = "http://baselife-dev.herokuapp.com/api/1.0/media";
        try {
            MultipartUtility multipart = new MultipartUtility(url, charset,access_token);

            multipart.addHeaderField("User-Agent", "CodeJava");
            multipart.addHeaderField("Test-Header", "Header-Value");

            multipart.addFormField("description", "Cool Pictures");
            multipart.addFormField("keywords", "Java,upload,Spring");

            //  multipart.addFilePart("fileUpload", uploadFile1);

            ContentResolver cr = this.getContentResolver();
            String[] projection = {MediaStore.MediaColumns.DATA};
            Cursor cur = cr.query(Uri.parse(resultImageUrl), projection, null, null, null);
            if (cur != null) {
                cur.moveToFirst();
                String filePath = cur.getString(0);
                File imageFile = new File(filePath);
                if (imageFile.exists()) {
                    // do something if it exists
                    multipart.addFilePart("fileUpload", imageFile);
                } else {
                    // File was not found
                    Log.e("MMP", "Image not Found");
                }
            } else {
                // content Uri was invalid or some other error occurred
                Log.e("MMP", "Invalid content or some error occured");
            }
            response = multipart.finish();

            System.out.println("SERVER REPLIED:");

            for (String line : response) {
                System.out.println(line);
//                Toast.makeText(this, line + resultImageUrl, Toast.LENGTH_LONG).show();
            }
        } catch (IOException ex) {
            System.err.println(ex);
            // Toast.makeText(this, ex.getMessage(), Toast.LENGTH_LONG).show();
        }
        return response;


    }

    private class MyAsyncTask extends AsyncTask<Void, Void, List<String>> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
           /* progress = ProgressDialog.show(CreateThreadActivity.this, "Base Life",
                    "loading ... please wait", true);*/
        }

        @Override
        protected List<String> doInBackground(Void... params) {
            List<String> res=null;
            try {
                 res= uploadImage();

            } catch (Exception e) {

            }
            return res;

        }

        protected void onPostExecute(List<String> result) {
            //   progress.dismiss();
            // for (String line : result) {
            //  System.out.println(line);
            try {
                Toast.makeText(CreateThreadActivity.this, result.get(0) + resultImageUrl, Toast.LENGTH_LONG).show();
                CreateThreadActivity.this.finish();
            }catch(Exception e)
            {

            }
            // }
        }
    }
}
