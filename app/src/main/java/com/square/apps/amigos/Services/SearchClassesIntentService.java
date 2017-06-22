package com.square.apps.amigos.Services;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

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
public class SearchClassesIntentService extends IntentService {

    private String tag_search_key, tag_search_value;

    public SearchClassesIntentService() {
        super("SearchClasesInt");
    }

    @Override
    protected void onHandleIntent(@NonNull Intent intent) {
        Log.d("searchKey", "crashed here");
        tag_search_key = intent.getSerializableExtra("searchKey").toString();
        tag_search_value = intent.getSerializableExtra("searchValue").toString();

        switch (tag_search_key) {
            case "Class Subject":
                tag_search_key = Contract.SUBJECT_CRS;
                break;
            case "Class Title":
                tag_search_key = Contract.TITLE;
                break;
            case "Class CRN":
                tag_search_key = Contract.CRN;
                break;
            default:
        }

        Log.d("searchKey", tag_search_key);
        Log.d("searchKey", tag_search_value);

        Response.Listener<String> getCoursesListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject jObj = new JSONObject(response);
                    /*Check for error node in json**/
                    if (!jObj.getBoolean("error")) {
                        for (int i = 0; i < jObj.getJSONArray(Contract.COURSE_ID).length(); i++) {
                            ContentValues values = new ContentValues();
                            values.put(Contract.DEPARTMENT, jObj.getJSONArray(Contract.DEPARTMENT).get(i).toString());
                            values.put(Contract.CAMPUS, jObj.getJSONArray(Contract.CAMPUS).get(i).toString());
                            values.put(Contract.COLLEGE, jObj.getJSONArray(Contract.COLLEGE).get(i).toString());
                            values.put(Contract.TITLE, jObj.getJSONArray(Contract.TITLE).get(i).toString());
                            values.put(Contract.SUBJECT_CRS, jObj.getJSONArray(Contract.SUBJECT_CRS).get(i).toString());
                            values.put(Contract.SECTION, jObj.getJSONArray(Contract.SECTION).get(i).toString());
                            values.put(Contract.INSTRUCTOR, jObj.getJSONArray(Contract.INSTRUCTOR).get(i).toString());
                            values.put(Contract.PRIMARY_KEY, jObj.getJSONArray(Contract.COURSE_ID).get(i).toString());
                            values.put(Contract.DAYS, jObj.getJSONArray(Contract.DAYS).get(i).toString());
                            values.put(Contract.TIME, jObj.getJSONArray(Contract.TIME).get(i).toString());
                            values.put(Contract.ROOM, jObj.getJSONArray(Contract.ROOM).get(i).toString());
                            values.put(Contract.BUILDING, jObj.getJSONArray(Contract.BUILDING).get(i).toString());
                            values.put(Contract.CRN, jObj.getJSONArray(Contract.CRN).get(i).toString());
                            getContentResolver().insert(DataProvider.CONTENT_URI_COURSES_BUFFER, values);
                            Log.d("error_mesg", jObj.getJSONArray(Contract.TITLE).get(i).toString());
                            notifyActivity(false, "noErrors");
                        }

                    } else {
                        String errorMsg = jObj.getString("error_mesg");
                        notifyActivity(true, errorMsg);
                    }
                } catch (JSONException e) {
                    notifyActivity(true, e.toString());
                }
            }
        };
        Response.ErrorListener errorListener = new Response.ErrorListener() {
            @Override
            public void onErrorResponse(@NonNull VolleyError volleyError) {
                notifyActivity(true, volleyError.toString());
            }
        };

        /*builds string request**/
        StringRequest strReq = new StringRequest(
                Request.Method.POST,
                AppControl.URL_INDEX,
                getCoursesListener,
                errorListener) {


            /**Posting parameters to back-end db in accordance to the request type**/

            @NonNull
            @Override
            protected Map<String, String> getParams() {
                /*assigns the correct listener and parameters for the String Request**/
                HashMap<String, String> params = new HashMap<>(); // instantiation of the parameters to return in getParams
                params.put("tag", AppControl.GET_COURSES_TAG);
                params.put("searchKey", tag_search_key);
                params.put("searchValue", tag_search_value);
                return params;
            }

        };
        /* Adding request to request queue**/
        MyRequestQueue.getInstance(getApplicationContext()).addToRequestQueue(strReq, "getFriends");
    }

    private void notifyActivity(boolean error, String error_msg) {
        Intent intent = new Intent(Contract.SEARCH_COURSES_COMPLETE);
        intent.putExtra("error", error);
        intent.putExtra("error_msg", error_msg);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);
    }
}
