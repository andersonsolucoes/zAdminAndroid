package br.com.unidosdacachorra.zadmin.login;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.inicio.ActivityPrincipal;

/**
 * Created by Anderson on 20/07/2016.
 */
public class AlterarSenhaFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_alterar_senha, container, false);
    }
}
