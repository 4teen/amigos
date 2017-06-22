package com.square.apps.amigos.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
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
import com.square.apps.amigos.common.common.db.DataProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YOEL on 11/12/2015.
 */
@SuppressWarnings("ALL")
public class GetPendingRequestsServiceIntent extends IntentService {
    private Context context;
    @Nullable
    private String StudentID;

    public GetPendingRequestsServiceIntent() {
        super("getPendingFriendRequest");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        context = getApplicationContext();
        StudentID = PreferenceManager
                .getDefaultSharedPreferences(context)
                .getString(Contract.STUDENT_ID, null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppControl.URL_INDEX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jObj = new JSONObject(s);
                    if (!jObj.getBoolean("error")) {
                        for (int i = 0; i < jObj.getJSONArray(Contract.STUDENT_ID).length(); i++) {
                            Log.d("no", "erros");
                            ContentValues values = new ContentValues();
                            values.put(Contract.PRIMARY_KEY, jObj.getJSONArray(Contract.STUDENT_ID).get(i).toString());
                            values.put(Contract.NAME, jObj.getJSONArray(Contract.NAME).get(i).toString());
                            getContentResolver().insert(DataProvider.CONTENT_URI_PENDINGREQUESTS, values);
                        }
                        notifyActivity(false, "no_errors");
                    } else {/* There was an error sending this message*/
                        String errorMsg = jObj.getString("error_msg");
                        Log.d("error", "StudentID php");
                        notifyActivity(true, errorMsg);
                    }
                } catch (JSONException e) {
                    notifyActivity(true, e.toString());
                    Log.d("catch", "occured");
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError volleyError) {
                Log.e("Response Error", volleyError.getMessage());
                Log.d("error", "StudentID volley");
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
                params.put("tag", "showPendingRequests");
                params.put("StudentID", StudentID);
                return params;
            }
        };
        MyRequestQueue.getInstance(context).addToRequestQueue(stringRequest, "showPendingRequests");
    }

    private void notifyActivity(boolean error, String error_msg) {
        Log.d("PENDING RQT error", error_msg);
        Log.d("trying to", "sendBackIntent");

    }

}

