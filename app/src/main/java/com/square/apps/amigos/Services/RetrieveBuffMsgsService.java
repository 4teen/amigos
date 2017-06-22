package com.square.apps.amigos.Services;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

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
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p/>
 * helper methods.
 */
public class RetrieveBuffMsgsService extends IntentService {
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.square.apps.amigos.Services.action.FOO";

    public RetrieveBuffMsgsService() {
        super("RetrieveBuffMsgsService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    public static void startActionRetrieveBuffMsgs(@NonNull Context context) {
        Intent intent = new Intent(context, RetrieveBuffMsgsService.class);
        intent.setAction(ACTION_FOO);
        context.startService(intent);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_FOO.equals(action)) {
                final String studentID = PreferenceManager.getDefaultSharedPreferences(this).getString(Contract.STUDENT_ID, null);
                if ((studentID == null)) throw new AssertionError();
                HandleActionRetrieveBuffMsgsService(studentID);
            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void HandleActionRetrieveBuffMsgsService(final String StudentID) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppControl.URL_INDEX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jObj = new JSONObject(s);
                    if (jObj.getBoolean("error"))
                        displayError(jObj.getString("error_mesg")); /* There was an error registering this course*/
                } catch (JSONException e) {
                    displayError(e.getMessage());
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError volleyError) {
                displayError(volleyError.getMessage());
            }
        }) {

            /** Posting parameters to back-end db in accordance to the request type*/
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                /*assigns the correct listener and parameters for the String Request*/
                HashMap<String, String> params = new HashMap<>(); // instantiation of the parameters to return in getParams
                params.put("tag", AppControl.RETRIEVE_BUFFERED_MESSAGES);
                params.put(Contract.STUDENT_ID, StudentID);
                return params;
            }

        };
        /* Adding request to request queue**/
        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, AppControl.ADD_COURSE_TAG);

    }

     /*
private void notifyActivity(boolean error, String error_msg) {
Intent intent = new Intent(Contract.SEND_COMPLETE);
intent.putExtra("error", error);
intent.putExtra("error_mesg", error_msg);
LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
}
*/

    private void displayError(String error_mesg) {
        Log.d("BUFFERED MESSAGES error", error_mesg);
        Toast.makeText(getApplicationContext(), error_mesg, Toast.LENGTH_LONG).show();
        // notifyActivity(true, error_mesg);
    }

}
