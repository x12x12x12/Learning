package com.example.tut_slz.da_client;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Build;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;
import android.widget.Toast;

import java.util.concurrent.ExecutionException;

//
///**
// * Created by tut_slz on 19/11/2014.
// */
public class JSInterface extends Activity{
    Context ctActivity;
    public JSInterface(Context ctAct)
    {
        ctActivity=ctAct;
    }
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    @JavascriptInterface
    public void ShowChildActivity(final int iIndex,final int Rcselect,final byte bWeb)
    {
        new Thread() {
            public void run() {
                    try {
                        runOnUiThread(new Runnable() {

                            @Override
                            public void run() {
                                switch (bWeb) {
                                    case 0: {
                                        Food.iSelect = iIndex;
                                        AsyncTask<Integer,Void,Boolean> aWorker=new ChangUI().execute(0);
                                        break;
                                    }
                                    case 1: {
                                        Restaurant.RSelect=iIndex;
                                        Restaurant.RCSeclect=Rcselect;
                                        AsyncTask<Integer,Void,Boolean> aWorker=new ChangUI().execute(1);
                                        break;
                                    }
                                    default: {
                                        break;
                                    }
                                }
                            }
                        });
                        Thread.sleep(300);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
        }.start();


    }
    @JavascriptInterface
    public String ShowChildItem(int Index){
        String sRet="";
        try {
            Restaurant.ChildItem[Index]= Food.LoadData("http://"+ClientDA.IPServer+":8080/rest-webapp/rest/store/Restaurant");

            sRet=Food.MakeHTML(Restaurant.ChildItem[Index],(byte)1).toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sRet;
    }
    @JavascriptInterface
    public void OrderDelete(int iIndex){
        try {
            Order.FOrder.remove(iIndex);
            AsyncTask<Integer,Void,Boolean> asyn =new ChangUI().execute(2);


        }catch (Exception e){
            Toast.makeText(Order.OContext,iIndex,Toast.LENGTH_SHORT).show();
        }

    }
    @JavascriptInterface
    public double CalMoney(int iIndex,String sCount){
        int Count=Integer.parseInt(sCount);
        Order.FOrder.get(iIndex).setCount(Count);
        return Order.FOrder.get(iIndex).getItem().getPrice()*Count;
    }

    @JavascriptInterface
    public void SendOrder(){
        Toast.makeText(Order.OContext,"Order success.",Toast.LENGTH_SHORT).show();
    }
}
