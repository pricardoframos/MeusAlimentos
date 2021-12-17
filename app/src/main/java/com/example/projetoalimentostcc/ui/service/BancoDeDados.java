package com.example.projetoalimentostcc.ui.service;

import static java.lang.String.valueOf;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;
import android.text.Editable;
import android.util.Log;

import androidx.annotation.Nullable;

import com.example.projetoalimentostcc.ui.classes.*;

import java.util.ArrayList;
import java.util.List;

public class BancoDeDados extends SQLiteOpenHelper {
    //STRINGS PARA CRIAÇÃO DAS TABELAS
    //Tabela PRODUTOXDESPENSA
    private final String criaTabelaPRODUTOXDESPENSA = "CREATE TABLE PRODUTOXDESPENSA (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ID_PRODUTO INTEGER NOT NULL, " +
            "ESTOQUE_PRODUTO DOUBLE, " +
            "VALID_PRODUTO VARCHAR(10), " +
            "ID_DESPENSA INTEGER NOT NULL," +
            "CONSTRAINT CHAVE_PRODUTO " +
            "FOREIGN KEY (ID_PRODUTO) " +
            "REFERENCES PRODUTO(_ID)," +
            "CONSTRAINT CHAVE_DESPENSA " +
            "FOREIGN KEY (ID_DESPENSA)" +
            "REFERENCES DESPENSA(_ID));";

    //Tabela PRODUTOXLISTA
    private final String criaTabelaPRODUTOXLISTA = "CREATE TABLE PRODUTOXLISTA (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT," +
            "ID_PRODUTO INTEGER NOT NULL, " +
            "QUANTIDADE DOUBLE, " +
            "PRECO DOUBLE, " +
            "ID_LISTA INTEGER NOT NULL," +
            "STATUS INTEGER NOT NULL," +
            "CONSTRAINT CHAVE_PRODUTO " +
            "FOREIGN KEY (ID_PRODUTO) " +
            "REFERENCES PRODUTO(_ID)," +
            "CONSTRAINT CHAVE_LISTA " +
            "FOREIGN KEY (ID_LISTA)" +
            "REFERENCES LISTA(_ID));";

    //Tabela PRODUTOXCOMPRA
    private final String criaTabelaPRODUTOXCOMPRA = "CREATE TABLE PRODUTOXCOMPRA (" +
            "ID_PRODUTO INTEGER NOT NULL, " +
            "QUANTIDADE DOUBLE, " +
            "PRECO DOUBLE, " +
            "ID_COMPRA INTEGER NOT NULL," +
            "CONSTRAINT CHAVE_PRODUTO " +
            "FOREIGN KEY (ID_PRODUTO) " +
            "REFERENCES PRODUTO(_ID)," +
            "CONSTRAINT CHAVE_COMPRA " +
            "FOREIGN KEY (ID_COMPRA)" +
            "REFERENCES COMPRA(_ID));";

    //Tabela PRODUTO
    private final String criaTabelaPRODUTO = "CREATE TABLE PRODUTO (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "CODIGO VARCHAR (15), " +
            "DESCRICAO VARCHAR (60) NOT NULL UNIQUE, " +
            "CATEGORIA VARCHAR (20), " +
            "UNIDADE VARCHAR (10), " +
            "IMAGEM TEXT);";

    //Tabela USUARIO
//    private final String criaTabelaUSUARIO = "CREATE TABLE USUARIO (" +
//            "_ID VARCHAR (10) PRIMARY KEY AUTOINCREMENT, " +
//            "CODIGO INTEGER, " +
//            "NOME VARCHAR (60) NOT NULL, " +
//            "EMAIL VARCHAR (80) NOT NULL UNIQUE, " +
//            "SENHA VARCHAR (10) NOT NULL, " +
//            "ID_DISPENSA INTEGER NOT NULL," +
//            "CONSTRAINT CHAVE_DESPENSA " +
//            "FOREIGN KEY (ID_DESPENSA) " +
//            "REFERENCES DESPENSA(_ID));";

