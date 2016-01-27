package com.instirepo.app.serverApi;

import com.android.volley.VolleyError;

/**
 * Created by Ashish Goel on 1/25/2016.
 */
public interface AppRequestListener {

    public void onRequestStarted(String requestTag);

    public void onRequestFailed(String requestTag, VolleyError error);

    public void onRequestCompleted(String requestTag, String response);
}
