package com.example.tut_slz.da_client;

import Items.Item;

/**
 * Created by tut_slz on 26/11/2014.
 */
public class OrderFood {
    private Item item;
    private int count;

    public Item getItem() {
        return item;
    }

    public void setItem(Item item) {
        this.item = item;
    }

    public int getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public boolean Itemcmp(Item titem){
        if(item.getId().equals(titem.getId())) return true;
        return false;
    }

    public void IncCount(){++count;}
    public void DesCount(int Subt){count=count>=Subt?count-Subt:0;}
}
