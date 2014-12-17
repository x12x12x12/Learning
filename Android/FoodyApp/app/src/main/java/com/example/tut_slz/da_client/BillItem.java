package com.example.tut_slz.da_client;

/**
 * Created by tut_slz on 15/12/2014.
 */
public class BillItem {
    private String id;
    private String userName;
    private String phone;
    private String addr;

    public void AddItem(String sid,String suser, String sphone, String saddr){
        id=sid;
        userName=suser;
        phone=sphone;
        addr=saddr;
    }
    public String getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public String getPhone() {
        return phone;
    }

    public String getAddr() {
        return addr;
    }
}
