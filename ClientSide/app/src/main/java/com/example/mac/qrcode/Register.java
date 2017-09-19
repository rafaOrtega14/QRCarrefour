package com.example.mac.qrcode;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.Toolbar;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.fatsecret.platform.model.CompactFood;
import com.fatsecret.platform.services.FoodService;
import com.fatsecret.platform.services.Response;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;

/**
 * Created by mac on 14/10/16.
 */

public class Register extends Activity{

    private EditText phone;
    private ProgressDialogFragment progressFragment;
    private ImageView img;
    private SharedPreferences prefs;

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        setContentView(R.layout.first_screen);

            setContentView(R.layout.activity_main);

        setupToolbar();
        phone=(EditText)findViewById(R.id.editText);
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        ImageView img=(ImageView)findViewById(R.id.imageView3);
        img.setImageResource(R.drawable.icon);
    }

    public void setupToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        //setSupportActionBar(toolbar);

    }

    public void launchSimpleActivity(View v) {
        HttpProcess();

    }
    public void ShowDialog(View v){
        AlertDialog.Builder dialog=new AlertDialog.Builder(new ContextThemeWrapper(Register.this, R.style.myDialog));
        dialog.setTitle("Introduce el codigo");
        final EditText input = new EditText(Register.this);
        input.setTextColor(Color.parseColor("#ffffff"));
        input.setInputType(InputType.TYPE_CLASS_NUMBER);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        input.setLayoutParams(lp);
        dialog.setView(input);
        dialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                AsyncHttpClient client2 = new AsyncHttpClient();
                RequestParams param = new RequestParams();
                param.put("phone","+34"+phone.getText().toString());
                param.put("code",input.getText().toString());
                client2.post(getApplicationContext(),"http://52.166.205.90:9123/register/confirmation",param,new AsyncHttpResponseHandler(){

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                        try {
                            String res=new String(responseBody, "UTF-8");
                            if(res.equals("ok")){
                                Intent i=new Intent(Register.this,PaymentData.class);
                                startActivity(i);
                            }else{
                                Toast.makeText(getApplicationContext(),"Error, introduce el codigo de nuevo",Toast.LENGTH_SHORT).show();
                            }
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
        dialog.create();
        dialog.show();

    }
    public void HttpProcess(){
        AsyncHttpClient client = new AsyncHttpClient();
        RequestParams params = new RequestParams();
        String phoneR="+34"+phone.getText().toString();
        params.put("phone",phoneR);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString("phone",phoneR);
        editor.commit();
        client.post(getApplicationContext(), "http://52.166.205.90:9123/register", params, new AsyncHttpResponseHandler() {
            @Override
            //confirmation code phone
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode==200){
                    AlertDialog.Builder dialog=new AlertDialog.Builder(new ContextThemeWrapper(Register.this, R.style.myDialog));
                    dialog.setTitle("Introduce el codigo");
                    final EditText input = new EditText(Register.this);
                    input.setTextColor(Color.parseColor("#ffffff"));
                    input.setInputType(InputType.TYPE_CLASS_NUMBER);
                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                            LinearLayout.LayoutParams.MATCH_PARENT,
                            LinearLayout.LayoutParams.MATCH_PARENT);
                    input.setLayoutParams(lp);
                    dialog.setView(input);
                    dialog.setNegativeButton("cancelar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    });
                    dialog.setPositiveButton("Aceptar", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            AsyncHttpClient client2 = new AsyncHttpClient();
                            RequestParams param = new RequestParams();
                            param.put("phone","+34"+phone.getText().toString());
                            param.put("code",input.getText().toString());
                            client2.post(getApplicationContext(),"http://52.166.205.90:9123/register/confirmation",param,new AsyncHttpResponseHandler(){

                                @Override
                                public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                                    try {
                                        String res=new String(responseBody, "UTF-8");
                                        if(res.equals("ok")){
                                            Intent i=new Intent(Register.this,PaymentData.class);
                                            startActivity(i);
                                        }else{
                                            Toast.makeText(getApplicationContext(),"Error, introduce el codigo de nuevo",Toast.LENGTH_SHORT).show();
                                        }
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
                    dialog.create();
                    dialog.show();

                }else{
                    Toast.makeText(getApplicationContext(),"Un error ocurrio porfavor intentelo de nuevo",Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(),""+statusCode,Toast.LENGTH_SHORT).show();
            }
        });
    }

}
