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
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    public void showProgress(final boolean show, final View form, final View progressBar) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            if(form != null) {
                form.setVisibility(show ? View.GONE : View.VISIBLE);
                form.animate().setDuration(shortAnimTime).alpha(
                        show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        form.setVisibility(show ? View.GONE : View.VISIBLE);
                    }
                });
            }

            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            progressBar.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            progressBar.setVisibility(show ? View.VISIBLE : View.GONE);
            if(form != null) {
                form.setVisibility(show ? View.GONE : View.VISIBLE);
            }
        }
    }
}
