package br.com.unidosdacachorra.zadmin.util;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.View;

import java.text.NumberFormat;
import java.util.Locale;

/**
 * Created by Anderson on 28/07/2016.
 */
public class AbstractFragment  extends Fragment {
    protected Bundle getArgumentos(String titulo, int itemId){
        Bundle arguments = new Bundle();
        arguments.putString("titulo", titulo);
        arguments.putInt("itemId",itemId);
        return arguments;
    }

    public String getValorFormatado(String valor) {
        NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR"));
        try {
            return nf.format(new Double(valor));
        } catch (Exception e) {
            return valor;
        }
    }
}
