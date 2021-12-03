package com.example.projetoalimentostcc.ui.inicio;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.casa.CasaFragment;
import com.example.projetoalimentostcc.ui.supermercado.MercadoFragment;
import com.google.android.material.navigation.NavigationView;

public class InicioFragment extends Fragment {
    //Declaração dos elementos do fragment
    public View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_inicio, container, false);

        ImageButton imgCasabtn = view.findViewById(R.id.imgCasabtn);
        ImageButton imgMercadobtn = view.findViewById(R.id.imgMercadobtn);

        NavigationView menu = getActivity().findViewById(R.id.nav_view);
        menu.getMenu().getItem(0).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Início");

        //Abrir CasaFragment
        imgCasabtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,
                                new CasaFragment()).addToBackStack("Casa").commit();
            }
        });

        //Abrir MercadoFragment
        imgMercadobtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v2) {
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,
                                new MercadoFragment()).addToBackStack("Mercado").commit();
            }
        });

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}