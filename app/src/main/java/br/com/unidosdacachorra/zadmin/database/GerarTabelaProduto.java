package br.com.unidosdacachorra.zadmin.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.view.View;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * Created by Anderson on 25/07/2016.
 */
public class GerarTabelaProduto extends SQLiteOpenHelper {

    public static final String NOME_BANCO = GerarDatabase.NOME_BANCO;
    public static final String TABELA = "grbc_produto";
    public static final String ID = "_id";
    public static final String NOME = "nome";
    public static final String DESCRICAO = "descricao";
    public static final String VALOR = "valor";
    public static final String ATIVO = "ativo";
    public static final String SINCRONIZADO = "sincronizado";
    public static final int VERSAO = GerarDatabase.VERSAO;

    public GerarTabelaProduto(Context context){
        super(context, NOME_BANCO,null,VERSAO);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String sql = "CREATE TABLE "+TABELA+"("
                + ID + " integer primary key autoincrement,"
                + NOME + " text,"
                + DESCRICAO + " text,"
                + VALOR + " numeric(10,2),"
                + ATIVO + " integer,"
                + SINCRONIZADO + " integer DEFAULT 0 "
                +")";
        db.execSQL(sql);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABELA);
        onCreate(db);
    }

    public static SimpleCursorAdapter getAdaptador(Context context, int layout, Cursor cursor) {
        String[] nomeCampos = new String[]{GerarTabelaProduto.NOME, GerarTabelaProduto.VALOR, GerarTabelaProduto.SINCRONIZADO};
        int[] idViews = new int[]{R.id.nomeProduto, R.id.valorProduto, R.id.sincronizacao};

        SimpleCursorAdapter adaptador = new SimpleCursorAdapter(context,
                layout, cursor, nomeCampos, idViews, 0);

        adaptador.setViewBinder(new SimpleCursorAdapter.ViewBinder() {

            public boolean setViewValue(View aView, Cursor aCursor, int aColumnIndex) {
                String createDate = aCursor.getString(aColumnIndex);

                if (aColumnIndex == 3) {
                    TextView textView = (TextView) aView;
                    textView.setText(AbstractActivity.getValorFormatado(createDate));
                    return true;
                } else if(aColumnIndex == 5) {
                    int valor = aCursor.getInt(aColumnIndex);
                    ImageView img = (ImageView) aView;
                    if(valor == 1) {
                        img.setImageResource(R.drawable.ic_check);
                    } else {
                        img.setImageResource(R.drawable.ic_alert);
                    }
                    return true;
                }

                return false;
            }

        });
        return adaptador;
    }
}
