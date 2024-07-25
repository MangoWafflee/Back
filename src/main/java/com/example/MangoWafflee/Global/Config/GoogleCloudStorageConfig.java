package com.example.MangoWafflee.Global.Config;

import com.google.auth.oauth2.GoogleCredentials;
import com.google.cloud.storage.Storage;
import com.google.cloud.storage.StorageOptions;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Base64;

@Configuration
public class GoogleCloudStorageConfig {

    @Value("${google.cloud.credentials.base64key}")
    private String base64Key;

    @Bean
    public Storage storage() throws IOException {
        byte[] decodedKey = Base64.getDecoder().decode(base64Key);
        GoogleCredentials credentials = GoogleCredentials.fromStream(new ByteArrayInputStream(decodedKey));
        return StorageOptions.newBuilder().setCredentials(credentials).build().getService();
    }
}
