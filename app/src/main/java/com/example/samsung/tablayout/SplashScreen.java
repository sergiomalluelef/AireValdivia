package com.example.samsung.tablayout;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Handler;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;

import java.io.IOException;

public class SplashScreen extends AppCompatActivity {
    private LottieAnimationView animationView, progresCarga, buttoniniciar;
    private RelativeLayout layout1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);

        layout1 = (RelativeLayout)findViewById(R.id.layout1);

        animationView = (LottieAnimationView) findViewById(R.id.animation_view);
        animationView.setAnimation("floating_cloud.json");
        animationView.loop(true);
        animationView.playAnimation();

        progresCarga = (LottieAnimationView) findViewById(R.id.progressBar);
        progresCarga.setAnimation("cargar.json");
        progresCarga.loop(true);
        progresCarga.playAnimation();

        buttoniniciar = (LottieAnimationView) findViewById(R.id.buttoniniciar);
        buttoniniciar.setAnimation("play.json");
        buttoniniciar.loop(true);
        buttoniniciar.playAnimation();




        if (conectado(getApplicationContext()))
        {
            try {
                if (descargaDatos())
                {
                    progresCarga.setVisibility(View.INVISIBLE);
                    buttoniniciar.setVisibility(View.VISIBLE);


                    buttoniniciar.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Intent intent = new Intent(SplashScreen.this, MainActivity.class);
                            startActivity(intent);
                        }
                    });


                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else{
            Snackbar.make(layout1, "Conecte su dispositivo a internet", Snackbar.LENGTH_LONG).show();

            new Handler().postDelayed(new Runnable(){
                public void run(){
                    finish();
                }
            }, 3000);
        }
    }

    public static boolean conectado(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        return networkInfo != null && networkInfo.isAvailable() && networkInfo.isConnected();
    }
    public boolean descargaDatos() throws InterruptedException, IOException
    {
        String command = "ping -c 1 8.8.8.8";
        return (Runtime.getRuntime().exec (command).waitFor() == 0);
    }
}
