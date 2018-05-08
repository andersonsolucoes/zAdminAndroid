package br.com.unidosdacachorra.zadmin.loja.produto.fragmentos;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.app.Activity;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.MergeCursor;
import android.database.sqlite.SQLiteCursor;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.database.GerarTabelaProduto;
import br.com.unidosdacachorra.zadmin.inicio.ActivityPrincipal;
import br.com.unidosdacachorra.zadmin.login.Credencial;
import br.com.unidosdacachorra.zadmin.loja.produto.activities.AdicionarProdutoActivity;
import br.com.unidosdacachorra.zadmin.loja.produto.activities.DetalharProdutoActivity;
import br.com.unidosdacachorra.zadmin.loja.produto.dao.ProdutoDao;
import br.com.unidosdacachorra.zadmin.loja.produto.model.Produto;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;
import br.com.unidosdacachorra.zadmin.util.AbstractFragment;

/**
 * Created by Anderson on 20/07/2016.
 */
public class ProdutoFragment extends AbstractFragment implements SwipeRefreshLayout.OnRefreshListener {
    private ListView lista;
    private CarregarProdutoTask mLoadTask = null;
    private SincronizadorProdutoTask mSincronizadorTask = null;
    private View mLayoutConsultarProduto;
    private View progressBar;
    private Toolbar cabecalho;
    private String criterioBusca;
    private static int numeroLinhas = 15;
    private static int posicaoInicial = 0;
    private ListView listaTemp;
    private int preLast;
    private int mTotalItemCount;
    private int mVisibleItemCount;
    private SwipeRefreshLayout swipeRefreshLayout;
    private ProgressBar progressList;
    private ProgressBar progressSincronizacao;
    private ConsultarProdutoTask mConsultarProdutosTask = null;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v =  inflater.inflate(R.layout.activity_loja_produto, container, false);
        setHasOptionsMenu(true);
        return v;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        if(menu!=null)menu.clear();
        inflater.inflate(R.menu.menu_loja_produto_cabecalho, menu);
        super.onCreateOptionsMenu(menu, inflater);
        cabecalho = (Toolbar) getActivity().findViewById(R.id.toolbar);
        defineMenuItemAction(cabecalho);
        defineSearchAction(cabecalho);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLayoutConsultarProduto = getActivity().findViewById(R.id.consultar_produto_layout);
        progressBar = getActivity().findViewById(R.id.progresso);
        progressList = (ProgressBar) getActivity().findViewById(R.id.load_list_bottom);
        progressSincronizacao = (ProgressBar) getActivity().findViewById(R.id.progressoHorizontal);
        progressSincronizacao.setIndeterminate(true);

        lista = (ListView) getActivity().findViewById(R.id.listView);
        listaTemp = new ListView(getContext());
        ListAdapter a = new SimpleCursorAdapter(getContext(),-1,null,null,null,-1);
        listaTemp.setAdapter(a);

        swipeRefreshLayout = (SwipeRefreshLayout) getActivity().findViewById(R.id.consultar_produto_form);
        swipeRefreshLayout.setOnRefreshListener(this);


        //lista.addHeaderView(View.inflate(getContext(), R.layout.activity_loja_produto_consultar_layout, null));

        //((TextView) view.findViewById(R.id.nomeProduto)).setTypeface(null, Typeface.BOLD);
        //((TextView) view.findViewById(R.id.valorProduto)).setTypeface(null, Typeface.BOLD);


        if(lista.getCount() < 2) {
            popularListView(lista, null,posicaoInicial, numeroLinhas, true, false);
        }

