package com.eaapps.wordpress.Interface;

import com.eaapps.wordpress.Product.Category;
import com.eaapps.wordpress.Product.Product;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface InterfaceRetro {

    @GET("products/categories")
    Call<List<Category>> getCategories();


}
