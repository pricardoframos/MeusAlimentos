package com.example.projetoalimentostcc;

import android.app.Activity;
import android.app.FragmentManager;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Menu;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.example.projetoalimentostcc.ui.addProdutos.AddProdutosFragmentDespensa;
import com.example.projetoalimentostcc.ui.casa.CasaFragment;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.Despensa;
import com.example.projetoalimentostcc.ui.inicio.InicioFragment;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.annotation.RequiresApi;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.example.projetoalimentostcc.databinding.ActivityMainBinding;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity{

    private AppBarConfiguration mAppBarConfiguration;
    private ActivityMainBinding binding;
    private String fragmentAtual = AlterFragment.fragmentAtual;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Tela apenas na vertical
        this.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);


        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.appBarMain.toolbar);


        //Verificar se já existem despensas cadastradas
        BancoDeDados bancoDeDados = new BancoDeDados(this,1);
        List<Despensa> listaDespensa = bancoDeDados.buscaDespensa();
        //Caso não existam despensas cadastradas, cadastrar Armário e Geladeira como padrão
        if(listaDespensa.size()<1){
            Log.d("Success","MainActivity >>> Despensas ainda não cadastradas");
            bancoDeDados.cadastrarDespensa("Armário");
            Log.d("Success","MainActivity >>> Despensa Armário cadastrada");
            bancoDeDados.cadastrarDespensa("Geladeira");
            Log.d("Success","MainActivity >>> Despensa Geladeira cadastrada");
            Log.d("Success","MainActivity >>> Despensas cadastradas");
        }else{
            Log.d("Success","MainActivity >>> Despensas já cadastradas");
        }

        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_inicio, R.id.nav_casa, R.id.nav_mercado, R.id.nav_lista, R.id.nav_despensa, R.id.nav_sobre, R.id.nav_configuracao)
                .setOpenableLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        
    }


}