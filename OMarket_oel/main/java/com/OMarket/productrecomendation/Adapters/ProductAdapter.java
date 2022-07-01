package com.OMarket.product.Adapters;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.OMarket.product.Activities.ProductInfo;
import com.OMarket.product.R;
import com.OMarket.product.model.Product;
import com.OMarket.product.model.ProductResult;

import java.util.List;

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ProductViewHolder> {

    private final Context context;
    private final LayoutInflater inflater;
    private View view;
    private ProductViewHolder productViewHolder;
    private List<Product> products;
    private ProductResult productResult = new ProductResult();

    public ProductAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    public void setProducts(List<Product> lists) {
        this.products = lists;
        productResult.setProducts(products);
        ProductResult productResult = new ProductResult();
        productResult.setProducts(products);
        storeUpdates(productResult);
        notifyDataSetChanged();
    }

    public void storeUpdates(ProductResult productResult) {
        SharedPreferences.Editor spe = context.getSharedPreferences("product", Context.MODE_PRIVATE).edit();
        Gson gson = new Gson();
        spe.putString("data", gson.toJson(productResult));
        spe.apply();

    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        view = inflater.inflate(R.layout.hotelcard, parent, false);
       productViewHolder = new ProductViewHolder(view);

        return productlViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, final int position) {
        Picasso
                .with(context)
                .load(Uri.parse(products.get(position).getImageUrl()))
                .into(holder.productsImage);

        holder.productRatings.setText(products.get(position).getRatings());
        holder.tags.setText(products.get(position).getTags());
        holder.productsName.setText(products.get(position).getName());
        holder.productsViews.setText(products.get(position).getVisits() + "\nViews");
        holder.bookButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int vis = Integer.valueOf(products.get(position).getVisits());
                products.get(position).setVisits(String.valueOf(++vis));
                setProducts(products);
                Intent i = new Intent(context,productsInfo.class);
                i.putExtra("products", productsResult);
                i.putExtra("pos", position);
                i.putExtra("data", products.get(position));
                context.startActivity(i);

            }
        });

    }


    @Override
    public int getItemCount() {
        return products.size();
    }

    class ProductsViewHolder extends RecyclerView.ViewHolder {

        ImageView productsImage;
        TextView productsRatings, productsName, productsViews;

        TextView tags;
        Button bookButton;

        public HotelViewHolder(@NonNull View itemView) {
            super(itemView);
            productsImage = itemView.findViewById(R.id.products.Image);
           productsRatings = itemView.findViewById(R.id.ratings);
            bookButton = itemView.findViewById(R.id.productsBookButton);
            tags = itemView.findViewById(R.id.tagsList);
            productsName = itemView.findViewById(R.id.productsName);
          productsViews = itemView.findViewById(R.id.productsCardViews);


        }
    }


}
