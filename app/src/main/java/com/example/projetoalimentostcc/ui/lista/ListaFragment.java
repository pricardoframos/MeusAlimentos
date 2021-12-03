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
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.addProdutos.AddProdutosFragmentDespensa;
import com.example.projetoalimentostcc.ui.addProdutos.AddProdutosFragmentLista;
import com.example.projetoalimentostcc.ui.casa.AdapterCasa;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.ProdutoXDespensa;
import com.example.projetoalimentostcc.ui.classes.ProdutoXLista;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.google.android.material.navigation.NavigationView;

import java.util.ArrayList;
import java.util.List;

public class ListaFragment extends Fragment implements AdapterLista.IProdutoXListaRecycler {
    //Declaração dos elementos do fragment
    private View view;
    private ImageButton imgButtonListaAdd, imgButtonLerDispensa;
    private RecyclerView recyclerViewLista;
    private TextView textViewListaCusto, textViewCarrinhoCusto;
    private String titulo = AlterFragment.listaAtual;

    //Declaração de variáveis
    private List<ProdutoXLista> listaProdutoXLista = new ArrayList<>();
    private double valorLista;
    private double valorTotal;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_lista, container, false);


        NavigationView menu = getActivity().findViewById(R.id.nav_view);
        //menu.getMenu().getItem(3).setChecked(true);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle(titulo);

        //Referência dos elementos do fragment
        imgButtonListaAdd = view.findViewById(R.id.imgButtonListaAdd);
        imgButtonLerDispensa = view.findViewById(R.id.imgButtonLerDispensa);
        textViewListaCusto = view.findViewById(R.id.textViewListaCusto);
        textViewCarrinhoCusto = view.findViewById(R.id.textViewCarrinhoCusto);

        //Alterar os valores da lista e do carrinho
        atualizarValorLista();
        atualizarValorCarrinho();

        //Abrir AddProdutoFragment
        imgButtonListaAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v1) {
                AlterFragment.fragmentoOrigemDestino = "ListaFragment-AddProduto";
                //Apresentar o fragment AddProdutoFragmentLista
                getParentFragmentManager().beginTransaction()
                        .replace(R.id.nav_host_fragment_content_main,
                                new AddProdutosFragmentLista()).addToBackStack("Adicionar Produto Lista").commit();
            }
        });

        recyclerViewLista = view.findViewById(R.id.recyclerViewLista);
        listaProdutoXLista.clear();

        //DataSet
        Log.d("Success", "ListaFragment.onCreateView >>> Método criarProduto acionado");
        this.criarProdutoXLista();

        //Adapter
        AdapterLista adapter = new AdapterLista(listaProdutoXLista, this);

        //Layout Manager
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity().getApplicationContext());
        recyclerViewLista.setLayoutManager(layoutManager);
        recyclerViewLista.setHasFixedSize(true);
        recyclerViewLista.setAdapter(adapter);

        return view;
    }

    private void criarProdutoXLista() {
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        List<ProdutoXLista>listaProdutosXLista = bancoDeDados.buscaListaProdutoXLista
                (bancoDeDados.buscaDescricaoLista(AlterFragment.listaAtual).get(0).getId());
        if(listaProdutosXLista.size()>0){
            int ciclo = 0;
            do {//----AS BUSCAS PRECISAM SER FEITAS COM BASE NA LISTA ATUAL
                ProdutoXLista produtoXLista = new ProdutoXLista(bancoDeDados.buscaListaProdutoXLista
                        (bancoDeDados.buscaDescricaoLista(AlterFragment.listaAtual).get(0).getId())
                        .get(ciclo).getIdRegistro(),
                        bancoDeDados.buscaListaProdutoXLista(bancoDeDados.buscaDescricaoLista
                                (AlterFragment.listaAtual).get(0).getId()).get(ciclo).getIdProduto(),
                        bancoDeDados.buscaListaProdutoXLista(bancoDeDados.buscaDescricaoLista
                                (AlterFragment.listaAtual).get(0).getId()).get(ciclo).getQuantidadeProduto(),
                        bancoDeDados.buscaListaProdutoXLista(bancoDeDados.buscaDescricaoLista
                                (AlterFragment.listaAtual).get(0).getId()).get(ciclo).getPrecoProduto(),
                        bancoDeDados.buscaListaProdutoXLista(bancoDeDados.buscaDescricaoLista
                                (AlterFragment.listaAtual).get(0).getId()).get(ciclo).getIdLista(),
                        bancoDeDados.buscaListaProdutoXLista(bancoDeDados.buscaDescricaoLista
                                (AlterFragment.listaAtual).get(0).getId()).get(ciclo).getStatus());
                listaProdutoXLista.add(produtoXLista);
                ciclo++;
            }while(ciclo<listaProdutosXLista.size());
            Log.d("Success", "ListaFragment.criarProdutoXLista >>> Lista de produtosXLista encontrada");//
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }

    @Override
    public void irparaDetalhesProduto(int indice) {
        AlterFragment.fragmentoOrigemDestino = "RecycleViewLista-AddProdutosFragmentLista";
        getParentFragmentManager().beginTransaction()
                .replace(R.id.nav_host_fragment_content_main,
                        new AddProdutosFragmentLista()).addToBackStack("Lista").commit();
    }

    @Override
    public void atualizarValorLista(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        List<ProdutoXLista>listaProdutosXLista = bancoDeDados.buscaListaProdutoXLista
                (bancoDeDados.buscaDescricaoLista(titulo).get(0).getId());
        if(listaProdutosXLista.size()>0){
            double totalProduto = 0;
            for(int i=0;i<listaProdutosXLista.size();i++){
                totalProduto = listaProdutosXLista.get(i).getPrecoProduto()*listaProdutosXLista.get(i)
                        .getQuantidadeProduto();
                valorLista += totalProduto;
            }
        }
        textViewListaCusto.setText(("Lista: R$ " + valorLista).replace(".",","));
        valorLista = 0;
    }
    @Override
    public void atualizarValorCarrinho() {
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        List<ProdutoXLista>listaProdutosXLista = bancoDeDados.buscaListaProdutoXLista
                (bancoDeDados.buscaDescricaoLista(titulo).get(0).getId());
        double totalProdutoCarrinho = 0;
        for(int i=0;i<listaProdutosXLista.size();i++){
            if(listaProdutosXLista.get(i).getStatus()==1){
                totalProdutoCarrinho = listaProdutosXLista.get(i).getPrecoProduto()*listaProdutosXLista.get(i)
                        .getQuantidadeProduto();
                valorTotal += totalProdutoCarrinho;
            }
        }
        textViewCarrinhoCusto.setText(("Carrinho: R$ " + valorTotal).replace(".",","));
        valorTotal = 0;
    }

}