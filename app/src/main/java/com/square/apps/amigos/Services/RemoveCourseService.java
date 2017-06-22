package com.square.apps.amigos.Services;

import android.app.IntentService;
import android.content.Intent;
import android.net.Uri;
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
import com.square.apps.amigos.common.common.db.DataProvider;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by YOEL on 11/15/2015.
 */
@SuppressWarnings("ALL")
public class RemoveCourseService extends IntentService {
    @Nullable
    private String CourseID, StudentID;

    public RemoveCourseService() {
        super("removeCourseIntent");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        CourseID = intent.getSerializableExtra(Contract.COURSE_ID).toString();

        StudentID = PreferenceManager
                .getDefaultSharedPreferences(getApplicationContext())
                .getString(Contract.STUDENT_ID, null);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, AppControl.URL_INDEX, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONObject jObj = new JSONObject(s);
                    if (!jObj.getBoolean("error")) {
                        Toast.makeText(getApplicationContext(), "Course Sussesfully Removed", Toast.LENGTH_LONG).show();
                        getContentResolver().delete(Uri.withAppendedPath(DataProvider.CONTENT_URI_COURSES,CourseID),null,null);
                    } else { /* There was an error deleting this course*/
                        Toast.makeText(getApplicationContext(), jObj.getString("error_msg"), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError volleyError) {
                Log.e("Response Error", volleyError.getMessage());
                Toast.makeText(getApplicationContext(), volleyError.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            /** Posting parameters to back-end db in accordance to the request type*/
            @NonNull
            @Override
            protected Map<String, String> getParams() {
                /*assigns the correct listener and parameters for the String Request*/
                HashMap<String, String> params = new HashMap<>(); // instantiation of the parameters to return in getParams
                params.put("tag", AppControl.REMOVE_COURSE_TAG);
                params.put(Contract.COURSE_ID, CourseID);
                params.put(Contract.STUDENT_ID, StudentID);
                return params;
            }

        };
        /* Adding request to request queue**/
        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(stringRequest, AppControl.ADD_COURSE_TAG);
    }

}