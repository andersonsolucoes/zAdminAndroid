package br.com.unidosdacachorra.zadmin.loja.venda.activities;

import android.os.Bundle;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.loja.venda.fragmentos.VendaFragment;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * Created by Anderson on 29/07/2016.
 */
public class VendaActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new VendaFragment()).commit();

        setTitle("Vendas");

        nvDrawer.getMenu().clear();
        nvDrawer.inflateMenu(R.menu.menu_itens_loja);
        nvDrawer.setCheckedItem(R.id.nav_loja_vendas);
        mDrawer.closeDrawers();
    }
}
