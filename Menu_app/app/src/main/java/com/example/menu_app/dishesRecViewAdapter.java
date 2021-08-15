package com.example.menu_app;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class dishesRecViewAdapter extends RecyclerView.Adapter<dishesRecViewAdapter.ViewHolder>{

    private ArrayList<dish> dishes = new ArrayList<dish>();
    private Context context;

    //constructor
    public dishesRecViewAdapter(Context context){
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.dishes_list_item, parent, false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.txtDishes.setText(dishes.get(position).getName());
        holder.txt_cn_name.setText(dishes.get(position).getCn_name());
        holder.parent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(context, dishes.get(position).getName() + " selected", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public int getItemCount() {
        return dishes.size();
    }

    public void setDishes(ArrayList<dish> dishes){
        this.dishes = dishes;
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        private TextView txtDishes, txt_cn_name, doublePrice;
        private CardView parent;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txtDishes = itemView.findViewById(R.id.txtDishes);
            txt_cn_name = itemView.findViewById(R.id.txt_cn_name);
            doublePrice = itemView.findViewById(R.id.doublePrice);
            parent = itemView.findViewById(R.id.parent);
        }
    }
}
