package br.com.unidosdacachorra.zadmin.inicio;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.List;

import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * Created by Anderson on 18/07/2016.
 */
public class ActivityPrincipal extends AbstractActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        getSupportFragmentManager().beginTransaction().add(R.id.content, new HomeFragment()).commit();

        setTitle("Início");

        nvDrawer.setCheckedItem(R.id.nav_home);
        mDrawer.closeDrawers();
    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        ImageView myImage = (ImageView) findViewById(R.id.imgLogo);
        myImage.setImageAlpha(50);
        super.onPostCreate(savedInstanceState);
    }
}