package br.com.unidosdacachorra.zadmin.loja.produto.activities;

import android.os.Bundle;
import android.view.Menu;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.loja.produto.fragmentos.ProdutoFragment;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * Created by Anderson on 29/07/2016.
 */
public class ProdutoActivity extends AbstractActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new ProdutoFragment()).commit();

        //setTitle("Produtos");
        setTitle("Loja");

        //nvDrawer.getMenu().clear();
        //nvDrawer.inflateMenu(R.menu.menu_itens_loja);
        //nvDrawer.setCheckedItem(R.id.nav_loja_produtos);
        nvDrawer.setCheckedItem(R.id.nav_loja);
        mDrawer.closeDrawers();
    }

}
