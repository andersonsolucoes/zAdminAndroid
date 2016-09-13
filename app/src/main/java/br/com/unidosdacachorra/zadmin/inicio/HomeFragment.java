package br.com.unidosdacachorra.zadmin.inicio;

import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;

import br.com.unidosdacachorra.zadmin.login.Credencial;
import br.com.unidosdacachorra.zadmin.login.LoginActivity;
import br.com.unidosdacachorra.zadmin.R;

/**
 * Created by Anderson on 20/07/2016.
 */
public class HomeFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.activity_main, container, false);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        RelativeLayout rl = (RelativeLayout) view.findViewById(R.id.main_home);
        rl.getBackground().setAlpha(120);
    }
}
