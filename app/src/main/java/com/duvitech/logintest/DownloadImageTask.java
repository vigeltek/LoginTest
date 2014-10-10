package com.duvitech.logintest;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by George on 10/9/2014.
 */
public abstract class DownloadImageTask  extends AsyncTask<String, Void, MarkerOptions> {

    protected MarkerOptions doInBackground(String... params)
    {
        MarkerOptions retMarkerOptions = null;
        try {
            URL url = new URL(params[0]);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.connect();
            InputStream input = connection.getInputStream();
            Bitmap bMarker = BitmapFactory.decodeStream(input);
            JSONObject latlng = new JSONObject(params[2]);
            retMarkerOptions = new MarkerOptions()
                    .position(new LatLng(latlng.getDouble("k"), latlng.getDouble("B")))
                    .draggable(false)
                    .title(params[1])
                    .icon(BitmapDescriptorFactory.fromBitmap(bMarker));

        } catch (Exception e) {
            // Log exception
            Log.e("URL_Exception", e.getMessage() + "");
        }

        return retMarkerOptions;
    }
}
