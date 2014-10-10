package com.duvitech.logintest;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.location.Address;
import android.location.Geocoder;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONStringer;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;
import java.util.Map;

public class MapsActivity extends FragmentActivity {

    private GoogleMap mMap; // Might be null if Google Play services APK is not available.


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        setUpMapIfNeeded();
    }

    @Override
    protected void onResume() {
        super.onResume();
        setUpMapIfNeeded();
    }

    /**
     * Sets up the map if it is possible to do so (i.e., the Google Play services APK is correctly
     * installed) and the map has not already been instantiated.. This will ensure that we only ever
     * call {@link #setUpMap()} once when {@link #mMap} is not null.
     * <p>
     * If it isn't installed {@link SupportMapFragment} (and
     * {@link com.google.android.gms.maps.MapView MapView}) will show a prompt for the user to
     * install/update the Google Play services APK on their device.
     * <p>
     * A user can return to this FragmentActivity after following the prompt and correctly
     * installing/updating/enabling the Google Play services. Since the FragmentActivity may not
     * have been completely destroyed during this process (it is likely that it would only be
     * stopped or paused), {@link #onCreate(Bundle)} may not be called again so we should call this
     * method in {@link #onResume()} to guarantee that it will be called.
     */
    private void setUpMapIfNeeded() {
        // Do a null check to confirm that we have not already instantiated the map.
        if (mMap == null) {
            // Try to obtain the map from the SupportMapFragment.
            mMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map))
                    .getMap();

            // Check if we were successful in obtaining the map.
            if (mMap != null) {
                setUpMap();
            }
        }
    }

    private Bitmap writeTextOnDrawable(int drawableId, String text) {

        Bitmap bm = BitmapFactory.decodeResource(getResources(), drawableId)
                .copy(Bitmap.Config.ARGB_8888, true);

        Typeface tf = Typeface.create("Helvetica", Typeface.BOLD);

        Paint paint = new Paint();
        paint.setStyle(Paint.Style.FILL);
        paint.setColor(Color.WHITE);
        paint.setTypeface(tf);
        paint.setTextAlign(Paint.Align.CENTER);
        paint.setTextSize(convertToPixels(this.getApplicationContext(), 11));

        Rect textRect = new Rect();
        paint.getTextBounds(text, 0, text.length(), textRect);

        Canvas canvas = new Canvas(bm);

        //If the text is bigger than the canvas , reduce the font size
        if(textRect.width() >= (canvas.getWidth() - 4))     //the padding on either sides is considered as 4, so as to appropriately fit in the text
            paint.setTextSize(convertToPixels(this.getApplicationContext(), 7));        //Scaling needs to be used for different dpi's

        //Calculate the positions
        int xPos = (canvas.getWidth() / 2) - 2;     //-2 is for regulating the x position offset

        //"- ((paint.descent() + paint.ascent()) / 2)" is the distance from the baseline to the center.
        int yPos = (int) ((canvas.getHeight() / 2) - ((paint.descent() + paint.ascent()) / 2)) ;

        canvas.drawText(text, xPos, yPos, paint);

        return  bm;
    }


    public static int convertToPixels(Context context, int nDP)
    {
        final float conversionScale = context.getResources().getDisplayMetrics().density;

        return (int) ((nDP * conversionScale) + 0.5f) ;

    }

    /**
     * This is where we can add markers or lines, add listeners or move the camera. In this case, we
     * just add a marker near Africa.
     * <p>
     * This should only be called once and when we are sure that {@link #mMap} is not null.
     */
    private void setUpMap() {

        // add some markers
        if(SharedObject.getInstance().ScheduleEntry != null)
        {
            Geocoder gc = new Geocoder(this.getBaseContext());
            // add hub location
            try {
                if(SharedObject.getInstance().ScheduleEntry.getInt("HubId")>0) {
                    JSONObject accountHub = SharedObject.getInstance().ScheduleEntry.getJSONObject("AccountHub");
                    JSONObject address = accountHub.getJSONObject("Address");
                    String addr = address.getString("DisplayAddress");
                    if(address.getBoolean("isGeocoded")) {
                        JSONObject latLng = address.getJSONObject("LatLngJson");
                        mMap.addMarker(
                                new MarkerOptions()
                                    .position(new LatLng(latLng.getDouble("k"), latLng.getDouble("B")))
                                    .draggable(false)
                                    .title(addr)
                                    .icon(BitmapDescriptorFactory.fromResource(R.drawable.home))
                        );
                    }
                    else
                    {
                        // must geocode the address.
                        if(gc.isPresent()) {
                            List<Address> list = gc.getFromLocationName(addr, 1);
                            if(!list.isEmpty()) {
                                mMap.addMarker(
                                        new MarkerOptions()
                                                .position(new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude()))
                                                .draggable(false)
                                                .title(addr)
                                                .icon(BitmapDescriptorFactory.fromResource(R.drawable.home))
                                );
                            }
                        }
                    }


                }

                // add pins for route
                JSONArray arrRequests = SharedObject.getInstance().ScheduleEntry.getJSONArray("Requests");
                // place markers
                if(arrRequests.length()>0)
                {
                    for(int x = 0; x < arrRequests.length(); x++)
                    {
                        //for each request get the entries
                        JSONObject entry = arrRequests.getJSONObject(x);
                        JSONArray arrDeliveryItems = new JSONArray(entry.getString("DeliveryItems"));
                        if(arrDeliveryItems.length()>0)
                        {
                            for(int y=0; y < arrDeliveryItems.length(); y++)
                            {
                                JSONObject dItem = arrDeliveryItems.getJSONObject(y);
                                JSONObject dCust = dItem.getJSONObject("Customer");
                                JSONObject dCustAddr = dCust.getJSONObject("Address");
                                int seqNum = dItem.getInt("SequenceNum");
                                String dispAddress = dCustAddr.getString("DisplayAddress");
                                String jsonLatLng = "";
                                if(dCustAddr.getBoolean("isGeocoded"))
                                {
                                    // use pre geocoded values
                                    //JSONObject latLng = dCustAddr.getJSONObject("LatLngJson");
                                    jsonLatLng = dCustAddr.getString("LatLngJson");
                                    //mMap.addMarker(
                                    //        new MarkerOptions()
                                    //                .position(new LatLng(latLng.getDouble("k"), latLng.getDouble("B")))
                                    //                .draggable(false)
                                    //                .title(dispAddress)
                                    //                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))
                                    //);
                                }
                                else
                                {

                                    // geocode address
                                    if(gc.isPresent()) {
                                        List<Address> list = gc.getFromLocationName(dispAddress, 1);
                                        if(!list.isEmpty()) {
                                            jsonLatLng = new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude()).toString();
                                            //mMap.addMarker(
                                            //        new MarkerOptions()
                                            //                .position(new LatLng(list.get(0).getLatitude(), list.get(0).getLongitude()))
                                            //                .draggable(false)
                                            //                .title(dispAddress)
                                            //                .icon(BitmapDescriptorFactory.fromBitmap(markerBitmap))
                                            //);
                                        }
                                    }
                                }

                                new DownloadImageTask()
                                {
                                    protected void onPostExecute(MarkerOptions markerOpt) {
                                        mMap.addMarker(markerOpt);
                                    }
                                }.execute("https://chart.googleapis.com/chart?chst=d_map_pin_letter&chld="+seqNum+"|FF0000|000000",dispAddress,jsonLatLng);

                            }

                        }
                        else
                        {
                            Toast.makeText(MapsActivity.this, "Failed to get Delivery Items",
                                    Toast.LENGTH_SHORT).show();
                        }

                    }
                }
                else
                {

                    Toast.makeText(MapsActivity.this, "Failed to get Requests in schedule",
                            Toast.LENGTH_SHORT).show();
                }

                // draw the route

            } catch (Exception ex) {
                Log.i("Error", ex.getMessage() + "");
            }
        }
    }
}
