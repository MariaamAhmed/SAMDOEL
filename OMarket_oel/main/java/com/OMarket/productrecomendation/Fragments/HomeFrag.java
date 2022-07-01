package com.OMarket.product.Fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.OMarket.products.Adapters.ProductAdapter;
import com.OMarket.products.R;
import com.OMarket.product.model.productResult;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;


public class HomeFrag extends Fragment {
   
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private ProductAdapter productAdapter;
    ProductResult productResult;
    RecyclerView recyclerView;
    int lastPos=0;

    public HomeFrag() {
       
    }

   
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFrag.
     */
    
    public static HomeFrag newInstance(String param1, String param2) {
        HomeFrag fragment = new HomeFrag();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View view=inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView=view.findViewById(R.id.productlist);


        return view;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

    }

    @Override
    public void onPause() {
        super.onPause();
        lastPos = ((LinearLayoutManager)recyclerView.getLayoutManager()).findFirstCompletelyVisibleItemPosition();

    }

    public String getProducts(){
        SharedPreferences sp=getActivity().getSharedPreferences("Product",Context.MODE_PRIVATE);
        Gson gson=new Gson();
        if(sp.contains("data")){

            return sp.getString("data",null);
        }
        else{
            return null;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        ProductAdapter productAdapter=new ProductAdapter(getContext());
        Gson gson=new Gson();

        if(getProducts()==null) {


            BufferedReader br = null;
            try {
                br = new BufferedReader(new InputStreamReader(getContext().getAssets().open("products.json")));
            } catch (IOException e) {
                e.printStackTrace();
            }
            productResult =gson.fromJson(br, ProductResult.class);


        }
        else {
            productResult=gson.fromJson(getProducts(),ProductResult.class);
        }

        productAdapter.setProducts(productResult.getPrducts());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(hotelAdapter);

        recyclerView.getLayoutManager().scrollToPosition(lastPos);

    }

   
    public void onButtonPressed(String uri) {

    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    public void updateList() {
        if(productAdapter!=null){

            productAdapter.setProducts(productResult.getProducts());

        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
