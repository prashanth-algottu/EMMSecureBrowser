package com.tectoro.emm.browser.db;

import android.content.Context;
import android.os.Build;
import android.widget.Toast;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.Map;

public class FireStore {
    private static Context context;

    public FireStore(Context context) {
        this.context = context;
    }

    private String getSerial() {
        String serial = Build.getSerial();
        return serial;
    }

    private String readPolicy(DocumentSnapshot doc) {
        Map<String, Object> data = doc.getData();
        if (data == null) {
            return null;
        }
        return (String) data.get("policy");
    }

    private Config readConfig(DocumentSnapshot doc) {
        Map<String, Object> data = doc.getData();
        System.out.println(data.toString());
        Map<String, Object> browserConfig = (Map<String, Object>) data.get("browser");
        return new Config(browserConfig);
    }

    public Config load() {
        try {
//            String serial = getSerial();
            String serial = "R9ZT10AHXWN";
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            DocumentSnapshot doc1 = Tasks.await(db.collection("device").document(serial).get());
            String policy = readPolicy(doc1);
            DocumentSnapshot doc2 = Tasks.await(db.collection("ext_config").document(policy).get());
            Config config = readConfig(doc2);

            return config;

        } catch (Exception ex) {
            Toast.makeText(context, "Fail to load from server." + ex.getMessage(), Toast.LENGTH_SHORT).show();
            return null;
        }
    }
}
