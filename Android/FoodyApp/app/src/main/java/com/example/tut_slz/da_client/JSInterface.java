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

import ConnectToServer.PostToServer;

import static com.example.tut_slz.da_client.ClientDA.*;

//
///**
// * Created by tut_slz on 19/11/2014.
// */
public class JSInterface extends Activity{
    Context ctActivity;
    String sTotalPrice;
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
            Restaurant.RSelect=Index;
            //Restaurant.ChildItem[Index]= Food.LoadData("http://"+ClientDA.IPServer+":8080/rest-webapp/rest/store/Restaurant");
            Food.LoadData("http://"+ rsInfo.getIPAddr()+":8080"+ rsInfo.getUrlResFood()+Restaurant.aItem[Index].getName(),"2");
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
    public void SendToServer(String sUsername, String sPhoneNumber, String sAddr){
        ClientDA.sUsername=sUsername;
        ClientDA.sPhoneNumber=sPhoneNumber;
        ClientDA.sAddr=sAddr;
        Toast.makeText(Order.OContext,ClientDA.sUsername,Toast.LENGTH_LONG).show();
        Activity activity=(Activity)ctActivity;
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String sbJson="";
                sbJson+="{\"userOrderName\":\""+ClientDA.sUsername+"\",\"phone\":\""+ClientDA.sPhoneNumber+"\",\"address\":\""+ClientDA.sAddr+"\",\"totalPrice\":\""+sTotalPrice+"\",\"list_food\":\"";
                String list_food="";
                for(int i=0;;){
                    list_food+=Order.FOrder.get(i).getItem().getId()+"."+Order.FOrder.get(i).getCount();
                    if(i<Order.FOrder.size()-1){
                        list_food+=",";++i;
                    }else{
                        //sbJson+="\"}";
                        break;
                    }
                }
                Toast.makeText(Order.OContext,sbJson,Toast.LENGTH_LONG).show();
                Order.sendOrder.setUserOrderName(ClientDA.sUsername);Order.sendOrder.setPhone(ClientDA.sPhoneNumber);Order.sendOrder.setAddress(ClientDA.sAddr);
                Order.sendOrder.setTotalPrice(sTotalPrice);
                Order.sendOrder.setList_food(list_food);
                Order.sendOrder.setStatus(0);Order.sendOrder.setRestaurant_code(null);Order.sendOrder.setId(null);Order.sendOrder.setCreateData(null);

                //Order.wvOrder.loadUrl("javascript:jSendToServer('"+sbJson+"')");
                try {
                    AsyncTask<String,Void,Boolean> asyncTask=new PostToServer().execute("http://"+ rsInfo.getIPAddr()+":8080"+ rsInfo.getUrlOrder(),sbJson);
                    IsOnline=asyncTask.get();
                    if(IsOnline){
                        ResultBill(Order.OrderResult);
                    }
                }catch (InterruptedException e) {
                    Toast.makeText(Order.OContext,"Cannot connect to Server.",Toast.LENGTH_SHORT).show();
                } catch (ExecutionException e) {
                    Toast.makeText(Order.OContext,"Cannot connect to Server.",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }

    @JavascriptInterface
    public void SendOrder(String sTPrice){
        sTotalPrice=sTPrice;
        sUsername="";
        sPhoneNumber="";
        sAddr="";
        AsyncTask<Integer,Void,Boolean> asyn =new ChangUI().execute(3);
        //Boolean bRet=ChangUI().execute(3).get();
        //Order.wvOrder.loadUrl("javascript:diplayJavaMsg()");
    }

    @JavascriptInterface
    public void ResultBill(String sJson){
        if(""!=sJson) {
            String[] sAJson = sJson.split(",");
            BillItem bitem;
            for (int i = 0; i < sAJson.length; ++i) {
                bitem = new BillItem();
                bitem.AddItem(sAJson[i], sUsername, sPhoneNumber, sAddr);
                Bill.bItem.add(bitem);
            }
            Order.FOrder.clear();Order.Refresh();
            Toast.makeText(Order.OContext,"Order successfully :)",Toast.LENGTH_LONG).show();
            Bill.bChanged = true;
        }else {Toast.makeText(Order.OContext,"Order Fail, try again :(",Toast.LENGTH_LONG).show();Order.Refresh();}
    }

    @JavascriptInterface
    public void Mess(String data){
        Toast.makeText(Order.OContext,data,Toast.LENGTH_LONG).show();
    }
}
