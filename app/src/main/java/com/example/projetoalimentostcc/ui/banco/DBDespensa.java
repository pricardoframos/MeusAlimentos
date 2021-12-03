package com.example.projetoalimentostcc.ui.banco;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DBDespensa extends SQLiteOpenHelper {
    private static  int versao=1;
    private static String nome="Despensa.db";

    public DBDespensa(Context context) {
        super(context, nome, null, versao);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String str = "CREATE TABLE Cliente(usuario TEXT PRIMARY KEY, senha TEXT);";
        db.execSQL(str);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS Cliente;");
        onCreate(db);
    }

    public long CriarCliente(String usuario, String senha){
        SQLiteDatabase db = getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put("usuario", usuario);
        cv.put("senha", senha);
        long result = db.insert("Cliente", null, cv);
        return result;
    }

    public String ValidarLogin(String usuario, String senha){
        SQLiteDatabase db = getReadableDatabase();
        Cursor c = db.rawQuery("SELECT * FROM Cliente WHERE usuario=? AND senha=?",
                new String[]{usuario, senha});
        if(c.getCount()>0){
            return "OK";
        }
        return"ERRO";
    }

}
