package com.squareapps.a4teen.amigos.Common.Utils;

import android.net.Uri;
import android.util.Log;

import com.google.gson.JsonParseException;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;



public class FirebaseFunctions {
    private static final String PATH = "https://us-central1-amigos-147308.cloudfunctions.net";
    private static final String TAG = "FirebaseFunctions";

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

    public void sendGroupAddNotification(String from, String to, String groupId) {

        try {

            Uri.Builder builder = new Uri.Builder();
            Uri uri = builder.encodedPath(PATH)
                    .appendEncodedPath("sendaddnotification")
                    .appendQueryParameter("from", from)
                    .appendQueryParameter("to", to)
                    .appendQueryParameter("groupid", groupId)
                    .build();

            String url = uri.toString();
            Log.i(TAG, "Sent Jurl: " + url);
            String jsonString = getUrlString(url);
            Log.i(TAG, "Received JSON: " + jsonString);

        } catch (IOException ioe) {
            Log.e(TAG, "Failed to send notification ", ioe);
        } catch (JsonParseException je) {
            Log.e(TAG, "Failed to parse JSON", je);
        }

    }
}