        lista.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (id != -1) {
                    Context context = view.getContext();
                    Intent intent = new Intent(context, DetalharProdutoActivity.class);
                    intent.putExtra("idProduto", id + "");
                    startActivityForResult(intent, 1);
                }
            }
        });

        lista.setOnScrollListener(new AbsListView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {


                if (scrollState == 0 && preLast == view.getCount()) {
                    if (preLast == mTotalItemCount) {


                        popularListView(listaTemp, criterioBusca, preLast, numeroLinhas, false, true);


                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                preLast = firstVisibleItem + visibleItemCount;
                if (preLast == totalItemCount) {
                    mVisibleItemCount = visibleItemCount;
                    mTotalItemCount = totalItemCount;
                }
            }
        });

        registerForContextMenu(lista);


        lista.setEmptyView(view.findViewById(android.R.id.empty));



    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v,
                                    ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        MenuInflater inflater = getActivity().getMenuInflater();
        inflater.inflate(R.menu.menu_loja_produto_opcoes, menu);
    }

    @Override
    public boolean onContextItemSelected(final MenuItem item) {
        if(item.getItemId()==R.id.action_excluir_produto) {
            final AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();

            new AlertDialog.Builder(getActivity())
                    .setTitle("Excluir produto")
                    .setMessage("Deseja realmente excluir ?")
                    .setIcon(R.drawable.ic_excluir)
                    .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                        public void onClick(DialogInterface dialog, int whichButton) {
                            try {
                                excluirProduto(info.id);
                                popularListView(lista,criterioBusca, posicaoInicial,numeroLinhas, true, false);
                                Toast.makeText(getActivity(), "Produto excluido com sucesso", Toast.LENGTH_SHORT).show();
                            } catch (Exception e) {
                                Toast.makeText(getActivity(), "Não foi possível excluir o produto: " + e.getCause(), Toast.LENGTH_LONG).show();
                            }
                        }
                    })
                    .setNegativeButton(android.R.string.no, null).show();
        }
        return super.onContextItemSelected(item);
    }

    private void popularListView(ListView lista, String query, int posicaoInicial ,int numeroLinhas, boolean showProgress, boolean isConsulta) {
        if (mLoadTask != null) {
            return;
        }

        mLoadTask = new CarregarProdutoTask(lista, query, posicaoInicial, numeroLinhas, showProgress, isConsulta);
        mLoadTask.execute((Void) null);
    }

    private void defineMenuItemAction(Toolbar cabecalho){
        MenuItem addProdutoItem = cabecalho.getMenu().findItem(R.id.action_add_produto);
        addProdutoItem.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {


                Intent intent = new Intent(getActivity(), AdicionarProdutoActivity.class);
                //intent.setFlags(Intent.);
                startActivityForResult(intent, 1);

                return true;
            }
        });

        final MenuItem sincronizar = cabecalho.getMenu().findItem(R.id.action_sincronizar_produto);
        sincronizar.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                ProdutoDao dao = new ProdutoDao(getContext());
                Cursor cursor = dao.carregarDessincronizados(true);
                if(cursor != null && cursor.getCount() > 0) {
                    sincronizar(cursor, "produto");
                } else {
                    Toast.makeText(getActivity(), "Não existem itens para sincronizar", Toast.LENGTH_SHORT).show();
                }
                return true;
            }
        });

        MenuItem refreshList = cabecalho.getMenu().findItem(R.id.action_refresh_list);
        refreshList.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                //popularListView(lista, null, posicaoInicial, numeroLinhas, true, false);
                showProgress(true, null, progressBar);
                mConsultarProdutosTask = new ConsultarProdutoTask(lista);
                mConsultarProdutosTask.execute((Void) null);
                return true;
            }
        });

    }

    private void sincronizar(final Cursor c, String processador){
        new AlertDialog.Builder(getActivity())
                .setTitle("Sincronizar")
                .setMessage("Deseja sincronizar " + c.getCount() + (c.getCount() == 1 ? " item" : " itens") + " com a Nuvem?")
                .setIcon(R.drawable.ic_help)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        ProdutoDao dao = new ProdutoDao(getContext());
                        int[] ids = new int[c.getCount()];
                        if (c != null) {
                            ids[0] = c.getInt(0);
                            int i = 1;
                            while (c.moveToNext()) {
                                ids[i] = c.getInt(0);
                                i++;
                            }
                        }
                        if (mSincronizadorTask != null) {
                            return;
                        }

                        mSincronizadorTask = new SincronizadorProdutoTask(ids);
                        mSincronizadorTask.execute((Void) null);
                    }
                })
                .setNegativeButton(android.R.string.no, null).show();
    }

    private void defineSearchAction(Toolbar cabecalho) {
        final MenuItem searchItem = cabecalho.getMenu().findItem(R.id.action_consultar_produto);

        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        SearchView searchView = null;
        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                popularListView(lista, query, posicaoInicial, numeroLinhas, true, false);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                return false;
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1) {
            if (resultCode == 1) {
                popularListView(lista,criterioBusca, posicaoInicial, numeroLinhas, true, false);
            }
        }
    }

    private void excluirProduto(long id) {
        ProdutoDao dao = new ProdutoDao(getContext());
        dao.inativar(id);
    }

    @Override
    public void onRefresh() {
        popularListView(lista, null, posicaoInicial, numeroLinhas, true, false);
    }

    private class CarregarProdutoTask extends AsyncTask<Void, Void, Boolean> {

        private String mQuery;
        private ListView mListView;
        private SimpleCursorAdapter adaptador;
        private int mPosicaoInicial;
        private int mNumeroLinhas;
        private boolean mShowProgress;
        private boolean mIsConsulta;

        CarregarProdutoTask(ListView listView, String query, int posicaoInicial, int numeroLinhas, boolean showProgress, boolean isConsulta) {
            mQuery = query;
            mListView = listView;
            mPosicaoInicial = posicaoInicial;
            mNumeroLinhas = numeroLinhas;
            mShowProgress = showProgress;
            mIsConsulta = isConsulta;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            if(mShowProgress) {
                ((AbstractActivity)getActivity()).showProgress(true, mLayoutConsultarProduto, progressBar);
            }
            if(mIsConsulta){
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                progressList.setVisibility(View.VISIBLE);
                progressList.animate().setDuration(shortAnimTime).alpha(1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressList.setVisibility(View.VISIBLE);
                    }
                });
            }
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            /*try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }*/
            ProdutoDao dao = new ProdutoDao(getContext());
            final Cursor cursor;
            if(mQuery==null) {
                cursor = dao.carregarAtivos(mPosicaoInicial, mNumeroLinhas);
            } else {
                cursor = dao.carregarPorNome(mQuery.toUpperCase(), mPosicaoInicial, mNumeroLinhas, true);
            }

            criterioBusca = mQuery;

            adaptador = GerarTabelaProduto.getAdaptador(getActivity().getBaseContext(), R.layout.activity_loja_produto_consultar_layout, cursor);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLoadTask = null;

            mListView.setAdapter(adaptador);
            swipeRefreshLayout.setRefreshing(false);

            if(mShowProgress) {
                ((AbstractActivity)getActivity()).showProgress(false, mLayoutConsultarProduto, progressBar);
            }

            if(mIsConsulta) {
                ListAdapter listaAdapter = lista.getAdapter();
                ListAdapter listaTempAdapter = listaTemp.getAdapter();

                if (listaTempAdapter != null && listaTempAdapter.getCount() != 0) {

                    SQLiteCursor cursor = (SQLiteCursor) listaTempAdapter.getItem(0);
                    Cursor novoCursor;

                    try{
                        novoCursor = new MergeCursor(new Cursor[]{(SQLiteCursor) listaAdapter.getItem(0), cursor});
                    } catch (Exception e) {
                        novoCursor = new MergeCursor(new Cursor[]{(MergeCursor) listaAdapter.getItem(0), cursor});
                    }
                    novoCursor.moveToNext();

                    SimpleCursorAdapter adaptador = GerarTabelaProduto.getAdaptador(getActivity().getBaseContext(), R.layout.activity_loja_produto_consultar_layout, novoCursor);

                    if (adaptador.getCount() != 0) {
                        lista.setAdapter(adaptador);
                        lista.setSelection(preLast - mVisibleItemCount + 1);
                    }


                } else {
                    preLast = 0;
                }
                int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

                progressList.setVisibility(View.GONE);
                progressList.animate().setDuration(shortAnimTime).alpha(0).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        progressList.setVisibility(View.GONE);
                    }
                });
            }
        }

        @Override
        protected void onCancelled() {
            mLoadTask = null;
            if(mShowProgress) {
                ((AbstractActivity)getActivity()).showProgress(false, mLayoutConsultarProduto, progressBar);
            }
        }
    }



    private class SincronizadorProdutoTask extends AsyncTask<Void, Void, Boolean> {

        private int[] mIds;
        private int mPosicaoInicial;
        private int mNumeroLinhas;

        SincronizadorProdutoTask(int[] ids) {
            mIds = ids;
            mPosicaoInicial = posicaoInicial;
            mNumeroLinhas = numeroLinhas;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            ((AbstractActivity)getActivity()).showProgress(true, null, progressSincronizacao);
            cabecalho.getMenu().findItem(R.id.action_sincronizar_produto).setEnabled(false);
            cabecalho.getMenu().findItem(R.id.action_sincronizar_produto).setIcon(R.drawable.ic_upload_inativo);
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            try {
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            ProdutoDao dao = new ProdutoDao(getContext());
            dao.sincronizar(mIds);

            return true;
        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mLoadTask = null;
            if(success) {
                Toast.makeText(getActivity(), "Sincronização concluída com sucesso", Toast.LENGTH_SHORT).show();
                ((AbstractActivity)getActivity()).showProgress(false, null, progressSincronizacao);
                popularListView(lista, null, posicaoInicial, numeroLinhas, true, false);
                cabecalho.getMenu().findItem(R.id.action_sincronizar_produto).setEnabled(true);
                cabecalho.getMenu().findItem(R.id.action_sincronizar_produto).setIcon(R.drawable.ic_upload);
            } else {
                Log.i("SINCRONIZAR PRODUTO", "Erro ao executar a operação");
            }

        }

        @Override
        protected void onCancelled() {
            mLoadTask = null;
            ((AbstractActivity)getActivity()).showProgress(false, null, progressSincronizacao);
        }
    }

    public class ConsultarProdutoTask extends AsyncTask<Void, Void, Boolean> {

        static final String SOAP_ACTION = "http://www.unidosdacachorra.com.br/zAdmin/nusoap/consultarProdutos";
        static final String NAMESPACE = "http://www.unidosdacachorra.com.br/zAdmin/nusoap";
        static final String URL = "http://www.unidosdacachorra.com.br/zAdmin/nusoap/ws_produtos.php?WSDL";
        static final String METHOD = "consultarProdutos";
        static final int TIMEOUT = 600000;
        private ListView mListView;
        private ArrayAdapter<Produto> arrayAdapter;
        private int atualizado = 0;
        private int inserido = 0;

        ConsultarProdutoTask(ListView lista) {
            this.mListView = lista;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(URL, TIMEOUT);
            //httpTransport.debug  = true;

            try {
                httpTransport.call(SOAP_ACTION, envelope);
                String rs = (String) envelope.getResponse();



                if (rs != null) {
                    JSONParser jsonParser = new JSONParser();
                    JSONArray jObj = new JSONObject(rs).getJSONArray("Produtos");

                    for(int i = 0; i < jObj.length(); i++) {
                        Produto p = new Produto();
                        Long id = new Long(((JSONObject) jObj.get(i)).get("id").toString());
                        p.setId(id);
                        p.setNome(((JSONObject) jObj.get(i)).getString("nome"));
                        p.setDescricao(((JSONObject) jObj.get(i)).getString("descricao"));
                        BigDecimal valor = new BigDecimal(((JSONObject) jObj.get(i)).get("valor").toString());
                        p.setValor(valor);
                        p.setQuantidade(((JSONObject) jObj.get(i)).getInt("quantidade"));
                        p.setSincronizado(Boolean.TRUE);
                        p.setAtivo(Boolean.TRUE);

                        ProdutoDao dao = new ProdutoDao(getActivity());
                        Cursor obj = dao.carregarPorId(p.getId().intValue());
                        if(obj != null && obj.getCount() > 0) {
                            BigDecimal valorDecimal = new BigDecimal(obj.getString(obj.getColumnIndex("valor")));
                            if(obj.getString(obj.getColumnIndex("sincronizado")) == null || obj.getInt(obj.getColumnIndex("sincronizado")) == 0){
                                dao.alterar(p);
                                atualizado++;
                            } else if (obj.getInt(obj.getColumnIndex("quantidade")) != p.getQuantidade() || !obj.getString(obj.getColumnIndex("descricao")).equals(p.getDescricao())  || !obj.getString(obj.getColumnIndex("nome")).equals(p.getNome()) || valorDecimal.compareTo(p.getValor()) != 0) {
                                dao.alterar(p);
                                atualizado++;
                            }
                        } else {
                            dao.inserir(p);
                            inserido++;
                        }
                    }




                    //arrayAdapter = new ArrayAdapter<Produto>(getActivity(),android.R.layout.simple_list_item_1, produtos );

                    //new Credencial(jObj.getString("login"),new Boolean(jObj.getString("logado")), jObj.getString("user_name"),jObj.getString("email"), jObj.getString("erro"));
                    return true;
                }

            } catch (Exception e) {
                Log.d("tag", "outr", e);
            }


            return false;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            String msg = "";
            if(inserido>0){
                msg += inserido + " registro(s) inserido(s)\n";
            }
            if(atualizado>0){
                msg += atualizado + " registro(s) atualizado(s)\n";
            }
            if(msg.length() > 0) {
                Toast.makeText(getActivity(), msg, Toast.LENGTH_SHORT).show();
                popularListView(lista, null, posicaoInicial, numeroLinhas, true, false);
                mListView.setAdapter(arrayAdapter);
            } else {
                Toast.makeText(getActivity(), "Não existem itens para sincronizar", Toast.LENGTH_SHORT).show();
            }

            mConsultarProdutosTask = null;

            showProgress(false, null, progressBar);

        }

        @Override
        protected void onCancelled() {
            mConsultarProdutosTask = null;
            showProgress(false, null, progressBar);
        }
    }






    }
