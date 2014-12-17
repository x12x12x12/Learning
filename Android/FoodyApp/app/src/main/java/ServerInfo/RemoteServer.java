package ServerInfo;

/**
 * Created by tut_slz on 16/12/2014.
 */
public class RemoteServer {
    private String IPAddr;
    private String UrlHotFood;
    private String UrlRestaurant;
    private String UrlResFood;
    private String UrlOrder;

    public RemoteServer(){
        IPAddr="192.168.1.30";
        UrlHotFood="/rest/items";
        UrlRestaurant="/rest/restaurants";
        UrlResFood="/rest/store/";
        UrlOrder="/rest/create_order";
    }
    public String getIPAddr() {
        return IPAddr;
    }

    public String getUrlHotFood() {
        return UrlHotFood;
    }

    public String getUrlRestaurant() {
        return UrlRestaurant;
    }

    public String getUrlResFood() {
        return UrlResFood;
    }

    public String getUrlOrder() {
        return UrlOrder;
    }
}
