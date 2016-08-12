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
public class EditarProdutoFragment extends AbstractFragment {

    private View mFormEditarProduto;
    private EditarProdutoTask mEditarTask = null;
    private View progressBar;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_loja_produto_editar, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EditText id = (EditText) getActivity().findViewById(R.id.editar_id_produto);
        id.setText(getActivity().getIntent().getStringExtra("idProduto"));

        EditText nome = (EditText) getActivity().findViewById(R.id.editar_nome_produto);
        nome.setText(getActivity().getIntent().getStringExtra("nomeProduto"));

        EditText descricao = (EditText) getActivity().findViewById(R.id.editar_descricao_produto);
        descricao.setText(getActivity().getIntent().getStringExtra("descricaoProduto"));

        EditText valor = (EditText) getActivity().findViewById(R.id.editar_valor_produto);
        valor.setText(getActivity().getIntent().getStringExtra("valorProduto"));


        mFormEditarProduto = getActivity().findViewById(R.id.editar_produto_form);
        progressBar = getActivity().findViewById(R.id.progresso);
        Button botaoEditar = (Button) getActivity().findViewById(R.id.btn_editar_produto);

        botaoEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editarProduto();
            }
        });
    }

    private void editarProduto(){
        if (mEditarTask != null) {
            return;
        }

        EditText idTxt = (EditText) getActivity().findViewById(R.id.editar_id_produto);
        EditText nome = (EditText) getActivity().findViewById(R.id.editar_nome_produto);
        EditText descricao = (EditText) getActivity().findViewById((R.id.editar_descricao_produto));
        EditText valor = (EditText) getActivity().findViewById(R.id.editar_valor_produto);

        if(isProdutoValido(idTxt,nome, descricao, valor)) {
            ((AbstractActivity)getActivity()).showProgress(true, mFormEditarProduto, progressBar);
            int idInt = Integer.parseInt(idTxt.getText().toString());
            String nomeString = nome.getText().toString();
            String descricaoString = descricao.getText().toString();
            String valorString = valor.getText().toString();

            mEditarTask = new EditarProdutoTask(idInt, nomeString, descricaoString, new BigDecimal(valorString));
            mEditarTask.execute((Void) null);
        }

    }

    private boolean isProdutoValido(EditText id,EditText nome, EditText descricao, EditText valor) {
        boolean valido = true;

        if(id==null) {
            valido = false;
        } else {
            try {
                Integer.parseInt(id.getText().toString());
            } catch (Exception e) {
                valido = false;
            }
        }

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

        return valido;
    }

    public class EditarProdutoTask extends AsyncTask<Void, Void, Boolean> {

        private final String mNome;
        private final String mDescricao;
        private final BigDecimal mValor;
        private int  mId;

        EditarProdutoTask(int id, String nome, String descricao, BigDecimal valor) {
            mId = id;
            mNome = nome;
            mDescricao = descricao;
            mValor = valor;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            ProdutoDao dao = new ProdutoDao(getContext());
            dao.alterar(mId, mNome, mDescricao, mValor);
            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mEditarTask = null;
            ((AbstractActivity)getActivity()).showProgress(false, mFormEditarProduto, progressBar);

            if(success) {
                Context context = getContext();
                Intent intent = new Intent(context, DetalharProdutoActivity.class);
                intent.putExtra("idProduto", mId + "");
                intent.putExtra("nomeProduto", mNome);
                intent.putExtra("descricaoProduto", mDescricao);
                intent.putExtra("valorProduto", mValor.toString());
                //intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                getActivity().setResult(1, intent);
                getActivity().finish();
                //Toast.makeText(getActivity().getApplicationContext(), "Produto inserido com sucesso", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(getContext(), "Falha ao editar o produto", Toast.LENGTH_LONG).show();
            }

        }

        @Override
        protected void onCancelled() {
            mEditarTask = null;
            ((AbstractActivity)getActivity()).showProgress(false, mFormEditarProduto, progressBar);
        }
    }
}
