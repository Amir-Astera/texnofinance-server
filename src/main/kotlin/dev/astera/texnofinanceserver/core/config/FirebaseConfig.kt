package dev.astera.texnofinanceserver.core.config

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import com.google.firebase.auth.FirebaseAuth
import dev.astera.texnofinanceserver.core.config.properties.FirebaseProperties
import dev.astera.texnofinanceserver.core.config.properties.SecurityProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.context.annotation.Primary
import java.io.FileInputStream
import java.io.IOException


@Configuration
class FirebaseConfig(
    private val securityProperties: SecurityProperties
) {
    @Primary
    @Bean
    @Throws(IOException::class)
    fun init() {
        val stream = FileInputStream(securityProperties.firebaseProps.googleCredentials)
        val options = FirebaseOptions.builder()
            .setCredentials(GoogleCredentials.fromStream(stream))
            .build()
        
        if (FirebaseApp.getApps().isEmpty()) {
            FirebaseApp.initializeApp(options)
        }
    }

    @Bean
    fun auth(): FirebaseAuth {
        return FirebaseAuth.getInstance()
    }
}