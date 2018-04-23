package br.com.unidosdacachorra.zadmin.loja.produto.fragmentos;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.database.GerarTabelaProduto;
import br.com.unidosdacachorra.zadmin.login.Credencial;
import br.com.unidosdacachorra.zadmin.login.LoginActivity;
import br.com.unidosdacachorra.zadmin.loja.produto.activities.DetalharProdutoActivity;
import br.com.unidosdacachorra.zadmin.loja.produto.activities.EditarProdutoActivity;
import br.com.unidosdacachorra.zadmin.loja.produto.dao.ProdutoDao;
import br.com.unidosdacachorra.zadmin.util.AbstractFragment;

/**
 * Created by Anderson on 28/07/2016.
 */
public class DetalharProdutoFragment extends AbstractFragment {
    private Cursor mItem;
    String id;
    String nome;
    String descricao;
    String valor;
    String valorFormatado;
    String quantidade;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey("idProduto")) {

            ProdutoDao dao = new ProdutoDao(getContext());
            mItem = dao.carregarPorId(Integer.parseInt(getArguments().getString("idProduto")));
            String nome = mItem.getString(mItem.getColumnIndex(GerarTabelaProduto.NOME));

            Activity activity = this.getActivity();
            CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) activity.findViewById(R.id.toolbar_layout);
            if (appBarLayout != null) {
                appBarLayout.setTitle(nome);
            }
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_loja_produto_detalhe_item, container, false);
        setHasOptionsMenu(true);

        if (mItem != null) {
            id = mItem.getString(mItem.getColumnIndex(GerarTabelaProduto.ID));
            nome = mItem.getString(mItem.getColumnIndex(GerarTabelaProduto.NOME));
            descricao = mItem.getString(mItem.getColumnIndex(GerarTabelaProduto.DESCRICAO));
            valor = mItem.getString(mItem.getColumnIndex(GerarTabelaProduto.VALOR));
            valorFormatado = getValorFormatado(valor);
            quantidade = mItem.getString(mItem.getColumnIndex(GerarTabelaProduto.QUANTIDADE));

            //((TextView) rootView.findViewById(R.id.item_produto_id)).setText(id);
            ((TextView) rootView.findViewById(R.id.item_produto_nome)).setText(nome);
            ((TextView) rootView.findViewById(R.id.item_produto_descricao)).setText(descricao);
            ((TextView) rootView.findViewById(R.id.item_produto_valor)).setText(valorFormatado);
            ((TextView) rootView.findViewById(R.id.item_produto_quantidade)).setText(quantidade + " em estoque");

            FloatingActionButton fab = (FloatingActionButton) getActivity().findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, EditarProdutoActivity.class);
                    intent.putExtra("idProduto", id);
                    intent.putExtra("nomeProduto", nome);
                    intent.putExtra("descricaoProduto", descricao);
                    intent.putExtra("valorProduto", valor);
                    intent.putExtra("quantidadeProduto", quantidade);
                    //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivityForResult(intent, 1);
                    //getActivity().finish();
                }
            });
        }

        return rootView;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                nome = data.getStringExtra("nomeProduto");
                descricao = data.getStringExtra("descricaoProduto");
                valor = data.getStringExtra("valorProduto");
                quantidade = data.getStringExtra("quantidadeProduto");
                valorFormatado = getValorFormatado(valor);
                CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout) getActivity().findViewById(R.id.toolbar_layout);
                if (appBarLayout != null) {
                    appBarLayout.setTitle(nome);
                }
                ((TextView) getActivity().findViewById(R.id.item_produto_nome)).setText(nome);
                ((TextView) getActivity().findViewById(R.id.item_produto_descricao)).setText(descricao);
                ((TextView) getActivity().findViewById(R.id.item_produto_valor)).setText(valorFormatado);
                ((TextView) getActivity().findViewById(R.id.item_produto_quantidade)).setText(quantidade);
                ((DetalharProdutoActivity) getActivity()).setAtualizaProduto(true);
            }
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_loja_produto_opcoes, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(item.getItemId()==R.id.action_excluir_produto) {
            new AlertDialog.Builder(getActivity())
                    .setTitle("Excluir produto")
                    .setMessage("Deseja realmente excluir " + nome + "?")
                    .setIcon(R.drawable.ic_excluir)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            try {
                                excluirProduto(Long.parseLong(id));
                                getActivity().setResult(1);
                                getActivity().finish();
                                //Toast.makeText(getActivity(), "Produto excluido com sucesso", Toast.LENGTH_LONG).show();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Não foi possível excluir o produto: " + e.getCause(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
        return super.onOptionsItemSelected(item);
    }

    private void excluirProduto(long id) {
        ProdutoDao dao = new ProdutoDao(getContext());
        dao.inativar(id);
    }
}
