package com.example.projetoalimentostcc.ui.despensa;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.classes.Despensa;
import com.example.projetoalimentostcc.ui.classes.Produto;

import java.util.List;

public class AdapterCadastroDespensa extends RecyclerView.Adapter<AdapterCadastroDespensa.MyViewHolder>{

    private List<Despensa> listaDespensa;
    public AdapterCadastroDespensa(List<Despensa> listaDespensa){
        this.listaDespensa = listaDespensa;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recycler_lista_despensa, parent, false);

        return new MyViewHolder(itemLista);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Despensa despensa = listaDespensa.get(position);

        holder.descricao.setText(despensa.getDescricao());
    }

    @Override
    public int getItemCount() {
        return listaDespensa.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //Declaração dos elementos do ViewHolder
        TextView descricao;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            //Referência dos elementos do ViewHolder
            descricao = itemView.findViewById(R.id.textViewDespensaDescricao);
        }
    }
}