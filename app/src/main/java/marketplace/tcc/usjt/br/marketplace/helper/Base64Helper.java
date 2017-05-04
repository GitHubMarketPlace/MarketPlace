package marketplace.tcc.usjt.br.marketplace.helper;

import android.util.Base64;

public class Base64Helper {

    public static String codifyBase64(String text){
        // Retorna uma String convertida para Base64
        return Base64.encodeToString(text.getBytes(), Base64.DEFAULT).replaceAll("(\\n|\\r)", "");
    }

    public static String decodifyBase64(String codifyText){
        return new String (Base64.decode(codifyText, Base64.DEFAULT));
    }

}
