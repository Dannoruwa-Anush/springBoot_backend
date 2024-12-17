package com.example.demo.service.commonService;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FileUploadService {

    private String uploadDirectory = "D:\\\\netCentricProjectImageUploads";

    public FileUploadService() throws IOException {
        // Ensure the upload directory exists
        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
    }
    // ---

    // Helper methods
    // Validate the uploaded file for size and type
    private void validateFile(MultipartFile file) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Uploaded file is empty.");
        }

        // Check file size (e.g., max 5MB)
        long maxFileSize = 5 * 1024 * 1024;
        if (file.getSize() > maxFileSize) {
            throw new IllegalArgumentException("File size exceeds the maximum limit of 5MB.");
        }

        // Check file type (e.g., allow only images)
        String contentType = file.getContentType();
        if (contentType == null || !contentType.startsWith("image/")) {
            throw new IllegalArgumentException("Invalid file type. Only image files are allowed.");
        }
    }
    // ---

    // Extract the file extension from the file name.
    private String getFileExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) {
            return "";
        }
        return fileName.substring(fileName.lastIndexOf("."));
    }
    // ---

    // Save the uploaded file and return its file path
    public String saveFile(MultipartFile file) throws IOException {
        validateFile(file);

        // Generate a unique file name
        String originalFilename = file.getOriginalFilename();
        String fileExtension = getFileExtension(originalFilename);
        String uniqueFileName = UUID.randomUUID().toString() + fileExtension;

        // Save the file
        Path targetPath = Paths.get(uploadDirectory, uniqueFileName);
        file.transferTo(targetPath);

        return uniqueFileName;
    }
}
