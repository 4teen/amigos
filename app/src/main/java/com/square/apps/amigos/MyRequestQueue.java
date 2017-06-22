package com.square.apps.amigos;

import android.content.Context;
import android.support.annotation.NonNull;
import android.text.TextUtils;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;


public class MyRequestQueue {
    private static final String REQUEST_TAG = MyRequestQueue.class.getSimpleName();

    private static MyRequestQueue sRequestQueue;
    private final Context mAppContext;
    private RequestQueue requestQueue;


    private MyRequestQueue(Context appContext) {
        mAppContext = appContext;
    }

    public static synchronized MyRequestQueue getInstance(@NonNull Context appContext) {
        if (sRequestQueue == null) {
            sRequestQueue = new MyRequestQueue(appContext.getApplicationContext());
        }
        return sRequestQueue;
    }

    private RequestQueue getRequestQueue() {
        if (requestQueue == null) {
            requestQueue = Volley.newRequestQueue(mAppContext);
        }

        return requestQueue;
    }

    public <T> void addToRequestQueue(@NonNull Request<T> request, String tag) {
        request.setTag(TextUtils.isEmpty(tag) ? REQUEST_TAG : tag);
        getRequestQueue().add(request);
    }

    public <T> void addToRequestQueue(@NonNull Request<T> request) {
        request.setTag(REQUEST_TAG);
        getRequestQueue().add(request);
    }

    public void cancelPendingRequests(@NonNull Object tag) {
        if (requestQueue != null) {
            requestQueue.cancelAll(tag);
        }

    }
}
