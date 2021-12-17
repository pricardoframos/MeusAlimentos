package com.example.projetoalimentostcc.ui.addProdutos;


import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.bumptech.glide.Glide;

import com.example.projetoalimentostcc.ui.casa.CasaFragment;
import com.example.projetoalimentostcc.ui.classes.AlterFragment;
import com.example.projetoalimentostcc.ui.classes.Despensa;
import com.example.projetoalimentostcc.ui.classes.Produto;
import com.example.projetoalimentostcc.ui.classes.ProdutoXDespensa;
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
import com.example.projetoalimentostcc.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.nio.charset.StandardCharsets;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AddProdutosFragmentDespensa extends Fragment {
    //Declaração dos elementos do fragment
    private View view;
    private Spinner spinnerCategoria, spinnerMedida, spinnerDespensa;
    private ImageButton imgButtonLer;
    private EditText editTextCod, editTextDesc, editTextQuant, editTextDate;
    private ImageView imageViewProduto, imageViewCalend;
    private Button buttonAdicionar;
    TextView textViewData;
    DatePickerDialog.OnDateSetListener setListener;

    //Declaração de variáveis
    private static String scannerResult;
    private final String TOKEN = "SzqFhlzVZEjHHt1i2wFBDA";
    private String desc;
    private String imgUrl;
    private String cod;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_add_produtos_despensa, container, false);

        //Referência dos elementos do fragment
        editTextCod = view.findViewById(R.id.editTextCod);
        editTextDesc = view.findViewById(R.id.editTextDesc);
        textViewData = view.findViewById(R.id.textViewData);
        editTextQuant = view.findViewById(R.id.editTextQuant);
        spinnerCategoria = view.findViewById(R.id.spinnerCategoria);
        spinnerMedida = view.findViewById(R.id.spinnerMedida);
        spinnerDespensa = view.findViewById(R.id.spinnerDespensa);
        imgButtonLer = view.findViewById(R.id.imgButtonLer);
        imageViewProduto = view.findViewById(R.id.imageViewProduto);
        buttonAdicionar = view.findViewById(R.id.buttonAdicionar);
