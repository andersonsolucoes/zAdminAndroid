package br.com.unidosdacachorra.zadmin.login;

/**
 * Created by Anderson on 19/07/2016.
 */
public class Credencial {
    public static String login;
    public static Boolean logado;
    public static String user_name;
    public static String email;
    public static String erro;

    private String senha;

    public Credencial(String login, Boolean logado, String user_name, String email, String erro){
        this.login = login;
        this.logado = logado;
        this.user_name = user_name;
        this.email = email;
        this.erro = erro;
    }

    public Credencial(String erro){
        this.logado = Boolean.FALSE;
        this.erro = erro;
    }

    public String getSenha(){
        return this.senha;
    }
    public void setSenha(String senha) {
        this.senha = senha;
    }

}
