package com.example.tut_slz.da_client;

import android.os.AsyncTask;

import java.net.Socket;
import java.util.Objects;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import Items.Item;
import Items.ResItem;

/**
 * Created by tut_slz on 19/11/2014.
 */
public class ConnectServer extends AsyncTask<String , Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        try {
            Socket s=new Socket(ClientDA.rsInfo.getIPAddr(),8080);
            s.close();
            final String url = params[0];
            final String sItem=params[1];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            if(sItem.equalsIgnoreCase("0")){
                ResponseEntity<Item[]> aitem=restTemplate.getForEntity(url,Item[].class);
                Food.aItem=aitem.getBody();
            }else {
                if(sItem.equalsIgnoreCase("1")){
                    ResponseEntity<ResItem[]> aitem=restTemplate.getForEntity(url,ResItem[].class);
                    Restaurant.aItem=aitem.getBody();
                }else{
                    ResponseEntity<Item[]> aitem=restTemplate.getForEntity(url,Item[].class);
                    Restaurant.ChildItem[Restaurant.RSelect]=aitem.getBody();
                }
            }
            //return aitem.getBody();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }
}
