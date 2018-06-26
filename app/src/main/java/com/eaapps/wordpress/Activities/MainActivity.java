package com.eaapps.wordpress.Activities;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.eaapps.wordpress.Interface.InterfaceRetro;
import com.eaapps.wordpress.Product.Category;
import com.eaapps.wordpress.R;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import se.akerfeldt.okhttp.signpost.OkHttpOAuthConsumer;
import se.akerfeldt.okhttp.signpost.SigningInterceptor;

public class MainActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.Adapter adapter;
    List<Category> categories = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recycle);
        recyclerView.setHasFixedSize(false);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        adapter = new RecyclerView.Adapter() {
            @Override
            public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                return new RecyclerView.ViewHolder(LayoutInflater.from(MainActivity.this).inflate(R.layout.custom_category, parent, false)) {
                    @Override
                    public String toString() {
                        return super.toString();
                    }
                };
            }

            @Override
            public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
                View v = holder.itemView;
                Category category = categories.get(position);
                TextView textCategory = v.findViewById(R.id.textCategory);
                textCategory.setText(category.getName());
            }

            @Override
            public int getItemCount() {
                return categories.size();
            }
        };

        recyclerView.setAdapter(adapter);


        String url = "http://fictionapps.com/demo/souq/wp-json/wc/v2/";

        OkHttpOAuthConsumer consumer = new OkHttpOAuthConsumer("ck_baeba18c4bf961af446ad4aad1aaf50b46b16e95",
                "cs_10467bc00edd399bcd24e21f7f3c338c02975dd0");

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new SigningInterceptor(consumer))
                .build();


        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();

        InterfaceRetro service = retrofit.create(InterfaceRetro.class);

        service.getCategories().enqueue(new Callback<List<Category>>() {
            @Override
            public void onResponse(@NonNull Call<List<Category>> call, @NonNull Response<List<Category>> response) {

                if (response.body() != null) {
                    categories.addAll(response.body());
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(@NonNull Call<List<Category>> call, @NonNull Throwable t) {
                Log.v("Error", t.getMessage() + "");

            }
        });

    }
}
