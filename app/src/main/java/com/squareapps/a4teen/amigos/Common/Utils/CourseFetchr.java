package com.squareapps.a4teen.amigos.Common.Utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;
import com.squareapps.a4teen.amigos.Common.POJOS.Course;
import com.squareapps.a4teen.amigos.Fragments.SearchFormFragment;

import org.json.JSONException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;


public class CourseFetchr {
    private static final String PATH = "http://ec2-54-205-151-150.compute-1.amazonaws.com";
    private static final String ENCODED_PATH = "amigosApp/amigos_login_api/getCourses.php";
    private static final String TAG = "CourseFetchr";

    private byte[] getUrlBytes(String urlSpec) throws IOException {
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

    private String getUrlString(String urlSpec) throws IOException {
        return new String(getUrlBytes(urlSpec));
    }

    public List<Course> fetchCourses(String prefix, String num, String credits, String title, String output) {
        List<Course> courses = new ArrayList<>();
        Log.d(TAG, prefix + num + credits + title);

        final String PREFIX = (output.equals(SearchFormFragment.SEARCH) ? "prefix" : "subj");

        try {

            Uri.Builder builder = new Uri.Builder();
            builder.encodedPath(PATH);
            builder.appendEncodedPath(ENCODED_PATH);
            builder.appendQueryParameter("output", output);

            if (!prefix.equals("")) builder.appendQueryParameter(PREFIX, prefix);
            if (!num.equals("")) builder.appendQueryParameter("num", num);
            if (!credits.equals("")) builder.appendQueryParameter("credits", credits);
            if (!title.equals("")) builder.appendQueryParameter("title", title);

            Uri uri = builder.build();
            String url = uri.toString();
            Log.i(TAG, "Sent Jurl: " + url);
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);
            courses = parseItems(jsonString);
        } catch (IOException ioe) {
            Log.e(TAG, "Failed to fetch courses", ioe);
        } catch (JSONException | JsonParseException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }


        return courses;
    }


    private List<Course> parseItems(String jsonString) throws IOException, JSONException, JsonParseException {


        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE_WITH_SPACES);

        Gson gson = gsonBuilder.create();

        Type courseListType = new TypeToken<List<Course>>() {
        }.getType();


        return gson.fromJson(jsonString, courseListType);

    }
}
