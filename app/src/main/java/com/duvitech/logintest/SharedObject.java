package com.duvitech.logintest;

import org.json.JSONObject;

/**
 * Created by George on 10/9/2014.
 */
public class SharedObject {
    private static SharedObject ourInstance = new SharedObject();

    public static SharedObject getInstance() {
        return ourInstance;
    }

    private SharedObject() {
    }

    public String AuthToken;
    public JSONObject ScheduleEntry;
}
