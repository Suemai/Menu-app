package com.example.menu_app.Activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.menu_app.Adapter.dishesRecViewAdapter;
import com.example.menu_app.R;
import com.example.menu_app.Class.dish;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private RecyclerView dishesRecView;
    private final ArrayList<dish> dishes = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dishesRecView = findViewById(R.id.dishesRecView);

        dishes.add(new dish("Barbecued Spare Ribs with BBQ Sauce", "骨", "骨", "1", 650, 0));
        dishes.add(new dish("Spare Ribs with Chilli and Salt", "椒盐骨", "盐骨", "2",650, 0));
        dishes.add(new dish("Barbecued Spare Ribs with Honey Sauce", "蜜骨", "蜜骨", "3", 650, 0));
        dishes.add(new dish("Sweet & Sour Spare Ribs", "咕咾骨", "古骨", "4", 650, 0));
        dishes.add(new dish("Chicken Wings with Chilli & Salt", "椒盐鸡翅", "盐鸡羽", "8", 650, 0));
        dishes.add(new dish("Crab Claws (4)", "蟹爪", "蟹爪", "21", 360, 0));
        dishes.add(new dish("1/2 Crispy Aromatic Duck", "1/2 香鸭", "1/2 香甲", "28", 880, 0));
        dishes.add(new dish("King Prawns with Green Peppers & Black Bean Sauce", "豉椒大虾", "士大下", "59", 650, 0));
        dishes.add(new dish("Sweet and Sour King Prawns Balls (Cantonese Style)", "中式咕咾大虾", "中古大下", "162", 700, 0));

        dishesRecyclerView();

    }

    private void dishesRecyclerView(){
        dishesRecViewAdapter adapter = new dishesRecViewAdapter(this);
        adapter.setDishes(dishes);

        dishesRecView.setAdapter(adapter);
        dishesRecView.setLayoutManager(new GridLayoutManager(this,3));
    }
}