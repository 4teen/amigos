package com.square.apps.amigos.Services;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.square.apps.amigos.AppControl;
import com.square.apps.amigos.Contract;
import com.square.apps.amigos.MyRequestQueue;
import com.square.apps.amigos.common.common.db.DataProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YOEL on 11/10/2015.
 */
@SuppressWarnings("ALL")
public class sendMessageIntentService extends IntentService {

    private String message, friendEmail;

    public sendMessageIntentService() {
        super("sendingMessage serviceIntent");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        message = intent.getSerializableExtra(DataProvider.COL_MSG).toString();
        friendEmail = intent.getSerializableExtra(DataProvider.COL_EMAIL).toString();

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppControl.URL_SEND, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jObj = new JSONObject(s);
                    Log.d("notification_key", jObj.toString());
                    boolean error = jObj.getBoolean("error");
                    if (!error) {
                        notifyActivity(false, "no_errors");
                    } else {/* There was an error sending this message*/
                        displayError(jObj.getString("error_mesg"));
                    }
                } catch (JSONException e) {
                    displayError(e.toString());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError volleyError) {
                displayError(volleyError.getMessage());
            }
        }) {
            @NonNull
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                /*assigns the correct listener and parameters for the String Request*/
                String myEmail = AppControl.getInstance(getApplicationContext()).getUserEmail();
                HashMap<String, String> params = new HashMap<>();
                Log.d("catch", friendEmail);
                Log.d("catch", message);
                Log.d("catch", myEmail);
                params.put("receiverEmail", friendEmail);
                params.put("message", message);
                params.put("senderEmail", myEmail);
                return params;
            }
        };
        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, "sendingMessage serviceIntent");
    }

    private void notifyActivity(boolean error, String error_msg) {
        Intent intent = new Intent(Contract.SEND_COMPLETE);
        intent.putExtra("error", error);
        intent.putExtra("error_mesg", error_msg);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }

    private void displayError(String error_mesg){
        Log.d("SEND MESSAGE error", error_mesg);
        notifyActivity(true, error_mesg);
    }
}
