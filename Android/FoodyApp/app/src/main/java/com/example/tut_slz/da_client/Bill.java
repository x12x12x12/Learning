package com.example.tut_slz.da_client;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.webkit.WebView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

/**
 * Created by tut_slz on 15/12/2014.
 */
public class Bill extends Activity {
    public static ArrayList<BillItem> bItem;
    public static WebView wvBill;
    //private static String sbHTML;
    public static Context ctBill;
    public static Boolean bChanged=false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ctBill=this;
        bItem=new ArrayList<BillItem>();
        wvBill =new WebView(ctBill);
        wvBill.getSettings().setJavaScriptEnabled(true);
        wvBill.addJavascriptInterface(new JSInterface(ctBill), "pBill");
        bChanged=false;
        SetView();
        setContentView(wvBill);
    }
    private static void SetView(){
        String sbHTML=MakeHTML();
        wvBill.loadDataWithBaseURL("file:///android_asset/", sbHTML, "text/html", "utf-8", null);
    }

    private static String MakeHTML(){
        String sbHTML="<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/><script src=\"JScript.js\" type=\"text/javascript\"></script></HEAD><body background=\"background.jpg\">";
        try{

        for(int i=0;i<bItem.size();++i){
            sbHTML+="<div><h1>ID Bill: "+bItem.get(i).getId()+"</h1><p>UserName: "+bItem.get(i).getUserName()+"</p><p>Phone: "+bItem.get(i).getPhone()+"</p><p>Address: "+bItem.get(i).getAddr()+"</p>";
            sbHTML+="</hr>";
        }
       }
        catch (Exception e) {Toast.makeText(Bill.ctBill,"Fail to load Bill",Toast.LENGTH_SHORT).show();}
        sbHTML+="</body></html>";
        return sbHTML;
    }


    public static void Refresh(){
        SetView();
        bChanged=false;
    }
}
