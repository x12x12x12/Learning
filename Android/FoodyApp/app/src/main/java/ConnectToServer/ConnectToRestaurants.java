package ConnectToServer;

import android.os.AsyncTask;

import com.example.tut_slz.da_client.ClientDA;
import com.example.tut_slz.da_client.Food;
import com.example.tut_slz.da_client.Restaurant;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.web.client.RestTemplate;

import java.net.Socket;

import Items.Item;
import Items.ResItem;

/**
 * Created by tut_slz on 17/12/2014.
 */
public class ConnectToRestaurants extends AsyncTask<String, Void, Boolean> {
    @Override
    protected Boolean doInBackground(String... params) {
        try {
            Socket s=new Socket(ClientDA.rsInfo.getIPAddr(),8080);
            s.close();
            final String url = params[0];
            RestTemplate restTemplate = new RestTemplate();
            restTemplate.getMessageConverters().add(new MappingJackson2HttpMessageConverter());
            ResponseEntity<ResItem[]> aitem=restTemplate.getForEntity(url,ResItem[].class);
            Restaurant.aItem=aitem.getBody();
            return true;
        }
        catch (Exception e)
        {
            return false;
        }

    }
}
