package br.com.unidosdacachorra.zadmin.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import java.text.NumberFormat;
import java.util.Locale;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.inicio.ActivityPrincipal;
import br.com.unidosdacachorra.zadmin.inicio.HomeFragment;
import br.com.unidosdacachorra.zadmin.login.AlterarSenhaActivity;
import br.com.unidosdacachorra.zadmin.login.AlterarSenhaFragment;
import br.com.unidosdacachorra.zadmin.login.Credencial;
import br.com.unidosdacachorra.zadmin.login.LoginActivity;
import br.com.unidosdacachorra.zadmin.loja.compra.activities.CompraActivity;
import br.com.unidosdacachorra.zadmin.loja.compra.fragmentos.CompraFragment;
import br.com.unidosdacachorra.zadmin.loja.estoque.activities.EstoqueActivity;
import br.com.unidosdacachorra.zadmin.loja.estoque.fragmentos.EstoqueFragment;
import br.com.unidosdacachorra.zadmin.loja.produto.activities.ProdutoActivity;
import br.com.unidosdacachorra.zadmin.loja.produto.fragmentos.ProdutoFragment;
import br.com.unidosdacachorra.zadmin.loja.venda.activities.VendaActivity;
import br.com.unidosdacachorra.zadmin.loja.venda.fragmentos.VendaFragment;

/**
 * Created by Anderson on 28/07/2016.
 */
public class AbstractActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    public DrawerLayout mDrawer;
    public Toolbar toolbar;
    public NavigationView nvDrawer;
    public ActionBarDrawerToggle toggle;
    public View progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.template);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        nvDrawer = (NavigationView) findViewById(R.id.nav_view);

        progressBar = findViewById(R.id.progresso);

        toggle = new ActionBarDrawerToggle(this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.setDrawerListener(toggle);
        toggle.syncState();

        nvDrawer.setNavigationItemSelectedListener(this);

        View headerLayout = nvDrawer.getHeaderView(0);

        TextView txtUserName = (TextView) headerLayout.findViewById(R.id.txt_menu_user_name);
        txtUserName.setText(Credencial.user_name);
        TextView txtUserEmail = (TextView) headerLayout.findViewById(R.id.txt_menu_user_email);
        txtUserEmail.setText(Credencial.email);

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {

        int id;

        if(item!=null) {
            id = item.getItemId();
            Intent intent = null;
            if (id == R.id.nav_home) {
                intent = new Intent(AbstractActivity.this, ActivityPrincipal.class);
            } else if (id == R.id.nav_back) {
                nvDrawer.getMenu().clear();
                nvDrawer.inflateMenu(R.menu.menu_itens_principal);
            } else if (id == R.id.nav_loja) {
                nvDrawer.getMenu().clear();
                nvDrawer.inflateMenu(R.menu.menu_itens_loja);
            } else if (id == R.id.nav_loja_vendas) {
                onNavigationItemSelected(nvDrawer.getMenu().findItem(R.id.nav_back));
                intent = new Intent(AbstractActivity.this, VendaActivity.class);
            } else if (id == R.id.nav_loja_compras) {
                onNavigationItemSelected(nvDrawer.getMenu().findItem(R.id.nav_back));
                intent = new Intent(AbstractActivity.this, CompraActivity.class);
            } else if (id == R.id.nav_loja_estoque) {
                onNavigationItemSelected(nvDrawer.getMenu().findItem(R.id.nav_back));
                intent = new Intent(AbstractActivity.this, EstoqueActivity.class);
            } else if (id == R.id.nav_loja_produtos) {
                onNavigationItemSelected(nvDrawer.getMenu().findItem(R.id.nav_back));
                intent = new Intent(AbstractActivity.this, ProdutoActivity.class);
            } else if (id == R.id.nav_alterar_senha) {
                intent = new Intent(AbstractActivity.this, AlterarSenhaActivity.class);
            } else if (id == R.id.nav_sair) {
                new AlertDialog.Builder(this)
                        .setTitle("Sair")
                        .setMessage("Deseja realmente sair do login atual " + Credencial.user_name + "?")
                        .setIcon(R.drawable.ic_help)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                //Toast.makeText(getActivity(), "Yaay", Toast.LENGTH_SHORT).show();
                                SharedPreferences pref;
                                pref = getSharedPreferences("info", MODE_PRIVATE);
                                SharedPreferences.Editor editor = pref.edit();
                                editor.clear();
                                editor.commit();
                                finish();
                                Intent i = new Intent(AbstractActivity.this, LoginActivity.class);
                                startActivity(i);

                                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                                android.app.FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction transaction = fragmentManager.beginTransaction();
                                transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout); //animação aqui
                                transaction.commit();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null).show();
            }

            if(intent!=null) {
                startActivity(intent);
                if(!(this instanceof ActivityPrincipal))
                    finish();
                overridePendingTransition(R.anim.fadein, R.anim.fadeout);
                //nvDrawer.setCheckedItem(id);
                mDrawer.closeDrawers();
            }
        }

        return true;
    }

    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    protected void showProgress(final boolean show, final View form, final View progressBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            form.setVisibility(show ? View.GONE : View.VISIBLE);
            form.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    form.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            form.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }

    public static String getValorFormatado(String valor) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        try {
            return nf.format(new Double(valor));
        } catch (Exception e) {
            return valor;
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            finish();
            super.onBackPressed();
        }
    }
}
