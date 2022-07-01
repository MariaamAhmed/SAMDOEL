package com.OMarket.products.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class ProductResult implements Serializable {

    @SerializedName("products")
    @Expose
    private List<Product> products = null;

    public List<products> getProducts() {
        return products;
    }

    public void setProducts(List<Product> products) {
        this.products = products;
    }

}