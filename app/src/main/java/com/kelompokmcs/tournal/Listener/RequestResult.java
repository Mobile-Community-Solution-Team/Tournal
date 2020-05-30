package com.kelompokmcs.tournal.Listener;

import com.android.volley.VolleyError;

import org.json.JSONObject;

public interface RequestResult {
    void notifySuccess(String requestType, JSONObject response);
    void notifyError(String requestType, VolleyError error);
}