    //Tabela DESPENSA
    private final String criaTabelaDESPENSA = "CREATE TABLE DESPENSA (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "DESCRICAO VARCHAR (60) NOT NULL UNIQUE);";

    //Tabela LISTA
    private final String criaTabelaLISTA = "CREATE TABLE LISTA (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "DESCRICAO VARCHAR (60) NOT NULL UNIQUE);";


    //Tabela COMPRA
    private final String criaTabelaCOMPRA = "CREATE TABLE COMPRA (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "DATA INTEGER NOT NULL, " +
            "ID_SUPERMERCADO INTEGER, " +
            "CONSTRAINT CHAVE_SUPERMERCADO " +
            "FOREIGN KEY (ID_SUPERMERCADO) " +
            "REFERENCES SUPERMERCADO(ID_SUPERMERCADO));";

    //Tabela SUPERMERCADO
    private final String criaTabelaSUPERMERCADO = "CREATE TABLE SUPERMERCADO (" +
            "_ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
            "NOME VARCHAR (60) NOT NULL, " +
            "BAIRRO VARCHAR (60) NOT NULL);";

    public BancoDeDados(@Nullable Context context, int version) {
        super(context, "BANCO_APP", null, version);
    }

    @Override
    public void onConfigure(SQLiteDatabase db) {
        super.onConfigure(db);
        //O método do onConfigure é executado antes do onCreate
        //Usamos o onConfigure para habilitar o uso de chave estrangeira
        if(Build.VERSION.SDK_INT <= Build.VERSION_CODES.JELLY_BEAN){
            //Se a versão do Android do dispositivo for a 4.1 ou anterior
            db.setForeignKeyConstraintsEnabled(true);
        }else{
            //Se for versões após a 4.1
            db.execSQL("PRAGMA foreign_keys=ON");
        }
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(criaTabelaPRODUTO);
        //sqLiteDatabase.execSQL(criaTabelaUSUARIO);
        sqLiteDatabase.execSQL(criaTabelaDESPENSA);
        sqLiteDatabase.execSQL(criaTabelaLISTA);
        sqLiteDatabase.execSQL(criaTabelaCOMPRA);
        sqLiteDatabase.execSQL(criaTabelaSUPERMERCADO);
        sqLiteDatabase.execSQL(criaTabelaPRODUTOXDESPENSA);
        sqLiteDatabase.execSQL(criaTabelaPRODUTOXCOMPRA);
        sqLiteDatabase.execSQL(criaTabelaPRODUTOXLISTA);
    }

    //---------------------------MÉTODOS DE CADASTRO------------------------------------
    //Método para cadastrar um novo produto na tabela PRODUTOXDESPENSA (INSERT)
    public boolean cadastrarProdutoXDespensa(int id_produto, double estoque_produto, String valid_produto,
                                             int id_despensa){
        //Abrir a conexão com o banco de dados
        SQLiteDatabase conexao = getWritableDatabase();
        Log.d("Success", "BancoDeDados.cadastrarProdutoXDespensa >>> Conexão efetuada com sucesso");

        //Utilizar a classe ContentValues para passar o valor de cada coluna
        ContentValues valores = new ContentValues();
        valores.put("ID_PRODUTO", id_produto);
        if(estoque_produto != 0.0){
            valores.put("ESTOQUE_PRODUTO", estoque_produto);
        }

        valores.put("VALID_PRODUTO", valid_produto);
        valores.put("ID_DESPENSA", id_despensa);
        Log.d("Success", "BancoDeDados.cadastrarProdutoXDespensa >>> Valores inseridos com sucesso no Content valores");

        //Executar o comando INSERT já verificando com IF o retorno
        if(conexao.insert("PRODUTOXDESPENSA", null, valores) != -1){
            //O método insert() retorna -1 se caso ocorreu algum erro, se
            //estive diferente de -1 então o INSERT executou corretamente
            Log.d("Success","BancoDeDados.cadastrarProdutoXDespensa >>> Valores inseridos na tabela");
            return true;
        }else{
            Log.d("Success", "BancoDeDados.cadastrarProdutoXDespensa >>> Não foi possível inserir os valores na tabela");
            return false;
        }
    }

    //Método para cadastrar um novo produto na tabela PRODUTOXLISTA (INSERT)
    public boolean cadastrarProdutoXLista(int id_produto, double quantidade, double preco,
                                          int id_lista, int stat){
        //Abrir a conexão com o banco de dados
        SQLiteDatabase conexao = getWritableDatabase();
        Log.d("Success", "BancoDeDados.cadastrarProdutoXLista >>> Conexão efetuada com sucesso");

        //Utilizar a classe ContentValues para passar o valor de cada coluna
        ContentValues valores = new ContentValues();
        valores.put("ID_PRODUTO", id_produto);
        if(quantidade != 0.0){
            valores.put("QUANTIDADE", quantidade);
        }
        valores.put("PRECO", preco);
        valores.put("ID_LISTA", id_lista);
        valores.put("STATUS", stat);
        Log.d("Success", "BancoDeDados.cadastrarProdutoXLista >>> Valores inseridos com sucesso no Content valores");

        //Executar o comando INSERT já verificando com IF o retorno
        if(conexao.insert("PRODUTOXLISTA", null, valores) != -1){
            //O método insert() retorna -1 se caso ocorreu algum erro, se
            //estive diferente de -1 então o INSERT executou corretamente
            Log.d("Success","BancoDeDados.cadastrarProdutoXLista >>> Valores inseridos na tabela");
            return true;
        }else{
            Log.d("Success", "BancoDeDados.cadastrarProdutoXLista >>> Não foi possível inserir os valores na tabela");
            return false;
        }
    }

    //Método para cadastrar um novo produto na tabela PRODUTOXCOMPRA (INSERT)
    public boolean cadastrarProdutoXCompra(int id_produto, double quantidade, double preco,
                                           int id_compra){
        //Abrir a conexão com o banco de dados
        SQLiteDatabase conexao = getWritableDatabase();
        Log.d("Success", "BancoDeDados.cadastrarProdutoXCompra >>> Conexão efetuada com sucesso");

        //Utilizar a classe ContentValues para passar o valor de cada coluna
        ContentValues valores = new ContentValues();
        valores.put("ID_PRODUTO", id_produto);
        valores.put("QUANTIDADE", quantidade); //nome_da_coluna, valor_a_inserir
        valores.put("PRECO", preco);
        valores.put("ID_COMPRA", id_compra);
        Log.d("Success", "BancoDeDados.cadastrarProdutoXCompra >>> Valores inseridos com sucesso no Content valores");

        //Executar o comando INSERT já verificando com IF o retorno
        if(conexao.insert("PRODUTOXCOMPRA", null, valores) != -1){
            //O método insert() retorna -1 se caso ocorreu algum erro, se
            //estive diferente de -1 então o INSERT executou corretamente
            Log.d("Success","BancoDeDados.cadastrarProdutoXCompra >>> Valores inseridos na tabela");
            return true;
        }else{
            Log.d("Success", "BancoDeDados.cadastrarProdutoXCompra >>> Não foi possível inserir os valores na tabela");
            return false;
        }
    }

    //Método para cadastrar um novo produto na tabela PRODUTO (INSERT)
    public boolean cadastrarProduto(String codigo, String descricao, String categoria,
                                    String unidade, String urlDaImagem){
        //Abrir a conexão com o banco de dados
        SQLiteDatabase conexao = getWritableDatabase();
        Log.d("Success", "BancoDeDados.cadastrarProduto >>> Conexão efetuada com sucesso");

        //Utilizar a classe ContentValues para passar o valor de cada coluna
        ContentValues valores = new ContentValues();
        valores.put("CODIGO", codigo);
        valores.put("DESCRICAO", descricao); //nome_da_coluna, valor_a_inserir
        valores.put("CATEGORIA", categoria);
        valores.put("UNIDADE", unidade);
        valores.put("IMAGEM", urlDaImagem);
        Log.d("Success", "BancoDeDados.cadastrarProduto >>> Valores inseridos com sucesso no Content valores");

        //Executar o comando INSERT já verificando com IF o retorno
        if(conexao.insert("PRODUTO", null, valores) != -1){
            //O método insert() retorna -1 se caso ocorreu algum erro, se
            //estive diferente de -1 então o INSERT executou corretamente
            Log.d("Success","BancoDeDados.cadastrarProduto >>> Valores inseridos na tabela");
            return true;
        }else{
            Log.d("Success", "BancoDeDados.cadastrarProduto >>> Não foi possível inserir os valores na tabela");
            return false;
        }
    }

    //Método para cadastrar um novo produto na tabela DESPENSA (INSERT)
    public boolean cadastrarDespensa(String descricao){
        //Abrir a conexão com o banco de dados
        SQLiteDatabase conexao = getWritableDatabase();
        Log.d("Success", "BancoDeDados.cadastrarDespensa >>> Conexão efetuada com sucesso");

        //Utilizar a classe ContentValues para passar o valor de cada coluna
        ContentValues valores = new ContentValues();
        valores.put("DESCRICAO", descricao);

        Log.d("Success", "BancoDeDados.cadastrarDespensa >>> Valores inseridos com sucesso no Content valores");

        //Executar o comando INSERT já verificando com IF o retorno
        if(conexao.insert("DESPENSA", null, valores) != -1){
            //O método insert() retorna -1 se caso ocorreu algum erro, se
            //estive diferente de -1 então o INSERT executou corretamente
            Log.d("Success","BancoDeDados.cadastrarDespensa >>> Valores inseridos na tabela");
            return true;
        }else{
            Log.d("Success", "BancoDeDados.cadastrarDespensa >>> Não foi possível inserir os valores na tabela");
            return false;
        }
    }

    //Método para cadastrar um novo produto na tabela LISTA (INSERT)
    public boolean cadastrarLista(String descricao){
        //Abrir a conexão com o banco de dados
        SQLiteDatabase conexao = getWritableDatabase();
        Log.d("Success", "BancoDeDados.cadastrarLista >>> Conexão efetuada com sucesso");

        //Utilizar a classe ContentValues para passar o valor de cada coluna
        ContentValues valores = new ContentValues();

        valores.put("DESCRICAO", descricao);

        Log.d("Success", "BancoDeDados.cadastrarLista >>> Valores inseridos com sucesso no Content valores");

        //Executar o comando INSERT já verificando com IF o retorno
        if(conexao.insert("LISTA", null, valores) != -1){
            //O método insert() retorna -1 se caso ocorreu algum erro, se
            //estive diferente de -1 então o INSERT executou corretamente
            Log.d("Success","BancoDeDados.cadastrarLista >>> Valores inseridos na tabela");
            return true;
        }else{
            Log.d("Success", "BancoDeDados.cadastrarLista >>> Não foi possível inserir os valores na tabela");
            return false;
        }
    }

    //Método para cadastrar um novo produto na tabela COMPRA (INSERT)
    public boolean cadastrarCompra(int data, int id_supermercado){
        //Abrir a conexão com o banco de dados
        SQLiteDatabase conexao = getWritableDatabase();
        Log.d("Success", "BancoDeDados.cadastrarCompra >>> Conexão efetuada com sucesso");

        //Utilizar a classe ContentValues para passar o valor de cada coluna
        ContentValues valores = new ContentValues();
        valores.put("DATA", data);
        valores.put("ID_SUPERMERCADO", id_supermercado);
        Log.d("Success", "BancoDeDados.cadastrarCompra >>> Valores inseridos com sucesso no Content valores");




        //Executar o comando INSERT já verificando com IF o retorno
        if(conexao.insert("COMPRA", null, valores) != -1){
            //O método insert() retorna -1 se caso ocorreu algum erro, se
            //estive diferente de -1 então o INSERT executou corretamente
            Log.d("Success","BancoDeDados.cadastrarCompra >>> Valores inseridos na tabela");
            return true;
        }else{
            Log.d("Success", "BancoDeDados.cadastrarCompra >>> Não foi possível inserir os valores na tabela");
            return false;
        }
    }

    //Método para cadastrar um novo produto na tabela SUPERMERCADO (INSERT)
    public boolean cadastrarSupermercado(String nome, String bairro){
        //Abrir a conexão com o banco de dados
        SQLiteDatabase conexao = getWritableDatabase();
        Log.d("Success", "BancoDeDados.cadastrarSupermercado >>> Conexão efetuada com sucesso");

        //Utilizar a classe ContentValues para passar o valor de cada coluna
        ContentValues valores = new ContentValues();
        valores.put("NOME", nome);
        valores.put("BAIRRO", bairro);
        Log.d("Success", "BancoDeDados.cadastrarSupermercado >>> Valores inseridos com sucesso no Content valores");

        //Executar o comando INSERT já verificando com IF o retorno
        if(conexao.insert("SUPERMERCADO", null, valores) != -1){
            //O método insert() retorna -1 se caso ocorreu algum erro, se
            //estive diferente de -1 então o INSERT executou corretamente
            Log.d("Success","BancoDeDados.cadastrarSupermercado >>> Valores inseridos na tabela");
            return true;
        }else{
            Log.d("Success", "BancoDeDados.cadastrarSupermercado >>> Não foi possível inserir os valores na tabela");
            return false;
        }
    }

    //---------------------------MÉTODOS DE BUSCA------------------------------------
    //Método para buscar todos os produtos armazenados em despensas
    public List<ProdutoXDespensa> buscaProdutoXDespensa() {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("PRODUTOXDESPENSA", null,
                null,null, null, null, null);
        Log.d("Success", "BancoDeDados.buscaProdutoXDespensa >>> Query executada com sucesso");

        List<ProdutoXDespensa> lista = new ArrayList<ProdutoXDespensa>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaProdutoXDespensa >>> Registro encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int idRegistro = resultado.getInt(0); //Coluna de índice zero == coluna _id
                Log.d("Success", "idRegistro recuperado: " + idRegistro);
                int idProduto = resultado.getInt(1);
                Log.d("Success", "idProduto recuperado: " + idProduto);
                double estoqueProduto = resultado.getDouble(2);
                Log.d("Success", "estoqueProduto recuperado: " + estoqueProduto);
                String validProduto = resultado.getString(3);
                Log.d("Success", "validProduto recuperado: " + validProduto);
                int idDespensa = resultado.getInt(4);
                Log.d("Success", "idDespensa recuperado: " + idDespensa);

                Log.d("Success", "BancoDeDados.buscaProdutoXDespensa >>> " +
                        "Valores captados da tabela PRODUTOXDESPENSA");

                ProdutoXDespensa pXd = new ProdutoXDespensa(idRegistro, idProduto, estoqueProduto,
                        validProduto, idDespensa);
                Log.d("Success", "BancoDeDados.buscaProdutoXDespensa >>> " +
                        "Registro adicionado ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(pXd);
                Log.d("Success", "BancoDeDados.buscaProdutoXDespensa >>> " +
                        "Registro adicionado à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaProdutoXDespensa >>> " +
                    "Registro não encontrado");
        }
        Log.d("Success", "BancoDeDados.buscaProdutoXDespensa >>> Lista construída");
        return lista;
    }

    //Método para buscar o produto de acordo com o id
    public List<ProdutoXDespensa> buscaIdProdutoXDespensa(int idReg) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        Cursor resultado = conexao.query("PRODUTOXDESPENSA", null,
                "_ID = ?",
                new String[]{valueOf(idReg)}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaIdProdutoXDespensa >>> Query executada com sucesso");

        List<ProdutoXDespensa> lista = new ArrayList<ProdutoXDespensa>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaIdProdutoXDespensa >>> Registro encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int idRegistro = resultado.getInt(0); //Coluna de índice zero == coluna _id
                Log.d("Sucess", "idRegistro recuperado: " + idRegistro);
                int idProduto = resultado.getInt(1);
                Log.d("Sucess", "idProduto recuperado: " + idProduto);
                double estoqueProduto = resultado.getDouble(2);
                Log.d("Sucess", "estoqueProduto recuperado: " + estoqueProduto);
                String validProduto = resultado.getString(3);
                Log.d("Sucess", "validProduto recuperado: " + validProduto);
                int idDespensa = resultado.getInt(4);
                Log.d("Sucess", "idDespensa recuperado: " + idDespensa);
                Log.d("Success", "BancoDeDados.buscaIdProdutoXDespensa >>> " +
                        "Valores captados da tabela PRODUTOXDESPENSA");

                ProdutoXDespensa pXd = new ProdutoXDespensa(idRegistro, idProduto, estoqueProduto,
                        validProduto, idDespensa);
                Log.d("Success", "BancoDeDados.buscaIdProdutoXDespensa >>> Registro adicionado ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(pXd);
                Log.d("Success", "BancoDeDados.buscaIdProdutoXDespensa >>> Registro adicionado à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaIdProdutoXDespensa >>> Registro não encontrado");
        }
        Log.d("Success", "BancoDeDados.buscaIdProdutoXDespensa >>> Lista construída");
        return lista;
    }

    //Método para buscar todos os produtos armazenados em uma lista
    public List<ProdutoXLista> buscaProdutoXLista() {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("PRODUTOXLISTA", null,
                null,null, null, null, null);
        Log.d("Success", "BancoDeDados.buscaProdutoXLista >>> Query executada com sucesso");

        List<ProdutoXLista> lista = new ArrayList<ProdutoXLista>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaProdutoXLista >>> Registro encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int idRegistro = resultado.getInt(0); //Coluna de índice zero == coluna _id
                Log.d("Success", "idRegistro recuperado: " + idRegistro);
                int idProduto = resultado.getInt(1);
                Log.d("Success", "idProduto recuperado: " + idProduto);
                double quantidadeProduto = resultado.getDouble(2);
                Log.d("Success", "quantidadeProduto recuperado: " + quantidadeProduto);
                double precoProduto = resultado.getDouble(3);
                Log.d("Success", "precoProduto recuperado: " + precoProduto);
                int idLista = resultado.getInt(4);
                Log.d("Success", "idLista recuperado: " + idLista);
                int status = resultado.getInt(5);
                Log.d("Success", "status recuperado: " + status);

                Log.d("Success", "BancoDeDados.buscaProdutoXLista >>> " +
                        "Valores captados da tabela PRODUTOXLISTA");

                ProdutoXLista pXl = new ProdutoXLista(idRegistro, idProduto, quantidadeProduto,
                        precoProduto, idLista, status);
                Log.d("Success", "BancoDeDados.buscaProdutoXLista >>> " +
                        "Registro adicionado ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(pXl);
                Log.d("Success", "BancoDeDados.buscaProdutoXLista >>> " +
                        "Registro adicionado à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaProdutoXLista >>> " +
                    "Registro não encontrado");
        }
        Log.d("Success", "BancoDeDados.buscaProdutoXLista >>> Lista construída");
        return lista;
    }

    //Método para buscar o produto de acordo com o id
    public List<ProdutoXLista> buscaIdProdutoXLista(int idReg) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        Cursor resultado = conexao.query("PRODUTOXLISTA", null,
                "_ID = ?",
                new String[]{valueOf(idReg)}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaIdProdutoXLista >>> Query executada com sucesso");

        List<ProdutoXLista> lista = new ArrayList<ProdutoXLista>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaIdProdutoXLista >>> Registro encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int idRegistro = resultado.getInt(0); //Coluna de índice zero == coluna _id
                Log.d("Sucess", "idRegistro recuperado: " + idRegistro);
                int idProduto = resultado.getInt(1);
                Log.d("Sucess", "idProduto recuperado: " + idProduto);
                double quantidade = resultado.getDouble(2);
                Log.d("Sucess", "quantidade recuperado: " + quantidade);
                double precoProduto = resultado.getDouble(3);
                Log.d("Sucess", "precoProduto recuperado: " + precoProduto);
                int idLista = resultado.getInt(4);
                Log.d("Sucess", "idLista recuperado: " + idLista);
                int status = resultado.getInt(5);
                Log.d("Sucess", "status recuperado: " + status);

                Log.d("Success", "BancoDeDados.buscaIdProdutoXLista >>> " +
                        "Valores captados da tabela PRODUTOXLISTA");

                ProdutoXLista pXl = new ProdutoXLista(idRegistro, idProduto, quantidade,
                        precoProduto, idLista, status);
                Log.d("Success", "BancoDeDados.buscaIdProdutoXLista >>> Registro adicionado ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(pXl);
                Log.d("Success", "BancoDeDados.buscaIdProdutoXLista >>> Registro adicionado à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaIdProdutoXLista >>> Registro não encontrado");
        }
        Log.d("Success", "BancoDeDados.buscaIdProdutoXLista >>> Lista construída");
        return lista;
    }

    //Método para buscar o produto de acordo com a lista
    public List<ProdutoXLista> buscaListaProdutoXLista(int idList) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        Cursor resultado = conexao.query("PRODUTOXLISTA", null,
                "ID_LISTA = ?",
                new String[]{valueOf(idList)}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaListaProdutoXLista >>> Query executada com sucesso");

        List<ProdutoXLista> lista = new ArrayList<ProdutoXLista>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaListaProdutoXLista >>> Registro encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int idRegistro = resultado.getInt(0); //Coluna de índice zero == coluna _id
                Log.d("Sucess", "idRegistro recuperado: " + idRegistro);
                int idProduto = resultado.getInt(1);
                Log.d("Sucess", "idProduto recuperado: " + idProduto);
                double quantidade = resultado.getDouble(2);
                Log.d("Sucess", "quantidade recuperado: " + quantidade);
                double precoProduto = resultado.getDouble(3);
                Log.d("Sucess", "precoProduto recuperado: " + precoProduto);
                int idLista = resultado.getInt(4);
                Log.d("Sucess", "idLista recuperado: " + idLista);
                int status = resultado.getInt(5);
                Log.d("Sucess", "status recuperado: " + status);

                Log.d("Success", "BancoDeDados.buscaListaProdutoXLista >>> " +
                        "Valores captados da tabela PRODUTOXLISTA");

                ProdutoXLista pXl = new ProdutoXLista(idRegistro, idProduto, quantidade,
                        precoProduto, idLista, status);
                Log.d("Success", "BancoDeDados.buscaListaProdutoXLista >>> Registro adicionado ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(pXl);
                Log.d("Success", "BancoDeDados.buscaListaProdutoXLista >>> Registro adicionado à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaListaProdutoXLista >>> Registro não encontrado");
        }
        Log.d("Success", "BancoDeDados.buscaListaProdutoXLista >>> Lista construída");
        return lista;
    }

    //Método para buscar o produto de acordo com o id
    public List<Produto> buscaIdProduto(int idProd) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("PRODUTO", null,
                "_ID = ?",
                new String[]{valueOf(idProd)}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaIdProduto >>> Query executada com sucesso");

        List<Produto> lista = new ArrayList<Produto>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaIdProduto >>> Produto encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String codigo = resultado.getString(1);
                String descricao = resultado.getString(2);
                String categoria = resultado.getString(3);
                String unidade = resultado.getString(4);
                String imagem = resultado.getString(5);
                Log.d("Success", "BancoDeDados.buscaIdProduto >>> Valores captados da tabela PRODUTO");

                Produto p = new Produto(id, codigo, descricao, categoria, unidade, imagem);
                Log.d("Success", "BancoDeDados.buscaIdProduto >>> Produto adicionado ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(p);
                Log.d("Success", "BancoDeDados.buscaIdProduto >>> Produto adicionado à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaIdProduto >>> Produto não encontrado");
        }
        Log.d("Success", "BancoDeDados.buscaIdProduto >>> Lista construída");
        return lista;
    }

    //Método para buscar o produto de acordo com a descrição
    public List<Produto> buscaDescricaoProduto(String desc) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("PRODUTO", null,
                "DESCRICAO = ?",
                new String[]{desc}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaDescricaoProduto >>> Query executada com sucesso");

        List<Produto> lista = new ArrayList<Produto>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaDescricaoProduto >>> Produto encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String codigo = resultado.getString(1);
                String descricao = resultado.getString(2);
                String categoria = resultado.getString(3);
                String unidade = resultado.getString(4);
                String imagem = resultado.getString(5);
                Log.d("Success", "BancoDeDados.buscaDescricaoProduto >>> Valores captados da tabela PRODUTO");

                Produto p = new Produto(id, codigo, descricao, categoria, unidade, imagem);
                Log.d("Success", "BancoDeDados.buscaDescricaoProduto >>> Produto adicionado ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(p);
                Log.d("Success", "BancoDeDados.buscaDescricaoProduto >>> Produto adicionado à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaDescricaoProduto >>> Produto não encontrado");
        }
        Log.d("Success", "BancoDeDados.buscaDescricaoProduto >>> Lista construída");
        return lista;
    }

    //Método para buscar a despensa de acordo com a descrição
    public List<Despensa> buscaIdDespensa(int idDesc) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("DESPENSA", null,
                "_ID = ?",
                new String[]{valueOf(idDesc)}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaIdDespensa >>> Query executada com sucesso");

        List<Despensa> lista = new ArrayList<Despensa>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaIdDespensa >>> Despensa encontrada");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String descricao = resultado.getString(1);
                Log.d("Success", "BancoDeDados.buscaIdDespensa >>> Valores captados da tabela DESPENSA");

                Despensa p = new Despensa(id, descricao);
                Log.d("Success ", "BancoDeDados.buscaIdDespensa >>> Despensa adicionada ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(p);
                Log.d("Success ", "BancoDeDados.buscaIdDespensa >>> Despensa adicionada à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaIdDespensa >>> Despensa não encontrada");
        }
        Log.d("Success", "BancoDeDados.buscaIdDespensa >>> Lista construída");
        return lista;
    }

    //Método para buscar a despensa de acordo com a descrição
    public List<Despensa> buscaDescricaoDespensa(String desc) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("DESPENSA", null,
                "DESCRICAO = ?",
                new String[]{desc}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaDescricaoDespensa >>> Query executada com sucesso");

        List<Despensa> lista = new ArrayList<Despensa>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaDescricaoDespensa >>> Despensa encontrada");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String descricao = resultado.getString(1);
                Log.d("Success", "BancoDeDados.buscaDescricaoDespensa >>> Valores captados da tabela DESPENSA");

                Despensa p = new Despensa(id, descricao);
                Log.d("Success: ", "BancoDeDados.buscaDescricaoDespensa >>> Despensa adicionada ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(p);
                Log.d("Success: ", "BancoDeDados.buscaDescricaoDespensa >>> Despensa adicionada à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaDescricaoDespensa >>> Despensa não encontrada");
        }
        Log.d("Success", "BancoDeDados.buscaDescricaoDespensa >>> Lista construída");
        return lista;
    }

    //Método para buscar todas as despensas cadastradas
    public List<Despensa> buscaDespensa() {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("DESPENSA", null,
                null,null, null, null, null);
        Log.d("Success", "BancoDeDados.buscaDespensa >>> Query executada com sucesso");

        List<Despensa> lista = new ArrayList<Despensa>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaDespensa >>> Despensa encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String descricao = resultado.getString(1);
                Log.d("Success", "BancoDeDados.buscaDespensa >>> Valores captados da tabela DESPENSA");

                Despensa p = new Despensa(id, descricao);
                Log.d("Success: ", "BancoDeDados.buscaDespensa >>> Despensa adicionada ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(p);
                Log.d("Success: ", "BancoDeDados.buscaDespensa >>> Despensa adicionada à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaDespensa >>> Despensa não encontrada");
        }
        Log.d("Success", "BancoDeDados.buscaDespensa >>> Lista construída");
        return lista;
    }

    //Método para buscar a lista de acordo com a descrição
    public List<Lista> buscaIdLista(int idDesc) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("LISTA", null,
                "_ID = ?",
                new String[]{valueOf(idDesc)}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaIdLista >>> Query executada com sucesso");

        List<Lista> lista = new ArrayList<Lista>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaIdLista >>> Lista encontrada");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String descricao = resultado.getString(1);
                Log.d("Success", "BancoDeDados.buscaIdLista >>> Valores captados da tabela LISTA");

                Lista p = new Lista(id, descricao);
                Log.d("Success ", "BancoDeDados.buscaIdLista >>> Lista adicionada ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(p);
                Log.d("Success ", "BancoDeDados.buscaIdLista >>> Lista adicionada à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaIdLista >>> Lista não encontrada");
        }
        Log.d("Success", "BancoDeDados.buscaIdLista >>> Lista construída");
        return lista;
    }

    //Método para buscar a lista de acordo com a descrição
    public List<Lista> buscaDescricaoLista(String desc) {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("LISTA", null,
                "DESCRICAO = ?",
                new String[]{desc}, null, null, null);
        Log.d("Success", "BancoDeDados.buscaDescricaoLista >>> Query executada com sucesso");

        List<Lista> lista = new ArrayList<Lista>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaDescricaoLista >>> Lista encontrada");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String descricao = resultado.getString(1);
                Log.d("Success", "BancoDeDados.buscaDescricaoLista >>> Valores captados da tabela LISTA");

                Lista p = new Lista(id, descricao);
                Log.d("Success: ", "BancoDeDados.buscaDescricaoLista >>> Lista adicionada ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(p);
                Log.d("Success: ", "BancoDeDados.buscaDescricaoLista >>> Lista adicionada à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaDescricaoLista >>> Lista não encontrada");
        }
        Log.d("Success", "BancoDeDados.buscaDescricaoLista >>> Lista construída");
        return lista;
    }

    //Método para buscar todas as listas cadastradas
    public List<Lista> buscaLista() {
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Produto produto = new Produto();

        Cursor resultado = conexao.query("LISTA", null,
                null,null, null, null, null);
        Log.d("Success", "BancoDeDados.buscaLista >>> Query executada com sucesso");

        List<Lista> lista = new ArrayList<Lista>();

        //Testar se há algum conteúdo em "resultado"
        if (resultado.moveToFirst()) {
            Log.d("Success","BancoDeDados.buscaLista >>> Lista encontrado");
            //Utilização do do/while para executar algo e depois avançar
            do {
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String descricao = resultado.getString(1);
                Log.d("Success", "BancoDeDados.buscaLista >>> Valores captados da tabela LISTA");

                Lista p = new Lista(id, descricao);
                Log.d("Success: ", "BancoDeDados.buscaLista >>> Lista adicionada ao objeto");

                //Adicionar o produto "p" na lista
                lista.add(p);
                Log.d("Success: ", "BancoDeDados.buscaLista >>> Lista adicionada à lista");

            } while (resultado.moveToNext());
        }else{
            Log.d("Success","BancoDeDados.buscaLista >>> Lista não encontrada");
        }
        Log.d("Success", "BancoDeDados.buscaLista >>> Lista construída");
        return lista;
    }

    //Método para montar uma lista com todos os dados da tabela "produto"
    public List<Produto> buscaProduto(){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        Cursor resultado = conexao.query("produto", null, null,
                null, null, null, null);

        List<Produto> lista = new ArrayList<Produto>();

        //Testar se há algum conteúdo em "resultado"
        if(resultado.moveToFirst()){
            //Utilização do do/while para executar algo e depois avançar
            do{
                //Recuperar os valores de cada coluna da tabela
                int id = resultado.getInt(0); //Coluna de índice zero == coluna _id
                String codigo = resultado.getString(1);
                String descricao = resultado.getString(2);
                String categoria = resultado.getString(3);
                String unidade = resultado.getString(4);
                String imagem = resultado.getString(5);

                Produto p = new Produto(id, codigo, descricao, categoria, unidade, imagem);

                //Adicionar o Produto "p" na lista
                lista.add(p);

            }while(resultado.moveToNext());
        }
        return lista;
    }

    //Método para pesquisar produtos que estão na lista pela descrição
    public List<ProdutoXDespensa> buscaLikeDescricaoProdutos(String desc){
        Log.d("Success","Pesquisa via descrição do produto ");
        SQLiteDatabase conexao = getWritableDatabase();
        Cursor resultado = conexao.rawQuery("SELECT * FROM PRODUTOXDESPENSA pd" +
                        "INNER JOIN PRODUTO p ON pd.ID_PRODUTO = p._ID" +
                        "WHERE p.DESCRICAO like ?;",
                new String[]{"%" + desc + "%"});
        Log.d("Success","Query executada com sucesso");

        List<ProdutoXDespensa> listaFiltrada = new ArrayList<>();

//        SELECT a1, a2, b1, b2
//        FROM A
//        INNER JOIN B on B.f = A.f;

        if (resultado.moveToFirst()){
            do {
                //Recuperar os valores de cada coluna da tabela
                int idRegistro = resultado.getInt(0); //Coluna de índice zero == coluna _id
                Log.d("Success", "idRegistro recuperado: " + idRegistro);
                int idProduto = resultado.getInt(1);
                Log.d("Success", "idProduto recuperado: " + idProduto);
                double estoqueProduto = resultado.getDouble(2);
                Log.d("Success", "estoqueProduto recuperado: " + estoqueProduto);
                String validProduto = resultado.getString(3);
                Log.d("Success", "validProduto recuperado: " + validProduto);
                int idDespensa = resultado.getInt(4);
                Log.d("Success", "idDespensa recuperado: " + idDespensa);

                Log.d("Success", "BancoDeDados.buscaLikeDescricaoProdutos >>> " +
                        "Valores captados da tabela PRODUTOXDESPENSA");

                ProdutoXDespensa pXd = new ProdutoXDespensa(idRegistro, idProduto, estoqueProduto,
                        validProduto, idDespensa);
                Log.d("Success", "BancoDeDados.buscaLikeDescricaoProdutos >>> " +
                        "Registro adicionado ao objeto");

                //Adicionar o produto "p" na lista
                listaFiltrada.add(pXd);
                Log.d("Success", "BancoDeDados.buscaLikeDescricaoProdutos >>> " +
                        "Registro adicionado à lista");
            }while (resultado.moveToNext());


        }
        return listaFiltrada;
    }

    //---------------------------MÉTODOS DE ATUALIZAÇÃO--------------------------------
    //Método para atualizar (UPDATE) os valores de um registro na tabela PRODUTOXDESPENSA
    public boolean atualizarProdutoXDespensa(int idReg, int idProd, double quant, String valid, int idDesp){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Utilizando a classe ContentValues para indicar os valores atualizados
        ContentValues valores = new ContentValues();
        valores.put("ID_PRODUTO", idProd);
        valores.put("ESTOQUE_PRODUTO", quant);
        valores.put("VALID_PRODUTO", valid);
        valores.put("ID_DESPENSA", idDesp);

        //Chamando o método update
        if(conexao.update(
                "PRODUTOXDESPENSA", //Nome da tabela que será atualizada
                valores, //Valores que serão atualizados
                "_ID = ?", //Condição de atualização "WHERE nome = ?"
                new String[]{String.valueOf(idReg)} //Valor que irá no lugar do ?
        ) != 0){ //Se o resultado do UPDATE for != 0, então atualizou algo
            return true;
        }else{
            return false;
        }
    }

    //Método para atualizar (UPDATE) os valores de um registro na tabela PRODUTOXLISTA
    public boolean atualizarProdutoXLista(int idReg, int idProd, double quant, double preco, int idList){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Utilizando a classe ContentValues para indicar os valores atualizados
        ContentValues valores = new ContentValues();
        valores.put("ID_PRODUTO", idProd);
        valores.put("QUANTIDADE", quant);
        valores.put("PRECO", preco);
        valores.put("ID_LISTA", idList);

        //Chamando o método update
        if(conexao.update(
                "PRODUTOXLISTA", //Nome da tabela que será atualizada
                valores, //Valores que serão atualizados
                "_ID = ?", //Condição de atualização "WHERE nome = ?"
                new String[]{String.valueOf(idReg)} //Valor que irá no lugar do ?
        ) != 0){ //Se o resultado do UPDATE for != 0, então atualizou algo
            return true;
        }else{
            return false;
        }
    }

    //Método para atualizar (UPDATE) os valores de um registro na tabela PRODUTOXLISTA
    public boolean atualizarStatusProdutoXLista(int idReg, int status){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Utilizando a classe ContentValues para indicar os valores atualizados
        ContentValues valores = new ContentValues();
        valores.put("STATUS", status);

        //Chamando o método update
        if(conexao.update(
                "PRODUTOXLISTA", //Nome da tabela que será atualizada
                valores, //Valores que serão atualizados
                "_ID = ?", //Condição de atualização "WHERE nome = ?"
                new String[]{String.valueOf(idReg)} //Valor que irá no lugar do ?
        ) != 0){ //Se o resultado do UPDATE for != 0, então atualizou algo
            return true;
        }else{
            return false;
        }
    }

    //Método para atualizar (UPDATE) os valores de um registro na tabela PRODUTO
    public boolean atualizarProduto(int idProd, String cod, String desc, String categ, String unid,
                                    String img){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //Utilizando a classe ContentValues para indicar os valores atualizados
        ContentValues valores = new ContentValues();
        valores.put("CODIGO", cod);
        valores.put("DESCRICAO", desc);
        valores.put("CATEGORIA", categ);
        valores.put("UNIDADE", unid);
        valores.put("IMAGEM", img);

        //Chamando o método update
        if(conexao.update(
                "PRODUTO", //Nome da tabela que será atualizada
                valores, //Valores que serão atualizados
                "_ID = ?", //Condição de atualização "WHERE nome = ?"
                new String[]{String.valueOf(idProd)} //Valor que irá no lugar do ?
        ) != 0){ //Se o resultado do UPDATE for != 0, então atualizou algo
            return true;
        }else{
            return false;
        }
    }



    //---------------------------MÉTODOS DE REMOÇÃO-------------------------------------
    public boolean removerProduto(int id){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        if(conexao.delete("PRODUTO", "_ID = ?",
                new String[]{String.valueOf(id)}) != 0){
            return true;
        }else{
            return false;
        }
    }

    public boolean removerProdutoXDespensa(int id){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        if(conexao.delete("PRODUTOXDESPENSA", "_ID = ?",
                new String[]{String.valueOf(id)}) != 0){
            Log.d("Success", "BancoDeDados.removerProdutoXDespensa >>> " +
                    "Registro excluído com sucessoId: "+id);
            return true;
        }else{
            Log.d("Success", "BancoDeDados.removerProdutoXDespensa >>> " +
                    "Registro não excluído Id: "+id);
            return false;
        }
    }

    public boolean removerProdutoXLista(int id){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        if(conexao.delete("PRODUTOXLISTA", "_ID = ?",
                new String[]{String.valueOf(id)}) != 0){
            Log.d("Success", "BancoDeDados.removerProdutoXlista >>> " +
                    "Registro excluído com sucessoId: "+id);
            return true;
        }else{
            Log.d("Success", "BancoDeDados.removerProdutoXlista >>> " +
                    "Registro não excluído Id: "+id);
            return false;
        }
    }

    public boolean removerLista(int id){
        SQLiteDatabase conexao = getWritableDatabase(); //Abre a conexão

        //REMOVER TODOS OS PRODUTOS CADASTRADOS NA LISTA QUE SERÁ REMOVIDA
        conexao.delete("PRODUTOXLISTA", "ID_LISTA = ?",
                new String[]{String.valueOf(id)});

        if(conexao.delete("LISTA", "_ID = ?",
                new String[]{String.valueOf(id)}) != 0){
            Log.d("Success", "BancoDeDados.removerLista >>> " +
                    "Registro excluído com sucessoId: "+id);
            return true;
        }else{
            Log.d("Success", "BancoDeDados.removerLista >>> " +
                    "Registro não excluído Id: "+id);
            return false;
        }
    }


    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}