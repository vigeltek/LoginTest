package com.duvitech.logintest;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class ListActivity extends Activity {

    final ArrayList<String> list = new ArrayList<String>();
    JSONArray array = null;

    private void SetAdapter()
    {
        final ListView listview = (ListView) findViewById(R.id.lvDeliveries);

        final StableArrayAdapter adapter = new StableArrayAdapter(this,
                android.R.layout.simple_list_item_1, list);

        listview.setAdapter(adapter);

        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, final View view,
                                    int position, long id) {
                final String item = (String) parent.getItemAtPosition(position);
                try {
                    SharedObject.getInstance().ScheduleEntry = array.getJSONObject(position);
                }
                catch (Exception ex)
                {
                    SharedObject.getInstance().ScheduleEntry = null;
                }
                longInfo(SharedObject.getInstance().ScheduleEntry.toString());
                view.animate().setDuration(2000).alpha(0)
                        .withEndAction(new Runnable() {
                            @Override
                            public void run() {
                                list.remove(item);
                                adapter.notifyDataSetChanged();
                                view.setAlpha(1);
                            }
                        });
            }
        });
    }

    public static void longInfo(String str) {
        if(str.length() > 4000) {
            Log.i("JSON", str.substring(0, 4000));
            longInfo(str.substring(4000));
        } else
            Log.i("JSON", str);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);



        new RestGetTask() {
            protected void onPostExecute(String result) {
                {
                    int id;
                    String ticket = "";
                    try {
                        JSONObject jsonResult = new JSONObject(result);
                        if(jsonResult.getBoolean("result"))
                        {
                            array = new JSONArray(jsonResult.getString("data"));
                            Log.i("Schedules", "Found " + array.length() + " schedules");
                            for (int i = 0; i < array.length(); i++) {
                                JSONObject row = array.getJSONObject(i);
                                id = row.getInt("Id");
                                Log.i("Id", id + "");

                                JSONArray reqArr = new JSONArray(row.getString("Requests"));
                                for(int y = 0; y<reqArr.length(); y++)
                                {
                                    JSONObject yrow = reqArr.getJSONObject(y);
                                    if(y>0)
                                        ticket = ticket + ", ";
                                    ticket = ticket + yrow.getString("Ticket");
                                }

                                list.add(ticket);
                            }
                        }

                        SetAdapter();

                        Toast.makeText(ListActivity.this, jsonResult.getString("message"),
                                Toast.LENGTH_SHORT).show();
                    }
                    catch (Exception ex)
                    {
                        Log.e("list_error",ex.getMessage() + " " + result + "");
                    }

                }
            }
        }.execute("api/scheduleEntries");
    }


    private class StableArrayAdapter extends ArrayAdapter<String> {

        HashMap<String, Integer> mIdMap = new HashMap<String, Integer>();

        public StableArrayAdapter(Context context, int textViewResourceId,
                                  List<String> objects) {
            super(context, textViewResourceId, objects);
            for (int i = 0; i < objects.size(); ++i) {
                mIdMap.put(objects.get(i), i);
            }
        }

        @Override
        public long getItemId(int position) {
            String item = getItem(position);
            return mIdMap.get(item);
        }

        @Override
        public boolean hasStableIds() {
            return true;
        }

    }
}
