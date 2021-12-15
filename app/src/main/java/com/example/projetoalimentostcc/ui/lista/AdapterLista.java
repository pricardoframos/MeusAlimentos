package com.example.projetoalimentostcc.ui.lista;

import android.content.DialogInterface;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.ProdutoXDespensa;
import com.example.projetoalimentostcc.ui.classes.ProdutoXLista;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;

import java.util.List;
import java.util.Locale;

public class AdapterLista extends RecyclerView.Adapter<AdapterLista.MyViewHolder>{

    private List<ProdutoXLista> listaProdutoXLista;
    private View itemLista;
    IProdutoXListaRecycler mListener;
    int indice;
    String imagemProduto;

    public AdapterLista(List<ProdutoXLista> listaProdutoXLista, IProdutoXListaRecycler mListener){
        this.listaProdutoXLista = listaProdutoXLista;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        itemLista = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.adapter_recycler_lista, parent, false);

        MyViewHolder myViewHolder = new MyViewHolder(itemLista, mListener);

        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        BancoDeDados bancoDeDados = new BancoDeDados(itemLista.getContext(),1);
        ProdutoXLista produtoXLista = listaProdutoXLista.get(position);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlterFragment.indiceRecyclerViewLista = produtoXLista.getIdRegistro();
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
                        int indice = produtoXLista.getIdRegistro();
                        int remover = 0;
                        if(bancoDeDados.removerProdutoXLista(produtoXLista.getIdRegistro()) == true){
                            Log.d("Success","indice = "+ indice);
                            Toast.makeText(itemLista.getContext(), "Registro excluído com sucesso!",
                                    Toast.LENGTH_SHORT).show();
                            remover = removerProdutoXLista(indice);
                            Log.d("Success","AdapterLista.itemView.onLongClick >>> " +
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

        if(produtoXLista.getStatus()==0){
            holder.checkBox.setChecked(false);
        }else{
            holder.checkBox.setChecked(true);
        }

        holder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(produtoXLista.getStatus()==0){
                    produtoXLista.setStatus(1);
                }else{
                    produtoXLista.setStatus(0);
                }
                bancoDeDados.atualizarStatusProdutoXLista(produtoXLista.getIdRegistro(),produtoXLista.getStatus());
                mListener.atualizarValorCarrinho();
            }
        });

        //System.out.println(String.format(
        //                Locale.GERMAN, "%,.2f", d)

        //Variável quantidade declarada antes da atribuição no holder para execução do replace
        double quantidade = (bancoDeDados.buscaIdProdutoXLista(produtoXLista.getIdRegistro()).get(0)
                .getQuantidadeProduto());
        String quantFormat;
        if(quantidade %1 == 0){
            quantFormat = String.format("%.0f",quantidade).replace(".",",");
        }else{
            quantFormat = String.valueOf(quantidade).replace(".",",");
        }

        String unidade = bancoDeDados.buscaIdProduto(produtoXLista.getIdProduto()).
                get(0).getUnidade();

        //Variável preco declarada antes da atribuição no holder para execução do replace
        String preco = ("R$" + String.format( "%.2f",bancoDeDados.buscaIdProdutoXLista(produtoXLista.
                getIdRegistro()).get(0).getPrecoProduto()*bancoDeDados.buscaIdProdutoXLista
                (produtoXLista.getIdRegistro()).get(0).getQuantidadeProduto()));

        holder.descricao.setText(bancoDeDados.buscaIdProduto(listaProdutoXLista.get(position)
                .getIdProduto()).get(0).getDescricao());
        holder.precoQuantidade.setText(quantFormat+" "+unidade+" = " +preco);
        Glide.with(holder.fotoProduto.getContext()).load(bancoDeDados.
                buscaIdProduto(listaProdutoXLista.get(position).getIdProduto()).get(0).
                getUrlDaImagem()).into(holder.fotoProduto);
    }

    //Método para remover o produto da listaProdutoXLista
    private int removerProdutoXLista(int indice) {
        int remover=0;
        for(int i=0; i<listaProdutoXLista.size();i++){
            Log.d("Success","AdapterLista.removerProdutoXLista >>> " +
                    "Laço FOR valor de i = "+i);
            if(listaProdutoXLista.get(i).getIdRegistro() == indice){
                Log.d("Success","AdapterLista.removerProdutoXLista >>> " +
                        "ID do registro = "+listaProdutoXLista.get(i).getIdRegistro());
                listaProdutoXLista.remove(i);
                remover=i;
            }
        }
        mListener.atualizarValorLista();
        return remover;
    }

    @Override
    public int getItemCount() {
        return listaProdutoXLista.size();
    }

    @Override
    public int getItemViewType(int position){
        return position;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder{
        //Declaração dos elementos do ViewHolder
        TextView descricao, validade, precoQuantidade;
        ImageView fotoProduto;
        CheckBox checkBox;
        IProdutoXListaRecycler mListener;

        public MyViewHolder(@NonNull View itemView, IProdutoXListaRecycler mListener) {
            super(itemView);
            //Referência dos elementos do ViewHolder
            descricao = itemView.findViewById(R.id.textViewListaDescricao);
            precoQuantidade = itemView.findViewById(R.id.textViewListaUniPreco);
            checkBox = itemView.findViewById(R.id.checkBox);
            fotoProduto = itemView.findViewById(R.id.imageViewListaProduto);

            this.mListener = mListener;
        }
    }

    interface IProdutoXListaRecycler{
        void irparaDetalhesProduto(int indice);
        void atualizarValorCarrinho();
        void atualizarValorLista();
    }
}