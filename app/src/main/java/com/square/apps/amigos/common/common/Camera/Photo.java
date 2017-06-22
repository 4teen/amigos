package com.square.apps.amigos.common.common.Camera;

import android.graphics.Bitmap;
import android.support.annotation.NonNull;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by YOEL on 12/16/2015.
 */
@SuppressWarnings("ALL")
class Photo {
    private static final String JSON_FILENAME = "filename";

    private String mFilename;
    private String encodedBitmap;
    private Bitmap bitmap;

    /**create a Photo representing an existing file on disk**/
     public Photo(String filename){
        mFilename = filename;
    }

     public Photo (@NonNull JSONObject jsonObject) throws JSONException{
         mFilename = jsonObject.getString(JSON_FILENAME);
     }

    public String getEncodedBitmap() {
        return encodedBitmap;
    }

    public void setEncodedBitmap(String encodedBitmap) {
        this.encodedBitmap = encodedBitmap;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    @NonNull
    public JSONObject toJSON() throws JSONException{
        JSONObject jsonObject = new JSONObject();
        jsonObject.put(JSON_FILENAME, mFilename);
        return jsonObject;
    }

    public String getFilename(){
        return mFilename;
    }
}
