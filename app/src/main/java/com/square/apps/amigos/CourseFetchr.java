package com.square.apps.amigos;

import android.net.Uri;
import android.util.Log;

import com.square.apps.amigos.common.common.course.Course;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by y-pol on 6/24/2017.
 */

public class CourseFetchr {
    private static final String TAG = "CourseFetchr";

    private static final String API_KEY = "REPLACE_ME_WITH_A_REAL_KEY";

    public byte[] getUrlBytes(String urlSpec) throws IOException{
        URL url = new URL(urlSpec);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("POST"); // sets HTTP method to POST
        connection.setRequestProperty("Accept-Encoding", "gzip, deflate, br");
        connection.setRequestProperty("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        try {
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            InputStream in = connection.getInputStream();
            if (connection.getResponseCode() != HttpURLConnection.HTTP_OK) {
                throw new IOException(connection.getResponseMessage() + ": with " + urlSpec);
            }
            int bytesRead = 0;
            byte[] buffer = new byte[1024];
            while ((bytesRead = in.read(buffer)) > 0) {
                out.write(buffer, 0, bytesRead);
            }
            out.close();
            return out.toByteArray();
        } finally {
            connection.disconnect();
        }
    }

    public String getUrlString(String urlSpec) throws IOException{
        return new String(getUrlBytes(urlSpec));
    }

    public List<Course> fetchCourses(){

        List<Course> courses = new ArrayList<>();

        try {
            String url = Uri.parse("http://ec2-54-205-151-150.compute-1.amazonaws.com/amigosApp/amigos_login_api/getCourses.php").buildUpon().appendQueryParameter("lvl", "u").appendQueryParameter("coll", "EN").appendQueryParameter("title", "program").appendQueryParameter("output", "search").build().toString();
            Log.i(TAG, "Sent Jurl: " + url);
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseItems(courses, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch courses", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return courses;
    }

    public List<Course> fetchCourses(String prefix, String dept, String coll, String lvl, String title){
        List<Course> courses = new ArrayList<>();
        Log.d(TAG, prefix + dept + coll + lvl + title);

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.encodedPath("http://ec2-54-205-151-150.compute-1.amazonaws.com");
            builder.appendEncodedPath("amigosApp/amigos_login_api/getCourses.php");
            builder.appendQueryParameter("output", "search");

            if (!prefix.equals("")) builder.appendQueryParameter("prefix", prefix);
            if (!dept.equals("")) builder.appendQueryParameter("dept", dept);
            if (!coll.equals("")) builder.appendQueryParameter("coll", coll);
            if (!lvl.equals("")) builder.appendQueryParameter("lvl", lvl);
            if (!title.equals("")) builder.appendQueryParameter("title", title);

            Uri uri = builder.build();
            String url = uri.toString();

            Log.i(TAG, "Sent Jurl: " + url);
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            JSONArray jsonBody = new JSONArray(jsonString);
            parseItems(courses, jsonBody);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch courses", ioe);
        } catch (JSONException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

        return courses;
    }


    private void parseItems(List<Course> courses, JSONArray coursesJsonArray) throws IOException, JSONException{

        for (int i = 0; i < coursesJsonArray.length(); i++) {
            JSONObject courseJSONObject = coursesJsonArray.getJSONObject(i);

            Course course = new Course();
            course.setCollege(courseJSONObject.getString("College"));
            course.setTitle(courseJSONObject.getString("Title"));
            course.setDepartment(courseJSONObject.getString("Department"));
            course.setPrefix(courseJSONObject.getString("Prefix"));
            course.setNumber(courseJSONObject.getString("Number"));
            courses.add(course);
        }
    }

}
