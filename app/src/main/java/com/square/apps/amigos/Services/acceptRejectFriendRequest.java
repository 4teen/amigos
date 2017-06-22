package com.square.apps.amigos.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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

/**
 * Created by YOEL on 11/12/2015.
 */
@SuppressWarnings("ALL")
public class acceptRejectFriendRequest extends IntentService {

    public static final String TAG = "tag";
    @Nullable
    private String name, StudentID, tag;
    private Context context;

    public acceptRejectFriendRequest() {
        super("AcceptFriendRequest");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        context = getApplicationContext();
        name = intent.getStringExtra(Contract.STUDENT_ID);
        tag = intent.getStringExtra(TAG);
        StudentID = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getString(Contract.STUDENT_ID, null);

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
                if ((StudentID == null)) throw new AssertionError();
                HashMap<String, String> params = new HashMap<>();
                params.put("tag", tag);
                params.put("StudentID", StudentID);
                params.put("FriendOfID", name);
                return params;
            }
        };
        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, "sendingMessage serviceIntent");
    }

    private void notifyActivity(boolean error, String error_msg) {
        Intent intent = new Intent(Contract.SEND_COMPLETE);
        intent.putExtra("error", error);
        intent.putExtra("error_msg", error_msg);

    }

}

