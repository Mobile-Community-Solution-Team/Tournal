package com.kelompokmcs.tournal.API;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;

public class VolleySingleton {
    public static VolleySingleton instance;
    public RequestQueue requestQueue;

    public VolleySingleton(Context context){
        requestQueue = getRequestQueue(context);
    }

    public RequestQueue getRequestQueue(Context context) {
        if(requestQueue == null)
            requestQueue = Volley.newRequestQueue(context);
        return requestQueue;
    }

    public static VolleySingleton getInstance(Context context){
        if(instance == null){
            instance = new VolleySingleton(context);
        }
        return instance;
    }

    public void addToRequestQueue(Request req){
        requestQueue.add(req);
    }
}
