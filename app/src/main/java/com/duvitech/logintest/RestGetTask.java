package com.duvitech.logintest;

import android.os.AsyncTask;
import android.util.Log;

import org.apache.http.Header;
import org.apache.http.HeaderElement;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by George on 10/9/2014.
 */
public abstract class RestGetTask extends AsyncTask<String, Void, String> {
    protected String doInBackground(String... params)
    {
        String responseStr = null;

        HttpClient client = new DefaultHttpClient();
        HttpGet get = new HttpGet("http://siriusgpswebui.azurewebsites.net/" + params[0]);

        get.setHeader("Content-Type","application/json; charset=utf-8");
        get.addHeader(new Header() {
            @Override
            public String getName() {
                return "Authorization";
            }

            @Override
            public String getValue() {
                return "bearer " + SharedObject.getInstance().AuthToken;
            }

            @Override
            public HeaderElement[] getElements() throws ParseException {
                return new HeaderElement[0];
            }
        });

        try
        {

            // StringEntity se = new StringEntity(params[1]);
            // se.setContentType("application/json;charset=UTF-8");
            // se.setContentEncoding(new BasicHeader(HTTP.CONTENT_TYPE,"application/json;charset=UTF-8"));

            // post.setEntity(se);

            HttpResponse response = client.execute(get);
            responseStr = EntityUtils.toString(response.getEntity());
            Log.i("Response", responseStr + "");

            return responseStr;
        }catch (UnsupportedOperationException uee)
        {
            // error creating entity object
            uee.printStackTrace();
            Log.i("UnsupportedEncodingException", uee + "");
        }catch (ClientProtocolException cpe)
        {
            // client protocol exception
            cpe.printStackTrace();
            Log.i("ClientProtocolException", cpe + "");
        }catch (IOException ioe)
        {
            // IO exception
            ioe.printStackTrace();
            Log.i("IOException", ioe + "");
        }

        return responseStr;
    }
}
