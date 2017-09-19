package com.example.mac.qrcode;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.app.AlertDialog;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URL;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by mac on 15/10/16.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.LViewHolder>{
    ArrayList<ItemsBean> Food;

    ListAdapter(ArrayList<ItemsBean> persons){
        this.Food = persons;
    }
    @Override
    public LViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.list_items, viewGroup, false);
        LViewHolder pvh = new LViewHolder(v);
        return pvh;
    }
    @Override
    public void onBindViewHolder(final LViewHolder personViewHolder, final int i) {
        String unite=String.format("%.2f",Food.get(i).getprecio());
        personViewHolder.precio.setText(unite+"€");
        personViewHolder.calorias.setText(String.valueOf(Food.get(i).getCalorias()));
        try {
            if (android.os.Build.VERSION.SDK_INT > 9) {
                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
                StrictMode.setThreadPolicy(policy);
            }
            URL url = new URL(Food.get(i).getFoto());
            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
            personViewHolder.img.setImageBitmap(bmp);
        } catch (Exception e) {
           Log.w(Log.getStackTraceString(e),"defsa");
        }
    }


    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }
    @Override
    public int getItemCount() {
        return Food.size();
    }
    public static class LViewHolder extends RecyclerView.ViewHolder {
        CardView cv;
        TextView precio;
        TextView calorias;
        ImageView img;

        LViewHolder(View itemView) {
            super(itemView);
            cv = (CardView)itemView.findViewById(R.id.card_view);
            precio = (TextView)itemView.findViewById(R.id.precioC);
            calorias = (TextView)itemView.findViewById(R.id.caloriasC);
            img=(ImageView)itemView.findViewById(R.id.foto);
        }
    }

}

