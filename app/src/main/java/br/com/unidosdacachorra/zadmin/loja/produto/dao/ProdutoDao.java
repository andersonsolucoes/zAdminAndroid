package br.com.unidosdacachorra.zadmin.loja.produto.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.math.BigDecimal;
import java.math.BigInteger;

import br.com.unidosdacachorra.zadmin.database.GerarTabelaProduto;
import br.com.unidosdacachorra.zadmin.loja.produto.model.Produto;

/**
 * Created by Anderson on 25/07/2016.
 */
public class ProdutoDao {

    private SQLiteDatabase db;
    private GerarTabelaProduto banco;
    private int TRUE = 1;
    private int FALSE = 0;

    public ProdutoDao(Context context){
        banco = new GerarTabelaProduto(context);
    }

    public long inserir(Produto produto){
        Long id = produto.getId();
        String nome = produto.getNome();
        String descricao = produto.getDescricao();
        BigDecimal valor = produto.getValor();
        Boolean ativo = true;
        Integer quantidade = produto.getQuantidade();
        Boolean sincronizado = produto.getSincronizado();

        return inserir(id,nome,descricao,valor,ativo,quantidade,sincronizado);

    }

    public long inserir(Long id,String nome, String descricao, BigDecimal valor, Boolean ativo, Integer quantidade, Boolean sincronizado){
        ContentValues valores;
        long resultado;

        db = banco.getWritableDatabase();
        valores = new ContentValues();
        if(id != null) {
            valores.put(banco.ID, id);
        }
        valores.put(banco.NOME, nome);
        valores.put(banco.DESCRICAO, descricao);
        valores.put(banco.VALOR, valor.toString());
        valores.put(banco.ATIVO,ativo);
        valores.put(banco.QUANTIDADE,quantidade);
        valores.put(banco.SINCRONIZADO, sincronizado);

        resultado = db.insert(banco.TABELA, null, valores);
        db.close();

        return resultado;

    }
    public Cursor carregarAtivos(int posicaoInicial, int numeroLinhas){
        Cursor cursor;
        String[] campos =  {banco.ID,banco.NOME,banco.DESCRICAO,banco.VALOR, banco.ATIVO, banco.SINCRONIZADO, banco.QUANTIDADE};
        String where = banco.ATIVO + " = " + TRUE;
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA, campos, where, null, null, null, banco.NOME + " ASC ", posicaoInicial + "," + numeroLinhas);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor carregarDessincronizados(boolean ativo){
        Cursor cursor;
        String[] campos =  {banco.ID};
        String where = banco.SINCRONIZADO + "=" + FALSE;
        if(ativo){
            where += " and " + banco.ATIVO + " = " + TRUE;
        }
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA,campos,where, null, null, null, null, null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor carregarPorId(int id){
        Cursor cursor;
        String[] campos =  {banco.ID,banco.NOME,banco.DESCRICAO,banco.VALOR, banco.ATIVO, banco.SINCRONIZADO, banco.QUANTIDADE};
        String where = banco.ID + "=" + id;
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA,campos,where, null, null, null, banco.NOME + " ASC ", null);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public Cursor carregarPorNome(String nome, int posicaoInicial, int numeroLinhas, boolean ativo){
        Cursor cursor;
        String[] campos =  {banco.ID,banco.NOME,banco.DESCRICAO,banco.VALOR, banco.ATIVO, banco.SINCRONIZADO, banco.QUANTIDADE};
        String where = "UPPER("+banco.NOME + ") LIKE '%" + nome + "%'";
        if(ativo) {
            where +=  " and " + banco.ATIVO + "=" + 1;
        }
        db = banco.getReadableDatabase();
        cursor = db.query(banco.TABELA,campos,where, null, null, null, banco.NOME + " ASC ", posicaoInicial+","+numeroLinhas);

        if(cursor!=null){
            cursor.moveToFirst();
        }
        db.close();
        return cursor;
    }

    public void alterar(Produto produto){
        Long id = produto.getId();
        String nome = produto.getNome();
        String descricao = produto.getDescricao();
        BigDecimal valor = produto.getValor();
        Boolean ativo = true;
        Integer quantidade = produto.getQuantidade();
        Boolean sincronizado = produto.getSincronizado();

        alterar(id.intValue(),nome,descricao,valor, ativo,quantidade,sincronizado);

    }

    public void alterar(int id, String nome, String descricao, BigDecimal valor, Boolean ativo, Integer quantidade, Boolean sincronizado){
        ContentValues valores;
        String where;

        db = banco.getWritableDatabase();

        where = banco.ID + "=" + id;

        valores = new ContentValues();
        valores.put(banco.NOME, nome);
        valores.put(banco.DESCRICAO, descricao);
        valores.put(banco.VALOR, valor.toString());
        valores.put(banco.ATIVO, ativo);
        valores.put(banco.QUANTIDADE, quantidade);
        valores.put(banco.SINCRONIZADO, sincronizado);

        db.update(banco.TABELA,valores,where,null);
        db.close();
    }
    public void deletar(long id){
        String where = banco.ID + "=" + id;
        db = banco.getReadableDatabase();
        db.delete(banco.TABELA, where, null);
        db.close();
    }

    public void inativar(long id){
        ContentValues valores;
        valores = new ContentValues();
        valores.put(banco.ATIVO, 0);
        valores.put(banco.SINCRONIZADO, 0);

        String where = banco.ID + "=" + id;

        db = banco.getReadableDatabase();
        db.update(banco.TABELA, valores, where, null);
        db.close();
    }

    public void sincronizar(int... ids) {
        ContentValues valores;
        valores = new ContentValues();
        valores.put(banco.SINCRONIZADO, 1);
        String where = banco.ID + " in (";
        for (int i = 0; i < ids.length; i++){
            if(i > 0){
                where += ",";
            }
            where += ids[i];
        }
        where += ")";

        db = banco.getReadableDatabase();
        db.update(banco.TABELA, valores, where, null);
        db.close();

    }
}
