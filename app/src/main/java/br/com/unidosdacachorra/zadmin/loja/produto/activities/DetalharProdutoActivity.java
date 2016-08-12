package br.com.unidosdacachorra.zadmin.loja.produto.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.view.View;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.loja.produto.fragmentos.DetalharProdutoFragment;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * Created by Anderson on 28/07/2016.
 */
public class DetalharProdutoActivity extends AbstractActivity {
    private boolean atualizaProduto;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loja_produto_detalhe);

        Toolbar toolbar = (Toolbar) findViewById(R.id.detail_toolbar);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(atualizaProduto) {
                    Intent intent = new Intent(DetalharProdutoActivity.this, ProdutoActivity.class);
                    intent.putExtra("atualizaProduto", true);
                    setResult(1, intent);
                }
                finish();
            }
        });

        // savedInstanceState is non-null when there is fragment state
        // saved from previous configurations of this activity
        // (e.g. when rotating the screen from portrait to landscape).
        // In this case, the fragment will automatically be re-added
        // to its container so we don't need to manually add it.
        // For more information, see the Fragments API guide at:
        //
        // http://developer.android.com/guide/components/fragments.html
        //
        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity
            // using a fragment transaction.
            Bundle arguments = new Bundle();
            arguments.putString("idProduto",
                    getIntent().getStringExtra("idProduto"));
            DetalharProdutoFragment fragment = new DetalharProdutoFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.item_detail_container, fragment)
                    .commit();
        }
    }

    @Override
    public void onBackPressed() {
        if (mDrawer.isDrawerOpen(GravityCompat.START)) {
            mDrawer.closeDrawer(GravityCompat.START);
        } else {
            if(atualizaProduto) {
                Intent intent = new Intent(DetalharProdutoActivity.this, ProdutoActivity.class);
                intent.putExtra("atualizaProduto", true);
                setResult(1, intent);
                super.onBackPressed();
            }
            finish();
        }
    }

    public void setAtualizaProduto(boolean b) {
        this.atualizaProduto = b;
    }

}
