package com.example.projetoalimentostcc.ui.sobre;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.text.Html;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.projetoalimentostcc.R;

public class SobreFragment extends Fragment {

    public View view;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_sobre, container, false);

        //Código para abrir o email
TextView email = (TextView) view.findViewById(R.id.textViewEmailSobre);
email.setText(Html.fromHtml
("<a href=\"mailto:projetoalimentostcc2021@gmail.com\">projetoalimentostcc2021@gmail.com</a>"));
email.setMovementMethod(LinkMovementMethod.getInstance());

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}