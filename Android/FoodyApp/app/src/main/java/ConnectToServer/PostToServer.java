package ConnectToServer;

import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import com.example.tut_slz.da_client.ClientDA;
import com.example.tut_slz.da_client.Food;
import com.example.tut_slz.da_client.Order;
import com.example.tut_slz.da_client.Restaurant;

import org.apache.http.Header;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.HttpHead;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.FormHttpMessageConverter;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

import Items.Item;
import Items.ResItem;
import Items.SendOrder;

public class PostToServer extends AsyncTask<String, Void,Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        final String url = params[0];
        try {
            Socket s=new Socket(ClientDA.rsInfo.getIPAddr(),8080);
            s.close();
            RestTemplate restTemplate = new RestTemplate();
            HttpHeaders hHeader=new HttpHeaders();
            hHeader.setContentType(MediaType.APPLICATION_JSON);
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            org.springframework.http.HttpEntity<SendOrder> httpRequest=new org.springframework.http.HttpEntity<SendOrder>(Order.sendOrder,hHeader);
            SendOrder sendOrder=restTemplate.postForObject(url,httpRequest,SendOrder.class);
            SendOrder testdata=sendOrder;
        } catch (Exception e){
           Exception data=e;
           return  false;
        }
        return true;
    }
}
