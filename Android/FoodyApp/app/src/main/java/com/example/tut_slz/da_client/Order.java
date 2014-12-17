package com.example.tut_slz.da_client;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import java.util.ArrayList;

import Items.SendOrder;

/**
 * Created by tut_slz on 26/11/2014.
 */
public class Order extends Activity{
    public static Context OContext;
    public static ArrayList<OrderFood> FOrder=new ArrayList<OrderFood>();
    public static int iSeclect;
    public static WebView wvOrder;
    public static boolean Changed=false;
    public static SendOrder sendOrder;
    public static String OrderResult;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        OContext=this;
        OrderResult="";
        sendOrder=new SendOrder();
        String sHTML=MakeHTML();
        wvOrder =new WebView(this);
        wvOrder.getSettings().setJavaScriptEnabled(true);
        wvOrder.addJavascriptInterface(new JSInterface(this), "pOrd");
        //wvOrder.setWebChromeClient(new WebChromeClient());
        wvOrder.loadDataWithBaseURL("file:///android_asset/", sHTML, "text/html", "utf-8", null);
        setContentView(wvOrder);
    }

    private static String MakeHTML()
    {
        Double TotalMoney=0.0;
        String sHTML="<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/><script src=\"JScript.js\" type=\"text/javascript\"></script></HEAD><body background=\"background.jpg\">";
        for (int i=0;i<FOrder.size();++i)
        {
            sHTML+="<div id=\"Order" +i+ "\"  class=\"divordert\">";
            sHTML+="<image class=\"Image\" src=\"" + FOrder.get(i).getItem().getImg_url() + "\"/>";
            sHTML+="<h1 class=\"horder\">" + FOrder.get(i).getItem().getName() + "</h1>";
            sHTML+="<p class=\"Detail Resname\" >Restaurant: \""+FOrder.get(i).getItem().getRestaurant_name()+"\"</p>";
            sHTML+="<p class=\"Detail\"> Date Create: "+ FOrder.get(i).getItem().getCreateDate().toString() + "<br>Date Update: "+ FOrder.get(i).getItem().getUpdateDate().toString() + "<br>Price: "+FOrder.get(i).getItem().getPrice()+ "<br>Status:  "+ FOrder.get(i).getItem().getStatus() +"</p>";
            //sHTML+="<p class=\"Detail\">Price: " + FOrder.get(i).getItem().getPrice() + "<br>Status:  " + FOrder.get(i).getItem().getStatus() + "</p>";

            sHTML+="</div>";
            sHTML+="<div id=\"Odetail"+i+"\" class=\"divodetail\">";
            sHTML+="<span><a id=\"aQuantity"+i+"\">Quantity= "+ FOrder.get(i).getCount() +"</a></br>Money: <a id=\"aMoney"+i+"\">"+ FOrder.get(i).getCount()*FOrder.get(i).getItem().getPrice() +"</a> vnd</br><button onclick=\"OrderEdit("+i+")\">Edit</button><span id=\"SpanEdit"+i+"\" class=\"modcart\"><label>Quantity: </lable><input type=\"text\" id=\"text"+i+"\" style=\"width:50%;\" onblur=\"setBlur("+i+")\" onkeypress='return event.charCode >= 48 && event.charCode <= 57'/></span></br><button onclick=\"OrderDelete("+i+")\">Delete</button></span>";
            sHTML+="</div>";
            sHTML+="</br>";
            TotalMoney+=FOrder.get(i).getItem().getPrice()*FOrder.get(i).getCount();
        }
        sHTML+="<div class=\"atotal\"><a class=\"atotallabel\">Total: </a><a id=\"TotalMoney\" >"+TotalMoney+"</a><a class=\" atotaldv\"> vnd</a></br><button class=\"btnsend\" onclick=\"SendOrder()\">Send Order</button></div>";
        sHTML+="</body></html>";
        return sHTML;
    }

    public static void Refresh()
    {
        String sHTML=MakeHTML();
        wvOrder.loadDataWithBaseURL("file:///android_asset/", sHTML, "text/html", "utf-8", null);
        Changed=false;
    }
}
