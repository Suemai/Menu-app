package com.example.menu_app;

import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class cartRecViewAdapter {



    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView cart_dishes, dish_price_cart;
        private RelativeLayout cart_parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            cart_dishes = itemView.findViewById(R.id.cart_dishes);
            dish_price_cart = itemView.findViewById(R.id.dish_price_cart);
            cart_parent = itemView.findViewById(R.id.cart_parent);

        }
    }
}
