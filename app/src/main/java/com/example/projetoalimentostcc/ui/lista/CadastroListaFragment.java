package com.example.projetoalimentostcc.ui.lista;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.addProdutos.AddProdutosFragmentLista;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.Despensa;
import com.example.projetoalimentostcc.ui.classes.Lista;
import com.example.projetoalimentostcc.ui.despensa.AdapterCadastroDespensa;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class CadastroListaFragment extends Fragment implements AdapterCadastroLista.IListaRecycler {
    //Declaração dos elementos do fragment
    private View view;
    private EditText editTextLista;
    private Button btnCadastrarList, btnDeletarList;
    private RecyclerView recyclerViewLista;

    //Declaração de variáveis
    private List<Lista> listaLista = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cadastro_lista, container, false);

        //Acessar o menu lateral
        NavigationView menu = getActivity().findViewById(R.id.nav_view);
        menu.getMenu().getItem(3).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cadastro de Listas");

        //Referência dos elementos do fragment
        editTextLista = view.findViewById(R.id.editTextLista);
        btnCadastrarList = view.findViewById(R.id.btnCadastrarList);
        recyclerViewLista = view.findViewById(R.id.recyclerViewLista);

        btnCadastrarList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastraLista();
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,
                                new CadastroListaFragment()).addToBackStack("Cadastro Lista").commit();
            }
        });

        recyclerViewLista = view.findViewById(R.id.recyclerViewLista);

        //DataSet
        this.criarLista();

        //Adapter
        AdapterCadastroLista adapter = new AdapterCadastroLista(listaLista, this);

        //Layout Manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewLista.setLayoutManager(layoutManager);
        recyclerViewLista.setHasFixedSize(true);
        recyclerViewLista.setAdapter(adapter);

        return view;
    }

    //Cadastrar LISTA
    private void cadastraLista(){
        //Instanciação da classe BancoDeDados
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);

        try {
            //Validar se a lista ainda não está cadastrada no banco
            //Verificar, via descrição, se a lista existe no banco de dados
            List<Lista> listaLista = bancoDeDados.buscaDescricaoLista(editTextLista.getText().toString().trim());
            if(listaLista.size()<1){
                Log.d("Success","CadastroListaFragment.cadastrarLista >>> " +
                        "Lista ainda não cadastrada");
                if(!editTextLista.getText().toString().isEmpty()){
                    bancoDeDados.cadastrarLista(editTextLista.getText().toString());
                    Log.d("Success", "Descrição: "+editTextLista.getText().toString());
                    editTextLista.setText("");
                    Toast.makeText(getContext(), "Lista cadastrada com sucesso!",
                            Toast.LENGTH_SHORT).show();

                }else{
                    Log.d("Success","CadastroListaFragment.cadastrarLista >>> " +
                            "A descrição da lista não foi inserida");
                }
            }else{//Caso a lista já esteja cadastrada no banco
                Log.d("Success","CadastroListaFragment.cadastrarLista >>> " +
                        "Lista " + listaLista.get(0).getDescricao() + " já cadastrado");
                Toast.makeText(getContext(), "Lista já cadastrada! Por favor insira uma nova lista",
                        Toast.LENGTH_SHORT).show();
                editTextLista.setText("");
            }

        }catch (Exception e){
            Log.d("Success", "CadastroListaFragment.cadastrarLista >>> " +
                    "CadastroListaFragment.cadastrarLista.Execpetion 1 ->" + e.toString());
        }
    }

    private void criarLista() {
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        listaLista = bancoDeDados.buscaLista();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    @Override
    public void irparaDetalhesProduto(int indice) {
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        String lista = bancoDeDados.buscaIdLista(indice).get(0).getDescricao();
        Log.d("Success","CadastroListaFragment. indice = " +indice+" desc = "+lista);
        AlterFragment.listaAtual = lista;
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_main,
                        new ListaFragment()).addToBackStack("Lista").commit();
    }
}