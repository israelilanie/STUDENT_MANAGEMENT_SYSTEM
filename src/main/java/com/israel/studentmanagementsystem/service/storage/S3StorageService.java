package com.israel.studentmanagementsystem.service.storage;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.DeleteObjectRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class S3StorageService {

    private final S3Client s3Client;
    private final FileValidator fileValidator;

    @Value("${aws.s3.bucket}")
    private String bucketName;

    @Value("${aws.s3.region}")
    private String region;

    public String uploadAvatar(MultipartFile file, Long studentId) {


        fileValidator.validateAvatar(file);

        String extension = getExtension(file.getOriginalFilename());
        String key = String.format("avatars/student-%d/%s%s",
                studentId, UUID.randomUUID(), extension);

        try {
            PutObjectRequest request = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .contentType(file.getContentType())
                    .contentLength(file.getSize())
                    .build();

            s3Client.putObject(request,
                    RequestBody.fromInputStream(
                            file.getInputStream(), file.getSize()));


            String url = String.format(
                    "https://%s.s3.%s.amazonaws.com/%s",
                    bucketName, region, key);

            log.info("Uploaded avatar for student {} to {}",
                    studentId, url);

            return url;

        } catch (IOException e) {
            log.error("Failed to upload file to S3: {}", e.getMessage());
            throw new RuntimeException("File upload failed", e);
        }
    }

    public void deleteFile(String fileUrl) {
        try {

            String key = extractKeyFromUrl(fileUrl);

            DeleteObjectRequest request = DeleteObjectRequest.builder()
                    .bucket(bucketName)
                    .key(key)
                    .build();

            s3Client.deleteObject(request);
            log.info("Deleted file from S3: {}", key);

        } catch (Exception e) {

            log.error("Failed to delete file from S3: {}", e.getMessage());
        }
    }

    private String getExtension(String filename) {
        if (filename == null || !filename.contains(".")) return ".jpg";
        return filename.substring(filename.lastIndexOf(".")).toLowerCase();
    }

    private String extractKeyFromUrl(String url) {

        int idx = url.indexOf(".amazonaws.com/");
        if (idx == -1) throw new IllegalArgumentException(
                "Invalid S3 URL: " + url);
        return url.substring(idx + ".amazonaws.com/".length());
    }

}
