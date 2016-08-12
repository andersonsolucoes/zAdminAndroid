package br.com.unidosdacachorra.zadmin.loja.compra.fragmentos;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import br.com.unidosdacachorra.zadmin.R;

/**
 * Created by Anderson on 20/07/2016.
 */
public class CompraFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_loja_compras, container, false);
    }

}
