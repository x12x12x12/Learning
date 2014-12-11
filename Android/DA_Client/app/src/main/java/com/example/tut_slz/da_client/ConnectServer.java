package com.example.tut_slz.da_client;

import android.os.AsyncTask;

import java.net.Socket;
import java.util.Arrays;

import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

/**
 * Created by tut_slz on 19/11/2014.
 */
public class ConnectServer extends AsyncTask<String , Void, Item[]> {
    @Override
    protected Item[] doInBackground(String... params) {
        try {
            Socket s=new Socket(ClientDA.IPServer,8080);
            s.close();
            final String url = params[0];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<Item[]> aitem=restTemplate.getForEntity(url,Item[].class);
            return aitem.getBody();
        }
        catch (Exception e)
        {
            return null;
        }

    }
}
