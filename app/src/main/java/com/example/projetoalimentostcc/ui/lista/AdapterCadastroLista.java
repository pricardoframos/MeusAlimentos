package com.example.projetoalimentostcc.ui.lista;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.Lista;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;

import java.util.List;

public class AdapterCadastroLista extends RecyclerView.Adapter<AdapterCadastroLista.MyViewHolder>{

    private List<Lista> listaLista;
    private View itemLista;
    IListaRecycler mListener;
    int indice;

    public AdapterCadastroLista(List<Lista> listaLista, IListaRecycler mListener){
        this.listaLista = listaLista;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
         itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recycler_lista_lista, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemLista, mListener);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BancoDeDados bancoDeDados = new BancoDeDados(itemLista.getContext(),1);
        Lista lista = listaLista.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlterFragment.indiceRecyclerViewCadastroLista = lista.getId();
                mListener.irparaDetalhesProduto(lista.getId());
                Log.d("Success","AdapterCadastroLista.onBindViewHolder >>> " +
                        "lista.getId = "+lista.getId());
            }
        });

        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                AlertDialog.Builder confirmarExclusao = new AlertDialog.Builder(itemLista.getContext());
                confirmarExclusao.setTitle("Excluir registro");
                confirmarExclusao.setMessage("Tem certeza que deseja excluir este registro?");
                confirmarExclusao.setCancelable(false);
                confirmarExclusao.setPositiveButton("Sim", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        int indice = lista.getId();
                        int remover = 0;
                        if(bancoDeDados.removerLista(lista.getId()) == true){
                            Log.d("Success","indice = "+ indice);
                            Toast.makeText(itemLista.getContext(), "Lista excluída com sucesso!",
                                    Toast.LENGTH_SHORT).show();
                            remover = removerLista(indice);
                            Log.d("Success","AdapterCadastroLista.itemView.onLongClick >>> " +
                                    "Removeu da lista = "+ remover);
                            notifyItemRemoved(remover);
                            Log.d("Success","AdapterLista.itemView.onLongClick >>> " +
                                    "Removeu do recycler = "+ remover);
                            notifyDataSetChanged();
                        }
                    }
                });
                confirmarExclusao.setNegativeButton("Não", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });
                confirmarExclusao.create().show();
                return false;
            }
        });

        holder.descricao.setText(lista.getDescricao());
    }

    //Método para remover o produto da listaProdutoXLista
    private int removerLista(int indice) {
        int remover=0;
        for(int i=0; i<listaLista.size();i++){
            Log.d("Success","AdapterLista.removerLista >>> " +
                    "Laço FOR valor de i = "+i);
            if(listaLista.get(i).getId() == indice){
                Log.d("Success","AdapterLista.removerLista >>> " +
                        "ID da lista = "+listaLista.get(i).getId());
                listaLista.remove(i);
                remover=i;
            }
        }
        return remover;
    }

    @Override
    public int getItemCount() {
        return listaLista.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //Declaração dos elementos do ViewHolder
        TextView descricao;
        IListaRecycler mListener;

        public MyViewHolder(@NonNull View itemView, IListaRecycler mListener) {
            super(itemView);
            //Referência dos elementos do ViewHolder
            descricao = itemView.findViewById(R.id.textViewListaDescricao);

            this.mListener = mListener;
        }
    }

    interface IListaRecycler{
        void irparaDetalhesProduto(int indice);
    }
}