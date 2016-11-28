package com.example.baselife;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
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
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.Utility;
import com.example.baselife.util.Validate;

import org.apache.http.NameValuePair;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

public class SignupActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText edtUserName;
    private EditText edtEmailAddress;
    private EditText edtRank;
    private EditText edtFirstName;
    private EditText edtLastName;
    private EditText edtPassword;
    private ImageView ivUserImage;
    private TextView txtNextStep;
    private AlertDialog alertDialog;
    private ProgressDialog progress;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private TextView textChoosePhoto;
    private ImageView ivImage;
    private String userChoosenTask;
    private String resultImage;
    private String resultImageUrl="";
    private String userName;
    private TextView txtCancel;
    private TextView txtEula;
    private String password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getInitialised();
    }

    public void getInitialised() {

        edtUserName = (EditText) findViewById(R.id.edtusername);
        edtEmailAddress = (EditText) findViewById(R.id.edtemailaddress);
        edtRank = (EditText) findViewById(R.id.edtrank);
        edtFirstName = (EditText) findViewById(R.id.edtfirstname);
        edtLastName = (EditText) findViewById(R.id.edtlastname);
        edtPassword = (EditText) findViewById(R.id.edtpassword);
        ivUserImage = (ImageView) findViewById(R.id.ivuserimage);
        txtNextStep = (TextView) findViewById(R.id.txtnextstep);
        txtNextStep.setOnClickListener(this);
        alertDialog = new AlertDialog.Builder(
                SignupActivity.this).create();
        alertDialog.setTitle("BaseLife");
        textChoosePhoto = (TextView) findViewById(R.id.txtchoosephoto);
        textChoosePhoto.setOnClickListener(this);
        ivImage = (ImageView) findViewById(R.id.ivuserimage);
        txtCancel = (TextView) findViewById(R.id.txtcancel);
        txtCancel.setOnClickListener(this);
        txtEula = (TextView) findViewById(R.id.txtagreecondition);
        txtEula.setOnClickListener(this);
        //  resultImage="";
        // resultImageUrl="";
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.txtnextstep:
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
                } else if (edtUserName.getText()
                        .toString().length() < 3) {
                    alertDialog
                            .setMessage("username must be 3  characters long");
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

                } else if (Validate.Check_Lengh(edtEmailAddress)) {

                    alertDialog.setMessage("Enter Email.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(
                                        DialogInterface dialog,
                                        int which) {

                                    // here you can
                                    // add functions

                                }
                            });
                    alertDialog.show();
                } else if (Validate.checkEmail(edtEmailAddress) == false) {
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

                } else if (Validate.Check_Lengh(edtRank)) {

                    alertDialog.setMessage("Enter Rank.");
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
                } else if (Validate.Check_Lengh(edtFirstName)) {

                    alertDialog.setMessage("Enter FirstName.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // here you can add functions

                                }
                            });
                    alertDialog.show();
                } else if (Validate.Check_Lengh(edtLastName)) {

                    alertDialog.setMessage("Enter LastName.");
                    alertDialog.setButton("OK",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog,
                                                    int which) {

                                    // here you can add
                                    // functions

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

                } else if (edtPassword.getText()
                        .toString().length() < 6) {
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



                    if (CheckInternet.isNetworkAvailable(SignupActivity.this)) {
                        // code here
                        new SignupAsyncTask().execute();
                    } else {
                        // code

                        Toast.makeText(SignupActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
                    }


                }


                break;
            case R.id.txtchoosephoto:
                selectImage();
                break;
            case R.id.txtcancel:
                SignupActivity.this.finish();
                break;
            case R.id.txtagreecondition:
                startActivity(new Intent(SignupActivity.this, EulaActivity.class));
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Upload From Photoes"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    public String BitMapToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] b = baos.toByteArray();
        String temp = null;
        try {
            System.gc();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        } catch (OutOfMemoryError e) {
            baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.JPEG, 50, baos);
            b = baos.toByteArray();
            temp = Base64.encodeToString(b, Base64.DEFAULT);
            Log.e("EWN", "Out of memory error catched");
        }
        return temp;
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Upload From Photoes",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(SignupActivity.this);
        builder.setTitle("Change Profile Image!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(SignupActivity.this);

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Upload From Photoes")) {
                    userChoosenTask = "Upload From Photoes";
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
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
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
        resultImageUrl = getImageUri(SignupActivity.this, thumbnail).toString();
        resultImage = BitMapToString(thumbnail);
        Bitmap resized = Bitmap.createScaledBitmap(thumbnail, 100, 100, true);
        ivImage.setImageBitmap(resized);
    }

    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(inContext.getContentResolver(), inImage, "Title", null);
        return Uri.parse(path);
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
        ivImage.setImageBitmap(resized);
    }

    private class SignupAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(SignupActivity.this, "Base Life",
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

                Toast.makeText(SignupActivity.this, "Sign up success\n" + result1, Toast.LENGTH_LONG).show();
                SharedPreferences.Editor editor = getSharedPreferences("baselife", MODE_PRIVATE).edit();

                editor.putString("username", userName);
                editor.putString("password", password);
                editor.commit();
                startActivity(new Intent(SignupActivity.this, HomeActivity.class));
                SignupActivity.this.finish();
            } else {
                Toast.makeText(SignupActivity.this, "please try again", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            String response = "";
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/api/1.0/profiles");

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
                reqObj.put("username", edtUserName.getText().toString());
                reqObj.put("first_name", edtFirstName.getText().toString());
                reqObj.put("last_name", edtLastName.getText().toString());
                reqObj.put("rank", edtRank.getText().toString());
                reqObj.put("email", edtEmailAddress.getText().toString());
                reqObj.put("password", edtPassword.getText().toString());
                if (resultImageUrl.length() > 0) {
                    reqObj.put("image_url", resultImageUrl);

                }
                userName = edtUserName.getText().toString();
                password = edtPassword.getText().toString();
                Log.e("json", reqObj.toString());
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

    public Uri getPhotoFileUri(String fileName, File mediaStorageDir) {

        return Uri.fromFile(new File(mediaStorageDir.getPath() + File.separator
                + fileName));
    }
   /* public String getRealPathFromURI(Uri uri) {
        String[] projection = { MediaStore.Images.Media.DATA };
        @SuppressWarnings("deprecation")
        Cursor cursor = managedQuery(uri, projection, null, null, null);
        int column_index = cursor
                .getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/

}
