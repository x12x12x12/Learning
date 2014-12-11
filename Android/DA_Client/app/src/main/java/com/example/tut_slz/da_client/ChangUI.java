package com.example.tut_slz.da_client;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.AsyncTask;
import android.webkit.WebView;
import android.widget.Toast;

import java.net.Socket;

/**
 * Created by tut_slz on 20/11/2014.
 */
public class ChangUI extends AsyncTask<Integer,Void,Boolean> {
    int iInterface;
    int Arg1;
    @Override
    protected Boolean doInBackground(Integer... params) {
        try {
            iInterface = params[0];
            //Arg1=params[1];
            return true;
        }
        catch (Exception e)
        {
            return false;
        }
    }

    private void SetOrder(Item item)
    {
        for (int i=0;i<Order.FOrder.size();++i)
        {
            if (Order.FOrder.get(i).Itemcmp(item))
            {
                Order.FOrder.get(i).IncCount();
                return;
            }
        }
        OrderFood of=new OrderFood();
        of.setItem(item);
        of.setCount(1);
        Order.FOrder.add(of);
    }

    @Override
    protected void onPostExecute(Boolean b)
    {
        if(true == b)
        {
            WebView wvItem;
            final Context context;
            final Item sItem;
            StringBuilder sbHtml=new StringBuilder();
            sbHtml.append("<HTML><HEAD><LINK href=\"StyleCSS.css\" type=\"text/css\" rel=\"stylesheet\"/></HEAD><body>");
            if (2 == iInterface){
                //context=Order.OContext;
                Order.Refresh();
            }
            else{
                if (0 == iInterface)
                {
                    context=Food.Fcontext;
                    sItem=Food.aItem[Food.iSelect];
                }
                else {context=Restaurant.Rcontext;
                    sItem=Restaurant.ChildItem[Restaurant.RSelect][Restaurant.RCSeclect];}
                wvItem=new WebView(context);
                sbHtml.append("<image class=\"Image\" src=\"" + sItem.getImg_url() + "\"/>");
                sbHtml.append("<h1>"+sItem.getName()+"</h1>");
                sbHtml.append("<p class=\"Detail Resname\">Restaurant: "+sItem.getRestaurant_name()+"</p>");
                sbHtml.append("<p class=\"Detail\"> Date Create: "+ sItem.getCreateDate().toString() + "<br>Date Update: "+ sItem.getUpdateDate().toString() + "<br>Price: "+sItem.getPrice()+ "<br>Status:  "+ sItem.getStatus() +"</p>");
                sbHtml.append("</body></HTML>");
                wvItem.loadDataWithBaseURL("file:///android_asset/", sbHtml.toString(), "text/html", "utf-8", null);
                AlertDialog.Builder adb=new AlertDialog.Builder(context).setView(wvItem).setTitle("Your Food").setPositiveButton("Buy",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        SetOrder(sItem);
                        Order.Changed=true;
                        Toast.makeText(context, "Buy Sucess", Toast.LENGTH_SHORT).show();
                    }
                }).setNegativeButton("Cancel",new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.cancel();
                    }
                });
                adb.create().show();
            }
        }
        else
        {
            Toast.makeText(Food.Fcontext,"Fail",Toast.LENGTH_SHORT).show();
        }
    }
}
