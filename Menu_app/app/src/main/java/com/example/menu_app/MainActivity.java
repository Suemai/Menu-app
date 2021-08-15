package com.example.menu_app;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView dishesRecView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dishesRecView = findViewById(R.id.dishesRecView);

        ArrayList<dish> dishes = new ArrayList<>();
        dishes.add(new dish("Barbecued Spare Ribs with BBQ Sauce", "骨", "骨", "1", 0.00, 6.50, 0));
        dishes.add(new dish("Spare Ribs with Chilli and Salt", "椒盐骨", "盐骨", "2", 0.00, 6.50, 0));
        dishes.add(new dish("Barbecued Spare Ribs with Honey Sauce", "蜜骨", "蜜骨", "3", 0.00, 6.50, 0));
        dishes.add(new dish("Sweet & Sour Spare Ribs", "咕咾骨", "古骨", "4", 0.00, 6.50, 0));
        dishes.add(new dish("Chicken Wings with Chilli & Salt", "椒盐鸡翅", "盐鸡羽", "8", 0.00, 6.50, 0));
        dishes.add(new dish("Crab Claws (4)", "蟹爪", "蟹爪", "21", 3.60, 0.00, 0));
        dishes.add(new dish("Crispy Aromatic Duck", "香鸭", "香甲", "28", 8.80, 16.30, 0));
        dishes.add(new dish("King Prawns with Green Peppers & Black Bean Sauce", "豉椒大虾", "士大下", "59", 6.50, 0.00, 0));
        dishes.add(new dish("Sweet and Sour King Prawns Balls (Cantonese Style)", "中式咕咾大虾", "中古大下", "162", 7.00, 0.00, 0));

        dishesRecViewAdapter adapter = new dishesRecViewAdapter(this);
        adapter.setDishes(dishes);

        dishesRecView.setAdapter(adapter);
        dishesRecView.setLayoutManager(new GridLayoutManager(this,3));

    }
}