package com.example.tut_slz.da_client;

import android.app.Activity;
import android.app.TabActivity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TabHost;

import java.util.Objects;


public class ClientDA extends TabActivity {
    private TabHost tbMain;
    public static Database db;
    public static String IPServer="192.168.56.1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_client_d);
        tbMain=getTabHost();
        tbMain.setOnTabChangedListener(new TabHost.OnTabChangeListener() {
            @Override
            public void onTabChanged(String tabId) {
                if(2 == tbMain.getCurrentTab()) {
                    if (true == Order.Changed) {
                        Order.Refresh();
                    }
                }
            }
        });
        TabHost.TabSpec tSpec;
        Intent intent;

        //Food
        intent=new Intent(ClientDA.this,Food.class);
        tSpec=tbMain.newTabSpec("food").setIndicator("",getResources().getDrawable(R.drawable.foodconfig)).setContent(intent);
        tbMain.addTab(tSpec);

        //Restaurant
        intent=new Intent(ClientDA.this,Restaurant.class);
        tSpec=tbMain.newTabSpec("restaurant").setContent(intent);
        tSpec.setIndicator("",getResources().getDrawable(R.drawable.resconfig));
        tbMain.addTab(tSpec);

        //Order
        intent=new Intent(ClientDA.this,Order.class);
        tSpec=tbMain.newTabSpec("order").setIndicator("",getResources().getDrawable(R.drawable.cartconfig)).setContent(intent);
        tbMain.addTab(tSpec);


        tbMain.setCurrentTab(0);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_client_d, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.iRefresh) {
            int iIndex=tbMain.getCurrentTab();
            switch (iIndex)
            {
                case 0:{
                    Food.Refresh();
                    break;
                }
                case 1:{
                    Restaurant.Refresh();
                    break;
                }
                case 2:{
                    Order.Refresh();
                    break;
                }
                default:{break;}
            }
        }

        return super.onOptionsItemSelected(item);
    }
}
