package io.d4tzz.newrms.service;

import com.cloudinary.Cloudinary;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService{
    private final Cloudinary cloudinary;
    
    /**
     * Upload file to Cloudinary
     * 
     * @param file The file to upload
     * @param folder Destination folder in Cloudinary
     * @return URL of the uploaded file
     */
    public String uploadFile(MultipartFile file, String folder) {
        try {
            Map<String, Object> params = new HashMap<>();
            params.put("folder", folder);
            
            return cloudinary.uploader()
                    .upload(file.getBytes(), params)
                    .get("url")
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }
    
    /**
     * Upload file to Cloudinary with custom options
     * 
     * @param file The file to upload
     * @param options Custom upload options
     * @return URL of the uploaded file
     */
    public String uploadFile(MultipartFile file, Map<String, Object> options) {
        try {
            return cloudinary.uploader()
                    .upload(file.getBytes(), options)
                    .get("url")
                    .toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file to Cloudinary", e);
        }
    }

    public String uploadFile(MultipartFile file) {
        try {
            return cloudinary.uploader().upload(file.getBytes(), Map.of("folder", "cv")).get("url").toString();
        } catch (IOException e) {
            throw new RuntimeException("Failed to upload file", e);
        }
    }
    
    /**
     * Delete file from Cloudinary
     * 
     * @param publicId The public ID of the file to delete
     * @return Result of the deletion operation
     */
    public Map<?, ?> deleteFile(String publicId) {
        try {
            return cloudinary.uploader().destroy(publicId, Map.of());
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete file from Cloudinary", e);
        }
    }
}