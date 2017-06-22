package com.square.apps.amigos.Services;

import android.app.IntentService;
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
 * Created by YOEL on 11/12/2015.
 */
@SuppressWarnings("ALL")
public class AddCourseService extends IntentService {

    @Nullable
    private String CourseID, StudentID;

    public AddCourseService() {
        super("addCourse");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {

        CourseID = intent.getSerializableExtra("CourseID").toString();

        StudentID = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getString(Contract.STUDENT_ID, null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppControl.URL_INDEX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jObj = new JSONObject(s);
                    if (!jObj.getBoolean("error"))
                        Toast.makeText(getApplicationContext(), "Course Successfully Added", Toast.LENGTH_LONG).show();
                    else
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
                params.put("tag", AppControl.ADD_COURSE_TAG);
                params.put(Contract.COURSE_ID, CourseID);
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
        Log.d("ADD COURSE ERROR", error_mesg);
        Toast.makeText(getApplicationContext(), error_mesg, Toast.LENGTH_LONG).show();
        // notifyActivity(true, error_mesg);
    }


}

