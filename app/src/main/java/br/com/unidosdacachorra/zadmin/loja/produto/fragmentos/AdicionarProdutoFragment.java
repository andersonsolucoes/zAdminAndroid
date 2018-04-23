package br.com.unidosdacachorra.zadmin.loja.produto.fragmentos;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.loja.produto.activities.DetalharProdutoActivity;
import br.com.unidosdacachorra.zadmin.loja.produto.dao.ProdutoDao;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;
import br.com.unidosdacachorra.zadmin.util.AbstractFragment;
import br.com.unidosdacachorra.zadmin.util.Mensagem;

/**
 * Created by Anderson on 29/07/2016.
 */
public class AdicionarProdutoFragment extends AbstractFragment {
    private View mFormAddProduto;
    private InserirProdutoTask mAddTask = null;
    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_loja_produto_adicionar, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mFormAddProduto = getActivity().findViewById(R.id.add_produto_form);
        progressBar = getActivity().findViewById(R.id.progresso);
        Button botaoAdd = (Button) getActivity().findViewById(R.id.btn_add_produto);

        botaoAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserirProduto();
            }
        });
    }

    private void inserirProduto(){
        if (mAddTask != null) {
            return;
        }

        EditText nome = (EditText) getActivity().findViewById(R.id.add_nome_produto);
        EditText descricao = (EditText) getActivity().findViewById((R.id.add_descricao_produto));
        EditText valor = (EditText) getActivity().findViewById(R.id.add_valor_produto);
        EditText quantidade = (EditText) getActivity().findViewById(R.id.add_quantidade_produto);

        if(isProdutoValido(nome, descricao, valor, quantidade)) {
            ((AbstractActivity)getActivity()).showProgress(true, mFormAddProduto, progressBar);
            String nomeString = nome.getText().toString();
            String descricaoString = descricao.getText().toString();
            String valorString = valor.getText().toString();
            String quantidadeString = quantidade.getText().toString();

            mAddTask = new InserirProdutoTask(nomeString, descricaoString, new BigDecimal(valorString), new Integer(quantidadeString));
            mAddTask.execute((Void)null);
        }

    }

    private boolean isProdutoValido(EditText nome, EditText descricao, EditText valor, EditText quantidade) {
        boolean valido = true;

        if(valor==null) {
            valido = false;
        } else if(valor.getText().toString().equals("")) {
            valido = false;
            valor.setError(Mensagem.CAMPO_OBRIGATORIO);
            valor.requestFocus();
        }

        if(descricao==null) {
            valido = false;
        } else if(descricao.getText().toString().equals("")) {
            valido = false;
            descricao.setError(Mensagem.CAMPO_OBRIGATORIO);
            descricao.requestFocus();
        }

        if(nome==null) {
            valido = false;
        } else if(nome.getText().toString().equals("")) {
            valido = false;
            nome.setError(Mensagem.CAMPO_OBRIGATORIO);
            nome.requestFocus();
        }
        if(quantidade==null) {
            valido = false;
        } else if(quantidade.getText().toString().equals("")) {
            valido = false;
            quantidade.setError(Mensagem.CAMPO_OBRIGATORIO);
            quantidade.requestFocus();
        }


        return valido;
    }

    public class InserirProdutoTask extends AsyncTask<Void, Void, Boolean> {

        private final String mNome;
        private final String mDescricao;
        private final BigDecimal mValor;
        private final Integer mQuantidade;
        private long idInserido;

        public InserirProdutoTask(String nome, String descricao, BigDecimal valor, Integer quantidade) {
            mNome = nome;
            mDescricao = descricao;
            mValor = valor;
            mQuantidade = quantidade;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ProdutoDao dao = new ProdutoDao(getContext());
            idInserido = dao.inserir(null,mNome, mDescricao, mValor, true, mQuantidade, false);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAddTask = null;
            ((AbstractActivity)getActivity()).showProgress(false, mFormAddProduto, progressBar);

            if(success) {
                getActivity().setResult(1);
                getActivity().finish();
                //Toast.makeText(getActivity().getApplicationContext(), "Produto inserido com sucesso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Falha ao inserir o produto", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {
            mAddTask = null;
            ((AbstractActivity)getActivity()).showProgress(false, mFormAddProduto, progressBar);
        }
    }
}
