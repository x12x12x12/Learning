package Items;

import java.util.Date;

/**
 * Created by tut_slz on 19/11/2014.
 */
public class Item {
    private String id;
    private String name;
    private String restaurant_name;
    private Date createDate;
    private Date updateDate;
    private Double price;
    private int status;
    private String img_url;
    private String img_ico;

    public void SetItem(String Id,String Name, String ResName, Date CreDay,Date Update, Double Price, int Status, String Img_url)
    {
        this.id=Id;
        this.name=Name;
        this.restaurant_name=ResName;
        this.createDate=CreDay;
        this.updateDate=Update;
        this.price=Price;
        this.status=Status;
        this.img_url=Img_url;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getRestaurant_name() {
        return restaurant_name;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public Date getUpdateDate() {
        return updateDate;
    }

    public Double getPrice() {
        return price;
    }

    public int getStatus() {
        return status;
    }

    public String getImg_url() {
        return img_url;
    }

    public String getImg_ico() {
        return img_ico;
    }
}
