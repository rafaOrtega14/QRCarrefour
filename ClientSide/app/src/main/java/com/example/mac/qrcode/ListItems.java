package com.example.mac.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.mac.qrcode.Database.CreateTable;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 15/10/16.
 */

public class ListItems extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private ArrayList<ItemsBean> items;
    private Button btn,pay;
    private int basketid;
    private SharedPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.listcontainer);
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        mRecyclerView = (RecyclerView) findViewById(R.id.my_recycler_view);
        btn=(Button)findViewById(R.id.NewProduct);
        pay=(Button)findViewById(R.id.button4);
        mRecyclerView.setHasFixedSize(true);
        InitItemList();
        basketid=getIntent().getExtras().getInt("basketid");
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        pay.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            AsyncHttpClient client = new AsyncHttpClient();
            RequestParams params = new RequestParams();
            params.put("basket_id",getIntent().getExtras().getInt("basketid"));
            params.put("phone",prefs.getString("phone","00000"));
            params.put("creditcard",prefs.getString("CreditCard","000000"));
            client.post(getApplicationContext(), "http://52.166.205.90:9123/basket/payment", params, new AsyncHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                    try {
                        String res = new String(responseBody, "UTF-8");
                        Intent i=new Intent(ListItems.this,QrResultado.class);
                        DelDB();
                        i.putExtra("content",res);
                        startActivity(i);
                    } catch (UnsupportedEncodingException e) {
                        e.printStackTrace();
                    }
                }

                @Override
                public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

                }
            });
        }
      });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(ListItems.this,SimpleScannerActivity.class);
                i.putExtra("basketid",basketid);
                startActivity(i);
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        DelDB();
    }



    private void InitItemList(){
        CreateTable tab = new CreateTable(this, "DBItems", null, 1);
        SQLiteDatabase db = tab.getWritableDatabase();
        String query="SELECT * FROM Items";
        Cursor c =db.rawQuery(query, null);;
        items = new ArrayList<ItemsBean>();
        if (c.moveToFirst()) {
            do {
                String url= "http://52.166.205.90:9123/images/"+c.getString(1);
                items.add(new ItemsBean(c.getDouble(3),c.getDouble(2),url,c.getString(0)));
                mAdapter = new ListAdapter(items);
                mRecyclerView.setAdapter(mAdapter);
            } while(c.moveToNext());
        }
    }
    private void DelDB(){
        CreateTable tab = new CreateTable(this, "DBItems", null, 1);
        SQLiteDatabase db = tab.getWritableDatabase();
        db.execSQL("DELETE FROM Items");
    }
}
