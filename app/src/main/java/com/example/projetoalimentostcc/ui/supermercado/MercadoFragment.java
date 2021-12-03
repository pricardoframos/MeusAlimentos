package com.example.projetoalimentostcc.ui.supermercado;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.projetoalimentostcc.R;
import com.google.android.material.navigation.NavigationView;

public class MercadoFragment extends Fragment {

    private View view;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mercado, container, false);

        NavigationView menu = getActivity().findViewById(R.id.nav_view);
        menu.getMenu().getItem(2).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mercado");

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}