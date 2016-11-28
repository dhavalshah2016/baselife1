package com.example.baselife;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.baselife.adapter.BaseAdapter;
import com.example.baselife.model.BaseModel;
import com.example.baselife.util.CheckInternet;
import com.example.baselife.util.DividerItemDecoration;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;

import org.json.JSONArray;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class HomeActivity extends AppCompatActivity implements View.OnClickListener, LocationListener,
        GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private Dialog mPopDialog = null;
    private TextView locationText;
    private static final String TAG = "LocationActivity";
    private static final long INTERVAL = 1000 * 10;
    private static final long FASTEST_INTERVAL = 1000 * 5;
    String lat = "0.0", lon = "0.0";
    Location mLastLocation;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    Location mCurrentLocation;
    String mLastUpdateTime;
    boolean flag = false;
    private TextView manualtext;
    private ProgressDialog progress;
    private ArrayList<BaseModel> baseModelList = new ArrayList<BaseModel>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        locationText = (TextView) findViewById(R.id.findlocationtext);
        locationText.setOnClickListener(this);
        manualtext = (TextView) findViewById(R.id.findbasetextmanually);
        manualtext.setOnClickListener(this);
        buildGoogleApiClient();
    }

    synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API)
                .build();
        mGoogleApiClient.connect();


    }

    @Override
    public void onConnected(Bundle bundle) {


        mLocationRequest = LocationRequest.create();
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        mLocationRequest.setInterval(100); // Update location every second

        //  LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);


        mLastLocation = LocationServices.FusedLocationApi.getLastLocation(
                mGoogleApiClient);
        if (mLastLocation != null) {
            lat = String.valueOf(mLastLocation.getLatitude());
            lon = String.valueOf(mLastLocation.getLongitude());

        }
        //  updateUI();
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onLocationChanged(Location location) {
        lat = String.valueOf(location.getLatitude());
        lon = String.valueOf(location.getLongitude());
        //  updateUI();
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        buildGoogleApiClient();
    }

    @Override
    protected void onStart() {
        super.onStart();
        //   mGoogleApiClient.connect();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.disconnect();
    }

    void updateUI() {
        if (flag) {



            if (CheckInternet.isNetworkAvailable(HomeActivity.this)) {
                // code here
                new BaseAsyncTask().execute();
            } else {
                // code

                Toast.makeText(HomeActivity.this, " internet is not available now ", Toast.LENGTH_LONG).show();
            }


            // locationText.setText(getCompleteAddressString(Double.parseDouble(lat), Double.parseDouble(lon)));
        } else {
            locationText.setText("Find using phone's location");
        }

    }

    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
        String strAdd = "";
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(LATITUDE, LONGITUDE, 1);
            if (addresses != null) {
                Address returnedAddress = addresses.get(0);
                StringBuilder strReturnedAddress = new StringBuilder("");

                for (int i = 0; i < returnedAddress.getMaxAddressLineIndex(); i++) {
                    strReturnedAddress.append(returnedAddress.getAddressLine(i)).append("\n");
                }
                strAdd = strReturnedAddress.toString();
                //  Log.w("My Current loction address", "" + strReturnedAddress.toString());
            } else {
                // Log.w("My Current loction address", "No Address returned!");
            }
        } catch (Exception e) {
            e.printStackTrace();
            // Log.w("My Current loction address", "Canont get Address!");
        }
        return strAdd;
    }

    public void displayGpsAllowDialog(final android.content.Context mContext, String title, String message) {
        mPopDialog = new Dialog(mContext);
        mPopDialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        mPopDialog.setContentView(R.layout.dialog);
        mPopDialog.getWindow().setBackgroundDrawable(
                new ColorDrawable(android.graphics.Color.TRANSPARENT));
        TextView mTextViewTitle = (TextView) mPopDialog.findViewById(R.id.titleTextView);
        mTextViewTitle.setText(title);
        TextView mTextViewMessage = (TextView) mPopDialog.findViewById(R.id.messageTextView);
        mTextViewMessage.setText(message);
        Button mButtoncancel = (Button) mPopDialog.findViewById(R.id.btndonotallow);
        Button mButtonallow = (Button) mPopDialog.findViewById(R.id.btnallow);

        mButtonallow.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                flag = true;
                updateUI();

                mPopDialog.cancel();

            }
        });
        mButtoncancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mGoogleApiClient.disconnect();

                flag = false;
                locationText.setText("Find using phone's location");
                mPopDialog.cancel();
            }
        });
        mPopDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.findlocationtext:
               /* builder = new CustomDialog.Builder(this,R.style.Dialog);
                builder.setTitle("Allow \"Baselife\" to access your location while you use the app?");
                builder.setMessage("Show restaurants,events and bases nearby your current location")

                        .setNegativeButton(Html.fromHtml("<font color='#0000ff'>Don't Allow</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        })
                        .setPositiveButton(Html.fromHtml("<font color='#0000ff'>Allow</font>"), new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alert = builder.show();*/

                /*CustomDialog.Builder customBuilder = new
                        CustomDialog.Builder(HomeActivity.this);
                customBuilder.setTitle("Allow \"Baselife\" to access your location while you use the app?");
                customBuilder.setMessage("Show restaurants,events and bases nearby your current location");
                dialog = customBuilder.create();
                dialog.setContentView(R.layout.dialog);
                Button positivebutton = (Button) dialog.findViewById(R.id.positiveButton);
                Button negativebutton = (Button) dialog.findViewById(R.id.negativeButton);
                positivebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        mGoogleApiClient.connect();
                        updateUI();
                        flag = true;
                        dialog.cancel();
                    }
                });
                negativebutton.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        LocationServices.FusedLocationApi.removeLocationUpdates(
                                mGoogleApiClient, HomeActivity.this);
                        mGoogleApiClient.disconnect();
                        mGoogleApiClient = null;
                        mLocationRequest = null;
                        mLastLocation = null;
                        lat = "0.0";
                        lon = "0.0";
                        flag = false;
                        locationText.setText("Find using phone's location");
                        dialog.cancel();
                    }
                });*/
               /* customBuilder.setPositiveButton("Allow", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {



                    }
                }).
                        setNegativeButton("Don't Allow", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                    }
                });*/


