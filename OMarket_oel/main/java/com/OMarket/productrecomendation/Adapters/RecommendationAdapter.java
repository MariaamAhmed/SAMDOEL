package com.OMarket.products.Adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;
import com.OMarket.products.Activities.HotelInfo;
import com.OMarket.products.R;
import com.OMarket.products.model.Hotel;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RecommendationAdapter extends RecyclerView.Adapter<RecommendationAdapter.HotelViewHolder> implements Serializable {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private ProductsViewHolder prodctsViewHolder;
    private List<Product> hotels;

    public RecommendationAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setProducts(Set<Product> lists) {
        this.products = new ArrayList<>(lists);
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ProductsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.hotelcard, parent, false);

        productsViewHolder = new ProductViewHolder(view);

        return productViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final ProductViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        Picasso
                .with(context)
                .load(Uri.parse(products.get(position).getImageUrl()))
                .into(holder.productsImage);

        holder.productRatings.setText(products.get(position).getRatings());
        holder.tags.setText(products.get(position).getTags());
        holder.productName.setText(products.get(position).getName());
        holder.productViews.setText(products.get(position).getVisits() + "\nViews");
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vis = Integer.valueOf(products.get(position).getVisits());
                products.get(position).setVisits(String.valueOf(++vis));
                setProducts(new HashSet<>(products));
                Intent i = new Intent(context, ProductInfo.class);
                i.putExtra("data", hotels.get(position));
                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return products.size();
    }


    public class ProductViewHolder extends RecyclerView.ViewHolder implements UpdateListener, Serializable {

        TextView productViews;
        ImageView productImage;
        TextView productRatings, productsName;
        TextView tags;
        Button bookButton;

        public ProductViewHolder(@NonNull final View itemView) {
            super(itemView);
            productImage = itemView.findViewById(R.id.productImage);
            productRatings = itemView.findViewById(R.id.ratings);
            bookButton = itemView.findViewById(R.id.productsBookButton);
            tags = itemView.findViewById(R.id.tagsList);
            productName = itemView.findViewById(R.id.productName);
            productViews = itemView.findViewById(R.id.productCardViews);

        }

        @Override
        public void update() {
            notifyItemChanged(getAdapterPosition());
        }
    }

    public interface UpdateListener extends Serializable {
        void update();
    }


}
