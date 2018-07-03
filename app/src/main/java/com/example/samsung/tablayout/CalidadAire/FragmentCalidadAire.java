package com.example.samsung.tablayout.CalidadAire;

import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;
import android.widget.TextView;

import com.airbnb.lottie.LottieAnimationView;
import com.example.samsung.tablayout.R;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;

/**
 * Created by Samsung on 21/06/2018.
 */

public class FragmentCalidadAire extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "FragmentCalidadAire";

    private SwipeRefreshLayout swipeLayout;
    private TextView textView;
    private boolean validar_refresco = true;
    private Elements entradas, entradas2;
    private LottieAnimationView animNoHayDatos;
    private Document htmlDocument;
    private String htmlPageUrl = "http://airechile.mma.gob.cl/comunas/valdivia";
    DatosPaginaWeb datosPaginaWeb = new DatosPaginaWeb();

    private TextView txtfecha, txtaire,txtrecomendaciones,txtzona, txtTop;
    private ScrollView scroll;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_uno, container, false);

        txtfecha = (TextView)view.findViewById(R.id.fecha);
        txtaire = (TextView)view.findViewById(R.id.aire);
        txtrecomendaciones = (TextView)view.findViewById(R.id.recomendaciones);
        txtzona = (TextView)view.findViewById(R.id.zona);
        scroll = (ScrollView)view.findViewById(R.id.scroll);

        animNoHayDatos = (LottieAnimationView)view.findViewById(R.id.animation_no_hay_datos);
        animNoHayDatos.setAnimation("crying.json");
        animNoHayDatos.loop(true);
        animNoHayDatos.playAnimation();



        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.anim_carga);
        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);



        //Se utiliza al iniciar, pero si ya existen datos se muestran sin cargar
        if(validar_refresco == true){
            validar_refresco = false;
            onRefresh();
        }else{
            scroll.setVisibility(View.VISIBLE);
        }



        return  view;
    }

    @Override
    public void onRefresh() {

        new DatosTask().execute();

    }

    @Override
    public void onResume() {

        txtaire.setText(datosPaginaWeb.getAire());
        txtrecomendaciones.setText(datosPaginaWeb.getRecomendaciones());
        txtzona.setText(datosPaginaWeb.getZona());
        txtfecha.setText(datosPaginaWeb.getFecha());

        super.onResume();
    }

    public class DatosTask extends AsyncTask<Void, Void, Void>{

        @Override
        protected void onPreExecute() {
            Log.d(TAG, "onPreExecute: inicio...");
            animNoHayDatos.setVisibility(View.INVISIBLE);
            scroll.setVisibility(View.INVISIBLE);
            swipeLayout.setRefreshing(true);
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            Log.d(TAG, "doInBackground: inicio...");
            try {
                htmlDocument = Jsoup.connect(htmlPageUrl).get();
                entradas = htmlDocument.select("div.panel-medidas");
                entradas2 = htmlDocument.select("div.tab-content");

            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            Log.d(TAG, "onPostExecute: inicio...");

            if(entradas != null && entradas2 != null){
                Log.d(TAG, "onPostExecute: entradas no es vacio");
                for (Element elem : entradas) {

                    Log.d(TAG, "onPostExecute: element");

                    datosPaginaWeb.setAire(elem.getElementsByClass("panel-title").text());
                    datosPaginaWeb.setRecomendaciones(elem.getElementsByClass("item-inner").text());
                    datosPaginaWeb.setZona(elem.getElementsByClass("info").text());

                    Log.d("onPostExecute: ","Aire: "+datosPaginaWeb.getAire());
                    Log.d("onPostExecute: ","Recomendaciones: "+datosPaginaWeb.getRecomendaciones());
                    Log.d("onPostExecute: ","Zona: "+datosPaginaWeb.getZona());

                    txtaire.setText(datosPaginaWeb.getAire());
                    txtrecomendaciones.setText(datosPaginaWeb.getRecomendaciones());
                    txtzona.setText(datosPaginaWeb.getZona());

                }

                for (Element element : entradas2){
                    datosPaginaWeb.setFecha(element.getElementsByClass("date").text());

                    Log.d("onPostExecute: ","Fecha: "+datosPaginaWeb.getFecha());
                    txtfecha.setText(datosPaginaWeb.getFecha());

                }
                scroll.setVisibility(View.VISIBLE);
                swipeLayout.setRefreshing(false);
            }else{
                swipeLayout.setRefreshing(false);
                animNoHayDatos.setVisibility(View.VISIBLE);
            }




        }
    }


}
