package br.com.unidosdacachorra.zadmin.loja.estoque.activities;

import android.os.Bundle;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.loja.estoque.fragmentos.EstoqueFragment;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * Created by Anderson on 29/07/2016.
 */
public class EstoqueActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new EstoqueFragment()).commit();

        setTitle("Estoque");

        nvDrawer.getMenu().clear();
        nvDrawer.inflateMenu(R.menu.menu_itens_loja);
        nvDrawer.setCheckedItem(R.id.nav_loja_estoque);
        mDrawer.closeDrawers();
    }
}
