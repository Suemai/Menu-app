package com.example.menu_app.classes;

public class dish {
    private String name;            //name of the dish
    private String cn_name;         //chinese name of the dish
    private String staff_name;      //staff name of the dish
    private String id;              //the id of the dish (some dishes won't have any)
    private int price;           //price of the dish
    private int dish_counter;       //number of times the dish has been ordered

    public dish(String name, String cn_name, String staff_name, String id, int price, int dish_counter){
        this.name = name;
        this.cn_name = cn_name;
        this.staff_name = staff_name;
        this.id = id;
        this.price = price;
        this.dish_counter = 0;
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

    public double getPrice() {
        return price/100;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getDish_counter() {
        return dish_counter;
    }

    public void setDish_counter(int dish_counter) {
        this.dish_counter = dish_counter;
    }
}
