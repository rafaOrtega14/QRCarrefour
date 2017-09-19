package com.example.mac.qrcode;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.stripe.android.Stripe;
import com.stripe.android.TokenCallback;
import com.stripe.android.model.Card;
import com.stripe.exception.AuthenticationException;
import com.stripe.model.Token;

import java.io.UnsupportedEncodingException;

import cz.msebera.android.httpclient.Header;
import io.realm.Realm;
import io.realm.RealmResults;

import static java.security.AccessController.getContext;

/**
 * Created by mac on 14/10/16.
 */

public class PaymentData extends AppCompatActivity{
    private EditText year,month,credit,cvc;
    int yearI,monthI;
    private SharedPreferences prefs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.payment_form);
        prefs = getSharedPreferences("MisPreferencias", Context.MODE_PRIVATE);
        cvc=(EditText)findViewById(R.id.editText5);
        year=(EditText)findViewById(R.id.ano);
        month=(EditText)findViewById(R.id.mes);
        credit=(EditText)findViewById(R.id.editText2);

    }
    public void storeCard(View v){
        Card card = new Card(credit.getText().toString(),
                Integer.parseInt(year.getText().toString()),Integer.parseInt(month.getText().toString()),cvc.getText().toString());


        Stripe stripe = null;
        try {
            stripe = new Stripe("pk_test_cDfBeecydt9ZuFBMeGo2o1Md");
        } catch (AuthenticationException e) {
            e.printStackTrace();
        }
        stripe.createToken(
                card,
                new TokenCallback() {

                    public void onError(Exception error) {
                        // Show localized error message
                        Toast.makeText(getApplicationContext(),error.getLocalizedMessage().toString()+Integer.parseInt(year.getText().toString()),Toast.LENGTH_LONG).show();
                    }

                    @Override
                    public void onSuccess(com.stripe.android.model.Token token) {
                        clientRequest(token);
                    }
                }
        );
    }
    public void clientRequest(final com.stripe.android.model.Token token){

        AsyncHttpClient client = new AsyncHttpClient();
        final RequestParams params = new RequestParams();
        params.put("phone",prefs.getString("phone","0000000"));
        params.put("token",token.getId());
        client.post(getApplicationContext(), "http://52.166.205.90:9123/users/createFirstCustomer", params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                try {
                    String res = new String(responseBody, "UTF-8");
                    SharedPreferences.Editor editor = prefs.edit();
                    editor.putString("CreditCard",res);
                    editor.putString("CardType",token.getCard().getType());
                    editor.putString("Last4",token.getCard().getLast4());
                    editor.commit();
                    Intent i=new Intent(PaymentData.this,Menu.class);
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
