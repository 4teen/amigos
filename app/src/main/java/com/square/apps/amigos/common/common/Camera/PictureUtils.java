package com.square.apps.amigos.common.common.Camera;


import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.util.Base64;
import android.view.Display;

import com.square.apps.amigos.Contract;
import com.square.apps.amigos.Services.SendProfilePicToDb;
import com.square.apps.amigos.common.common.db.DataProvider;

import java.io.ByteArrayOutputStream;

/**
 * Created by YOEL on 12/15/2015.
 */
@SuppressWarnings("ALL")
public class PictureUtils {
    /**
     * Get a BitmapDrawable from a local file that is scaled down
     * to fit the current window size
     */
    @NonNull
    @SuppressWarnings("deprecation")
    public static BitmapDrawable getScaleDrawable(@NonNull Activity activity, String path) {
        Display display = activity.getWindowManager().getDefaultDisplay();
        float destWidth = display.getWidth();
        float destHeight = display.getHeight();

        //read in the dimensions of the image on disk
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeFile(path, options);

        float srcWidth = options.outWidth;
        float srcHeight = options.outHeight;

        int inSampleSize = Math.round(Math.min(srcHeight / destHeight, srcWidth / destWidth));

        options = new BitmapFactory.Options();
        options.inSampleSize = inSampleSize;

        Bitmap bitmap = BitmapFactory.decodeFile(path, options);
        return new BitmapDrawable(activity.getResources(), bitmap);

    }

    public static void storeImageInDb(@NonNull Activity activity, Bitmap image) {
        String encodedImage = encodeImageToBase64(image);
        Intent intent = new Intent(activity, SendProfilePicToDb.class);
        intent.putExtra(Contract.PROFILE_IMAGE, encodedImage);
        activity.startService(intent);

        ContentValues contentValues = new ContentValues();
        contentValues.put(Contract.PROFILE_IMAGE, encodedImage);
        String StudentId = PreferenceManager.getDefaultSharedPreferences(activity).getString(Contract.STUDENT_ID, null);
        activity
                .getContentResolver()
                .update(Uri.withAppendedPath(DataProvider
                        .CONTENT_URI_PROFILE, StudentId)
                        , contentValues, null, null);
    }

    /**
     * Convert image captured by camera to string which is stored as blob on DB
     */
    private static String encodeImageToBase64(Bitmap image) {
        Bitmap imageX = image;
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        imageX.compress(Bitmap.CompressFormat.JPEG, 100, byteArrayOutputStream);
        byte[] b = byteArrayOutputStream.toByteArray();
        String imageEncoded = Base64.encodeToString(b, Base64.DEFAULT);
        return imageEncoded;
    }

    public static Bitmap decodeBase64(String input) {
        byte[] decodedByte = Base64.decode(input, 0);
        return BitmapFactory.decodeByteArray(decodedByte, 0, decodedByte.length);
    }

}
