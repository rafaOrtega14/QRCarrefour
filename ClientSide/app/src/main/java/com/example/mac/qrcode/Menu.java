package com.example.mac.qrcode;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 15/10/16.
 */

public class Menu extends AppCompatActivity{
    private ImageView img;
    private int basketid;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.menu);
        img=(ImageView)findViewById(R.id.imageButton);
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getBasketId();
            }
        });
    }
    private void getBasketId(){
        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Menu.this, "http://52.166.205.90:9123/basket/basket_id", new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String res=new String(responseBody, "UTF-8");
                    basketid =Integer.valueOf(res);
                    Intent i=new Intent(Menu.this,SimpleScannerActivity.class);
                    i.putExtra("basketid",basketid);
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
}
