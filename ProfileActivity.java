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
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.adapter.AddGroupAdapter;
import com.example.baselife.model.GroupModel;
import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.DividerItemDecoration;
import com.example.baselife.util.ImageLoader1;
import com.example.baselife.util.MultipartUtility;
import com.example.baselife.util.ProfileMultiPathUtility;
import com.example.baselife.util.Utility;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {
    private ProgressDialog progress;
    private TextView tvRankDisplayName;
    private TextView tvUserName;
    private String userName, rank, displayName;
    private ImageView ivProfileImage;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 2;
    private String userChoosenTask;
    private String resultImage;
    private String resultImageUrl = "";
    private ImageView ivMenu;
    private ImageView ivSettings;
    private String access_token;
    private JSONObject profileObject;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        SharedPreferences prefs = getSharedPreferences("baselife", MODE_PRIVATE);
        access_token = prefs.getString("access_token", null);
        tvRankDisplayName = (TextView) findViewById(R.id.tvrankdisplayname);
        tvUserName = (TextView) findViewById(R.id.tvusername);
        ivProfileImage = (ImageView) findViewById(R.id.ivprofileimage);
        ivProfileImage.setOnClickListener(this);
        ivMenu = (ImageView) findViewById(R.id.imgmenu);
        ivMenu.setOnClickListener(this);
        ivSettings = (ImageView) findViewById(R.id.imgsettings);
        ivSettings.setOnClickListener(this);


        if (CheckInternet.isNetworkAvailable(ProfileActivity.this)) {
            // code here
            new ProfileAsyncTask().execute();
        } else {
            // code

            Toast.makeText(ProfileActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {

            case R.id.ivprofileimage:
                selectImage();
                break;
            case R.id.imgmenu:
                ProfileActivity.this.finish();
                break;
            case R.id.imgsettings:

                openSettingsDialog(view);
                break;

        }
    }
    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Upload From Photos",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle("Choose Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(ProfileActivity.this);

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

    private void openSettingsDialog(final View view) {
        final CharSequence[] items = {Html.fromHtml("<font color='#1e90ff'>Change Base</font>"), Html.fromHtml("<font color='#1e90ff'>Edit Profile</font>"),
                Html.fromHtml("<font color='#1e90ff'>Turn On Content Filter</font>"), Html.fromHtml("<font color='red'>Log Out</font>"), Html.fromHtml("<font color='#1e90ff'>Cancel</font>")};

        AlertDialog.Builder builder = new AlertDialog.Builder(ProfileActivity.this);
        builder.setTitle(Html.fromHtml("<font color='grey'>Settings</font>"));
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {


               /* if (items[item].equals("Change Base")) {


                } else if (items[item].equals("Edit Profile")) {


                } else if (items[item].equals("Turn On Content Filter")) {

                }
                else if (items[item].equals("Log Out")) {

                }
                else if (items[item].equals("Cancel")) {
                    if (result)
                    dialog.dismiss();
                }*/

                switch (item) {

                    case 0:
                        Intent intent = new Intent(view.getContext(), SettingsBaseActivity.class);


                        startActivityForResult(intent, 1);
                        break;
                    case 1:
                        Intent editIntent =new Intent(ProfileActivity.this, EditProfileActivity.class);
                        editIntent.putExtra("profile",profileObject.toString());
                        startActivity(editIntent);
                        break;
                    case 2:

                        break;
                    case 3:

                        SharedPreferences.Editor editor = getSharedPreferences("baselife", MODE_PRIVATE).edit();
                        editor.putString("access_token", null);
                        editor.putString("username", null);
                        editor.putBoolean("isLogin", false);
                        editor.putString("password", null);
                        editor.commit();
                        ProfileActivity.this.finish();
                        break;
                    case 4:
                        ProfileActivity.this.finish();
                        break;

                }

            }
        });
        builder.show();
    }



    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == Activity.RESULT_OK) {
                String result = data.getStringExtra("result");
                Toast.makeText(ProfileActivity.this, "selected base is " + result, Toast.LENGTH_LONG).show();
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
        resultImageUrl= getImageUri(ProfileActivity.this,thumbnail).toString();
        resultImage = BitMapToString(thumbnail);
        Bitmap resized = Bitmap.createScaledBitmap(thumbnail, 100,100, true);
        ivProfileImage.setImageBitmap(resized);



        if (CheckInternet.isNetworkAvailable(ProfileActivity.this)) {
            // code here
            new MyAsyncTask().execute();
        } else {
            // code

            Toast.makeText(ProfileActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }


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

        Bitmap resized = Bitmap.createScaledBitmap(bm, 100, 100, true);
        ivProfileImage.setImageBitmap(resized);



        if (CheckInternet.isNetworkAvailable(ProfileActivity.this)) {
            // code here
            new MyAsyncTask().execute();
        } else {
            // code

            Toast.makeText(ProfileActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
        }

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
            progress = ProgressDialog.show(ProfileActivity.this, "Base Life",
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
                //Toast.makeText(ProfileActivity.this, result1, Toast.LENGTH_LONG).show();

                try {
                     profileObject = new JSONObject(result1);
                    if ((!profileObject.isNull("rank")) && (profileObject.has("rank"))) {
                        rank = profileObject.getString("rank");

                    }
                    if ((!profileObject.isNull("username") && (profileObject.has("username")))) {
                        userName = profileObject.getString("username");

                    }
                    if ((!profileObject.isNull("display_name")) && (profileObject.has("display_name"))) {
                        displayName = profileObject.getString("display_name");

                    }
                    tvRankDisplayName.setText(rank + " " + displayName);
                    tvUserName.setText(userName);
                } catch (Exception e) {

                }
            }

        }

        public String postData() {
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
                url = new URL("http://baselife-dev.herokuapp.com/api/1.0/profiles/me");
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

    public List<String> uploadImage() {
        // File uploadFile1 = new File(resultImageUrl);
        Log.e("dhaval", resultImageUrl);
        String charset = "UTF-8";

        String url;
        List<String> response = null;
      //  url = "http://baselife-dev.herokuapp.com/api/1.0/media";
        url = "http://baselife-dev.herokuapp.com/api/1.0/profiles/me/images";
        try {
            ProfileMultiPathUtility multipart = new ProfileMultiPathUtility(url, charset,access_token);

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
                Toast.makeText(ProfileActivity.this, result.get(0) + resultImageUrl, Toast.LENGTH_LONG).show();

            }catch(Exception e)
            {

            }
            // }
        }
    }
}
