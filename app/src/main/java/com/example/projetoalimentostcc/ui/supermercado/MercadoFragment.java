package com.example.projetoalimentostcc.ui.supermercado;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.Lista;
import com.example.projetoalimentostcc.ui.lista.ListaFragment;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.google.android.material.navigation.NavigationView;

import java.util.List;

public class MercadoFragment extends Fragment {
    //Declaração dos elementos do fragment
    private View view;
    private EditText editTextMercado,editTextBairro,editTextLista;
    private Button btnOk;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_mercado, container, false);

        NavigationView menu = getActivity().findViewById(R.id.nav_view);
        menu.getMenu().getItem(2).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Mercado");

        //Referência dos elementos do fragment
        editTextMercado = view.findViewById(R.id.editTextMercado);
        editTextBairro = view.findViewById(R.id.editTextBairro);
        editTextLista = view.findViewById(R.id.editTextLista);
        btnOk = view.findViewById(R.id.btnOk);

        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(editTextBairro.getText().toString().isEmpty()||
                        editTextMercado.getText().toString().isEmpty()||
                        editTextLista.getText().toString().isEmpty()){
                    Toast.makeText(getContext(), "Preencha os campos corretamente!",
                            Toast.LENGTH_SHORT).show();
                }else{
                    if(cadastrarLista()==true){
                        cadastrarSupermercado();
                        AlterFragment.listaAtual = editTextLista.getText().toString();
                        //Apresentar o fragment AddProdutoFragmentLista
                        getParentFragmentManager().beginTransaction()
                                .replace(R.id.nav_host_fragment_content_main,
                                        new ListaFragment()).addToBackStack("Lista").commit();
                    }
                }
            }
        });

        return view;
    }

    //Método para cadastrar o supermercado
    private void cadastrarSupermercado(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        try {
            bancoDeDados.cadastrarSupermercado(editTextMercado.getText().toString(),
                    editTextBairro.getText().toString());
        }catch(Exception e){
            Log.d("Success","MercadoFragment.cadastrarSupermercado >>> Exception: " + e);
        }

    }

    //Método para cadastrar a lista
    private boolean cadastrarLista(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        //Verificar, via descrição, se a lista existe no banco de dados
        List<Lista> lista = bancoDeDados.buscaDescricaoLista(editTextLista.getText().toString().trim());
        if(lista.size()<1){
            try{
                bancoDeDados.cadastrarLista(editTextLista.getText().toString());
                return true;
            }catch (Exception e){
                Log.d("Success","MercadoFragment.cadastrarLista >>> Exception: " + e);
                Toast.makeText(getContext(), "Não foi possível cadastrar a lista! Tente novamente!",
                        Toast.LENGTH_SHORT).show();
                return false;
            }
        }else{
            Toast.makeText(getContext(), "A lista inserida já existe! Insira outra lista",
                    Toast.LENGTH_SHORT).show();
            return false;
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}