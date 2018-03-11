package com.squareapps.a4teen.amigos.Common.Utils;

import android.net.Uri;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.squareapps.a4teen.amigos.Common.Contract;

import java.io.ByteArrayOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import static com.squareapps.a4teen.amigos.Common.Contract.ACCESS_TOKEN;
import static com.squareapps.a4teen.amigos.Common.Contract.Group.GROUP_ID;

/**
 * Created by y-pol on 11/6/2017.
 */

public class BackEndAPI {
    private static final String PATH = "http://ec2-54-205-151-150.compute-1.amazonaws.com";
    private static final String ENCODED_PATH = "amigosApp/amigos_login_api/groups.php";
    private static final String TAG = "CourseFetchr";
    private static final String SCOPES = "https://www.googleapis.com/auth/firebase.messaging";

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

    public String notifyUserAdd(String groupId) {
        try {
            String accessToken = getAccessToken();

            Uri.Builder builder = new Uri.Builder();
            builder.encodedPath(PATH);
            builder.appendEncodedPath(ENCODED_PATH);
            builder.appendQueryParameter(GROUP_ID, groupId);
            builder.appendQueryParameter(ACCESS_TOKEN, accessToken);

            Uri uri = builder.build();
            String url = uri.toString();
            //  Log.i(TAG, "Sent Jurl: " + url);
            String jsonString = getUrlString(url);
            //  Log.i(TAG, "Received JSON: " + jsonString);
            return jsonString;

        } catch (IOException ioe) {
            //  Log.e(TAG, "Failed to fetch courses", ioe);
            return ioe.toString();
        }
    }

    private static String getAccessToken() throws IOException {
       // AssetManager assetManager = App
       // AssetManager().open("service-account.json");
        GoogleCredential googleCredential = GoogleCredential
                .fromStream(new FileInputStream("service-account.json"))
                .createScoped(Arrays.asList(SCOPES));
        googleCredential.refreshToken();
        return googleCredential.getAccessToken();
    }

}