/*
                dialog.show();
                final int titleDividerId = getResources().getIdentifier("titleDivider", "id", "android");
                final View titleDivider = dialog.findViewById(titleDividerId);
                if (titleDivider != null) {
                    titleDivider.setBackgroundColor(getResources().getColor(android.R.color.transparent));
                }
               *//* alert.show();*/


                displayGpsAllowDialog(view.getContext(), "Allow \"Baselife\" to access your location while you use the app?", "Show restaurants,events and bases nearby your current location");
                break;
            case R.id.findbasetextmanually:
                startActivity(new Intent(HomeActivity.this, BaseActivity.class));
                // HomeActivity.this.finish();
                break;
        }
    }

    private class BaseAsyncTask extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            // DIALOG_DOWNLOAD_PROGRESS is defined as 0 at start of class
            progress = ProgressDialog.show(HomeActivity.this, "Base Life",
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
            Log.e("dhaval response", result1);
            if (result1.toString().length() > 1) {
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
                } catch (Exception e) {

                }
                getNearbyBase(Double.parseDouble(lat), Double.parseDouble(lon), baseModelList);
            } else {

         //       Toast.makeText(HomeActivity.this, "Try again", Toast.LENGTH_LONG).show();
            }
        }


        public String postData() {
            // Create a new HttpClient and Post Header
            String response = "";
            try {
                URL url = new URL("http://baselife-dev.herokuapp.com/api/1.0/bases");

                HttpURLConnection http_conn = (HttpURLConnection) url.openConnection();
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

    public void getNearbyBase(double presentlat, double presentlng, ArrayList<BaseModel> baseModelList) {
        double distance;
        ArrayList<Double> distList = new ArrayList<Double>();
        for (int i = 0; i < baseModelList.size(); i++)

        {
            Location locationA = new Location("point A");
            locationA.setLatitude(presentlat);
            locationA.setLongitude(presentlng);
            Location locationB = new Location("point B");
            locationB.setLatitude(baseModelList.get(i).getLat());
            locationB.setLongitude(baseModelList.get(i).getLng());
            distance = locationA.distanceTo(locationB);
            distList.add(distance);
        }


        int minIndex = distList.indexOf(Collections.min(distList));

        locationText.setText(baseModelList.get(minIndex).getName());


    }
}
