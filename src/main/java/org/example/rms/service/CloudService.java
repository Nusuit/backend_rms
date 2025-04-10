package org.example.rms.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class CloudService {
    private final Cloudinary cloudinary;

    public Map<?, ?> uploadFile(MultipartFile file) {
        try {

            return cloudinary.uploader().upload(file.getBytes(), Map.of("folder", "cv"));
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }

    public String urlUploadFile(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), Map.of("folder", "cv")).get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
}
