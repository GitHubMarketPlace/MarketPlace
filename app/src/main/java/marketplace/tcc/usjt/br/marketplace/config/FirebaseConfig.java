package marketplace.tcc.usjt.br.marketplace.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public final class FirebaseConfig {
    private static DatabaseReference firebaseReference;
    private static FirebaseAuth auth;

    // Pega uma instância do Firebase
    public static DatabaseReference getFirebase(){
        if(firebaseReference == null){
            firebaseReference = FirebaseDatabase.getInstance().getReference();
        }
        firebaseReference = FirebaseDatabase.getInstance().getReference();
        return firebaseReference;
    }

    // Pega uma instância do Autenticador do Firebase
    public static FirebaseAuth getFirebaseAuth(){
        if(auth == null){
            auth = FirebaseAuth.getInstance();
        }
        return auth;
    }
}
