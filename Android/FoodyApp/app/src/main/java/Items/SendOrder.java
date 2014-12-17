package Items;

/**
 * Created by tut_slz on 17/12/2014.
 */
import java.util.Date;
public class SendOrder {

    private String id;
    private String userOrderName;
    private String phone;
    private String address;
    private String totalPrice;
    private String list_food;
    private Date createDate;
    private String restaurant_code;
    private int status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserOrderName() {
        return userOrderName;
    }

    public void setUserOrderName(String userOrderName) {
        this.userOrderName = userOrderName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getList_food() {
        return list_food;
    }

    public void setList_food(String list_food) {
        this.list_food = list_food;
    }

    public Date getCreateDate() {
        return createDate;
    }

    public void setCreateData(Date createDate) {
        this.createDate = createDate;
    }

    public String getRestaurant_code() {
        return restaurant_code;
    }

    public void setRestaurant_code(String restaurant_code) {
        this.restaurant_code = restaurant_code;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
