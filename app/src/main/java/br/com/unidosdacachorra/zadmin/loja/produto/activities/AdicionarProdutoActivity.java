package br.com.unidosdacachorra.zadmin.loja.produto.activities;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.math.BigDecimal;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.loja.produto.dao.ProdutoDao;
import br.com.unidosdacachorra.zadmin.loja.produto.fragmentos.AdicionarProdutoFragment;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;
import br.com.unidosdacachorra.zadmin.util.Mensagem;

/**
 * Created by Anderson on 20/07/2016.
 */
public class AdicionarProdutoActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportFragmentManager().beginTransaction().add(R.id.content, new AdicionarProdutoFragment()).commit();
        setTitle("Adicionar produto");

        toolbar.setNavigationIcon(R.drawable.ic_back);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        nvDrawer.getMenu().clear();
        nvDrawer.inflateMenu(R.menu.menu_itens_loja);
        nvDrawer.setCheckedItem(R.id.nav_loja_produtos);
        mDrawer.closeDrawers();

    }


}
