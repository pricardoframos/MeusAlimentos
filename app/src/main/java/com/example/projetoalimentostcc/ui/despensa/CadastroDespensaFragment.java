package com.example.projetoalimentostcc.ui.despensa;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.casa.AdapterCasa;
import com.example.projetoalimentostcc.ui.classes.Despensa;
import com.example.projetoalimentostcc.ui.classes.Produto;
import com.example.projetoalimentostcc.ui.classes.ProdutoXDespensa;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;


public class CadastroDespensaFragment extends Fragment {
    //Declaração dos elementos do fragment
    private View view;
    private EditText editTextDespensa;
    private Button btnCadastrar, btnDeletar;
    private RecyclerView recyclerViewDespensa;

    //Declaração de variáveis
    private List<Despensa> listaDespensa = new ArrayList<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_cadastro_despensa, container, false);

        //Acessar o menu lateral
        NavigationView menu = getActivity().findViewById(R.id.nav_view);
        menu.getMenu().getItem(4).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Cadastro de Despensas");

        //Referência dos elementos do fragment
        editTextDespensa = view.findViewById(R.id.editTextDespensa);
        btnCadastrar = view.findViewById(R.id.btnCadastrar);
        recyclerViewDespensa = view.findViewById(R.id.recyclerViewDespensa);

        btnCadastrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cadastraDespensa();
            }
        });

        recyclerViewDespensa = view.findViewById(R.id.recyclerViewDespensa);

        //DataSet
        this.criarDespensa();

        //Adapter
        AdapterCadastroDespensa adapter = new AdapterCadastroDespensa(listaDespensa);

        //Layout Manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewDespensa.setLayoutManager(layoutManager);
        recyclerViewDespensa.setHasFixedSize(true);
        recyclerViewDespensa.setAdapter(adapter);

        return view;
    }

    //Cadastrar DESPENSA
    private void cadastraDespensa(){
        //Instanciação da classe BancoDeDados
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);

        try {
            //Validar se a despensa ainda não está cadastrada no banco
            //Verificar, via descrição, se a despensa existe no banco de dados
            List<Despensa> listaDespensa = bancoDeDados.buscaDescricaoDespensa(editTextDespensa.getText().toString().trim());
            if(listaDespensa.size()<1){
                Log.d("Success","CadastroDespensaFragment.cadastrarDespensa >>> " +
                        "Despensa ainda não cadastrada");
                if(!editTextDespensa.getText().toString().isEmpty()){
                    bancoDeDados.cadastrarDespensa(editTextDespensa.getText().toString());
                    Log.d("Success", "Descrição: "+editTextDespensa.getText().toString());
                    editTextDespensa.setText("");
                    Toast.makeText(getContext(), "Despensa cadastrada com sucesso!",
                            Toast.LENGTH_SHORT).show();

                }else{
                    Log.d("Success","CadastroDespensaFragment.cadastrarDespensa >>> " +
                            "A descrição da despensa não foi inserida");
                }
            }else{//Caso a despensa já esteja cadastrada no banco
                Log.d("Success","CadastroDespensaFragment.cadastrarDespensa >>> " +
                        "Despensa " + listaDespensa.get(0).getDescricao() + " já cadastrado");
                Toast.makeText(getContext(), "Despensa já cadastrada! Por favor insira uma nova despensa",
                        Toast.LENGTH_SHORT).show();
                editTextDespensa.setText("");
            }

        }catch (Exception e){
            Log.d("Success", "CadastroDespensaFragment.cadastrarDespensa >>> " +
                    "CadastroDespensaFragment.cadastrarDespensa.Execpetion 1 ->" + e.toString());
        }
    }

    private void criarDespensa() {
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        listaDespensa = bancoDeDados.buscaDespensa();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

}