package com.square.apps.amigos.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.square.apps.amigos.AppControl;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.MyRequestQueue;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


public class UpdateCountService extends IntentService {

    // IntentService can perform, e.g. ACTION_UPDATE_COUNT
    private static final String ACTION_UPDATE_COUNT = "com.square.apps.amigos.Services.action.UPDATE_COUNT";
    private static final String FRIEND_ID = "com.square.apps.amigos.Services.extra.FRIEND_ID";
    private static final String COUNT = "com.square.apps.amigos.Services.extra.COUNT";

    public UpdateCountService() {
        super("UpdateCountService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionUpdateCount(@NonNull Context context, String friendID, String count) {
        Intent intent = new Intent(context, UpdateCountService.class);
        intent.setAction(ACTION_UPDATE_COUNT);
        intent.putExtra(FRIEND_ID, friendID);
        intent.putExtra(COUNT, count);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_COUNT.equals(action)) {
                final String friendID = intent.getStringExtra(FRIEND_ID);
                final String count = intent.getStringExtra(COUNT);
                handleActionUpdateCount(friendID, count);
            }
        }
    }

    /**
     * Handle action UPDATE_COUNT in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdateCount(final String friendID, final String count) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppControl.URL_INDEX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jObj = new JSONObject(s);
                    if (!jObj.getBoolean("error")) {
                        notifyActivity(false, "no_errors");
                    } else {/* There was an error sending this message*/
                        String errorMsg = jObj.getString("error_msg");
                        notifyActivity(true, errorMsg);
                    }
                } catch (JSONException e) {
                    notifyActivity(true, e.toString());
                    Log.d("catch", "occurred");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError volleyError) {
                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
                notifyActivity(true, volleyError.toString());
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*assigns the correct listener and parameters for the String Request*/
                HashMap<String, String> params = new HashMap<>();
                params.put("tag", "updateCount");
                params.put("StudentID", friendID);
                params.put("count", count);
                return params;
            }
        };
        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, "sendingMessage serviceIntent");
    }

    private void notifyActivity(boolean error, String error_msg) {
        Intent intent = new Intent(Contract.COUNT_UPDATE_COMPLETE);
        intent.putExtra("error", error);
        intent.putExtra("error_msg", error_msg);
    }
}
