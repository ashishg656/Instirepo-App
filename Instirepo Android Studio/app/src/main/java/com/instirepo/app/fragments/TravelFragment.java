package com.instirepo.app.fragments;

import java.util.HashMap;

import com.android.volley.Request.Method;
import com.android.volley.VolleyError;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.GoogleApiClient.ConnectionCallbacks;
import com.google.android.gms.common.api.GoogleApiClient.OnConnectionFailedListener;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStates;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMap.CancelableCallback;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.instirepo.app.R;
import com.instirepo.app.activities.HomeActivity;
import com.instirepo.app.application.ZApplication;
import com.instirepo.app.extras.ZUrls;
import com.instirepo.app.objects.RIdeSharingListingObject;
import com.instirepo.app.preferences.ZPreferences;
import com.instirepo.app.serverApi.AppRequestListener;
import com.instirepo.app.serverApi.CustomStringRequest;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.ImageView;

public class TravelFragment extends BaseFragment implements OnConnectionFailedListener, ConnectionCallbacks,
        LocationListener, ZUrls, AppRequestListener, OnMapReadyCallback, OnClickListener, CancelableCallback {

    GoogleApiClient mGoogleApiClient;
    public static int REQUEST_CHECK_SETTINGS = 50;
    LocationRequest mLocationRequest;
    int countTriesForLocation = 0;

    boolean isRequestRunning;
    public Location location;

    private GoogleMap mMap;
    public int permissionDeniedCount = 0;

    ImageView myLocation;

    public static TravelFragment newInstance(Bundle b) {
        TravelFragment f = new TravelFragment();
        f.setArguments(b);
        return f;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.travel_fragment_layout, container, false);

        myLocation = (ImageView) rootView.findViewById(R.id.mylocation);

        return rootView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        setUpGoogleClient();

        myLocation.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    private void setUpGoogleClient() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(getActivity()).addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this).addApi(LocationServices.API).build();
        }
    }

    @Override
    public void onConnected(Bundle bundle) {
        createLocationRequest();
    }

    public void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(10000);
        mLocationRequest.setFastestInterval(5000);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder().setAlwaysShow(true)
                .addLocationRequest(mLocationRequest);
        PendingResult<LocationSettingsResult> result = LocationServices.SettingsApi
                .checkLocationSettings(mGoogleApiClient, builder.build());

        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                if (permissionDeniedCount >= 1)
                    return;

                final Status status = result.getStatus();
                final LocationSettingsStates states = result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        getCurrentLocation();
                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        permissionDeniedCount += 1;
                        try {
                            status.startResolutionForResult(((HomeActivity) getActivity()), REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException e) {

                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        permissionDeniedCount += 1;
                        break;
                }

            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CHECK_SETTINGS) {
            if (resultCode == getActivity().RESULT_OK) {
                getCurrentLocation();
            }
        }
    }

    private void getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        Location mLastLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        if (mLastLocation != null) {
            Log.w("Hello",
                    "Latitude : " + mLastLocation.getLatitude() + " & longitude : " + mLastLocation.getLongitude());

            location = mLastLocation;
            loadData();
        } else {
            Log.w("ash", "MLastLocation Null");
            startLocationUpdates();
        }
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(getActivity(),
                Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {

    }

    public void onStart() {
        mGoogleApiClient.connect();
        super.onStart();
    }

    public void onStop() {
        if (mGoogleApiClient.isConnected())
            mGoogleApiClient.disconnect();
        super.onStop();
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mGoogleApiClient.isConnected()) {
            stopLocationUpdates();
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected()) {
            startLocationUpdates();
        }
    }

    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, this);
    }

    @Override
    public void onLocationChanged(Location location) {
        if (location != null) {
            Log.w("Hello", "Latitude : " + location.getLatitude() + " & longitude : " + location.getLongitude());

            this.location = location;
            loadData();

            stopLocationUpdates();
        } else {
            countTriesForLocation += 1;
            if (countTriesForLocation >= 10) {
                stopLocationUpdates();
            }
            Log.w("ash", "Location Null");
        }
    }

    void loadData() {
        if (!isRequestRunning) {
            HashMap<String, String> p = new HashMap<>();
            p.put("lat", Double.toString(location.getLatitude()));
            p.put("long", Double.toString(location.getLongitude()));
            p.put("user_id", ZPreferences.getUserProfileID(getActivity()));
            CustomStringRequest req = new CustomStringRequest(Method.POST, getAllNearbyTravels, getAllNearbyTravels, this, p);
            ZApplication.getInstance().addToRequestQueue(req, getAllNearbyTravels);

            if (location != null) {
                LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
                mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15), 1000, this);
            }
        }
    }

    @Override
    public void onRequestStarted(String requestTag) {
        isRequestRunning = true;
    }

    @Override
    public void onRequestFailed(String requestTag, VolleyError error) {
        isRequestRunning = false;

        makeToast("Unable to load data for ride sharing");
    }

    @Override
    public void onRequestCompleted(String requestTag, String response) {
        isRequestRunning = false;

        makeToast("Successfully loaded ride sharing data");

        RIdeSharingListingObject obj = new Gson().fromJson(response, RIdeSharingListingObject.class);

        for (RIdeSharingListingObject.RideShareListSingleObj data : obj.rides) {
            LatLng sydney = new LatLng(Double.parseDouble(data.latitude), Double.parseDouble(data.longitude));
            mMap.addMarker(new MarkerOptions().position(sydney).title(data.car + data.id));
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setPadding(getActivity().getResources().getDimensionPixelSize(R.dimen.z_button_height),
                getActivity().getResources().getDimensionPixelSize(R.dimen.z_button_height),
                getActivity().getResources().getDimensionPixelSize(R.dimen.z_button_height),
                getActivity().getResources().getDimensionPixelSize(R.dimen.z_button_height));

        LatLng sydney = new LatLng(28.737324, 77.090981);
        mMap.addMarker(new MarkerOptions().position(sydney).title("Delhi"));
        mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15), 1000, this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.mylocation:
                if (location == null) {
                    permissionDeniedCount = 0;
                    createLocationRequest();
                } else {
                    LatLng sydney = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.addMarker(new MarkerOptions().position(sydney).title("My Location"));
                    mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(sydney, 15), 1000, this);
                }
                break;

            default:
                break;
        }
    }

    @Override
    public void onCancel() {

    }

    @Override
    public void onFinish() {

    }
}
