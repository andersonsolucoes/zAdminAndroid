package br.com.unidosdacachorra.zadmin.login;

import android.os.Bundle;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.loja.compra.fragmentos.CompraFragment;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * Created by Anderson on 29/07/2016.
 */
public class AlterarSenhaActivity extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new AlterarSenhaFragment()).commit();

        setTitle("Alterar senha");

        nvDrawer.setCheckedItem(R.id.nav_alterar_senha);
        mDrawer.closeDrawers();
    }
}
