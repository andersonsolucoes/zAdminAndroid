package br.com.unidosdacachorra.zadmin.loja.compra.activities;

import android.os.Bundle;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.loja.compra.fragmentos.CompraFragment;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * Created by Anderson on 29/07/2016.
 */
public class CompraActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new CompraFragment()).commit();

        setTitle("Compras");

        nvDrawer.getMenu().clear();
        nvDrawer.inflateMenu(R.menu.menu_itens_loja);
        nvDrawer.setCheckedItem(R.id.nav_loja_compras);
        mDrawer.closeDrawers();
    }
}
