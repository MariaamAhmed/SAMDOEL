package com.OMarket.product.Activities;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.gson.Gson;
import com.squareup.picasso.Picasso;
import com.OMarket.product.Adapters.RecommendationAdapter;
import com.OMarket.product.Fragments.Recommendation;
import com.OMarket.product.R;
import com.OMarket.product.model.Booking;
import com.OMarket.product.model.Product;
import com.OMarket.product.model.ProductResult;
import com.OMarket.product.model.UserProduct;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class HotelInfo extends AppCompatActivity {

    private Toolbar toolbar;
    private ImageView productImage;
    private TextView productDesc, views, drafts, completed;
    private Button book, draftBook;
    private RecommendationAdapter.ProductViewHolder productViewHolder;
    Product product;
    int pos;
   ProductResult productResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_info);

        toolbar = findViewById(R.id.toolbarInfo);
        productImage = findViewById(R.id.productImage);
        productDesc = findViewById(R.id.productDesc);
        book = findViewById(R.id.confirmBooking);
        draftBook = findViewById(R.id.draftBooking);
        views = findViewById(R.id.views);
        drafts = findViewById(R.id.draftText);
        completed = findViewById(R.id.completedText);


    }

    @Override
    protected void onResume() {
        super.onResume();

        product = (Product) getIntent().getExtras().getSerializable("data");


       productResult = new Gson().fromJson(getProduct(), ProductResult.class);
        pos = getIntent().getExtras().getInt("pos");

        toolbar.setTitle(product.getName());

        setSupportActionBar(toolbar);
        Picasso
                .with(ProductInfo.this)
                .load(Uri.parse(hotel.getImageUrl()))
                .into(productImage);
       productDesc.setText(hotel.getDescription());

        views.setText(productResult.getProducts().get(pos).getVisits() + " views");
        drafts.setText(productResult.getProducts().get(pos).getDraftBookings() + " pendings");
        completed.setText(productResult.getProducts().get(pos).getCompletedBookings() + " booked");


        book.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBooking(true);
                finish();

            }
        });
        draftBook.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                setBooking(false);
                MainActivity.updatec(1);
                Log.d("ononon", "onClick: ");
                finish();

            }
        });
    }

    public String getProducts() {
        SharedPreferences sp = getSharedPreferences("product", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        if (sp.contains("data")) {
            return sp.getString("data", null);
        } else {
            return null;
        }

    }

    private void setBooking(Boolean complete) {


        UserProduct product1 = new UserProduct();
         product1.setName(hotel.getName());
         product1.setCompleted(complete);
         product1.setTags(hotel.getTags());

        Booking booking = new Booking();

        List<UserProduct> userProducts = MainActivity.bookings.getUserProducts();
        userProducts.add( product1);

        MainActivity.bookings.setUserProducts(userProducts);

        Set<String> s = new HashSet<>();
        for (UserProduct userProduct: MainActivity.bookings.getUserProducts()) {
            if (userProduct.getCompleted()) {
                for (String ss : userProduct.getTags().split("\n")) {
                    ss = ss.replace("null", "");
                    s.add(ss);
                }

            }
        }
        String sa = "";

        for (String sss : s) {
            sa += sss;
            Recommendation.tagSet.add(sss);
        }

        if (complete) {
            int c = Integer.valueOf(productResult.getProducts().get(pos).getCompletedBookings());
           productResult.getProducts().get(pos).setCompletedBookings(String.valueOf(c + 1));
            storeUpdates(productResult);
        } else {
            int c = Integer.valueOf(productResult.getProduts().get(pos).getDraftBookings());
            productResult.getProducts().get(pos).setDraftBookings(String.valueOf(c + 1));
            storeUpdates(productResult);
        }

    }

    public void storeUpdates(ProductResult productResult) {
        SharedPreferences.Editor spe = getSharedPreferences("Product", Context.MODE_PRIVATE).edit();
        String save = new Gson().toJson(productResult);
        spe.putString("data", save);
        spe.apply();


    }
}
