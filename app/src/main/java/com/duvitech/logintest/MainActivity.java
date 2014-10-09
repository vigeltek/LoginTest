package com.duvitech.logintest;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SharedObject.getInstance().AuthToken = "IEZKyQzA-6t60-H2RrYdKiFS_jy3PXTtvOSS_FCeah1fiz5BGoZY_Upy3AKw4G8ziJJbEUylkrNOOp0ADeZ3ol6rucfb0DHOBy-5RB8yP3-00kjl04sohCoTdVbir3wNKlkXXUHA04fkhvKsX4aqKLvUrkicxCoBLcVn29i-Ud_2uHBRjQItHU-X1z3hIXkrv84cQn2WtQ70zxvWvYERVq9jhFyxsoR_AeWEpjbo6-JHPevv6DY095jmNuiWeCTfQ1RDCUgZWOQbsMkU2m5HqQKxEZAnqQnK99PBlEao8DTroX6TX2mUChcg1OE1gns420QihV4pDoogdUmbQNM06gUb-CPJ0HqyNeDz20vdhb_jGFPsyvAM0wQp9QsQrHKsM8h8qh7AHZN1ZIV_bkBX0ruRbwBURvuGbUqIZUS25DIZvPjgvKU-WqF8Q3EOCjaFu_e_pZpw4aJJskdGGKhpBTw5QZMMWPTlwW3k4uIc1kzq4M2sm3vDuxfmbin4lNOmYcCFSTAgzBIS5FTdtvo62h6oL2sefaMVsICHJHqOpKw";

        final Button btnLogin = (Button)findViewById(R.id.btnLogin);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });

        final Button btnGet = (Button)findViewById(R.id.btnGet);
        btnGet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        final Button btnMaps = (Button)findViewById(R.id.btnMap);
        btnMaps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent = new Intent();
                intent.setClass(MainActivity.this, MapsActivity.class);
                startActivity(intent);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
