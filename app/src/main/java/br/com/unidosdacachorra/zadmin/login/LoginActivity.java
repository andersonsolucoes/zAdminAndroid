package br.com.unidosdacachorra.zadmin.login;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import android.os.AsyncTask;

import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.json.simple.parser.JSONParser;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.SoapFault;
import org.ksoap2.serialization.PropertyInfo;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.net.UnknownHostException;

import br.com.unidosdacachorra.zadmin.inicio.ActivityPrincipal;
import br.com.unidosdacachorra.zadmin.R;
import br.com.unidosdacachorra.zadmin.util.AbstractActivity;

/**
 * A login screen that offers login via email/password.
 */
public class LoginActivity extends AbstractActivity {

    static final String SOAP_ACTION = "http://www.unidosdacachorra.com.br/zAdmin/nusoap/login";
    static final String NAMESPACE = "http://www.unidosdacachorra.com.br/zAdmin/nusoap";
    static final String URL = "http://www.unidosdacachorra.com.br/zAdmin/nusoap/server_zAdmin.php?WSDL";
    static final String METHOD = "login";
    static final int TIMEOUT = 600000;

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */
    private UserLoginTask mAuthTask = null;

    // UI references.
    private EditText mLoginView;
    private EditText mSenhaView;
    private CheckBox mManterLoginView;
    private View mProgressView;
    private View mLoginFormView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        mLoginView = (EditText) findViewById(R.id.email);

        mManterLoginView = (CheckBox) findViewById(R.id.manterLogin);

        mSenhaView = (EditText) findViewById(R.id.password);
        mSenhaView.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });

        Button mLoginSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mLoginSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);
    }


    /**
     * Attempts to sign in or register the account specified by the login form.
     * If there are form errors (invalid email, missing fields, etc.), the
     * errors are presented and no actual login attempt is made.
     */
    private void attemptLogin() {
        if (mAuthTask != null) {
            return;
        }

        // Reset errors.
        mLoginView.setError(null);
        mSenhaView.setError(null);

        // Store values at the time of the login attempt.
        String login = mLoginView.getText().toString();
        String senha = mSenhaView.getText().toString();

        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(senha) && !isSenhaValid(senha)) {
            mSenhaView.setError(getString(R.string.error_invalid_senha));
            focusView = mSenhaView;
            cancel = true;
        }

        // Check for a valid password, if the user entered one.
        if (!TextUtils.isEmpty(login) && !isLoginValid(login)) {
            mLoginView.setError(getString(R.string.error_invalid_login));
            focusView = mLoginView;
            cancel = true;
        }

        if (cancel) {
            // There was an error; don't attempt login and focus the first
            // form field with an error.
            focusView.requestFocus();
        } else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true, mLoginFormView, mProgressView);
            mAuthTask = new UserLoginTask(login, senha);
            mAuthTask.execute((Void) null);
        }
    }

    private boolean isLoginValid(String login) {
        return true;
    }

    private boolean isSenhaValid(String password) {
        return password.length()>4;
    }

    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class UserLoginTask extends AsyncTask<Void, Void, Boolean> {

        private final String mLogin;
        private final String mSenha;

        UserLoginTask(String login, String senha) {
            mLogin = login;
            mSenha = senha;
        }

        @Override
        protected Boolean doInBackground(Void... params) {
            SoapObject request = new SoapObject(NAMESPACE, METHOD);

            PropertyInfo propLogin = new PropertyInfo();
            propLogin.setName("login");
            propLogin.setValue(mLogin);
            propLogin.setType(String.class);
            request.addProperty(propLogin);

            PropertyInfo propSenha = new PropertyInfo();
            propSenha.setName("senha");
            propSenha.setValue(mSenha);
            propSenha.setType(String.class);
            request.addProperty(propSenha);

            SoapSerializationEnvelope envelope = new SoapSerializationEnvelope(SoapEnvelope.VER11);
            envelope.setOutputSoapObject(request);

            HttpTransportSE httpTransport = new HttpTransportSE(URL, TIMEOUT);
            //httpTransport.debug  = true;

            try {

                httpTransport.call(SOAP_ACTION, envelope);

                String rs = (String) envelope.getResponse();

                if (rs != null) {

                    JSONParser jsonParser = new JSONParser();
                    JSONObject jObj = new JSONObject(rs).getJSONObject("Credencial");
                    new Credencial(jObj.getString("login"),new Boolean(jObj.getString("logado")), jObj.getString("user_name"),jObj.getString("email"), jObj.getString("erro"));
                    return true;
                }

            } catch (UnknownHostException e) {
                new Credencial("Não foi possível encontrar o host, verifique a conexão com a internet");
            } catch (JSONException e) {
                new Credencial("Erro no arquivo JSON: " + e.getMessage());
            } catch (SoapFault e) {
                new Credencial("Erro de comunicação com o serviço: " + e.getMessage());
            } catch (XmlPullParserException e) {
                new Credencial("Erro na conversão XML: " + e.getMessage());
            } catch (IOException e) {
                new Credencial("Erro de Entrada/Saída: " + e.getMessage());
            }

            return false;

        }

        @Override
        protected void onPostExecute(final Boolean success) {
            mAuthTask = null;
            showProgress(false, mLoginFormView, mProgressView);

            if (success && Credencial.logado) {
                if(mManterLoginView.isChecked()) {
                    SharedPreferences pref;
                    pref = getSharedPreferences("info", MODE_PRIVATE);
                    SharedPreferences.Editor editor = pref.edit();
                    editor.putBoolean("logado", true);
                    editor.putString("login", Credencial.login);
                    editor.putString("user_name", Credencial.user_name);
                    editor.putString("email", Credencial.email);
                    editor.commit();
                }
                finish();
                Intent i = new Intent(LoginActivity.this, ActivityPrincipal.class);
                startActivity(i);

                overridePendingTransition(R.anim.fadein, R.anim.fadeout);

                FragmentManager fragmentManager = getFragmentManager();
                FragmentTransaction transaction = fragmentManager.beginTransaction();
                transaction.setCustomAnimations(R.anim.fadein, R.anim.fadeout); //animação aqui
                transaction.commit();

            } else {
                mSenhaView.setError(Credencial.erro);
                mSenhaView.requestFocus();
            }
        }

        @Override
        protected void onCancelled() {
            mAuthTask = null;
            showProgress(false, mLoginFormView, mProgressView);
        }
    }
}

