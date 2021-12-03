package com.example.projetoalimentostcc.ui.addProdutos;


import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;
import com.example.projetoalimentostcc.R;
import com.example.projetoalimentostcc.ui.casa.CasaFragment;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.Despensa;
import com.example.projetoalimentostcc.ui.classes.Lista;
import com.example.projetoalimentostcc.ui.classes.Produto;
import com.example.projetoalimentostcc.ui.classes.ProdutoXDespensa;
import com.example.projetoalimentostcc.ui.classes.ProdutoXLista;
import com.example.projetoalimentostcc.ui.lista.ListaFragment;
import com.example.projetoalimentostcc.ui.service.BancoDeDados;
import com.example.projetoalimentostcc.ui.service.CaptureAct;
import com.github.rtoshiro.util.format.SimpleMaskFormatter;
import com.github.rtoshiro.util.format.text.MaskTextWatcher;
import com.google.android.material.navigation.NavigationView;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProdutosFragmentLista extends Fragment {
    //Declaração dos elementos do fragment
    private View view;
    private Spinner spinnerCategoria, spinnerMedida;
    private ImageButton imgButtonLer;
    private EditText editTextCodList, editTextDesc, editTextQuant, editTextPreco;
    private ImageView imageViewProduto;
    private Button buttonAdicionar;

    //Declaração de variáveis
    private static String scannerResult;
    private final String TOKEN = "SzqFhlzVZEjHHt1i2wFBDA";
    private String desc;
    private String imgUrl;
    private String cod;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_produtos_lista, container, false);

        //Referência dos elementos do fragment
        editTextCodList = view.findViewById(R.id.editTextCodList);
        editTextDesc = view.findViewById(R.id.editTextDesc);
        editTextPreco = view.findViewById(R.id.editTextPreco);
        editTextQuant = view.findViewById(R.id.editTextQuant);
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        spinnerMedida = view.findViewById(R.id.spinnerMedida);
        imgButtonLer = view.findViewById(R.id.imgButtonLer);
        imageViewProduto = view.findViewById(R.id.imageViewProduto);
        buttonAdicionar = view.findViewById(R.id.buttonAdicionar);

        //Botão Scanner/Add
        imgButtonLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        //ABRIR PRODUTO PARA EDIÇÃO CASO ESTE FRAGMENTE SEJA CHAMADO POR UM ITEM DO RECYCLERVIEW
        if(AlterFragment.fragmentoOrigemDestino == "RecycleViewLista-AddProdutosFragmentLista"){

            abrirProdutoRecycler();
        }

        //Botão Adicionar
        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlterFragment.fragmentoOrigemDestino == "RecycleViewLista-AddProdutosFragmentLista"){
                    atualizarProduto();
                    atualizarProdutoXLista();
                    AlterFragment.fragmentoOrigemDestino = null;
                    retornarFragment();
                }else{
                    //Validar se o campo Descrição está preenchido
                    if(!editTextDesc.getText().toString().isEmpty()){
                        //Validar se o produto a ser adicionado na lista já está cadastrado no banco
                        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
                        List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
                        Log.d("Success","AddProdutosFragmentLista.buttonAdicionar >>> Criou listaProduto");

                        if(listaProduto.size() > 0){
                            Log.d("Success","AddProdutosFragmentLista.buttonAdicionar >>> " +
                                    "Produto encontrado");
                            Log.d("Success","AddProdutosFragmentLista.buttonAdicionar >>> " +
                                    "Método cadastrarProdutoXLista acionado");
                            cadastrarProdutoXLista();

                            Log.d("Success","AddProdutosFragmentLista.buttonAdicionar >>> " +
                                    "Método retornarFragment acionado");
                            retornarFragment();
                        }else{
                            Log.d("Success","AddProdutosFragmentLista.buttonAdicionar >>> " +
                                    "Produto não encontrado");
                            Log.d("Success","AddProdutosFragmentLista.buttonAdicionar >>> " +
                                    "Método cadastrarProdutoXLista acionado");
                            cadastrarProduto();

                            Log.d("Success","AddProdutosFragmentLista.buttonAdicionar >>> " +
                                    "Método cadastrarProdutoXLista acionado");
                            cadastrarProdutoXLista();

                            Log.d("Success","AddProdutosFragmentLista.buttonAdicionar >>> " +
                                    "Método retornarFragment acionado");
                            retornarFragment();
                        }
                    }else {
                        Toast.makeText(getContext(), "O campo descrição não pode estar vazio!",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }

        });

        editTextDesc.addTextChangedListener(new TextWatcher() {
            BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }
            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

//                if(editTextDesc.getText().toString().length() >= 1){
//                    editTextDesc.setHint(bancoDeDados.procurarProdutos(editTextDesc.getText().toString()));
//                    rvLista.addAll(bancoDeDados.procurarProdutos(editTextDesc.getText().toString()));
//                    a1.atualizar();
//                }else{
//                    rvLista.clear();
//                    rvLista = bancoDeDados.buscaDescricao(editTextDesc.getText().toString());
//                    a1.atualizar();
//                }
            }
            @Override
            public void afterTextChanged(Editable editable) {

            }
        });

        editTextDesc.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
            @Override
            public void onFocusChange(View view, boolean hasfocus) {
                //Verificar, via descrição, se o produto existe no banco de dados
                List<Produto> lista = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
                if(!hasfocus && !editTextDesc.getText().toString().isEmpty()){
                    if(lista.size() > 0){
                        spinnerCategoria.setSelection(atribuirSpinnerCategoria(lista.get(0).getCategoria()));
                        Log.d("Success","AddProdutosFragmentLista.editTextDesc.onFocusChange >>> " +
                                "spinnerCategoria alterado com sucesso");
                        spinnerMedida.setSelection(atribuirSpinnerUnidade(lista.get(0).getUnidade()));
                        Log.d("Success","AddProdutosFragmentLista.editTextDesc.onFocusChange >>> " +
                                "spinnerMedida alterado com sucesso");
                        if(lista.get(0).getUrlDaImagem() == null){
                            Log.d("Success", "Imagem não encontrada");
                        }else{
                            Log.d("Success","Imagem encontrada");
                            Log.d("Success", lista.get(0).getUrlDaImagem());
                            //Glide.with(view.getContext()).load(lista.get(5).toString()).into(imageViewProduto);
                        }
                    }else{

                    }
                }else{

                }
            }
        });

        return view;
    }

    //Retornar para a fragment de origem
    private void retornarFragment(){
        getParentFragmentManager().beginTransaction().replace(R.id.nav_host_fragment_content_main,
                new ListaFragment()).addToBackStack("Lista").commit();
    }

    //Cadastrar PRODUTOXLISTA
    private void cadastrarProdutoXLista(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
        Log.d("Success","AddProdutosFragmentLista.cadastrarProdutoXLista >>> Criou listaProduto");
//        List<Produto> produtoXLista = bancoDeDados.busca(editTextDesc.getText().toString().trim());
//        Log.d("Success","AddProdutosFragmentLista.cadastrarProdutoXLista >>> Criou listaProduto");

        //Variáveis para validação de elementos
        double quantidade;
        double preco;

        //Validação dos elementos
        if(editTextQuant.getText().toString().isEmpty()){
            quantidade = 1;
        }else {
            quantidade = Double.parseDouble(editTextQuant.getText().toString());
        }
        if(editTextPreco.getText().toString().isEmpty()){
            preco = 0;
        }else {
            preco = Double.parseDouble(editTextPreco.getText().toString());
        }

        try {
            bancoDeDados.cadastrarProdutoXLista(listaProduto.get(0).getId(),
                    quantidade,preco,
                    bancoDeDados.buscaDescricaoLista(AlterFragment.listaAtual).get(0).getId(),
                    0);
        }catch(Exception e){
            Log.d("Success","Add.AddProdutosFragmentLista.cadastrarProdutoXLista >>> "+ e);
        }
    }

    //Cadastrar PRODUTO
    private void cadastrarProduto(){
        //Instanciação da classe BancoDeDados
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);

        //Validação de elemento
        if(editTextCodList.getText().toString() == null){
            cod = "";
        }else{
            cod = editTextCodList.getText().toString();
        }
        try {
            //Validar se o produto ainda não está cadastrado no banco
            //Verificar, via descrição, se o produto existe no banco de dados
            List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
            if(listaProduto.size()<1){
                Log.d("Success","AddProdutosFragmentLista.cadastrarProduto >>> " +
                        "Produto ainda não cadastrado");
                if(!editTextDesc.getText().toString().isEmpty()){
                    bancoDeDados.cadastrarProduto(cod,
                            editTextDesc.getText().toString().trim(),
                            spinnerCategoria.getSelectedItem().toString(),
                            spinnerMedida.getSelectedItem().toString(),
                            imgUrl);
                    Log.d("Success", "Código: "+editTextCodList.getText().toString());
                    Log.d("Success", "Descrição: "+editTextDesc.getText().toString());
                    Log.d("Success", "Categoria: "+spinnerCategoria.getSelectedItem().toString());
                    Log.d("Success", "Uni. de medida: "+spinnerMedida.getSelectedItem().toString());
                }else{
                    Log.d("Success","AddProdutosFragmentLista.cadastrarProduto >>> " +
                            "A descrição do produto não foi inserida");
                }
            }else{//Caso o produto já esteja cadastrado no banco
                Log.d("Success","AddProdutosFragmentLista.cadastrarProduto >>> " +
                        "Produto " + listaProduto.get(0).getDescricao() + " já cadastrado");
            }

        }catch (Exception e){
            Log.d("Success", "AddProdutosFragmentLista.cadastrarProduto >>> " +
                    "AddProdutosFragmentLista.cadastrarProduto.Execpetion 1 ->" + e.toString());
        }
    }

    //Atualizar PRODUTOXLISTA
    private void atualizarProdutoXLista(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        try{
            List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
            Log.d("Success","AddProdutosFragmentLista.atualizarProdutoXLista >>> Criou listaProduto");
            List<ProdutoXLista> listaProdutoXLista = bancoDeDados.buscaIdProdutoXLista(AlterFragment.indiceRecyclerViewLista);
            Log.d("Success","AddProdutosFragmentLista.atualizarProdutoXLista >>> Criou listaProdutoXLista");
            List<Lista>listaLista = bancoDeDados.buscaDescricaoLista(AlterFragment.listaAtual);
            Log.d("Success","AddProdutosFragmentLista.atualizarProdutoXLista >>> Criou listaLista");

            //Variáveis para validação de elementos
            double quantidade;
            double preco;

            //Validação dos elementos
            if(editTextQuant.getText().toString().isEmpty()){
                quantidade = 1;
            }else {
                quantidade = Double.parseDouble(editTextQuant.getText().toString());
            }
            if(editTextPreco.getText().toString().isEmpty()){
                preco = 0;
            }else{
                preco = Double.parseDouble(editTextPreco.getText().toString());
            }

            Log.d("Success","AddProdutosFragmentLista.atualizarProdutoXLista >>> Validações feitas");

            //int idReg, int idProd, double quant, int valid, int idDesp

            if(bancoDeDados.atualizarProdutoXLista(listaProdutoXLista.get(0).getIdRegistro(),
                    listaProduto.get(0).getId(),
                    quantidade,preco,
                    listaLista.get(0).getId()) == true){
                Log.d("Success","AddProdutosFragmentLista.atualizarProdutoXLista >>> " +
                        "ProdutoXLista atualizado com sucesso");
                Toast.makeText(getContext(), "Produto atualizado com sucesso", Toast.LENGTH_SHORT).show();
            }else{
                Log.d("Success","AddProdutosFragmentLista.atualizarProdutoXLista >>> " +
                        "Não foi possível atualizar ProdutoXLista");
                Toast.makeText(getContext(), "Não foi possível atualizar o produto", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Log.d("Success","Add.ProdutosFragment.atualizarProdutoXLista >>> "+ e);
        }
    }

    //Atualizar PRODUTO
    private void atualizarProduto(){
        //Instanciação da classe BancoDeDados
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);

        //Validação de elemento
        if(editTextCodList.getText().toString() == null){
            cod = "";
        }else{
            cod = editTextCodList.getText().toString();
        }
        try {
            //Validar se o produto ainda não está cadastrado no banco
            //Verificar, via descrição, se o produto existe no banco de dados
            List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
            Log.d("Success","AddProdutosFragmentLista.atualizarProduto >>> " +
                    "Lista criada. Id do produto: "+ listaProduto.get(0).getId());

            if(bancoDeDados.atualizarProduto(listaProduto.get(0).getId(),
                    cod,
                    editTextDesc.getText().toString(),
                    spinnerCategoria.getSelectedItem().toString(),
                    spinnerMedida.getSelectedItem().toString(),
                    bancoDeDados.buscaIdProduto(listaProduto.get(0).getId()).get(0).getUrlDaImagem())==true){
                Log.d("Success", "Código: "+editTextCodList.getText().toString());
                Log.d("Success", "Descrição: "+editTextDesc.getText().toString());
                Log.d("Success", "Categoria: "+spinnerCategoria.getSelectedItem().toString());
                Log.d("Success", "Uni. de medida: "+spinnerMedida.getSelectedItem().toString());
                Log.d("Success","AddProdutosFragmentLista.atualizarProduto >>> " +
                        "Produto atualizado com sucesso");
            }

        }catch (Exception e){
            Log.d("Success", "AddProdutosFragmentLista.atualizarProduto >>> " +
                    "AddProdutosFragmentLista.atualizarProduto.Execpetion 1 ->" + e.toString());
        }
    }

    //Atribuir valor ao spinnerCategoria
    private int atribuirSpinnerCategoria(String categoria){
        int indice = 0;
        switch (categoria){
            case "Bazar":
                indice = 0;
                break;
            case "Bebê":
                indice = 1;
                break;
            case "Bebidas":
                indice = 2;
                break;
            case "Carnes, Aves e Peixes":
                indice = 3;
                break;
            case "Congelados":
                indice = 4;
                break;
            case "Farmácia":
                indice = 5;
                break;
            case "Floricultura e Jardinagem":
                indice = 6;
                break;
            case "Frios e Lacticínios":
                indice = 7;
                break;
            case "Higiene e Perfumaria":
                indice = 8;
                break;
            case "Hortifruti":
                indice = 9;
                break;
            case "Limpeza":
                indice = 10;
                break;
            case "Mercearia":
                indice = 11;
                break;
            case "Pães e Bolos":
                indice = 12;
                break;
            case "Pet":
                indice = 13;
                break;
        }
        return indice;
    }

    //Atribuir valor ao spinnerCategoria
    private int atribuirSpinnerUnidade(String unidade){
        int indice = 0;
        switch (unidade){
            case "Un":
                indice = 0;
                break;
            case "Dz":
                indice = 1;
                break;
            case "Ml":
                indice = 2;
                break;
            case "L":
                indice = 3;
                break;
            case "Kg":
                indice = 4;
                break;
            case "gr":
                indice = 5;
                break;
            case "Caixa":
                indice = 6;
                break;
            case "Pacote":
                indice = 7;
                break;
            case "Garrafa":
                indice = 8;
                break;
            case "Lata":
                indice = 9;
                break;
        }
        return indice;
    }

    private void abrirProdutoRecycler(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);

        buttonAdicionar.setText("Atualizar");
        editTextCodList.setText(bancoDeDados.buscaIdProduto(bancoDeDados.buscaIdProdutoXLista
                (AlterFragment.indiceRecyclerViewLista).get(0).getIdProduto()).get(0).getCodigo());
        Log.d("Success","Add.ProdutosFragmentLista.abrirProdutoRecycler >>> " +
                "Codigo captado e inserido. IdRegistro: " + AlterFragment.indiceRecyclerViewLista);

        editTextDesc.setText(bancoDeDados.buscaIdProduto(bancoDeDados.buscaIdProdutoXLista
                (AlterFragment.indiceRecyclerViewLista).get(0).getIdProduto()).get(0).getDescricao());
        Log.d("Success","Add.ProdutosFragmentLista.abrirProdutoRecycler >>> " +
                "Descrição captada e inserida. IdRegistro: " + AlterFragment.indiceRecyclerViewLista);

        spinnerCategoria.setSelection(atribuirSpinnerCategoria(bancoDeDados.buscaIdProduto
                (bancoDeDados.buscaIdProdutoXLista(AlterFragment.indiceRecyclerViewLista).
                        get(0).getIdProduto()).get(0).getCategoria()));
        Log.d("Success","Add.ProdutosFragmentLista.abrirProdutoRecycler >>> " +
                "Categoria captada e inserida. IdRegistro: " + AlterFragment.indiceRecyclerViewLista);

        editTextQuant.setText(String.valueOf(bancoDeDados.buscaIdProdutoXLista
                (AlterFragment.indiceRecyclerViewLista).get(0).getQuantidadeProduto()));
        Log.d("Success","Add.ProdutosFragmentLista.abrirProdutoRecycler >>> " +
                "Quantidade captada e inserida");

        spinnerMedida.setSelection(atribuirSpinnerUnidade(bancoDeDados.buscaIdProduto
                (bancoDeDados.buscaIdProdutoXLista(AlterFragment.indiceRecyclerViewLista).
                        get(0).getIdProduto()).get(0).getUnidade()));
        Log.d("Success","Add.ProdutosFragmentLista.abrirProdutoRecycler >>> " +
                "Unidade de medida captada e inserida");

        editTextPreco.setText(String.valueOf(bancoDeDados.buscaIdProdutoXLista
                (AlterFragment.indiceRecyclerViewLista).get(0).getPrecoProduto()));
        Log.d("Success","Add.ProdutosFragmentLista.abrirProdutoRecycler >>> " +
                "Data captada e inserida");

        if(bancoDeDados.buscaIdProduto(bancoDeDados.buscaIdProdutoXLista
                (AlterFragment.indiceRecyclerViewLista).get(0).getIdProduto()).get(0).getUrlDaImagem()!=null){
            Glide.with(view.getContext()).load(bancoDeDados.buscaIdProduto(bancoDeDados
                    .buscaIdProdutoXLista(AlterFragment.indiceRecyclerViewLista).get(0)
                    .getIdProduto()).get(0).getUrlDaImagem()).into(imageViewProduto);
            imageViewProduto.setVisibility(View.VISIBLE);
        }

        //Desabilitar campos que o usuário não poderá editar
        editTextCodList.setEnabled(false);
        editTextDesc.setEnabled(false);
        imgButtonLer.setVisibility(view.INVISIBLE);
    }

    //Inicializa o Scanner
    private void scanCode() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(AddProdutosFragmentLista.this);
        integrator.setBeepEnabled(false);
        integrator.setCaptureActivity(CaptureAct.class);
        integrator.setOrientationLocked(false);
        integrator.setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES);
        integrator.setPrompt("ESCANEANDO");
        integrator.initiateScan();
    }

    //Obtem e trata os resultados obtidos através do Scanner
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data){
        IntentResult result = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if(result != null){
            if(result.getContents() != null){

                scannerResult = result.getContents();
                editTextCodList.setText(scannerResult);
                editTextCodList.setEnabled(false);
                buscarAtributos();
            }
            else{
                Toast.makeText(view.getContext(), "Sem resultado", Toast.LENGTH_LONG).show();
            }
        }else{
            super.onActivityResult(requestCode, resultCode, data);
        }
    }

    //Método para buscar atributos do produto com base no código digitado
    public void buscarAtributos(){
        RequestQueue queue = Volley.newRequestQueue(view.getContext());
        String url = "https://api.cosmos.bluesoft.com.br/gtins/" + scannerResult + ".json";

        JsonObjectRequest obreq = new JsonObjectRequest(Request.Method.GET, url, null,
                // The third parameter Listener overrides the method onResponse() and passes
                //JSONObject as a parameter
                new Response.Listener<JSONObject>() {
                    // Takes the response from the JSON request
                    @Override
                    public void onResponse(JSONObject retorno) {

                        try {
                            //Passar os atributos do JSon para variáveis
                            cod = retorno.get("gtin").toString();
                            desc = retorno.get("description").toString();
                            imgUrl = retorno.get("thumbnail").toString();

                            //Pegar atributos do JsonArray
                            String typePackaging = "";
                            JSONArray gtins = retorno.getJSONArray("gtins");
                            for(int i = 0; i < gtins.length(); i++) {
                                JSONObject commercialUnit = gtins.getJSONObject(i);
                                typePackaging = commercialUnit.optString("commercial_unit", "");
                            }

                            //Converter String para JsonArray
                            JsonParser jsonParser = new JsonParser();
                            JsonObject jsonObject = jsonParser.parse(typePackaging).getAsJsonObject();
                            String unit = jsonObject.get("type_packaging")
                                    .toString().replace("\"", "");//

                            //Exibir os atributos dos produtos
                            editTextDesc.setText(desc);
                            editTextDesc.setEnabled(false);
                            editTextCodList.setEnabled(false);
                            Glide.with(view.getContext()).load(imgUrl).into(imageViewProduto);
                            imageViewProduto.setVisibility(View.VISIBLE);

                        }
                        // Try and catch are included to handle any errors due to JSON
                        catch (JSONException e) {
                            // If an error occurs, this prints the error to the log
                            e.printStackTrace();
                        }
                    }

                },
                // The final parameter overrides the method onErrorResponse() and passes VolleyError
                //as a parameter
                new Response.ErrorListener() {
                    @Override
                    // Handles errors that occur due to Volley
                    public void onErrorResponse(VolleyError error) {
                        error.printStackTrace();
                    }
                }
        ){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String>  params = new HashMap<String, String>();
                params.put("X-Cosmos-Token", TOKEN);
                return params;
            }
        };
        // Adds the JSON object request "obreq" to the request queue
        queue.add(obreq);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        view = null;
    }
}