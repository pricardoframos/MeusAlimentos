package com.example.projetoalimentostcc.ui.configuracao;


import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.projetoalimentostcc.R;

public class ConfiguracaoFragment extends Fragment {

    public View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_configuracao, container, false);

        return view;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}