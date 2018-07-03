package com.example.samsung.tablayout;

import android.content.Intent;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.samsung.tablayout.CalidadAire.FragmentCalidadAire;
import com.example.samsung.tablayout.Territorios.FragmentTerritorios;
import com.example.samsung.tablayout.Utils.SectionPagerAdapter;

public class MainActivity extends AppCompatActivity {

    /*
    Esta app se realiza pra practricar los FragmentPagerAdapter, agregando tabs y viewPager

    -  Para comenzar se debe importar una libreria de material design al build.gradle
    -crear los layout para los tabs y los viewpager
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setupViewPager();
    }
    private void setupViewPager(){

        //Se agregar los Fragments al adapter
        SectionPagerAdapter adapter = new SectionPagerAdapter(getSupportFragmentManager());

        adapter.addFragment(new FragmentCalidadAire());
        adapter.addFragment(new FragmentTerritorios());

        //Se agregar los fragments por medio del adapter al ViewPager

        ViewPager viewPager = (ViewPager)findViewById(R.id.container);
        viewPager.setAdapter(adapter);

        //Se asocia el ViewPager al TabLayout


        TabLayout tabLayout = (TabLayout)findViewById(R.id.tabs);
        //lo siguiente sirve para hacer que los tabs ocupen
        // todo el ancho de la pantalla del dispositivo
        tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
        tabLayout.setTabMode(TabLayout.MODE_FIXED);
        tabLayout.setupWithViewPager(viewPager);

        tabLayout.getTabAt(0).setIcon(R.drawable.ic_aire);
        tabLayout.getTabAt(1).setIcon(R.drawable.ic_action_name);



    }
    @Override
    public void onBackPressed() {
        finish();
    }


}
