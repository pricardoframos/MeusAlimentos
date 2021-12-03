package com.example.projetoalimentostcc.ui.casa;

import android.content.DialogInterface;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.addProdutos.AddProdutosFragmentDespensa;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.Despensa;
import com.example.projetoalimentostcc.ui.classes.Produto;
import com.example.projetoalimentostcc.ui.classes.ProdutoXDespensa;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.List;

public class AdapterCasa extends RecyclerView.Adapter<AdapterCasa.MyViewHolder>{

    private List<ProdutoXDespensa> listaProdutoXDespensa;
    private View itemLista;
    IProdutoXDespensaRecycler mListener;
    int indice;
    String imagemProduto;

    public AdapterCasa(List<ProdutoXDespensa> listaProdutoXDespensa, IProdutoXDespensaRecycler mListener){
        this.listaProdutoXDespensa = listaProdutoXDespensa;
        this.mListener = mListener;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recycler_casa, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemLista, mListener);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BancoDeDados bancoDeDados = new BancoDeDados(itemLista.getContext(),1);
        ProdutoXDespensa produtoXDespensa = listaProdutoXDespensa.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlterFragment.indiceRecyclerViewCasa = produtoXDespensa.getIdRegistro();
                mListener.irparaDetalhesProduto(indice);
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
                        int indice = produtoXDespensa.getIdRegistro();
                        int remover = 0;
                        if(bancoDeDados.removerProdutoXDespensa(produtoXDespensa.getIdRegistro()) == true){
                            Log.d("Success","indice = "+ indice);
                            Toast.makeText(itemLista.getContext(), "Registro excluído com sucesso!", Toast.LENGTH_SHORT).show();
                            remover = removerProdutoXDespensa(indice);
                            Log.d("Success","AdapterCasa.itemView.onLongClick >>> " +
                                    "Removeu da lista = "+ remover);
                            notifyItemRemoved(remover);
                            Log.d("Success","AdapterCasa.itemView.onLongClick >>> " +
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

        holder.descricao.setText(bancoDeDados.buscaIdProduto(listaProdutoXDespensa.get(position)
                .getIdProduto()).get(0).getDescricao());
        holder.validade.setText(bancoDeDados.buscaIdProdutoXDespensa(produtoXDespensa.
                getIdRegistro()).get(0).getValidProduto());
        Glide.with(holder.fotoProduto.getContext()).load(bancoDeDados.
                buscaIdProduto(listaProdutoXDespensa.get(position).getIdProduto()).get(0).
                getUrlDaImagem()).into(holder.fotoProduto);
    }

    //Método para remover o produto da listaProdutoXDespensa
    private int removerProdutoXDespensa(int indice) {
        int remover=0;
        for(int i=0; i<listaProdutoXDespensa.size();i++){
            Log.d("Success","AdapterCasa.removerProdutoXDespensa >>> " +
                    "Laço FOR valor de i = "+i);
            if(listaProdutoXDespensa.get(i).getIdRegistro() == indice){
                Log.d("Success","AdapterCasa.removerProdutoXDespensa >>> " +
                        "ID do registro = "+listaProdutoXDespensa.get(i).getIdRegistro());
                listaProdutoXDespensa.remove(i);
                remover=i;
            }
        }
        return remover;
    }

    @Override
    public int getItemCount() {
        return listaProdutoXDespensa.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //Declaração dos elementos do ViewHolder
        TextView descricao, validade;
        ImageView fotoProduto;
        IProdutoXDespensaRecycler mListener;

        public MyViewHolder(@NonNull View itemView, IProdutoXDespensaRecycler mListener) {
            super(itemView);
            //Referência dos elementos do ViewHolder
            descricao = itemView.findViewById(R.id.textViewCasaDescricao);
            validade = itemView.findViewById(R.id.textViewCasaValidade);
            fotoProduto = itemView.findViewById(R.id.imageViewCasaProduto);

            //Máscara no para o campo data de validade
            SimpleMaskFormatter smfData = new SimpleMaskFormatter("NN/NN/NNNN");
            MaskTextWatcher mtwData = new MaskTextWatcher(validade, smfData);
            validade.addTextChangedListener(mtwData);

            this.mListener = mListener;
        }
    }

    interface IProdutoXDespensaRecycler{
        void irparaDetalhesProduto(int indice);
    }
}