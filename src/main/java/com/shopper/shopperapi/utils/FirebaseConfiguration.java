package com.shopper.shopperapi.utils;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.firebase.FirebaseApp;
import com.google.firebase.FirebaseOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
//import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;

@Configuration
public class FirebaseConfiguration {
    @Bean
    public DatabaseReference firebaseDatabse() {
        return FirebaseDatabase.getInstance().getReference();
    }

    @Value("${application.firebase.database.url}")
    private String databaseUrl;

    @Value("${application.firebase.config.path}")
    private String configPath;

    @PostConstruct
    public void init() throws IOException {

        /**
         * Using Firebase service account
         */

//        FileInputStream serviceAccount = new FileInputStream("path/to/serviceAccountKey.json");

        InputStream inputStream = FirebaseConfiguration.class.getClassLoader().getResourceAsStream(configPath);
        assert inputStream != null;

        FirebaseOptions options = new FirebaseOptions.Builder()
                .setCredentials(GoogleCredentials.fromStream(inputStream))
                .setDatabaseUrl(databaseUrl)
                .build();

        FirebaseApp.initializeApp(options);

    }
}
