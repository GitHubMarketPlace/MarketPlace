package marketplace.tcc.usjt.br.marketplace.config;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;

public final class FirebaseConfig {
    private static DatabaseReference firebaseReference;
    private static FirebaseAuth auth;
    private static FirebaseStorage storage;

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

    public static FirebaseStorage getFirebaseStorage(){
        if (storage != null){
            storage = FirebaseStorage.getInstance();
        }
        storage = FirebaseStorage.getInstance();
        return storage;
    }
}
