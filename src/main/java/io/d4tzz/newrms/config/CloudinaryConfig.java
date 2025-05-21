package io.d4tzz.newrms.config;

import com.cloudinary.Cloudinary;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.Map;

@Configuration
public class CloudinaryConfig {
    @Bean
    public Cloudinary cloudinary() {
        Map<String, Object> config = new HashMap<>();
        config.put("cloud_name", "dwkug07aa");
        config.put("api_key", "894237759951193");
        config.put("api_secret", "EaipnH1DvxHYFZimIAQmWoj_COo");
        config.put("secure", true);

        return new Cloudinary(config);
    }
}

//EaipnH1DvxHYFZimIAQmWoj_COo
