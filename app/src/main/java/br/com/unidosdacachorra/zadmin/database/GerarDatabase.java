package br.com.unidosdacachorra.zadmin.database;

import android.app.Activity;
import android.content.Context;
import android.content.ContextWrapper;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.io.File;

/**
 * Created by Anderson on 25/07/2016.
 */
public class GerarDatabase {
    private SQLiteDatabase db;
    public static final String NOME_BANCO = "zadmin.db";
    public static final int VERSAO = 3;
    private GerarTabelaProduto produto;

    public static boolean existeDatabase(ContextWrapper context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        return dbFile.exists();
    }

    public GerarDatabase(Context context){

        if(!existeDatabase(new ContextWrapper(context),NOME_BANCO)) {
            produto = new GerarTabelaProduto(context);
        } else {
        }

    }

}
