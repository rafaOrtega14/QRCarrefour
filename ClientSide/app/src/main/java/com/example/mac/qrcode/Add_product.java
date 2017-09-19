package com.example.mac.qrcode;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.mac.qrcode.Database.CreateTable;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 15/10/16.
 */

public class Add_product extends AppCompatActivity{
    private ImageView img;
    private EditText txt;
    private Button btn;
    private TextView p_u,calory,total;
    private String imagen;
    private double price;
    private double cal;
    private int unidades,rese;
    private String product;
    private int basketid;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_product);
        img=(ImageView)findViewById(R.id.imageView5);
        txt=(EditText)findViewById(R.id.editText3);
        btn=(Button)findViewById(R.id.NewProduct);
        p_u=(TextView)findViewById(R.id.textView6);
        calory=(TextView)findViewById(R.id.textView5);
        total=(TextView)findViewById(R.id.precio);
        imagen=getIntent().getExtras().getString("image");
        price=getIntent().getExtras().getDouble("price");
        product=getIntent().getExtras().getString("content");
        basketid=getIntent().getExtras().getInt("basketid");
        cal=getIntent().getExtras().getDouble("calories");
        p_u.setText(String.valueOf(price));
        calory.setText(String.valueOf(cal));
        setImage(imagen);
        txt.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String unit=s.toString();
                if(unit.isEmpty()){
                    unit="0";
                }else{
                    unidades=Integer.valueOf(unit);
                }
                String unite=String.format("%.2f", unidades*price);// String.valueOf();
                total.setText(unite+"€");
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AddElement(product,unidades);
            }
        });
    }
    private void setImage(String Iurl){
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL url = new URL("http://52.166.205.90:9123/images/"+Iurl);
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            img.setImageBitmap(bmp);
        } catch (Exception e) {
            Log.w(Log.getStackTraceString(e),"defsa");
        }
    }
    private void AddElement(final String product, int quantity){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        params.put("basket_id",basketid);
        params.put("product_id",product);
        params.put("quantity",quantity);
        client.post(Add_product.this,"http://52.166.205.90:9123/basket/storeProduct",params,new AsyncHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                StoreOnDB(imagen,unidades*price,product,cal);
                Intent i=new Intent(Add_product.this,ListItems.class);
                i.putExtra("basketid",basketid);
                startActivity(i);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {

            }
        });
    }
    private void StoreOnDB(String img,double price,String id,double calories){
        CreateTable tab = new CreateTable(this, "DBItems", null, 1);
        SQLiteDatabase db = tab.getWritableDatabase();
        if(db != null) {
            db.execSQL("INSERT INTO Items (Item_Id,product,calory,price) " +
                    "VALUES (" + id + ", '" + img +"','"+ price+"','"+calories+"')");
            db.close();
        }
    }


}
