package com.example.projetoalimentostcc.ui.casa;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoalimentostcc.MainActivity;
import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.addProdutos.AddProdutosFragmentDespensa;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.Despensa;
import com.example.projetoalimentostcc.ui.classes.Produto;
import com.example.projetoalimentostcc.ui.classes.ProdutoXDespensa;
import com.example.projetoalimentostcc.ui.inicio.InicioFragment;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class CasaFragment extends Fragment implements AdapterCasa.IProdutoXDespensaRecycler {
    //Declaração dos elementos do fragment
    private View view;
    private ImageButton imgCasaAdd, imgButtonLerDispensa;
    private RecyclerView recyclerViewCasa;
    private EditText editTextNomeProd;

    //Declaração de variáveis
    private List<ProdutoXDespensa> listaProdutoXDespensa = new ArrayList<>();


    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_casa, container, false);

        AlterFragment.fragmentAtual = "Casa";

        NavigationView menu = getActivity().findViewById(R.id.nav_view);
        menu.getMenu().getItem(1).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Casa");

        //Referência dos elementos do fragment
        imgCasaAdd = view.findViewById(R.id.imgButtonCasaAdd);
        imgButtonLerDispensa = view.findViewById(R.id.imgButtonLerDispensa);
        editTextNomeProd = view.findViewById(R.id.editTextNomeProd);

        //Abrir AddProdutoFragment
        imgCasaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                AlterFragment.fragmentoOrigemDestino = "CasaFragment-AddProduto";
                //Apresentar o fragment AddProdutoFragmentDespensa
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,
                                new AddProdutosFragmentDespensa()).addToBackStack("Adicionar Produto").commit();
            }
        });

        //Abrir AddProdutoFragment
        imgButtonLerDispensa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
                String exibir = String.valueOf(bancoDeDados.buscaProdutoXDespensa().get(0).getIdRegistro());
                Toast.makeText(getContext(), exibir, Toast.LENGTH_SHORT).show();
            }
        });

        recyclerViewCasa = view.findViewById(R.id.recyclerViewCasa);
        listaProdutoXDespensa.clear();

        //DataSet
        Log.d("Success", "CasaFragment.onCreateView >>> Método criarProduto acionado");
        this.criarProdutoXDespensa();

        //Adapter
        AdapterCasa adapter = new AdapterCasa(listaProdutoXDespensa, this);

        //Layout Manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewCasa.setLayoutManager(layoutManager);
        recyclerViewCasa.setHasFixedSize(true);
        recyclerViewCasa.setAdapter(adapter);

        editTextNomeProd.addTextChangedListener(new TextWatcher() {
            BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                if(editTextNomeProd.getText().toString().length() >= 1){
                    Log.d("Success","CasaFragment.editTextNomeProd.addTextChangedListener." +
                            "onTextChanged >>> editTextNomeProd Alterado");
                    listaProdutoXDespensa.addAll(bancoDeDados.buscaLikeDescricaoProdutos(editTextNomeProd.getText().toString()));
                    Log.d("Success","CasaFragment.editTextNomeProd.addTextChangedListener." +
                            "onTextChanged >>> Buscou produto pela descrição");
                    adapter.atualizar();
                    Log.d("Success","CasaFragment.editTextNomeProd.addTextChangedListener." +
                            "onTextChanged >>> Atualizou o recyclerView");
                }else{
                    listaProdutoXDespensa.clear();
                    Log.d("Success","CasaFragment.editTextNomeProd.addTextChangedListener." +
                            "onTextChanged >>> Lista resetada");
                    listaProdutoXDespensa = bancoDeDados.buscaProdutoXDespensa();
                    Log.d("Success","CasaFragment.editTextNomeProd.addTextChangedListener." +
                            "onTextChanged >>> editTextNomeProd Alterado");
                    adapter.atualizar();
                    Log.d("Success","CasaFragment.editTextNomeProd.addTextChangedListener." +
                            "onTextChanged >>> Atualizou o recyclerView");
                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        return view;
    }

    private void criarProdutoXDespensa() {
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        List<ProdutoXDespensa>listaProdutosXDespensa = bancoDeDados.buscaProdutoXDespensa();
        if(listaProdutosXDespensa.size()>0){
            int ciclo = 0;
            do {
                ProdutoXDespensa produtoXDespensa = new ProdutoXDespensa(bancoDeDados.buscaProdutoXDespensa().get(ciclo).getIdRegistro(),
                        bancoDeDados.buscaProdutoXDespensa().get(ciclo).getIdProduto(),
                        bancoDeDados.buscaProdutoXDespensa().get(ciclo).getEstoqueProduto(),
                        bancoDeDados.buscaProdutoXDespensa().get(ciclo).getValidProduto(),
                        bancoDeDados.buscaProdutoXDespensa().get(ciclo).getIdDespensa());
                listaProdutoXDespensa.add(produtoXDespensa);
                ciclo++;
            }while(ciclo<listaProdutosXDespensa.size());
            Log.d("Success", "CasaFragment.criarProdutoXDespensa >>> Lista de produtosXdespensa encontrada");
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    @Override
    public void irparaDetalhesProduto(int indice) {
        AlterFragment.fragmentoOrigemDestino = "RecycleViewCasa-AddProdutosFragmentDespensa";
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_main,
                        new AddProdutosFragmentDespensa()).addToBackStack("Adicionar Produto").commit();
    }

}