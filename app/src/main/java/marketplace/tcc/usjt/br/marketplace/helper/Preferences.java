package marketplace.tcc.usjt.br.marketplace.helper;

import android.content.Context;
import android.content.SharedPreferences;

public class Preferences {

    private Context context;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "marketplace.preferences";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;
    private String KEY_IDENTIFY = "identificadorUsuarioLogado";

    public Preferences(Context contextoParametro){
        context = contextoParametro;
        // MODE = 0, private, apenas o app tem acesso ao arquivo de preferências.
        preferences = context.getSharedPreferences(NOME_ARQUIVO,MODE);
        // O editor premite acrescentar ou remover itens de preferências
        editor = preferences.edit();
    }

    public void saveData(String identifyUser){
        // Salva no arquivo de preferências o e-mail
        editor.putString(KEY_IDENTIFY, identifyUser);
        editor.commit();
    }

    public String getIdentify(){
        return preferences.getString(KEY_IDENTIFY, null);
    }

}