//Calendário
        Calendar calendar = Calendar.getInstance();
        final int year = calendar.get(Calendar.YEAR);
        final int month = calendar.get(Calendar.MONTH);
        final int day = calendar.get(Calendar.DAY_OF_MONTH);
        imageViewCalend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(),
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth
                        , setListener,
                        year,
                        month,
                        day);
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.show();
            }
        });
        setListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                month = month+1;
                String date = day+"/"+month+"/"+year;
                textViewData.setText(date);
            }
        };
        imageViewCalend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        getContext(), new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        month = month+1;
                        String date = day+"/"+month+"/"+year;
                        textViewData.setText(date);
                    }
                }, year,month,day);
                datePickerDialog.show();
            }
        });


        //Botão Scanner/Add
        imgButtonLer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                scanCode();
            }
        });

        //ABRIR PRODUTO PARA EDIÇÃO CASO ESTE FRAGMENTE SEJA CHAMADO POR UM ITEM DO RECYCLERVIEW
        if(AlterFragment.fragmentoOrigemDestino == "RecycleViewCasa-AddProdutosFragmentDespensa"){
            abrirProdutoRecycler();
        }

        //Botão Adicionar
        buttonAdicionar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(AlterFragment.fragmentoOrigemDestino == "RecycleViewCasa-AddProdutosFragmentDespensa"){
                    atualizarProduto();
                    atualizarProdutoXDespensa();
                    AlterFragment.fragmentoOrigemDestino = null;
                    retornarFragment();
                }else{
                    //Validar se o campo Descrição está preenchido
                    if(!editTextDesc.getText().toString().isEmpty()){
                        //Validar se o produto a ser adicionado na despensa já está cadastrado no banco
                        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
                        List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
                        Log.d("Success","AddProdutosFragmentDespensa.buttonAdicionar >>> Criou listaProduto");

                        if(listaProduto.size() > 0){
                            Log.d("Success","AddProdutosFragmentDespensa.buttonAdicionar >>> " +
                                    "Produto encontrado");
                            Log.d("Success","AddProdutosFragmentDespensa.buttonAdicionar >>> " +
                                    "Método cadastrarProdutoXDespensa acionado");
                            cadastrarProdutoXDespensa();

                            Log.d("Success","AddProdutosFragmentDespensa.buttonAdicionar >>> " +
                                    "Método retornarFragment acionado");
                            retornarFragment();
                        }else{
                            Log.d("Success","AddProdutosFragmentDespensa.buttonAdicionar >>> " +
                                    "Produto não encontrado");
                            Log.d("Success","AddProdutosFragmentDespensa.buttonAdicionar >>> " +
                                    "Método cadastrarProdutoXDespensa acionado");
                            cadastrarProduto();

                            Log.d("Success","AddProdutosFragmentDespensa.buttonAdicionar >>> " +
                                    "Método cadastrarProdutoXDespensa acionado");
                            cadastrarProdutoXDespensa();

                            Log.d("Success","AddProdutosFragmentDespensa.buttonAdicionar >>> " +
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
                        Log.d("Success","AddProdutoFragmentDespensa.editTextDesc.onFocusChange >>> " +
                                "spinnerCategoria alterado com sucesso");
                        spinnerMedida.setSelection(atribuirSpinnerUnidade(lista.get(0).getUnidade()));
                        Log.d("Success","AddProdutoFragmentDespensa.editTextDesc.onFocusChange >>> " +
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
                new CasaFragment()).addToBackStack("Casa").commit();
    }

    //Cadastrar PRODUTOXDESPENSA
    private void cadastrarProdutoXDespensa(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
        Log.d("Success","AddProdutosFragmentDespensa.cadastrarProdutoXDespensa >>> Criou listaProduto");
        List<Despensa> listaDespensa = bancoDeDados.buscaDescricaoDespensa(spinnerDespensa.getSelectedItem().toString());
        Log.d("Success","AddProdutosFragmentDespensa.cadastrarProdutoXDespensa >>> Criou listaDespensa");

        //Variáveis para validação de elementos
        double quantidade;
        String data;

        //Validação dos elementos
        if(editTextQuant.getText().toString().isEmpty()){
            quantidade = 1;
        }else {
            quantidade = Double.parseDouble(editTextQuant.getText().toString());
        }
        if(editTextDate.getText().toString().isEmpty()){
            data = "01012001";
        }else{
            data = editTextDate.getText().toString().replace("/","");
        }

        try {
            bancoDeDados.cadastrarProdutoXDespensa(listaProduto.get(0).getId(),
                    quantidade,data,
                    listaDespensa.get(0).getId());
        }catch(Exception e){
            Log.d("Success","Add.ProdutosFragment.cadastrarProdutoXDespensa >>> "+ e);
        }
    }

    //Cadastrar PRODUTO
    private void cadastrarProduto(){
        //Instanciação da classe BancoDeDados
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);

        //Validação de elemento
        if(editTextCod.getText().toString() == null){
            cod = "";
        }else{
            cod = editTextCod.getText().toString();
        }
        try {
            //Validar se o produto ainda não está cadastrado no banco
            //Verificar, via descrição, se o produto existe no banco de dados
            List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
            if(listaProduto.size()<1){
                Log.d("Success","AddProdutosFragmentDespensa.cadastrarProduto >>> " +
                        "Produto ainda não cadastrado");
                if(!editTextDesc.getText().toString().isEmpty()){
                    bancoDeDados.cadastrarProduto(cod,
                            editTextDesc.getText().toString().trim(),
                            spinnerCategoria.getSelectedItem().toString(),
                            spinnerMedida.getSelectedItem().toString(),
                            imgUrl);
                    Log.d("Success", "Código: "+editTextCod.getText().toString());
                    Log.d("Success", "Descrição: "+editTextDesc.getText().toString());
                    Log.d("Success", "Categoria: "+spinnerCategoria.getSelectedItem().toString());
                    Log.d("Success", "Uni. de medida: "+spinnerMedida.getSelectedItem().toString());
                }else{
                    Log.d("Success","AddProdutosFragmentDespensa.cadastrarProduto >>> " +
                            "A descrição do produto não foi inserida");
                }
            }else{//Caso o produto já esteja cadastrado no banco
                Log.d("Success","AddProdutosFragmentDespensa.cadastrarProduto >>> " +
                        "Produto " + listaProduto.get(0).getDescricao() + " já cadastrado");
            }

        }catch (Exception e){
            Log.d("Success", "AddProdutoFragmentDespensa.cadastrarProduto >>> " +
                    "AddProdutoFragmentDespensa.cadastrarProduto.Execpetion 1 ->" + e.toString());
        }
    }

    //Atualizar PRODUTOXDESPENSA
    private void atualizarProdutoXDespensa(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);
        try{
            List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
            Log.d("Success","AddProdutosFragmentDespensa.atualizarProdutoXDespensa >>> Criou listaProduto");
            List<ProdutoXDespensa> listaProdutoXDespensa = bancoDeDados.buscaIdProdutoXDespensa(AlterFragment.indiceRecyclerViewCasa);
            Log.d("Success","AddProdutosFragmentDespensa.atualizarProdutoXDespensa >>> Criou listaProdutoXDespensa");
            List<Despensa> listaDespensa = bancoDeDados.buscaDescricaoDespensa(spinnerDespensa.getSelectedItem().toString());
            Log.d("Success","AddProdutosFragmentDespensa.atualizarProdutoXDespensa >>> Criou listaDespensa");


            //Variáveis para validação de elementos
            double quantidade;
            String data;

            //Validação dos elementos
            if(editTextQuant.getText().toString().isEmpty()){
                quantidade = 1;
            }else {
                quantidade = Double.parseDouble(editTextQuant.getText().toString());
            }
            if(editTextDate.getText().toString().isEmpty()){
                data = "01012001";
            }else{
                data = editTextDate.getText().toString();
            }

            Log.d("Success","AddProdutosFragmentDespensa.atualizarProdutoXDespensa >>> Validações feitas");

            //int idReg, int idProd, double quant, int valid, int idDesp

            if(bancoDeDados.atualizarProdutoXDespensa(listaProdutoXDespensa.get(0).getIdRegistro(),
                    listaProduto.get(0).getId(),
                    quantidade,data,
                    listaDespensa.get(0).getId()) == true){
                Log.d("Success","AddProdutosFragmentDespensa.atualizarProdutoXDespensa >>> " +
                        "ProdutoXDespensa atualizado com sucesso");
                Toast.makeText(getContext(), "Produto atualizado com sucesso", Toast.LENGTH_SHORT).show();
            }else{
                Log.d("Success","AddProdutosFragmentDespensa.atualizarProdutoXDespensa >>> " +
                        "Não foi possível atualizar ProdutoXDespensa");
                Toast.makeText(getContext(), "Não foi possível atualizar o produto", Toast.LENGTH_SHORT).show();
            }
        }catch(Exception e){
            Log.d("Success","Add.ProdutosFragment.atualizarProdutoXDespensa >>> "+ e);
        }
    }

    //Atualizar PRODUTO
    private void atualizarProduto(){
        //Instanciação da classe BancoDeDados
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);

        //Validação de elemento
        if(editTextCod.getText().toString() == null){
            cod = "";
        }else{
            cod = editTextCod.getText().toString();
        }
        try {
            //Validar se o produto ainda não está cadastrado no banco
            //Verificar, via descrição, se o produto existe no banco de dados
            List<Produto> listaProduto = bancoDeDados.buscaDescricaoProduto(editTextDesc.getText().toString().trim());
            Log.d("Success","AddProdutosFramentDespensa.atualizarProduto >>> " +
                    "Lista criada. Id do produto: "+ listaProduto.get(0).getId());

            if(bancoDeDados.atualizarProduto(listaProduto.get(0).getId(),
                    cod,
                    editTextDesc.getText().toString(),
                    spinnerCategoria.getSelectedItem().toString(),
                    spinnerMedida.getSelectedItem().toString(),
                    bancoDeDados.buscaIdProduto(listaProduto.get(0).getId()).get(0).getUrlDaImagem())==true){
                Log.d("Success", "Código: "+editTextCod.getText().toString());
                Log.d("Success", "Descrição: "+editTextDesc.getText().toString());
                Log.d("Success", "Categoria: "+spinnerCategoria.getSelectedItem().toString());
                Log.d("Success", "Uni. de medida: "+spinnerMedida.getSelectedItem().toString());
                Log.d("Success","AddProdutosFramentDespensa.atualizarProduto >>> " +
                        "Produto atualizado com sucesso");
            }

        }catch (Exception e){
            Log.d("Success", "AddProdutoFragmentDespensa.atualizarProduto >>> " +
                    "AddProdutoFragmentDespensa.atualizarProduto.Execpetion 1 ->" + e.toString());
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

    //Atribuir valor ao spinnerDespensa
    private int atribuirSpinnerDespensa(String despensa){
        int indice = 0;
        switch (despensa){
            case "Armário":
                indice = 0;
                break;
            case "Geladeira":
                indice = 1;
                break;
        }
        return indice;
    }

    private void abrirProdutoRecycler(){
        BancoDeDados bancoDeDados = new BancoDeDados(getContext(),1);

        buttonAdicionar.setText("Atualizar");
        editTextCod.setText(bancoDeDados.buscaIdProduto(bancoDeDados.buscaIdProdutoXDespensa
                (AlterFragment.indiceRecyclerViewCasa).get(0).getIdProduto()).get(0).getCodigo());
        Log.d("Success","Add.ProdutosFragmentDespensa.abrirProdutoRecycler >>> " +
                "Codigo captado e inserido. IdRegistro: " + AlterFragment.indiceRecyclerViewCasa);

        editTextDesc.setText(bancoDeDados.buscaIdProduto(bancoDeDados.buscaIdProdutoXDespensa
                (AlterFragment.indiceRecyclerViewCasa).get(0).getIdProduto()).get(0).getDescricao());
        Log.d("Success","Add.ProdutosFragmentDespensa.abrirProdutoRecycler >>> " +
                "Descrição captada e inserida. IdRegistro: " + AlterFragment.indiceRecyclerViewCasa);

        spinnerCategoria.setSelection(atribuirSpinnerCategoria(bancoDeDados.buscaIdProduto
                (bancoDeDados.buscaIdProdutoXDespensa(AlterFragment.indiceRecyclerViewCasa).
                        get(0).getIdProduto()).get(0).getCategoria()));
        Log.d("Success","Add.ProdutosFragmentDespensa.abrirProdutoRecycler >>> " +
                "Categoria captada e inserida. IdRegistro: " + AlterFragment.indiceRecyclerViewCasa);

        spinnerDespensa.setSelection(atribuirSpinnerDespensa(bancoDeDados.buscaIdDespensa
                (bancoDeDados.buscaIdProdutoXDespensa(AlterFragment.indiceRecyclerViewCasa).
                        get(0).getIdDespensa()).get(0).getDescricao()));
        Log.d("Success","Add.ProdutosFragmentDespensa.abrirProdutoRecycler >>> " +
                "Despensa captada e inserida. IdRegistro: " + AlterFragment.indiceRecyclerViewCasa);

        editTextQuant.setText(String.valueOf(bancoDeDados.buscaIdProdutoXDespensa
                (AlterFragment.indiceRecyclerViewCasa).get(0).getEstoqueProduto()));
        Log.d("Success","Add.ProdutosFragmentDespensa.abrirProdutoRecycler >>> " +
                "Quantidade captada e inserida");

        spinnerMedida.setSelection(atribuirSpinnerUnidade(bancoDeDados.buscaIdProduto
                (bancoDeDados.buscaIdProdutoXDespensa(AlterFragment.indiceRecyclerViewCasa).
                        get(0).getIdProduto()).get(0).getUnidade()));
        Log.d("Success","Add.ProdutosFragmentDespensa.abrirProdutoRecycler >>> " +
                "Unidade de medida captada e inserida");

        editTextDate.setText(String.valueOf(bancoDeDados.buscaIdProdutoXDespensa
                (AlterFragment.indiceRecyclerViewCasa).get(0).getValidProduto()));
        Log.d("Success","Add.ProdutosFragmentDespensa.abrirProdutoRecycler >>> " +
                "Data captada e inserida");

        if(bancoDeDados.buscaIdProduto(bancoDeDados.buscaIdProdutoXDespensa
                (AlterFragment.indiceRecyclerViewCasa).get(0).getIdProduto()).get(0).getUrlDaImagem()!=null){
            Glide.with(view.getContext()).load(bancoDeDados.buscaIdProduto(bancoDeDados
                    .buscaIdProdutoXDespensa(AlterFragment.indiceRecyclerViewCasa).get(0)
                    .getIdProduto()).get(0).getUrlDaImagem()).into(imageViewProduto);
            imageViewProduto.setVisibility(View.VISIBLE);
        }

        //Desabilitar campos que o usuário não poderá editar
        editTextCod.setEnabled(false);
        editTextDesc.setEnabled(false);
        imgButtonLer.setVisibility(view.INVISIBLE);
    }

    //Inicializa o Scanner
    private void scanCode() {
        IntentIntegrator integrator = IntentIntegrator.forSupportFragment(AddProdutosFragmentDespensa.this);
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
                editTextCod = view.findViewById(R.id.editTextCod);
                scannerResult = result.getContents();
                editTextCod.setText(scannerResult);
                editTextCod.setEnabled(false);
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
                            editTextCod.setEnabled(false);
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