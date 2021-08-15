package com.example.menu_app;

public class dish {
    private String name;            //name of the dish
    private String cn_name;         //chinese name of the dish
    private String staff_name;      //staff name of the dish
    private String id;              //the id of the dish (some dishes won't have any)
    private double large_price;     //price of the large/regular portion
    private double small_price;     //price of the small portion
    private int dish_counter;       //number of times the dish has been ordered

    public dish(String name, String cn_name, String staff_name, String id, double small_price, double large_price, int dish_counter){
        this.name = name;
        this.cn_name = cn_name;
        this.staff_name = staff_name;
        this.id = id;
        this.large_price = large_price;
        this.small_price = small_price;
        this.dish_counter = dish_counter;
    }
    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCn_name() {
        return cn_name;
    }

    public void setCn_name(String cn_name) {
        this.cn_name = cn_name;
    }

    public String getStaff_name() {
        return staff_name;
    }

    public void setStaff_name(String staff_name) {
        this.staff_name = staff_name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public double getLarge_price() {
        return large_price;
    }

    public void setLarge_price(float large_price) {
        this.large_price = large_price;
    }

    public double getSmall_price() {
        return small_price;
    }

    public void setSmall_price(float small_price) {
        this.small_price = small_price;
    }

    public int getDish_counter() {
        return dish_counter;
    }

    public void setDish_counter(int dish_counter) {
        this.dish_counter = dish_counter;
    }
}
